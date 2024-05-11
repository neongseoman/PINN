import { Hint } from '@/types/IngameRestTypes'
import styles from './hints.module.css'

interface HintProps {
  hints: Hint[] | null
}

export default function Hints({ hints }: HintProps) {
  return (
    <div className={`${styles.hintBox}`}>
      <div className={styles.title}>Hints</div>
      {!!hints &&
        hints.map((hint, index) => (
          <div className={styles.content} key={index}>
            {hint.hintValue}
          </div>
        ))}
    </div>
  )
}
