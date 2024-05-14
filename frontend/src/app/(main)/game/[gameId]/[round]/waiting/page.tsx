'use client'

import { useEffect, useRef, useState } from 'react'
import styles from './roundResult.module.css'
import { useRouter } from 'next/navigation'
import { Loader } from '@googlemaps/js-api-loader'
import RoundResultMap from './_components/RoundResultMap'
import Timer from '@/components/Timer'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { GameProgressInfo } from '@/types/IngameSocketTypes'


interface RoundResult {
  teamId: number
  roundNumber: number
  roundRank: number
  totalRank: number
  roundScore: number
  totalScore: number
  submitLat: number
  submitLng: number
}

export default function WaitingPage({ params }: { params: { gameId: string; round: string } }) {
  const [count, setCount] = useState(5)
  const router = useRouter()
  const stageTime = 100

  const loader = new Loader({
    apiKey: process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string,
    version: 'weekly',
    // ...additionalOptions,
  })

  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SERVER_SOCKET_URL,
      debug: function (str: string) {
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )

  const ingameSubscribeUrl = `/game/sse/${params.gameId}`
  useEffect(() => {
    // 소켓 연결 시 동작
    clientRef.current.onConnect = function (_frame: IFrame) {
      // 게임 진행 구독
      clientRef.current.subscribe(ingameSubscribeUrl, (message: IMessage) => {
        const gameProgressResponse = JSON.parse(
          message.body,
        ) as GameProgressInfo
        switch (gameProgressResponse.code) {
          case 1204:
            // 스테이지 2 끝
            router.push(`/game/${params.gameId}/${params.round}/result`)
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

  // api받아오기

  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles.round}>라운드 {1}</div>
        <div className={styles.waiting}>
          {/* <Timer stageTime={stageTime} /> */}
        </div>
        <div className={styles.mapWrapper}>
          <RoundResultMap loader={loader} />
        </div>
      </div>
    </main>
  )
}
