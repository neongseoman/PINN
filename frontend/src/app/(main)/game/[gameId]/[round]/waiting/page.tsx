'use client'

import Timer from '@/components/Timer'
import { GameProgressInfo } from '@/types/IngameSocketTypes'
import { Loader } from '@googlemaps/js-api-loader'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { useRouter } from 'next/navigation'
import { useEffect, useRef, useState } from 'react'
import RoundResultMap from './_components/RoundResultMap'
import styles from './roundResult.module.css'

export default function WaitingPage({
  params,
}: {
  params: { gameId: string; round: string }
}) {
  const [remainSeconds, setRemainSeconds] = useState<number>(30)
  const [stage, setStage] = useState<string>()
  const router = useRouter()

  const loader = new Loader({
    apiKey: process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string,
    version: 'weekly',
    // ...additionalOptions,
  })

  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SERVER_SOCKET_URL,
      debug: function (str: string) { },
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

          case 1210:
            // 현재 페이지와 라운드가 다를 경우
            if (gameProgressResponse.round != Number(params.round)) {
              router.push(
                `/game/${params.gameId}/${gameProgressResponse.round}`,
              )
              return
            }
            setRemainSeconds(gameProgressResponse.leftTime)
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
        <div className={styles.round}>라운드 {params.round}</div>
        <div className={styles.waiting}>
          <Timer remainSeconds={remainSeconds} />
        </div>
        <div className={styles.mapWrapper}>
          <RoundResultMap params={params} loader={loader} />
        </div>
      </div>
    </main>
  )
}
