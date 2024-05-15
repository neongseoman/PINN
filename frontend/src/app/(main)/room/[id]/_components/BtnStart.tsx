import styles from './btn.module.css'


// interface TeamGamers {
//     colorId: number
//     gamerId: string
//     teamId: number
//     nickname: string
// }

interface TeamsProp {
    // teams: {
    //     colorCode: string
    //     teamNumber: number
    //     teamGamer: TeamGamers[]
    //     ready: boolean
    // }[]
    gameStart: () => void;
}

export default function BtnStart({ gameStart }: TeamsProp) {



    return (
        <div>
            <button className={styles.start} onClick={gameStart}>게임 시작</button>
        </div>
    )
}