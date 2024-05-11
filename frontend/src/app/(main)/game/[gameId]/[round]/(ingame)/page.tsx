'use client'

import Chatting from '@/components/Chatting'
import Timer from '@/components/Timer'
import themeStyles from '@/components/theme.module.css'
import useUserStore from '@/stores/userStore'
import { Hint, RoundInit, StageTwoInit } from '@/types/IngameRestTypes'
import { GameProgressInfo } from '@/types/IngameSocketTypes'
import { getRoundInfo, getStageTwoHint } from '@/utils/IngameApi'
import { Loader } from '@googlemaps/js-api-loader'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { useRouter } from 'next/navigation'
import { useEffect, useRef, useState } from 'react'
import { LuPin, LuPinOff } from 'react-icons/lu'
import GameInfo from './_components/GameInfo'
import Hints from './_components/Hints'
import IngameMap from './_components/IngameMap'
import StreetView from './_components/StreetView'
import ThemeInfo from './_components/ThemeInfo'
import styles from './game.module.css'

export default function GamePage({
  params,
}: {
  params: { gameId: string; round: string }
}) {
  const router = useRouter()
  const { nickname } = useUserStore()

  // 요소 투명도 조절
  const [chatFocus, setChatFocus] = useState<boolean>(false)
  const [chatPin, setChatPin] = useState<boolean>(false)
  const [hintPin, setHintPin] = useState<boolean>(false)
  const [mapPin, setMapPin] = useState<boolean>(false)

  // 라운드 시작 여부
  const [roundStart, setRoundStart] = useState<boolean>(false)

  // 현재 스테이지
  const [stage, setStage] = useState<number>(1)

  // 힌트
  const [hints, setHints] = useState<Hint[] | null>(null)

  // 테마
  const theme = 'random'

  // 스테이지 시간
  const initialTime = 100

  // 정답 좌표
  const [lat, setLat] = useState<number>()
  const [lng, setLng] = useState<number>()

  //구글맵
  const loader = new Loader({
    apiKey: process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string,
    version: 'weekly',
  })

  // 채팅방 prop
  const chatTitle = '방 채팅 url로 임시구현'
  const subscribeUrl = `/game/${params.gameId}`
  const publishUrl = `/app/game/chat/${params.gameId}`

  // 소켓 연결
  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SERVER_SOCKET_URL,
      debug: function (str: string) {
        console.log(str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )

  // 라운드 시작 정보 받아오기
  async function roundStartRender() {
    const roundInfo = (await getRoundInfo(
      params.gameId,
      params.round,
    )) as RoundInit

    setHints(roundInfo.result.hints)
    setLat(roundInfo.result.lat)
    setLng(roundInfo.result.lng)
  }

  // 스테이지 2 정보 받아오기
  async function stageTwoRender() {
    const stageTwoInfo = (await getStageTwoHint(
      params.gameId,
      params.round,
    )) as StageTwoInit
    setStage(stageTwoInfo.result.stage)
    setHints(stageTwoInfo.result.hints)
  }

  // 소켓 구독
  const ingameSubscribeUrl = `/game/sse/${params.gameId}`
  useEffect(() => {
    // 라운드 시작 정보
    roundStartRender()

    // 소켓 연결 시 동작
    clientRef.current.onConnect = function (_frame: IFrame) {
      // 게임 진행 구독
      clientRef.current.subscribe(ingameSubscribeUrl, (message: IMessage) => {
        const gameProgressResponse = JSON.parse(
          message.body,
        ) as GameProgressInfo
        switch (gameProgressResponse.code) {
          case 1202:
            // 라운드 스타트
            setRoundStart(true)
            break
          case 1203:
            // 스테이지 2 스타트
            stageTwoRender()
            break
          case 1204:
            // 스테이지 2 끝
            // router.push(`/game/${params.gameId}/${params.round}/result`)
            break
          case 1206:
            router.push(`/game/${params.gameId}/${Number(params.round) + 1}`)
        }
      })
    }

    clientRef.current.onStompError = function (frame: IFrame) {
      console.log('스톰프 에러: ' + frame.headers['message'])
      console.log('추가 정보: ' + frame.body)
    }

    clientRef.current.activate()

    return () => {
      clientRef.current.deactivate()
    }
  }, [params.gameId, params.round])

  function handleChatFocus(bool: boolean) {
    setChatFocus(bool)
  }

  return (
    <main>
      <div className={styles.infos}>
        <GameInfo theme={theme} round={Number(params.round)} stage={stage} />
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
          subscribeUrl={subscribeUrl}
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
        <IngameMap
          theme={theme}
          loader={loader}
          gameId={params.gameId}
          round={params.round}
          stage={stage}
        />
      </div>
      <div className={styles.streetView}>
        <StreetView lat={lat!} lng={lng!} loader={loader} />
      </div>
    </main>
  )
}
