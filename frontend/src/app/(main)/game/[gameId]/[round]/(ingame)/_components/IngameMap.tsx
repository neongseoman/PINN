'use client'

import themeStyles from '@/components/theme.module.css'
import useIngameStore from '@/stores/ingameStore'
import useUserStore from '@/stores/userStore'
import { IngameMapRespoonse } from '@/types/IngameSocketTypes'
import { MapCenter } from '@/utils/MapPosition'
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
  soundOn: boolean
}

interface MyGuess {
  lat: number
  lng: number
}

interface CursorState {
  [nickname: string]: google.maps.Marker
}
export default function IngameMap({
  theme,
  loader,
  gameId,
  round,
  stage,
  soundOn,
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

  const cursorListRef = useRef<CursorState>({})

  const { nickname } = useUserStore()
  const { teamId, teamColor } = useIngameStore()

  const subUrl = `/team/${gameId}/${teamId}`
  const guessUrl = `/app/team/guess`
  const pinUrl = '/app/team/pin'
  const cursorUrl = '/app/team/cursor'

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
        fillColor: teamColor,
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

  function changeCursor(lat: number, lng: number, senderNickname: string) {
    if (senderNickname == nickname) {
      return
    }
    if (cursorListRef.current[senderNickname]) {
      // 기존에 같은 닉네임의 커서가 있으면 제거
      cursorListRef.current[senderNickname].setMap(null)
      delete cursorListRef.current[senderNickname]
    }

    // 새 커서 추가
    const newCursor = new google.maps.Marker({
      position: { lat, lng },
      map: mapObjectRef.current,
      icon: {
        path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
        rotation: -45,
        scale: 3,
        fillColor: 'blue',
        fillOpacity: 1,
        strokeColor: 'black',
        strokeWeight: 1,
      },
    })

    // 새 커서를 리스트에 추가
    cursorListRef.current[senderNickname] = newCursor
  }

  const hoverSound = () => {
    const audio = new Audio('/assets/sounds/hover.wav')
    if (soundOn) {
      audio.play()
    }
  }

  const clickSound = () => {
    const audio = new Audio('/assets/sounds/click.mp3')
    if (soundOn) {
      audio.play()
    }
  }
  // 지도 init
  useEffect(() => {
    loader.importLibrary('maps').then(async () => {
      // 서울 중심
      const position = MapCenter[theme]
      const { Map } = (await google.maps.importLibrary(
        'maps',
      )) as google.maps.MapsLibrary
      const allowedBounds = {
        north: 85,   // 북쪽 경계
        south: -85,  // 남쪽 경계
        west: -179.9,  // 서쪽 경계
        east: 179.9    // 동쪽 경계
      }

      const map = new Map(mapShowRef.current!, {
        center: position,
        disableDefaultUI: true,
        clickableIcons: false,
        zoom: 5,
        restriction: {  // 지도 범위 제한 설정
          latLngBounds: allowedBounds,
          strictBounds: true,  // 지도 범위를 엄격하게 제한할지 여부
        },
      })

      // 지도에 특정 지점을 클릭 했을 때
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

      // 지도에서 마우스를 움직일 때
      map.addListener('mousemove', function (e: google.maps.MapMouseEvent) {
        clientRef.current.publish({
          headers: {
            Auth: localStorage.getItem('accessToken') as string,
          },
          destination: cursorUrl,
          body: JSON.stringify({
            senderNickname: nickname,
            senderGameId: gameId,
            senderTeamId: teamId,
            lat: e.latLng?.lat(),
            lng: e.latLng?.lng(),
          }),
        })
      })
      mapObjectRef.current = map
    })
  }, [loader, nickname, teamId, gameId, round])

  // 소켓 init
  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      clientRef.current.subscribe(subUrl, (message: IMessage) => {
        const mesRes = JSON.parse(message.body) as IngameMapRespoonse
        switch (mesRes.code) {
          case 1115:
            // 핀 위치 변경
            if (mesRes.senderNickname != nickname) {
              changePin(mesRes.submitLat, mesRes.submitLng)
            }
            break
          case 1116:
            // 핀 제출
            router.push(`/game/${gameId}/${round}/waiting`)
            break
          case 1120:
            // 실시간 커서
            if (mesRes.senderTeamId == teamId) {
              changeCursor(mesRes.lat, mesRes.lng, mesRes.senderNickname)
            }
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
  }, [gameId, round, nickname])

  function handleSubmitGuess() {
    clickSound()
    if (myGuess.current) {
      clientRef.current.publish({
        headers: {
          Auth: localStorage.getItem('accessToken') as string,
        },
        destination: guessUrl,
        body: JSON.stringify({
          senderNickname: nickname,
          senderGameId: gameId,
          senderTeamId: teamId,
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
        onMouseEnter={hoverSound}
      >
        제출
      </button>
    </>
  )
}
