'use client'

import styles from './teamList.module.css'
import Team from './Team'
import { useState } from 'react'

interface Team {
    teamNumber: number
    teamColor: string
    teamMember: string[]
}

export default function TeamList() {
    const [teams, setTeams] = useState<Team[]>([
        { teamNumber: 1, teamColor: 'rgba()', teamMember: ['테스트', '', ''] },
        { teamNumber: 2, teamColor: 'rgba()', teamMember: ['', '테스트', ''] },
        { teamNumber: 3, teamColor: 'rgba()', teamMember: ['', '', ''] },
        { teamNumber: 4, teamColor: 'rgba()', teamMember: ['', '테스트', ''] },
        { teamNumber: 5, teamColor: 'rgba()', teamMember: ['', '', ''] },
        { teamNumber: 6, teamColor: 'rgba()', teamMember: ['', '', ''] },
        { teamNumber: 7, teamColor: 'rgba()', teamMember: ['', '', ''] },
        { teamNumber: 8, teamColor: 'rgba()', teamMember: ['', '', ''] },
        { teamNumber: 9, teamColor: 'rgba()', teamMember: ['', '', ''] },
        { teamNumber: 10, teamColor: 'rgba()', teamMember: ['', '', ''] },
    ])

    return (
        <div className={styles.container}>
            {teams.map((team) => (
                <Team key={team.teamNumber} team={team} />
            ))}
        </div>
    )
}