import styles from './btn.module.css'

interface Team {
    colorCode: string
    teamNumber: number
    teamGamer: string[]
    ready: boolean
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