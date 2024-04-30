'use client'

import styles from './room.module.css'

import TeamList from './_components/TeamList'
import Option from './_components/option'
import { useState } from 'react'

interface Team {
  teamNumber: number
  teamColor: string
  teamMember: string[]
  isReady: number
}

export default function RoomPage({ params }: { params: { id: number } }) {
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

  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles['option-team-container']}>
          <Option></Option>
          <TeamList teams={teams} ></TeamList>
        </div>
        <div className={styles['chat-ready-out']}>
          <div className={styles.chat}>chatting</div>
          <div className={styles['ready-out']}>
            <button className={styles.ready}>준비</button>
            <button className={styles.out}>나가기</button>
          </div>
        </div>
      </div>
    </main >
  )
}
