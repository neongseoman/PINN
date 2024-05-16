'use client'

import useIngameStore from '@/stores/ingameStore'
import { useEffect, useState } from 'react'
import styles from './result1.module.css'
import Image from 'next/image'
import { useRouter } from 'next/navigation'
import { Loader } from '@googlemaps/js-api-loader'
import RoundResultMap from './_components/RoundResultMap'

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

interface FilteredResult {
  roundNumber: number;
  roundRank: number;
  submitLat: number;
  submitLng: number;
  roundScore: number
}

export default function ResultPage1({ params }: { params: { gameId: string } }) {
  const router = useRouter()
  const { teamId } = useIngameStore()

  const loader = new Loader({
    apiKey: process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string,
    version: 'weekly',
    // ...additionalOptions,
  })

  const [result, setResult] = useState<RoundResult[][]>([])

  useEffect(() => {
    const getGameResult1 = async () => {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/game/result`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('accessToken') as string
              }`,
          },
          body: JSON.stringify({
            gameId: params.gameId,
            round: teamId
          }),
        },
      )

      if (response.ok) {
        const responseData = await response.json()
        console.log(responseData)
        if (responseData.code === 1000) {
          setResult(responseData.result.roundResults)
        } else {
          console.log('팀 목록 출력 실패!', responseData.code)
        }
      } else {
        console.error('팀 목록 요청 통신 실패', response)
      }
    }
    getGameResult1()
  }, [params])

  const myResult = result.map(round =>
    round.filter(r => r.teamId === teamId)
  )

  const filteredResults: FilteredResult[] = myResult.flatMap(round =>
    round.map(result => ({
      roundNumber: result.roundNumber,
      roundRank: result.roundRank,
      submitLat: result.submitLat,
      submitLng: result.submitLng,
      roundScore: result.roundScore
    }))
  );

  const toNext = () => {
    router.push(`/game/${params.gameId}/end/2`)
  }

  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles['map-container']}>
          <Image className={styles.tape1} src='\assets\images\svg\cut1.svg' alt="" width={300} height={300} />
          <Image className={styles.tape2} src='\assets\images\svg\cut2.svg' alt="" width={300} height={300} />
          <Image className={styles.tape3} src='\assets\images\svg\cut3.svg' alt="" width={300} height={300} />
          <Image className={styles.tape4} src='\assets\images\svg\cut4.svg' alt="" width={300} height={300} />
          <div className={styles.word}>지금까지의 여정...</div>
          <div className={styles.mapWrapper}>
            <RoundResultMap params={params} loader={loader} filteredResults={filteredResults} />
          </div>
        </div>
        <div className={styles['result-container']}>
          <Image src='\assets\images\svg\game-icons_open-treasure-chest.svg' alt="" width={200} height={200} />
          <div className={styles.resultWrapper}>
            {filteredResults.map((result) => (
              <div key={result.roundNumber} className={styles.result}>
                <div>{result.roundNumber} Round</div>
                <div>{result.roundScore.toLocaleString()}</div>
              </div>
            ))}
          </div>
          <button className={styles.btn} onClick={toNext}>순위보기</button>
        </div>
      </div>
    </main >
  )
}
