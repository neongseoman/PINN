'use client'

import { useRouter } from 'next/navigation'
import { useEffect, useState } from 'react'
import styles from './result2.module.css'

export default function ResultPage2({ params }: { params: { id: number } }) {
  const router = useRouter()
  // 더미
  const teamNum = 1
  const [teams, setTeams] = useState([
    {
      rank: 1,
      teamNumber: 1,
      score: 15000,
      teamColor: 'rgba(255, 0, 61, 0.7)',
    },
    {
      rank: 2,
      teamNumber: 2,
      score: 14000,
      teamColor: 'rgba(182, 53, 53, 0.7)',
    },
    {
      rank: 3,
      teamNumber: 3,
      score: 13000,
      teamColor: 'rgba(255, 111, 0, 0.7)',
    },
    {
      rank: 4,
      teamNumber: 4,
      score: 12000,
      teamColor: 'rgba(153, 155, 41, 0.7)',
    },
    {
      rank: 5,
      teamNumber: 5,
      score: 11000,
      teamColor: 'rgba(0, 131, 143, 0.7)',
    },
    {
      rank: 6,
      teamNumber: 6,
      score: 10000,
      teamColor: 'rgba(105, 53, 170, 0.7)',
    },
    {
      rank: 7,
      teamNumber: 7,
      score: 9000,
      teamColor: 'rgba(251, 52, 159, 0.7)',
    },
    {
      rank: 8,
      teamNumber: 8,
      score: 8000,
      teamColor: 'rgba(255, 172, 207, 0.7)',
    },
    {
      rank: 9,
      teamNumber: 9,
      score: 7000,
      teamColor: 'rgba(188, 157, 157, 0.7)',
    },
    {
      rank: 10,
      teamNumber: 10,
      score: 5000,
      teamColor: 'rgba(85, 85, 85, 0.7)',
    },
  ])

  useEffect(() => {
    // 게임결과api
  }, [])
  // [추가 필요] 현재 유저의 팀이 어디에 속해있는지 teamNum 계산

  const outToLobby = () => {
    router.push('/lobby')
  }

  const continueGame = (roomId: number) => {
    router.push(`/room/${roomId}`)
  }

  return (
    <main className={styles.background}>
      <div className={styles.words}>?등! ??????????</div>
      <div className={styles.container}>
        <div className={styles.button}>
          <button className={styles['button-out']} onClick={() => outToLobby()}>
            &lt;&minus; 방나가기
          </button>
        </div>
        <div className={styles['rank-container']}>
          {/* 1 ~ 10  단순 등수*/}
          <div className={styles.numbers}>
            {teams.map((team) => (
              <div
                key={team.rank}
                className={
                  team.teamNumber === teamNum ? styles.myNumber : styles.number
                }
              >
                {team.rank}
              </div>
            ))}
          </div>
          {/* 결과 데이터 뿌리기 */}
          <div className={styles.rank}>
            {teams.map((team, index) => (
              <div key={index} className={styles.team}>
                <div
                  className={
                    team.teamNumber === teamNum
                      ? styles.myTeamResult
                      : styles.teamResult
                  }
                >
                  TEAM {team.teamNumber}
                </div>
                <div
                  className={
                    team.teamNumber === teamNum
                      ? styles.myTeamResult
                      : styles.teamResult
                  }
                >
                  {team.score.toLocaleString()}
                </div>
                <div
                  className={styles.bar}
                  style={{
                    width: `${(team.score / teams[0].score) * 100}%`,
                    backgroundColor: team.teamColor,
                  }}
                ></div>
              </div>
            ))}
          </div>
        </div>
        <div className={styles.button}>
          <button
            className={styles['button-continue']}
            onClick={() => continueGame(1)}
          >
            계속하기 &minus;&gt;
          </button>
        </div>
      </div>
    </main>
  )
}
