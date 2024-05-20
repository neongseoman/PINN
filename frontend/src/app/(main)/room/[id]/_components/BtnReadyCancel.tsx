import styles from './btn.module.css'

interface Prop {
    gameReady: () => void;
}

export default function BtnReadyCancel({ gameReady }: Prop) {
    return (
        <div>
            <button className={styles['ready-cancel']} onClick={gameReady}>준비 완료</button>
        </div>
    )
}
