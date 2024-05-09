import styles from './btn.module.css'

interface Team {
    teamNumber: number
    teamColor: string
    teamMember: string[]
    isReady: number
}


interface TeamsProp {
    teams: Team[];
}

export default function BtnReadyCancel({ teams }: TeamsProp) {
    const handleReadyClick = () => {
    }


    return (
        <div>
            <button className={styles['ready-cancel']}>준비 완료</button>
        </div>
    )
}