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

export default function BtnStartCancel() {

    return (
        <div>
            <button className={styles['start-cancel']}>시작 취소</button>
        </div >
    )
}