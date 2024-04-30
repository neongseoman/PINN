'use client'

import Timer from '@/app/components/Timer'
import ThemeInfo from './_components/ThemeInfo'
import GameInfo from './_components/GameInfo'
import Hints from './_components/Hints'

import styles from './game.module.css'
import themeStyles from '@/app/components/theme.module.css'
import IngameMap from './_components/IngameMap'
import StreetView from './_components/StreetView'

import { Loader } from '@googlemaps/js-api-loader'

export default function GamePage() {
  //힌트
  const hints = ['hint 1', 'hint 2', 'hint 3']

  //테마
  const theme = 'random'

  //스테이지 시간
  const initialTime = 100

  // 멀캠
  const lat = 37.50155
  const lng = 127.039374

  //구글맵
  const loader = new Loader({
    apiKey: process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string,
    version: 'weekly',
    // ...additionalOptions,
  })

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
        <IngameMap theme={theme} loader={loader} />
      </div>
      <div className={styles.streetView}>
        <StreetView lat={lat} lng={lng} loader={loader} />
      </div>
    </main>
  )
}
