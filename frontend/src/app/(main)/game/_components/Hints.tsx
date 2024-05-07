import styles from './hints.module.css'

interface HintProps {
  hints: string[]
}

export default function Hints({ hints }: HintProps) {
  return (
    <div className={`${styles.hintBox}`}>
      <div className={styles.title}>Hints</div>
      {hints.map((hint, index) => (
        <div className={styles.content} key={index}>
          {hint}
        </div>
      ))}
    </div>
  )
}
