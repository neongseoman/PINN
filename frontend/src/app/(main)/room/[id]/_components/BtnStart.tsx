import styles from './btn.module.css'

interface TeamsProp {
    teams: {
        colorCode: string
        teamNumber: number
        teamGamer: string[]
        ready: boolean
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