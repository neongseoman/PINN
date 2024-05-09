'use client'

import Chatting from '@/components/Chatting'
import Timer from '@/components/Timer'
import themeStyles from '@/components/theme.module.css'
import { IngameResponse } from '@/types/IngameTypes'
import { Loader } from '@googlemaps/js-api-loader'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { useEffect, useRef, useState } from 'react'
import { LuPin, LuPinOff } from 'react-icons/lu'
import GameInfo from '../_components/GameInfo'
import Hints from '../_components/Hints'
import IngameMap from '../_components/IngameMap'
import StreetView from '../_components/StreetView'
import ThemeInfo from '../_components/ThemeInfo'
import styles from './game.module.css'

export default function GamePage({ params }: { params: { gameId: string } }) {
  const [chatFocus, setChatFocus] = useState<boolean>(false)
  const [chatPin, setChatPin] = useState<boolean>(false)
  const [hintPin, setHintPin] = useState<boolean>(false)
  const [mapPin, setMapPin] = useState<boolean>(false)

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
  // const gameId = 1

  // 채팅방 prop
  const chatTitle = '방 채팅 url로 임시구현'
  const subscribeUrl = `/game/${params.gameId}`
  const publishUrl = `/app/game/chat/${params.gameId}`

  // 소켓 코드

  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SOCKET_URL,
      debug: function (str: string) {
        console.log(str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )

  const ingameSubscribeUrl = `/game/sse/${params.gameId}`
  const gameStartPublishUrl = 'app/game/start'
  const gameStartRequest = {
    senderNickname: 'rockbison',
    senderGameId: params.gameId,
    senderTeamId: 1,
    gameId: params.gameId,
    roundCount: 3,
    stage1Time: 30,
    stage2Time: 30,
    scorePageTime: 30,
  }

  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      clientRef.current.subscribe(ingameSubscribeUrl, (message: IMessage) => {
        const ingameResponse = JSON.parse(message.body) as IngameResponse
        switch (ingameResponse.code) {
          case 1201:
            break
          case 1202:
            break
          case 1203:
            break
          case 1204:
            break
          case 1205:
            break
          case 1206:
            break
        }
      })
    }

    clientRef.current.onStompError = function (frame: IFrame) {
      console.log('Broker reported error: ' + frame.headers['message'])
      console.log('Additional details: ' + frame.body)
    }

    clientRef.current.activate()

    // clientRef.current.publish({
    //   headers: {
    //     Auth: localStorage.getItem('accessToken') as string,
    //   },
    //   destination: gameStartPublishUrl,
    //   body: JSON.stringify(gameStartRequest),
    // })

    return () => {
      clientRef.current.deactivate()
    }
  }, [params.gameId])

  function handleChatFocus(bool: boolean) {
    setChatFocus(bool)
  }

  return (
    <main>
      <div className={styles.infos}>
        <GameInfo theme={theme} round={1} stage={1} />
        <ThemeInfo theme={theme} />
      </div>
      <div
        className={`${styles.hints} ${hintPin ? '' : styles.opacity} ${
          themeStyles[theme]
        }`}
      >
        <div className={styles.pin} onClick={() => setHintPin((prev) => !prev)}>
          {hintPin ? <LuPinOff /> : <LuPin />}
        </div>
        <Hints hints={hints} />
      </div>
      <div className={`${styles.timer} ${themeStyles[theme]}`}>
        <Timer initialTime={initialTime} />
      </div>
      <div
        className={`${styles.chat} ${
          chatFocus || chatPin ? '' : styles.opacity
        } ${themeStyles[theme]}`}
        onFocus={() => handleChatFocus(true)}
        onBlur={() => handleChatFocus(false)}
      >
        <div className={styles.pin} onClick={() => setChatPin((prev) => !prev)}>
          {chatPin ? <LuPinOff /> : <LuPin />}
        </div>
        <Chatting
          chatTitle={chatTitle}
          subsrcibeUrl={subscribeUrl}
          publishUrl={publishUrl}
        />
      </div>
      <div className={`${styles.map} ${mapPin ? '' : styles.opacity}`}>
        <div
          className={`${styles.pin} ${styles.mapPin}`}
          onClick={() => setMapPin((prev) => !prev)}
        >
          {mapPin ? <LuPinOff /> : <LuPin />}
        </div>
        <IngameMap theme={theme} loader={loader} />
      </div>
      <div className={styles.streetView}>
        <StreetView lat={lat} lng={lng} loader={loader} />
      </div>
    </main>
  )
}
