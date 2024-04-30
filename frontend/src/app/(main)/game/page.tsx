import Timer from '@/app/components/Timer'
import ThemeInfo from './_components/ThemeInfo'
import GameInfo from './_components/GameInfo'
import Hints from './_components/Hints'

import styles from './game.module.css'
import themeStyles from '@/app/components/theme.module.css'
import IngameMap from './_components/IngameMap'

export default function GamePage() {
  const hints = ['hint 1', 'hint 2', 'hint 3']
  const theme = 'random'
  const initialTime = 100
  return (
    <main>
      <div className={styles.infos}>
        <GameInfo theme={theme} round={1} stage={1} />
        <ThemeInfo theme={theme} />
      </div>
      <div className={`${styles.hints} ${styles.opacity}`}>
        <Hints hints={hints} theme={theme} />
      </div>
      <div className={styles.timer}>
        <Timer initialTime={initialTime} theme={theme} />
      </div>
      <div className={`${styles.chat} ${styles.opacity} ${themeStyles[theme]}`}>
        Chatting
      </div>
      <div className={`${styles.map} ${styles.opacity}`}>
        <IngameMap theme={theme} />
      </div>
      <div className={styles.streetView}>StreetView</div>
    </main>
  )
}
