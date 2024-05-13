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

export default function BtnStartCancel() {

    return (
        <div>
            <button className={styles['start-cancel']}>시작 취소</button>
        </div >
    )
}