'use client'

import useIngameStore from '@/stores/ingameStore'
import { useEffect, useState } from 'react'
import styles from './result2.module.css'
import { useRouter } from 'next/navigation'
import { MdKeyboardDoubleArrowLeft } from "react-icons/md";
import { MdKeyboardDoubleArrowRight } from "react-icons/md";

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

interface Team {
  rank: number;
  teamNumber: number;
  score: number;
  teamColor: string;
}

export default function ResultPage2({ params }: { params: { gameId: string; } }) {
  const router = useRouter()
  const { teamId } = useIngameStore()
  const [result, setResult] = useState<RoundResult[][]>([])
  const teamNum = teamId
  const [teams, setTeams] = useState<Team[]>([]);
  // const myTeam = teams.find((team) =>
  //     team.teamGamers.some((gamer) => gamer?.gamerId === gamerId),
  // )

  useEffect(() => {
    const getGameResult = async () => {
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
          const lastRoundResults = responseData.result.roundResults.slice(-1)[0];
          const sortedTeams = lastRoundResults
            .map((r: RoundResult) => ({
              rank: r.roundRank,
              teamNumber: r.teamId,
              score: r.totalScore,
              teamColor: r.colorCode,
            }))
            .sort((a: Team, b: Team) => b.score - a.score);

          setTeams(sortedTeams);
        } else {
          console.log('팀 목록 출력 실패!', responseData.code)
        }
      } else {
        console.error('팀 목록 요청 통신 실패', response)
      }
    }
    getGameResult()
  }, [params])

  const outToLobby = () => {
    router.push('/lobby')
  }

  const backto = () => {
    router.push(`/game/${params.gameId}/end/1`)
  }

  const rankMapping: { [key: number]: string } = {
    1: "1등! 오늘 저녁은 치킨이닭!",
    2: "2등! '진정한 패배자는 준우승이다.'",
    3: "3등! 여행 좀 다녀보신 분",
    4: "4등! 선택과목 지리 하신 분",
    5: "5등! 그래도 평균 이상!",
    6: "6등! 아쉽게 평균 이하...",
    7: "7등! 분발하셔야겠어요",
    8: "8등! 여행 컨텐츠 좀 보셔야겠어요",
    9: "9등! 길치라는 소리 많이 듣죠?",
    10: "10등! 좀 밖에도 나가고 좀 해라",
  }
  const myTeam = teams.find(team => team.teamNumber === teamNum);
  console.log(teams)

  return (
    <main className={styles.background}>
      <div className={styles.words}>{rankMapping[myTeam!.rank]}</div>
      <div className={styles.container}>
        <div className={styles.button}>
          <button className={styles['button-out']} onClick={() => backto()}><MdKeyboardDoubleArrowLeft size={40} /> 뒤로가기</button>
        </div>
        <div className={styles['rank-container']}>
          {/* 1 ~ 10  단순 등수*/}
          <div className={styles.numbers}>
            {teams.map((team) => (
              <div key={team.rank} className={team.teamNumber === teamNum ? styles.myNumber : styles.number} >
                {team.rank}
              </div>
            ))}
          </div>
          {/* 결과 데이터 뿌리기 */}
          <div className={styles.rank}>
            {teams.map((team, index) => (
              <div key={index} className={styles.team}>
                <div className={team.teamNumber === teamNum ? styles.myTeamResult : styles.teamResult}>TEAM {team.teamNumber}</div>
                <div className={team.teamNumber === teamNum ? styles.myTeamResult : styles.teamResult}>{team.score.toLocaleString()}</div>
                <div className={styles.bar} style={{ width: `${(team.score) / teams[0].score * 100}%`, backgroundColor: team.teamColor }} ></div>
              </div>
            ))}
          </div>
        </div>
        <div className={styles.button}>
          <button className={styles['button-continue']} onClick={() => outToLobby()} >
            로비가기
            <MdKeyboardDoubleArrowRight size={40} />
          </button>
        </div>
      </div>
    </main >
  )
}
