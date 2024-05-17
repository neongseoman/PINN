'use client'

import { GameProgressInfo } from '@/types/IngameSocketTypes'
import { Loader } from '@googlemaps/js-api-loader'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import Image from 'next/image'
import { useRouter } from 'next/navigation'
import { useEffect, useRef, useState } from 'react'
import RoundResultMap from './_components/RoundResultMap'
import styles from './roundResult.module.css'
import useIngameStore from '@/stores/ingameStore'

interface RoundQuestion {
  lat: number
  lng: number
}

interface RoundResult {
  teamId: number
  roundNumber: number
  roundRank: number
  totalRank: number
  roundScore: number
  totalScore: number
  submitLat: number
  submitLng: number
  colorCode: string
}

interface GameInfo {
  roundCount: number
}

export default function RoundResultPage({ params }: { params: { gameId: string; round: string } }) {
  const [roundResult, setRoundResult] = useState<RoundResult[]>([])
  const [roundQuestion, setRoundQuestion] = useState<RoundQuestion>({ lat: 0, lng: 0 })
  const [gameInfo, setGameInfo] = useState<GameInfo>()
  const { teamId } = useIngameStore()
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

  useEffect(() => {
    const getGameInfo = async () => {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/room/${params.gameId}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('accessToken') as string
              }`,
          },
        },
      )

      if (response.ok) {
        const responseData = await response.json()
        if (responseData.code === 1000) {
          setGameInfo(responseData.result)
        } else {
          console.log('팀 목록 출력 실패!', responseData.code)
        }
      } else {
        console.error('팀 목록 요청 통신 실패', response)
      }
    }

    const roundResultList = async () => {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/game/round/result`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('accessToken') as string
              }`,
          },
          body: JSON.stringify({
            gameId: params.gameId,
            round: params.round,
          }),
        },
      )

      if (response.ok) {
        const responseData = await response.json()
        if (responseData.code === 1000) {
          setRoundResult(responseData.result.roundResult)
          setRoundQuestion(responseData.result.question)
        } else {
          console.log('라운드 결과 출력 실패!', responseData.code)
          alert(responseData.message)
        }
      } else {
        console.error('라운드 결과 요청 통신 실패', response)
      }
    }
    getGameInfo()
    roundResultList()
  }, [params])

  const ingameSubscribeUrl = `/game/sse/${params.gameId}`

  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      // 게임 진행 구독
      clientRef.current.subscribe(ingameSubscribeUrl, (message: IMessage) => {
        const gameProgressResponse = JSON.parse(
          message.body,
        ) as GameProgressInfo
        switch (gameProgressResponse.code) {
          case 1206:
            const totalRounds = gameInfo?.roundCount ?? 0
            if (parseInt(params.round) + 1 <= totalRounds) {
              router.push(`/game/${params.gameId}/${parseInt(params.round) + 1}`);
            }
            else {
              router.push(`/game/${params.gameId}/end/1`)
            }
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
  }, [params, gameInfo])

  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles.round}>라운드 {params.round}</div>
        <div className={styles.result}>Result</div>
        <div className={styles.mapWrapper}>
          <RoundResultMap
            params={params}
            loader={loader}
            roundResult={roundResult}
            roundQuestion={roundQuestion}
          />
        </div>
        <div className={styles['rank-container']}>
          <div className={styles.trophy}>
            {roundResult.slice(0, 3).map((result, index) => (
              <Image
                key={index}
                src={`/assets/images/svg/noto_${index + 1}-place-medal.svg`}
                alt=""
                width={28}
                height={28}
              />
            ))}
          </div>
          <div className={styles.rank}>
            {roundResult
              .sort((a, b) => a.roundRank - b.roundRank)
              .map((result) => (
                <div key={result.teamId} className={styles.teamList}>
                  <div className={result.teamId === teamId ? styles.myTeam : ''}>
                    {result.roundRank}. TEAM {result.teamId}
                  </div>
                  <div>{result.roundScore.toLocaleString()}</div>
                </div>
              ))}
          </div>
        </div>
      </div>
    </main>
  )
}
