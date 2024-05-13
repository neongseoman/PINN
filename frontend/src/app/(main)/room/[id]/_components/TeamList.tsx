'use client'

import styles from './teamList.module.css'
import Team from './Team'
import { useEffect, useRef, useState } from 'react'
import { Stomp, Client, IFrame, IMessage } from '@stomp/stompjs'

interface TeamsProp {
    teams: {
        colorCode: string
        teamNumber: number
        teamGamer: string[]
        ready: boolean
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