'use client'

import Image from 'next/image'
import { useRouter } from 'next/navigation'
import { useEffect, useState } from 'react'
import styles from './result1.module.css'

export default function ResultPage1({ params }: { params: { id: number } }) {
  const router = useRouter()
  // 게임결과 더미
  const [teams, setTeams] = useState([
    { round: 1, result: 20, score: 5000 },
    { round: 2, result: 50, score: 3000 },
    { round: 3, result: 100, score: 1000 },
  ])

  useEffect(() => {
    // 게임결과api
  }, [])

  // [추가 필요] 현재 유저의 팀이 어디에 속해있는지 teamNum 계산

  const toNext = () => {
    router.push(`/result/${params.id}/2`)
  }

  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles['map-container']}>
          <Image
            className={styles.tape1}
            src="\assets\images\svg\cut1.svg"
            alt=""
            width={300}
            height={300}
          />
          <Image
            className={styles.tape2}
            src="\assets\images\svg\cut2.svg"
            alt=""
            width={300}
            height={300}
          />
          <Image
            className={styles.tape3}
            src="\assets\images\svg\cut3.svg"
            alt=""
            width={300}
            height={300}
          />
          <Image
            className={styles.tape4}
            src="\assets\images\svg\cut4.svg"
            alt=""
            width={300}
            height={300}
          />
          <div className={styles.word}>지금까지의 여정...</div>
          <div className={styles.map}></div> {/* 지도 api 받아오기 */}
        </div>
        <div className={styles['result-container']}>
          <Image
            src="\assets\images\svg\game-icons_open-treasure-chest.svg"
            alt=""
            width={200}
            height={200}
          />
          {teams.map((team) => (
            <div key={team.round} className={styles.result}>
              <div>
                {team.round}R - {team.result}KM
              </div>
              <div>{team.score}</div>
            </div>
          ))}
          <button className={styles.btn} onClick={toNext}>
            순위보기
          </button>
        </div>
      </div>
    </main>
  )
}
