'use client'

import useIngameStore from '@/stores/ingameStore'
import { RoundWait } from '@/types/IngameRestTypes'
import { IngameMapRespoonse } from '@/types/IngameSocketTypes'
import { getTeamPins } from '@/utils/IngameApi'
import { MapCenter } from '@/utils/MapPosition'
import { Loader } from '@googlemaps/js-api-loader'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { useEffect, useRef } from 'react'
import styles from '../roundWaiting.module.css'

interface TeamPins {
  [teamId: number]: google.maps.Marker
}

export default function RoundWaitMap({
  params,
  loader,
}: {
  params: { gameId: string; round: string }
  loader: Loader
}) {
  const mapRef = useRef<HTMLDivElement | null>(null)
  const mapObjectRef = useRef<google.maps.Map | null>(null)
  const TeamPinsRef = useRef<TeamPins>({})
  const { teamId, theme } = useIngameStore()

  useEffect(() => {
    // 타 팀 핀 위치 REST API
    loader.importLibrary('maps').then(async () => {
      const position = MapCenter[theme]
      const { Map } = (await google.maps.importLibrary(
        'maps',
      )) as google.maps.MapsLibrary

      const map = new Map(mapRef.current!, {
        center: position,
        disableDefaultUI: true,
        clickableIcons: false,
        zoom: 5,
      })

      mapObjectRef.current = map
    })
    getRoundWaitInfo()
  }, [])

  async function getRoundWaitInfo() {
    const teamPinsInfo = (await getTeamPins(
      params.gameId,
      params.round,
    )) as RoundWait
    teamPinsInfo.result.teamPins.forEach((teamPin) => {
      changeTeamPins(
        teamPin.submitLat,
        teamPin.submitLng,
        teamPin.teamId,
        teamPin.colorCode,
      )
    })
    return teamPinsInfo.result.teamPins[teamId]
  }

  // 소켓
  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SERVER_SOCKET_URL,
      debug: function (str: string) {},
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )

  const roundWaitUrl = `/guess/${params.gameId}`
  useEffect(() => {
    // 소켓 연결 시 동작
    clientRef.current.onConnect = function (_frame: IFrame) {
      // 게임 진행 구독
      clientRef.current.subscribe(roundWaitUrl, (message: IMessage) => {
        const roundWaitResponse = JSON.parse(message.body) as IngameMapRespoonse
        switch (roundWaitResponse.code) {
          case 1115:
            // 팀 핀 변경
            changeTeamPins(
              roundWaitResponse.submitLat,
              roundWaitResponse.submitLng,
              roundWaitResponse.senderTeamId,
              roundWaitResponse.colorCode,
            )
            break
        }
      })
    }

    clientRef.current.onStompError = function (frame: IFrame) {
      console.log('스톰프 에러: ' + frame.headers['message'])
      console.log('추가 정보: ' + frame.body)
    }

    clientRef.current.activate()

    return () => {
      clientRef.current.deactivate()
    }
  }, [params.gameId, params.round])

  function changeTeamPins(
    lat: number,
    lng: number,
    pinTeamId: number,
    colorCode: string,
  ) {
    if (TeamPinsRef.current[pinTeamId]) {
      TeamPinsRef.current[pinTeamId].setMap(null)
      delete TeamPinsRef.current[pinTeamId]
    }

    const newPin = new google.maps.Marker({
      position: { lat, lng },
      map: mapObjectRef.current,
      icon: {
        path: google.maps.SymbolPath.CIRCLE,
        scale: pinTeamId !== teamId ? 5 : 8,
        fillColor: colorCode,
        fillOpacity: 1,
        strokeColor: pinTeamId !== teamId ? 'black' : 'yellow',
        strokeWeight: pinTeamId !== teamId ? 1 : 2,
      },
    })

    TeamPinsRef.current[pinTeamId] = newPin
  }

  return (
    <>
      <div className={styles.map} ref={mapRef} />
    </>
  )
}
