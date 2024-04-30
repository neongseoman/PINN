import styles from './ingamemap.module.css'
import themeStyles from '@/app/components/theme.module.css'

interface IngameMapProps {
  theme: string
}

export default function IngameMap({ theme }: IngameMapProps) {
  return (
    <>
      <div className={styles.map}>구글지도</div>
      <button className={`${styles.guess}  ${themeStyles[theme + '-inverse']}`}>
        제출
      </button>
    </>
  )
}
