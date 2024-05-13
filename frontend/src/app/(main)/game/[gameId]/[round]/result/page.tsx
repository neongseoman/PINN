'use client'

import { useEffect, useState } from 'react'
import styles from './roundResult.module.css'
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
}

export default function LoadingPage({ params }: { params: { id: number } }) {
  const [roundResult, setRoundResult] = useState<RoundResult[]>([])
  const [count, setCount] = useState(5)
  const router = useRouter()

  const loader = new Loader({
    apiKey: process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string,
    version: 'weekly',
    // ...additionalOptions,
  })

  useEffect(() => {
    const roundResultList = async () => {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/round/result`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('accessToken') as string
              }`,
          },
        },
      )

      if (response.ok) {
        console.log('라운드 결과 요청 통신 성공')
        const responseData = await response.json()
        if (responseData.code === 1000) {
          console.log('라운드 결과 출력 성공!', responseData)
          setRoundResult(responseData.result.roundResult)
        } else {
          console.log('라운드 결과 출력 실패!', responseData.code)
          alert(responseData.message)
        }
      } else {
        console.error('라운드 결과 요청 통신 실패', response)
      }
    }
    roundResultList()
  }, [])

  useEffect(() => {
    const timer = setInterval(() => {
      setCount((prevCount) => prevCount - 1)
    }, 1000)

    if (count === 0) {
      clearInterval(timer)
    }

    return () => clearInterval(timer)
  }, [count, router])

  // api받아오기

  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles.round}>라운드 {1}</div>
        <div className={styles.result}>Result</div>
        <div className={styles.mapWrapper}>
          <RoundResultMap loader={loader} roundResult={roundResult} />
        </div>
        <div className={styles['rank-container']}>
          <div className={styles.trophy}>
            {roundResult.slice(0, 3).map((result, index) => (
              <Image key={index} src={`/assets/images/svg/noto_${index + 1}-place-medal.svg`} alt="" width={28} height={28} />
            ))}
          </div>
          <div className={styles.rank}>
            {roundResult
              .sort((a, b) => a.roundRank - b.roundRank)
              .map(result => (
                <div key={result.teamId} className={styles.teamList}>
                  <div>{result.roundRank} TEAM {result.teamId}</div>
                  <div>{result.roundScore.toLocaleString()}</div>
                </div>
              ))
            }
          </div>
        </div>
      </div>
    </main>
  )
}
