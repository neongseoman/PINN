import themeStyles from '@/components/theme.module.css'
import styles from './info.module.css'

interface ThemeInfoProps {
  theme: string
}

export default function ThemeInfo({ theme }: ThemeInfoProps) {
  return <div className={`${styles.info} ${themeStyles[theme]}`}>{theme}</div>
}
