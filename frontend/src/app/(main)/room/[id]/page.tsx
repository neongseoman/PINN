'use client'

import styles from './room.module.css'

import useUserStore from '@/stores/userStore'
import useIngameStore from '@/stores/ingameStore'
import Chatting from '@/components/Chatting'
import BtnReady from './_components/BtnReady'
import SelectOption from './_components/SelectOption'
import TeamList from './_components/TeamList'
import BtnReadyCancel from './_components/BtnReadyCancel'
import BtnStart from './_components/BtnStart'
// import BtnStartCancel from './_components/BtnStartCancel'

import { GameProgressInfo } from '@/types/IngameSocketTypes'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { useRouter } from 'next/navigation'
import { useEffect, useRef, useState } from 'react'
import BtnWaiting from './_components/BtnWaiting'

interface RoomInfo {
  senderDateTime: string
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  senderTeamNumber: number
  code: number
  msg: string
}

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
  roomName: string
  leaderId: string
  roundCount: number
  stage1Time: number
  stage2Time: number
  themeId: number
}

export default function RoomPage({ params }: { params: { id: string } }) {
  const { gamerId, nickname } = useUserStore()
  const { setTeamColor, setTeamId, setTheme } = useIngameStore()
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

  const isLeader = gameInfo?.leaderId === gamerId ? true : false
  const isTeamLeader = teams.some(team =>
    team.teamGamers.length > 0 && team.teamGamers[0]?.gamerId === gamerId)
  const myTeam = teams.find((team) =>
    team.teamGamers.some((gamer) => gamer?.gamerId === gamerId),
  )

  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SERVER_SOCKET_URL,
      debug: function (str: string) { },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )

  // 채팅
  const chatTitle = '전체 채팅'
  const subscribeRoomUrl = `/game/${params.id}`
  const publishChatUrl = `/app/game/chat/${params.id}`
  // 유저 입장
  const publishUserUrl = `/app/game/enter/${params.id}`
  // 준비
  const publishReadyUrl = `/app/game/teamStatus/${params.id}`
  // 팀 옮기기
  const publishMoveUrl = `/app/room/move`
  // 방나가기
  const publishExitUrl = `/app/game/exit/${params.id}`
  // 시작
  const subscribeStartUrl = `/game/sse/${params.id}`
  // const isTeamReady = teams.some(team => team.teamNumber === 1 && team.ready === false)

  // 팀 목록 받아오는 함수
  const teamList = async () => {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/room/${params.id}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken') as string
            }`,
        },
      },
    )

    if (response.ok) {
      console.log('팀 목록 요청 통신 성공')
      const responseData = await response.json()
      if (responseData.code === 1000) {
        console.log('팀 목록 출력 성공!', responseData)
        setTeams(responseData.result.teams)
        setGameInfo(responseData.result)
      } else {
        console.log('팀 목록 출력 실패!', responseData.code)
      }
    } else {
      console.error('팀 목록 요청 통신 실패', response)
    }
  }

  // 입장
  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      console.log('Connected:', _frame);
      clientRef.current.publish({
        headers: {
          Auth: localStorage.getItem('accessToken') as string,
        },
        destination: publishUserUrl,
        body: JSON.stringify({
        })
      })

      // 메시지 구독
      clientRef.current.subscribe(subscribeRoomUrl, async (message: IMessage) => {
        const enterResponse = JSON.parse(message.body) as GameInfo
        console.log(enterResponse);

        // 비동기 함수 호출
        await teamList()
      });

      // 시작 메시지 구독
      clientRef.current.subscribe(subscribeStartUrl, (message: IMessage) => {
        const gameProgressResponse = JSON.parse(message.body) as GameProgressInfo
        switch (gameProgressResponse.code) {
          case 1202:
            const myTeamInfo = teams.find(team => team.teamGamers.some(gamer => gamer?.gamerId === gamerId))
            if (myTeamInfo) {
              const myTeamId = myTeamInfo.teamNumber
              const myTeamColor = myTeamInfo.colorCode
              setTeamId(myTeamId)
              setTeamColor(myTeamColor)
              // setTheme(gameInfo?.themeId)
            }
            // setTheme()
            router.push(`/game/${params.id}/1`)
            break

          case 1210:
            setRemainTime(gameProgressResponse.leftTime)
        }
      })
    }

    clientRef.current.onStompError = function (frame: IFrame) {
      console.log('Broker reported error: ' + frame.headers['message'])
      console.log('Additional details: ' + frame.body)
    }

    clientRef.current.activate()

    return () => {
      clientRef.current.deactivate()
    }
  }, [params.id])

  function gameStart() {
    // 다른 팀이 있는지 확인
    const otherTeamsExist = teams.some(team =>
      team.teamNumber !== myTeam?.teamNumber && team.teamGamers.length > 0
    );

    // 다른 팀이 있을 때, 그 팀들이 모두 준비 상태인지 확인
    const allOtherTeamsReady = !otherTeamsExist || teams.every(team =>
      team.teamNumber === myTeam?.teamNumber || (team.teamGamers.length > 0 && team.ready)
    );

    console.log(allOtherTeamsReady);
    if (!allOtherTeamsReady) {
      alert("모든 다른 팀이 준비 상태가 아닙니다. 게임을 시작할 수 없습니다.");
      return;
    }

    const gameStartRequest = {
      senderNickname: nickname,
      senderGameId: params.id,
      senderTeamId: myTeam?.teamNumber,
      gameId: params.id,
      roundCount: gameInfo?.roundCount,
      stage1Time: gameInfo?.stage1Time,
      stage2Time: gameInfo?.stage2Time,
      scorePageTime: 10,
    }

    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: `/app/game/start`,
      body: JSON.stringify(gameStartRequest),
    })
  }
  // 준비 버튼
  const gameReady = () => {
    console.log(myTeam)
    console.log(teams)
    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: publishReadyUrl,
      body: JSON.stringify({
        senderNickname: nickname,
        senderGameId: params.id,
        senderTeamId: myTeam?.teamNumber
      }),
    })
  }

  // 팀 옮기기
  const handleTeamDoubleClick = (teamNumber: number) => {
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
    teamList()
  }

  // 방 나가기
  const handleOutClick = () => {
    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: publishExitUrl,
      body: JSON.stringify({
        senderNickname: nickname,
        senderGameId: params.id,
        senderTeamId: myTeam?.teamNumber,
      }),
    })
    router.push(`/lobby`)
  }

  return (
    <main className={styles.background}>
      <div></div>
      <div className={styles.container}>
        <div className={styles['option-team-container']}>
          <SelectOption roomId={params.id} />
          <TeamList
            teams={teams}
            handleTeamDoubleClick={handleTeamDoubleClick}
          ></TeamList>
        </div>
        <div className={styles['chat-ready-out']}>
          <div className={styles.chat}>
            <Chatting
              chatTitle={chatTitle}
              subscribeUrl={subscribeRoomUrl}
              publishUrl={publishChatUrl}
              gameId={params.id}
            />
          </div>
          <div className={styles['ready-out']}>
            {isLeader ? (
              <BtnStart gameStart={gameStart} />
            ) : isTeamLeader ? (
              myTeam && myTeam.ready ? (
                <BtnReadyCancel gameReady={gameReady} />
              ) : (
                <BtnReady gameReady={gameReady} />
              )
            ) : (
              <BtnWaiting />
            )}
            <button className={styles.out} onClick={handleOutClick}>
              나가기
            </button>
          </div>
        </div>
      </div>
    </main>
  )
}
