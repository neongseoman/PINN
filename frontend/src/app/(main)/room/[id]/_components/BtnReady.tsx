import styles from './btn.module.css'

// interface TeamGamers {
//     colorId: number
//     gamerId: string
//     teamId: number
//     nickname: string
// }

// interface TeamsProp {
//     teams: {
//         colorCode: string
//         teamNumber: number
//         teamGamers: TeamGamers[]
//         ready: boolean
//     }[]
// }

export default function BtnReady() {
    const handleReadyClick = () => {
    }
    return (
        <div>
            <button className={styles.ready} onClick={handleReadyClick}>준비</button>
        </div>
    )
}