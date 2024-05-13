import themeStyles from '@/components/theme.module.css'
import styles from './info.module.css'

interface GameInfoProps {
  theme: string
  round: number
  stage: number
}

export default function GameInfo({ theme, round, stage }: GameInfoProps) {
  return (
    <div className={`${styles.info} ${themeStyles[theme]}`}>
      Round {round} - Stage {stage}
    </div>
  )
}
