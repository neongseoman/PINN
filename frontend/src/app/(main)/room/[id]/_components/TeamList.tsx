'use client'

import styles from './teamList.module.css'
import Team from './Team'

interface TeamGamers {
    colorId: number
    gamerId: string
    teamId: number
    nickname: string
}

interface TeamsProp {
    teams: {
        colorCode: string
        teamNumber: number
        teamGamers: TeamGamers[] | null[]
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