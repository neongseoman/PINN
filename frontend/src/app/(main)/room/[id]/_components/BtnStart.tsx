import styles from './btn.module.css'


interface Prop {
    gameStart: () => void;
}

export default function BtnStart({ gameStart }: Prop) {



    return (
        <div>
            <button className={styles.start} onClick={gameStart}>게임 시작</button>
        </div>
    )
}