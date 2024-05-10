'use client'

import themeStyles from '@/components/theme.module.css'
import useUserStore from '@/stores/userStore'
import { PinRespoonse } from '@/types/IngameTypes'
import { Loader } from '@googlemaps/js-api-loader'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { useEffect, useRef } from 'react'
import styles from './ingamemap.module.css'

interface IngameMapProps {
  theme: string
  loader: Loader
  gameId: string
  round: string
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
}: IngameMapProps) {
  const mapRef = useRef<any>()
  const currentMarker = useRef<google.maps.Marker | null>(null)
  const myGuess = useRef<MyGuess | null>(null)

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

  const subUrl = `/team/${gameId}/1`
  // const subUrl = `/guess/${gameId}`
  const guessUrl = `/app/team/guess`
  const pinUrl = '/app/team/pin'

  function changePin(lat: number, lng: number) {
    if (currentMarker.current) {
      currentMarker.current.setMap(null)
    }
    const newMarker = new google.maps.Marker({
      position: { lat, lng },
      map: mapRef.current,
      icon: {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 10,
        fillColor: 'red',
        fillOpacity: 1,
        strokeColor: 'black',
        strokeWeight: 3,
      },
    })

    currentMarker.current = newMarker
    myGuess.current = {
      lat,
      lng,
    }
  }

  useEffect(() => {
    loader.importLibrary('maps').then(async () => {
      const position = { lat: 37.5642135, lng: 127.0016985 }
      const { Map } = (await google.maps.importLibrary(
        'maps',
      )) as google.maps.MapsLibrary

      const map = new Map(mapRef.current, {
        center: position,
        disableDefaultUI: true,
        clickableIcons: false,
        zoom: 8,
      })
      map.addListener('click', (e: google.maps.MapMouseEvent) => {
        if (currentMarker.current) {
          currentMarker.current.setMap(null)
        }
        const newMarker = new google.maps.Marker({
          position: e.latLng!,
          map: map,
          icon: {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 10,
            fillColor: 'red',
            fillOpacity: 1,
            strokeColor: 'black',
            strokeWeight: 3,
          },
        })

        currentMarker.current = newMarker
        myGuess.current = {
          lat: e.latLng?.lat() as number,
          lng: e.latLng?.lng() as number,
        }
        clientRef.current.publish({
          headers: {
            Auth: localStorage.getItem('accessToken') as string,
          },
          destination: pinUrl,
          body: JSON.stringify({
            senderNickname: nickname,
            senderGameId: gameId,
            senderTeamId: 1,
            submitLat: e.latLng?.lat() as number,
            submitLng: e.latLng?.lng() as number,
            roundNumber: round,
            submitStage: 2,
          }),
        })
      })

      map.addListener('mousemove', function (e: google.maps.MapMouseEvent) {
        const lat = e.latLng?.lat() // 위도
        const lng = e.latLng?.lng() // 경도
        console.log(`위도: ${lat}, 경도: ${lng}`)
      })
    })
  }, [loader])

  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      clientRef.current.subscribe(subUrl, (message: IMessage) => {
        const mesRes = JSON.parse(message.body) as PinRespoonse
        switch (mesRes.code) {
          case 1115:
            // 핀 위치 변경
            if (
              mesRes.submitLat != myGuess.current?.lat &&
              mesRes.submitLng != myGuess.current?.lng
            ) {
              changePin(mesRes.submitLat, mesRes.submitLng)
            }
            break
          case 1116:
            // 핀 제출
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
  }, [gameId])

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
          submitLat: 38.2222,
          submitLng: 125.123,
          roundNumber: round,
          submitStage: 1,
        }),
      })
    }
  }
  return (
    <>
      <div className={styles.map} ref={mapRef} />
      <button
        onClick={handleSubmitGuess}
        className={`${styles.guess}  ${themeStyles[theme + '-inverse']}`}
      >
        제출
      </button>
    </>
  )
}
