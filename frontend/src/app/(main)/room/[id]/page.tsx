'use client'

import styles from './room.module.css'

import useTeamStore from '@/stores/useStore'
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


interface Team {
  teamNumber: number
  teamColor: string
  teamMember: string[]
  isReady: number
}

export default function RoomPage({ params }: { params: { id: number } }) {

  const { teams, setTeams, setMove } = useTeamStore()
  const { gamerId, nickname } = useUserStore()
  const router = useRouter()

  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SOCKET_URL,
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
  const publishUserUrl = `/app/game/enter/z${params.id}`
  // 방나가기
  const publishExitUrl = `/app/game/exit/${params.id}`

  const isTeamReady = teams.some(team => team.teamNumber === 1 && team.isReady === 0);

  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      clientRef.current.subscribe(subscribeUrl, (res: any) => {
        const messageResponse = JSON.parse(res.body)
        console.log(res.body)
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
  }, [subscribeUrl, publishUserUrl])



  const handleTeamDoubleClick = (teamNumber: number) => {
    if (nickname)
      setMove(teamNumber, nickname)
  };



  const handleOutClick = () => {
    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: publishUserUrl,
      // body: JSON.stringify({
      //   senderNickname: nickname,
      //   senderGameId: gamerId,
      //   senderTeamId: 1,
      // })
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
              subsrcibeUrl={subscribeUrl}
              publishUrl={publishChatUrl}
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


// useEffect(() => {
//   // nickname이 존재하고, 팀에 아직 배정되지 않았을 때에만 처리
//   if (nickname && teams.every(team => team.teamMember.every(member => member !== nickname))) {
//     // 빈 자리를 가진 팀을 찾아 nickname을 배정
//     let updatedTeams = [...teams];
//     for (let i = 0; i < updatedTeams.length; i++) {
//       const team = updatedTeams[i];
//       // 해당 팀에 nickname이 이미 배정된 경우 무시
//       if (team.teamMember.includes(nickname)) {
//         continue;
//       }
//       // 빈 자리를 찾으면 nickname을 배정하고 종료
//       const emptyIndex = team.teamMember.findIndex(member => member === '');
//       if (emptyIndex !== -1) {
//         const updatedTeamMember = [...team.teamMember];
//         updatedTeamMember[emptyIndex] = nickname;
//         updatedTeams[i] = { ...team, teamMember: updatedTeamMember };
//         break;
//       }
//     }

//     setTeams(updatedTeams);
//   }
// }, [teams, nickname]);