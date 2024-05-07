'use client'

import Chatting from '@/components/Chatting'
import Timer from '@/components/Timer'
import themeStyles from '@/components/theme.module.css'
import { Loader } from '@googlemaps/js-api-loader'
import { useState } from 'react'
import { LuPin, LuPinOff } from 'react-icons/lu'
import GameInfo from './_components/GameInfo'
import Hints from './_components/Hints'
import IngameMap from './_components/IngameMap'
import StreetView from './_components/StreetView'
import ThemeInfo from './_components/ThemeInfo'
import styles from './game.module.css'

export default function GamePage() {
  const [chatFocus, setChatFocus] = useState<boolean>(false)
  const [chatPin, setChatPin] = useState<boolean>(false)

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

  // 임시 게임 아이디
  const gameId = 1

  // 채팅방 prop
  const chatTitle = '팀 채팅'
  const subscribeUrl = `/game/${gameId}`
  const publishUrl = `/app/game/chat/${gameId}`

  function handleChatFocus(bool: boolean) {
    setChatFocus(bool)
  }

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
      <div
        className={`${styles.chat} ${
          chatFocus || chatPin ? '' : styles.opacity
        } ${themeStyles[theme]}`}
        onFocus={() => handleChatFocus(true)}
        onBlur={() => handleChatFocus(false)}
      >
        {/* <div className={styles.chatTitle}>
          <span style={{ marginLeft: 'auto', marginRight: 'auto' }}>
            팀 채팅
          </span>
          <span onClick={() => setChatPin((prev) => !prev)}>
            {chatPin ? <LuPinOff /> : <LuPin />}
          </span>
        </div> */}
        <div
          className={styles.chatPin}
          onClick={() => setChatPin((prev) => !prev)}
        >
          {chatPin ? <LuPinOff /> : <LuPin />}
        </div>
        <Chatting
          chatTitle={chatTitle}
          subsrcibeUrl={subscribeUrl}
          publishUrl={publishUrl}
        />
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
