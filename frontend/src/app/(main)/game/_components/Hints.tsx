import themeStyles from '@/app/components/theme.module.css'
import styles from './hints.module.css'

interface HintProps {
  theme: string
  hints: string[]
}

export default function Hints({ theme, hints }: HintProps) {
  return (
    <div className={`${styles.hintBox} ${themeStyles[theme]}`}>
      <div className={styles.title}>Hints</div>
      {hints.map((hint, index) => (
        <div className={styles.content} key={index}>
          {hint}
        </div>
      ))}
    </div>
  )
}
