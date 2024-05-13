'use client'

import CreateRoomModal from '@/app/(main)/lobby/_components/CreateRoomModal'
import RoomCard from '@/app/(main)/lobby/_components/RoomCard'
import useUserStore from '@/stores/userStore'
import Image from 'next/image'
import { useEffect, useState } from 'react'
import RuleModal from './_components/RuleModal'
import styles from './lobby.module.css'

interface GameInfo {
  readyGame: {
    gameId: number
    roomName: string
    themeId: number
    roundCount: number
    stage1Time: number
    stage2Time: number
    password: boolean
    gamerNumber: number
  }
  countPerson: number
}

export default function LobbyPage() {
  const { nickname } = useUserStore()
  const [gameList, setGameList] = useState<GameInfo[]>([])

  const profileModal = () => {
    // 프로필 수정 모달 띄우기
  }

  const fastStart = () => {
    // 빠른 시작
  }

  useEffect(() => {
    const roomList = async () => {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/lobby/search`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${
              localStorage.getItem('accessToken') as string
            }`,
          },
        },
      )

      if (response.ok) {
        console.log('게임 목록 요청 통신 성공')
        const responseData = await response.json()
        if (responseData.code === 1000) {
          console.log('게임 목록 출력 성공!', responseData)
          setGameList(responseData.result)
        } else {
          console.log('게임 목록 출력 실패!', responseData.code)
          alert(responseData.message)
        }
      } else {
        console.error('게임 목록 요청 통신 실패', response)
      }
    }

    roomList()
  }, [])

  return (
    <main className={styles.lobby}>
      <div className={styles.top}>
        <img className={styles.logo} src="/assets/images/logo.png" alt="로고" />
        <div className={styles.userInfo} onClick={profileModal}>
          <p className={styles.username}>{nickname}</p>
          <Image
            className={styles.profile}
            width={25}
            height={25}
            src="/assets/images/default_profile.png"
            alt="프로필 이미지"
            priority
          />
        </div>
      </div>
      <div className={styles.medium}>
        <div style={{ display: 'flex' }}>
          <CreateRoomModal />
          <p className={styles.buttons} onClick={fastStart}>
            빠른 시작
          </p>
        </div>
        <RuleModal />
      </div>
      <div className={styles.bottom}>
        {gameList &&
          gameList.map((game, i) => (
            <RoomCard
              key={i}
              gameId={game.readyGame.gameId}
              themeId={game.readyGame.themeId}
              roomName={game.readyGame.roomName}
              roundCount={game.readyGame.roundCount}
              stage1Time={game.readyGame.stage1Time}
              stage2Time={game.readyGame.stage2Time}
              password={game.readyGame.password}
              countPerson={game.countPerson}
            />
          ))}
      </div>
    </main>
  )
}
