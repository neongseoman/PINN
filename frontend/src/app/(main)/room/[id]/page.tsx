'use client'

import styles from './room.module.css'

import useUserStore from '@/stores/userStore'
import Chatting from '@/components/Chatting'
import TeamList from './_components/TeamList'
import Option from './_components/Option'
import BtnReady from './_components/BtnReady'
import BtnReadyCancel from './_components/BtnReadyCancel'
import BtnStart from './_components/BtnStart'
import BtnStartCancel from './_components/BtnStartCancel'

import { useEffect, useState } from 'react'

interface Team {
  teamNumber: number
  teamColor: string
  teamMember: string[]
  isReady: number
}

export default function RoomPage({ params }: { params: { id: number } }) {
  const { gamerId, nickname } = useUserStore()

  const [teams, setTeams] = useState<Team[]>([
    { teamNumber: 1, teamColor: 'rgba(255, 0, 61, 1)', teamMember: ['', '', ''], isReady: 0 },
    { teamNumber: 2, teamColor: 'rgba(182, 53, 53, 1)', teamMember: ['', '', ''], isReady: 0 },
    { teamNumber: 3, teamColor: 'rgba(255, 111, 0, 1)', teamMember: ['', '', ''], isReady: 0 },
    { teamNumber: 4, teamColor: 'rgba(153, 155, 41, 1)', teamMember: ['', '', ''], isReady: 0 },
    { teamNumber: 5, teamColor: 'rgba(0, 131, 143, 1)', teamMember: ['', '', ''], isReady: 0 },
    { teamNumber: 6, teamColor: 'rgba(105, 53, 170, 1)', teamMember: ['', '', ''], isReady: 0 },
    { teamNumber: 7, teamColor: 'rgba(251, 52, 159, 1)', teamMember: ['', '', ''], isReady: 0 },
    { teamNumber: 8, teamColor: 'rgba(255, 172, 207, 1)', teamMember: ['', '', ''], isReady: 0 },
    { teamNumber: 9, teamColor: 'rgba(188, 157, 157, 1)', teamMember: ['', '', ''], isReady: 0 },
    { teamNumber: 10, teamColor: 'rgba(85, 85, 85, 1)', teamMember: ['', '', ''], isReady: 0 },
  ])

  const chatTitle = '전체 채팅'
  const subscribeUrl = `/game/${params.id}`
  const publishUrl = `/app/game/chat/${params.id}`


  useEffect(() => {
    // nickname이 존재하고, 팀에 아직 배정되지 않았을 때에만 처리
    if (nickname && teams.every(team => team.teamMember.every(member => member !== nickname))) {
      // 빈 자리를 가진 팀을 찾아 nickname을 배정
      let updatedTeams = [...teams];
      for (let i = 0; i < updatedTeams.length; i++) {
        const team = updatedTeams[i];
        // 해당 팀에 nickname이 이미 배정된 경우 무시
        if (team.teamMember.includes(nickname)) {
          continue;
        }
        // 빈 자리를 찾으면 nickname을 배정하고 종료
        const emptyIndex = team.teamMember.findIndex(member => member === '');
        if (emptyIndex !== -1) {
          const updatedTeamMember = [...team.teamMember];
          updatedTeamMember[emptyIndex] = nickname;
          updatedTeams[i] = { ...team, teamMember: updatedTeamMember };
          break;
        }
      }

      setTeams(updatedTeams);
    }
  }, [teams, nickname]);

  const isTeamReady = teams.some(team => team.teamNumber === 1 && team.isReady === 0);

  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles['option-team-container']}>
          <Option></Option>
          <TeamList teams={teams} ></TeamList>
        </div>
        <div className={styles['chat-ready-out']}>
          <div className={styles.chat}>
            <Chatting
              chatTitle={chatTitle}
              subsrcibeUrl={subscribeUrl}
              publishUrl={publishUrl}
            />
          </div>
          <div className={styles['ready-out']}>
            {isTeamReady ? (
              <BtnReady teams={teams} setTeams={setTeams} />
            ) : (
              <BtnReadyCancel teams={teams} setTeams={setTeams} />
            )}
            <button className={styles.out}>나가기</button>
          </div>
        </div>
      </div>
    </main >
  )
}
