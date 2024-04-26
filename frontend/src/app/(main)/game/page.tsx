import Timer from '@/app/components/Timer'
import ThemeInfo from './_components/ThemeInfo'
import GameInfo from './_components/GameInfo'
import Hints from './_components/Hints'

import styles from './game.module.css'

export default function GamePage() {
  const hints = ['hint 1', 'hint 2', 'hint 3']
  return (
    <main>
      <div className={styles.infos}>
        <GameInfo theme={'random'} round={1} stage={1} />
        <ThemeInfo theme={'random'} />
      </div>
      <div className={`${styles.hints} ${styles.opacity}`}>
        <Hints hints={hints} theme={'random'} />
      </div>
      <div className={styles.timer}>
        <Timer initialTime={97} theme={'random'} />
      </div>
      <div className={`${styles.chat} ${styles.opacity}`}>Chatting</div>
      <div className={`${styles.map} ${styles.opacity}`}>Map</div>
    </main>
  )
}
