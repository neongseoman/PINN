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

export default function BtnReady({ teams }: TeamsProp) {
    const handleReadyClick = () => {
    }
    return (
        <div>
            <button className={styles.ready} onClick={handleReadyClick}>준비</button>
        </div>
    )
}