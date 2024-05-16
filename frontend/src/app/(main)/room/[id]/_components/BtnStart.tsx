import styles from './btn.module.css'


interface Prop {
    gameStart: () => void;
    gameReady: () => void;
}

export default function BtnStart({ gameStart, gameReady }: Prop) {



    return (
        <div className={styles.btnContainer}>
            <button className={styles.readyLeader} onClick={gameReady}>준비</button>
            <button className={styles.start} onClick={gameStart}>게임 시작</button>
        </div>
    )
}