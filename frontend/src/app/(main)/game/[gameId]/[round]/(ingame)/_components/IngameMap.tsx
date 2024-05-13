'use client'

import themeStyles from '@/components/theme.module.css'
import useIngameStore from '@/stores/ingameStore'
import useUserStore from '@/stores/userStore'
import { PinRespoonse } from '@/types/IngameSocketTypes'
import { Loader } from '@googlemaps/js-api-loader'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { useRouter } from 'next/navigation'
import { useEffect, useRef } from 'react'
import styles from './ingamemap.module.css'

interface IngameMapProps {
  theme: string
  loader: Loader
  gameId: string
  round: string
  stage: number
}

interface MyGuess {
  lat: number
  lng: number
}

export default function IngameMap({
  theme,
  loader,
  gameId,
  round,
  stage,
}: IngameMapProps) {
  const mapShowRef = useRef<HTMLDivElement | null>(null)
  const mapObjectRef = useRef<google.maps.Map | null>(null)
  const currentMarker = useRef<google.maps.Marker | null>(null)
  const myGuess = useRef<MyGuess | null>(null)
  const router = useRouter()

  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SERVER_SOCKET_URL,
      debug: function (str: string) {
        console.log(str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )

  const { nickname } = useUserStore()
  const { teamId } = useIngameStore()

  const subUrl = `/team/${gameId}/${teamId}`
  const guessUrl = `/app/team/guess`
  const pinUrl = '/app/team/pin'

  // 지도 핀 변경
  function changePin(lat: number, lng: number) {
    // 기존에 핀이 있다면 제거
    if (currentMarker.current) {
      currentMarker.current.setMap(null)
    }
    // 핀 추가
    const newMarker = new google.maps.Marker({
      position: { lat, lng },
      map: mapObjectRef.current,
      icon: {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 10,
        fillColor: 'red',
        fillOpacity: 1,
        strokeColor: 'black',
        strokeWeight: 3,
      },
    })

    // 핀은 하나만 있으니 거기에다가 새 마커 배정
    currentMarker.current = newMarker

    // 내 추측 저장
    myGuess.current = {
      lat,
      lng,
    }
  }

  // 지도 init
  useEffect(() => {
    loader.importLibrary('maps').then(async () => {
      // 서울 중심
      const position = { lat: 37.5642135, lng: 127.0016985 }
      const { Map } = (await google.maps.importLibrary(
        'maps',
      )) as google.maps.MapsLibrary

      const map = new Map(mapShowRef.current!, {
        center: position,
        disableDefaultUI: true,
        clickableIcons: false,
        zoom: 8,
      })
      map.addListener('click', (e: google.maps.MapMouseEvent) => {
        changePin(e.latLng?.lat()!, e.latLng?.lng()!)
        clientRef.current.publish({
          headers: {
            Auth: localStorage.getItem('accessToken') as string,
          },
          destination: pinUrl,
          body: JSON.stringify({
            senderNickname: nickname,
            senderGameId: gameId,
            senderTeamId: teamId,
            submitLat: e.latLng?.lat() as number,
            submitLng: e.latLng?.lng() as number,
            roundNumber: round,
            submitStage: stage,
          }),
        })
      })

      map.addListener('mousemove', function (e: google.maps.MapMouseEvent) {
        const lat = e.latLng?.lat() // 위도
        const lng = e.latLng?.lng() // 경도
        console.log(`위도: ${lat}, 경도: ${lng}`)
      })
      mapObjectRef.current = map
    })
  }, [loader])

  // 소켓 init
  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      clientRef.current.subscribe(subUrl, (message: IMessage) => {
        const mesRes = JSON.parse(message.body) as PinRespoonse
        switch (mesRes.code) {
          case 1115:
            // 핀 위치 변경
            if (
              !myGuess.current ||
              (mesRes.submitLat != myGuess.current?.lat &&
                mesRes.submitLng != myGuess.current?.lng)
            ) {
              changePin(mesRes.submitLat, mesRes.submitLng)
            }
            break
          case 1116:
            // 핀 제출
            // router.push(`/game/${gameId}/${round}/waiting`)
            break
        }
      })
    }

    clientRef.current.onStompError = function (frame: IFrame) {
      console.log('Broker reported error: ' + frame.headers['message'])
      console.log('Additional details: ' + frame.body)
    }

    clientRef.current.activate()

    return () => {
      clientRef.current.deactivate()
    }
  }, [gameId, round])

  function handleSubmitGuess() {
    if (myGuess.current) {
      clientRef.current.publish({
        headers: {
          Auth: localStorage.getItem('accessToken') as string,
        },
        destination: guessUrl,
        body: JSON.stringify({
          senderNickname: nickname,
          senderGameId: gameId,
          senderTeamId: 1,
          submitLat: myGuess.current.lat,
          submitLng: myGuess.current.lng,
          roundNumber: round,
          submitStage: stage,
        }),
      })
    }
  }
  return (
    <>
      <div className={styles.map} ref={mapShowRef} />
      <button
        onClick={handleSubmitGuess}
        className={`${styles.guess}  ${themeStyles[theme + '-inverse']}`}
      >
        제출
      </button>
    </>
  )
}
