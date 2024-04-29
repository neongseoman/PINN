import styles from './info.module.css'
import themeStyles from '@/app/components/theme.module.css'

interface ThemeInfoProps {
  theme: string
}

export default function ThemeInfo({ theme }: ThemeInfoProps) {
  return <div className={`${styles.info} ${themeStyles[theme]}`}>{theme}</div>
}
