'use client'

import styles from './teamList.module.css'
import Team from './Team'
import { useEffect, useRef, useState } from 'react'
import { Stomp, Client, IFrame, IMessage } from '@stomp/stompjs'

interface TeamsProp {
    teams: {
        teamNumber: number
        teamColor: string
        teamMember: string[]
        isReady: number
    }[]
    handleTeamDoubleClick: (teamNumber: number) => void
}

export default function TeamList({ teams, handleTeamDoubleClick }: TeamsProp) {

    return (
        <div className={styles.container}>
            {teams.map((team) => (
                <Team key={team.teamNumber} team={team} handleTeamDoubleClick={handleTeamDoubleClick} />
            ))}
        </div>
    )
}