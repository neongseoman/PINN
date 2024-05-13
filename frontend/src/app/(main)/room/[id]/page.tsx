'use client'

import styles from './room.module.css'

import useTeamStore from '@/stores/teamStore'
import useUserStore from '@/stores/userStore'
import Chatting from '@/components/Chatting'
import TeamList from './_components/TeamList'
import SelectOption from './_components/SelectOption'
import BtnReady from './_components/BtnReady'
import BtnReadyCancel from './_components/BtnReadyCancel'
import BtnStart from './_components/BtnStart'
import BtnStartCancel from './_components/BtnStartCancel'

import { useEffect, useRef, useState } from 'react'
import { Client, IFrame } from '@stomp/stompjs'
import { useRouter } from 'next/navigation'

interface EnterFormat {
  senderDateTime: string
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  senderTeamNumber: number
  code: string
  msg: string
}

interface Team {
  colorCode: string
  teamNumber: number
  teamGamer: string[]
  ready: boolean
}

export default function RoomPage({ params }: { params: { id: string } }) {

  // const teamStore = useTeamStore(params.id)
  // const { teams, setMove } = teamStore()

  const { gamerId, nickname } = useUserStore()
  const router = useRouter()

  const [teams, setTeams] = useState<Team[]>([
    {
      teamNumber: 1,
      colorCode: 'rgba(255, 0, 61, 1)',
      teamGamer: ['', '', ''],
      ready: false,
    },
    {
      teamNumber: 2,
      colorCode: 'rgba(182, 53, 53, 1)',
      teamGamer: ['', '', ''],
      ready: false,
    },
    {
      teamNumber: 3,
      colorCode: 'rgba(255, 111, 0, 1)',
      teamGamer: ['', '', ''],
      ready: false,
    },
    {
      teamNumber: 4,
      colorCode: 'rgba(153, 155, 41, 1)',
      teamGamer: ['', '', ''],
      ready: false,
    },
    {
      teamNumber: 5,
      colorCode: 'rgba(0, 131, 143, 1)',
      teamGamer: ['', '', ''],
      ready: false,
    },
    {
      teamNumber: 6,
      colorCode: 'rgba(105, 53, 170, 1)',
      teamGamer: ['', '', ''],
      ready: false,
    },
    {
      teamNumber: 7,
      colorCode: 'rgba(251, 52, 159, 1)',
      teamGamer: ['', '', ''],
      ready: false,
    },
    {
      teamNumber: 8,
      colorCode: 'rgba(255, 172, 207, 1)',
      teamGamer: ['', '', ''],
      ready: false,
    },
    {
      teamNumber: 9,
      colorCode: 'rgba(188, 157, 157, 1)',
      teamGamer: ['', '', ''],
      ready: false,
    },
    {
      teamNumber: 10,
      colorCode: 'rgba(85, 85, 85, 1)',
      teamGamer: ['', '', ''],
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
  const publishChatUrl = `/app/game/chat/${params.id}`
  // 유저 입장
  const publishUserUrl = `/app/game/enter/${params.id}`
  // 방나가기
  const publishExitUrl = `/app/game/exit/${params.id}`

  const isTeamReady = teams.some(team => team.teamNumber === 1 && team.ready === false);

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

      // 채팅 또는 다른 메시지를 위한 구독
      clientRef.current.subscribe(subscribeUrl, (message: any) => {
        const messageResponse = JSON.parse(message.body) as EnterFormat
        console.log('Subscribed message:', messageResponse)
      })
    }

    clientRef.current.onStompError = function (frame: IFrame) {
      console.log('Broker reported error: ' + frame.headers['message'])
      console.log('Additional details: ' + frame.body)
    }

    // 소켓을 활성화
    clientRef.current.activate()

    return () => {
      // 컴포넌트가 언마운트될 때 소켓을 비활성화
      clientRef.current.deactivate()
    }
  }, [subscribeUrl, publishUserUrl])


  const handleTeamDoubleClick = (teamNumber: number) => {
    // if (nickname)
    //   setMove(teamNumber, nickname)
  };

  useEffect(() => {
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
          const teamsArray = Object.keys(responseData.result.teams).map(key => responseData.result.teams[key]);
          setTeams(teamsArray)

        } else {
          console.log('팀 목록 출력 실패!', responseData.code);
        }
      } else {
        console.error('팀 목록 요청 통신 실패', response);
      }
    }
    teamList()
  }, [params.id]);

  const handleOutClick = () => {
    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: publishUserUrl,
      body: JSON.stringify({
        senderNickname: nickname,
        senderGameId: gamerId,
        senderTeamId: 1,
      })
    })
    router.push(`/lobby}`)
  }

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
            {isTeamReady ? (
              <BtnReady teams={teams} />
            ) : (
              <BtnReadyCancel teams={teams} />
            )}
            <button className={styles.out} onClick={handleOutClick}>나가기</button>
          </div>
        </div>
      </div>
    </main >
  )
}