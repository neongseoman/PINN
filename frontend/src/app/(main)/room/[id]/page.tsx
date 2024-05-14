'use client'

import styles from './room.module.css'

import useUserStore from '@/stores/userStore'
import Chatting from '@/components/Chatting'
import TeamList from './_components/TeamList'
import SelectOption from './_components/SelectOption'
import BtnReady from './_components/BtnReady'
// import BtnReadyCancel from './_components/BtnReadyCancel'
import BtnStart from './_components/BtnStart'
// import BtnStartCancel from './_components/BtnStartCancel'

import { useEffect, useRef, useState } from 'react'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { useRouter } from 'next/navigation'
import { GameProgressInfo } from '@/types/IngameSocketTypes'

interface EnterFormat {
  senderDateTime: string
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  senderTeamNumber: number
  code: string
  msg: string
}

interface TeamGamers {
  colorId: number
  gamerId: string
  teamId: number
  nickname: string
}

interface Team {
  colorCode: string
  teamNumber: number
  teamGamers: TeamGamers[] | null[]
  ready: boolean
}

interface GameInfo {
  gameId: number
  leaderId: string
  roundCount: number
  stage1Time: number
  stage2Time: number
  themeId: number
}

export default function RoomPage({ params }: { params: { id: string } }) {

  const { gamerId, nickname } = useUserStore()
  const router = useRouter()
  const [gameInfo, setGameInfo] = useState<GameInfo>()
  const [remainTime, setRemainTime] = useState<number>(0)
  const [teams, setTeams] = useState<Team[]>([
    {
      teamNumber: 1,
      colorCode: 'rgba(255, 0, 61, 1)',
      teamGamers: [],
      ready: false,
    },
    {
      teamNumber: 2,
      colorCode: 'rgba(182, 53, 53, 1)',
      teamGamers: [],
      ready: false,
    },
    {
      teamNumber: 3,
      colorCode: 'rgba(255, 111, 0, 1)',
      teamGamers: [],
      ready: false,
    },
    {
      teamNumber: 4,
      colorCode: 'rgba(153, 155, 41, 1)',
      teamGamers: [],
      ready: false,
    },
    {
      teamNumber: 5,
      colorCode: 'rgba(0, 131, 143, 1)',
      teamGamers: [],
      ready: false,
    },
    {
      teamNumber: 6,
      colorCode: 'rgba(105, 53, 170, 1)',
      teamGamers: [],
      ready: false,
    },
    {
      teamNumber: 7,
      colorCode: 'rgba(251, 52, 159, 1)',
      teamGamers: [],
      ready: false,
    },
    {
      teamNumber: 8,
      colorCode: 'rgba(255, 172, 207, 1)',
      teamGamers: [],
      ready: false,
    },
    {
      teamNumber: 9,
      colorCode: 'rgba(188, 157, 157, 1)',
      teamGamers: [],
      ready: false,
    },
    {
      teamNumber: 10,
      colorCode: 'rgba(85, 85, 85, 1)',
      teamGamers: [],
      ready: false,
    },
  ])

  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SERVER_SOCKET_URL,
      debug: function (str: string) {
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )

  // 채팅
  const chatTitle = '전체 채팅'
  const subscribeUrl = `/game/${params.id}`
  const subscribeUrl2 = `/game/sse/${params.id}`
  const publishChatUrl = `/app/game/chat/${params.id}`
  // 유저 입장
  const publishUserUrl = `/app/game/enter/${params.id}`
  // 팀 옮기기
  const publishMoveUrl = `/app/room/move`
  // 방나가기
  const publishExitUrl = `/app/game/exit/${params.id}`

  const isTeamReady = teams.some(team => team.teamNumber === 1 && team.ready === false)

  // 팀 목록 받아오는 함수
  const teamList = async () => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/room/${params.id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('accessToken') as string}`
      },
    });

    if (response.ok) {
      console.log('팀 목록 요청 통신 성공');
      const responseData = await response.json();
      if (responseData.code === 1000) {
        console.log('팀 목록 출력 성공!', responseData);
        setTeams(responseData.result.teams)
        setGameInfo(responseData.result)

      } else {
        console.log('팀 목록 출력 실패!', responseData.code);
      }
    } else {
      console.error('팀 목록 요청 통신 실패', response);
    }
  }

  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      // 사용자 입장을 알리는 publish
      clientRef.current.publish({
        headers: {
          Auth: localStorage.getItem('accessToken') as string
        },
        destination: publishUserUrl,
        body: JSON.stringify({
        }),
      })
      console.log('엔터')

      clientRef.current.subscribe(subscribeUrl2, (message: IMessage) => {
        const messageResponse = JSON.parse(message.body)
        console.log('Subscribed message:', messageResponse)
        if (messageResponse.code === 1202) {
          router.push(`/game/${params.id}/1`)
        }
      })

      clientRef.current.subscribe(subscribeUrl2, (message: IMessage) => {
        const gameProgressResponse = JSON.parse(
          message.body,
        ) as GameProgressInfo
        switch (gameProgressResponse.code) {
          case 1202:
            router.push(`/game/${params.id}/1`)
            break

          case 1210:
            setRemainTime(gameProgressResponse.leftTime)
        }
      })

      // 채팅 또는 다른 메시지를 위한 구독
      clientRef.current.subscribe(subscribeUrl, async (message: any) => {
        const messageResponse = JSON.parse(message.body) as EnterFormat
        console.log('Subscribed message:', messageResponse)

        await teamList()
      })
    }

    clientRef.current.onStompError = function (frame: IFrame) {
      console.log('Broker reported error: ' + frame.headers['message'])
      console.log('Additional details: ' + frame.body)
    }

    // 소켓을 활성화
    clientRef.current.activate()
    return () => {
      clientRef.current.deactivate()
    }

  }, [subscribeUrl, publishUserUrl, subscribeUrl2])

  const gameStartRequest = {
    senderNickname: nickname,
    senderGameId: params.id,
    senderTeamId: 1,
    gameId: params.id,
    roundCount: 3,
    stage1Time: 30,
    stage2Time: 30,
    scorePageTime: 10
  }

  function gameStart() {
    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: `/app/game/start`,
      body: JSON.stringify(gameStartRequest),
    })
  }

  // 팀 옮기기
  const handleTeamDoubleClick = (teamNumber: number) => {
    const myTeam = teams.find(team => team.teamGamers.some(gamer => gamer?.gamerId === gamerId))
    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: publishMoveUrl,
      body: JSON.stringify({
        senderGameId: params.id,
        senderNickname: nickname,
        oldTeamId: myTeam?.teamNumber,
        newTeamId: teamNumber
      }),
    })
  }

  // 팀 나가기
  const handleOutClick = () => {
    const myTeam = teams.find(team => team.teamGamers.some(gamer => gamer?.gamerId === gamerId))
    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: publishExitUrl,
      body: JSON.stringify({
        senderNickname: nickname,
        senderGameId: gamerId,
        senderTeamId: myTeam?.teamNumber,
      })
    })
    console.log('성공')
    router.push(`/lobby`)
  }
  const isLeader = gameInfo?.leaderId === gamerId ? true : false
  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles['option-team-container']}>
          <SelectOption></SelectOption>
          <TeamList teams={teams} handleTeamDoubleClick={handleTeamDoubleClick} ></TeamList>
        </div>
        <div className={styles['chat-ready-out']}>
          <div className={styles.chat}>
            <Chatting
              chatTitle={chatTitle}
              subscribeUrl={subscribeUrl}
              publishUrl={publishChatUrl}
              gameId={params.id}
            />
          </div>
          <div className={styles['ready-out']}>
            {/* {isTeamReady ? (
              <BtnReady teams={teams} />
            ) : (
              <BtnReadyCancel teams={teams} />
            )} */}
            {isLeader ? (
              <BtnStart gameStart={gameStart} />
            ) : (
              <BtnReady />
            )}
            <button className={styles.out} onClick={handleOutClick}>나가기</button>
          </div>
        </div>
      </div>
    </main >
  )
}