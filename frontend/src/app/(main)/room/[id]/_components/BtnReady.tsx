import styles from './btn.module.css'

interface Prop {
    gameReady: () => void;
}

export default function BtnReady({ gameReady }: Prop) {
    return (
        <div>
            <button className={styles.ready} onClick={gameReady}>준비</button>
        </div>
    )
}