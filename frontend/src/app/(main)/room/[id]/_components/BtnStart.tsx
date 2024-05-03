import styles from './btn.module.css'

interface TeamsProp {
    teams: {
        teamNumber: number
        teamColor: string
        teamMember: string[]
        isReady: number
    }[]
    setTeams: React.Dispatch<React.SetStateAction<TeamsProp[]>>;
}

export default function BtnStart() {
    return (
        <div>
            <button className={styles.start}>게임 시작</button>
        </div>
    )
}