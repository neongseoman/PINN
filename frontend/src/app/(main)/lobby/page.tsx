'use client'

import CreateRoomModal from '@/app/(main)/lobby/_components/CreateRoomModal'
import ProfileModal from '@/app/(main)/lobby/_components/ProfileModal'
import RoomCard from '@/app/(main)/lobby/_components/RoomCard'
import RuleModal from '@/app/(main)/lobby/_components/RuleModal'
import useCustomAlert from '@/components/useCustomAlert'
import useUserStore from '@/stores/userStore'
import { useRouter } from 'next/navigation'
import { useEffect, useState } from 'react'
import { GiSoundOff, GiSoundOn } from 'react-icons/gi'
import { MdReplay } from 'react-icons/md'
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
  const [soundOn, setSoundOn] = useState<boolean>(false)
  const router = useRouter()
  const { error } = useCustomAlert()

  const fastStart = async () => {
    clickSound()

    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/lobby/quickEnter`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${
            localStorage.getItem('accessToken') as string
          }`,
          body: JSON.stringify({
            senderNickname: nickname,
          }),
        },
      },
    )

    if (response.ok) {
      // console.log('빠른 시작 통신 성공')
      const responseData = await response.json()
      if (responseData.code === 1000) {
        // console.log('빠른 시작 성공!', responseData)
        const gameId = responseData.result.senderGameId
        // console.log(`${gameId}번 방으로 입장합니다`)
        router.push(`/room/${gameId}`)
      } else {
        // console.log('빠른 시작 실패!', responseData.code)
        error(responseData.message)
      }
    } else {
      // console.error('빠른 시작 통신 실패', response)
    }
  }

  const backgroundSound = () => {
    setSoundOn(!soundOn)
  }

  const hoverSound = () => {
    const audio = new Audio('/assets/sounds/hover.wav')
    if (soundOn) {
      audio.play()
    }
  }

  const clickSound = () => {
    const audio = new Audio('/assets/sounds/click.mp3')
    if (soundOn) {
      audio.play()
    }
  }

  const reload = () => {
    roomList()
    clickSound()
  }

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
      // console.log('게임 목록 요청 통신 성공')
      const responseData = await response.json()
      if (responseData.code === 1000) {
        // console.log('게임 목록 출력 성공!', responseData)
        setGameList(responseData.result)
      } else {
        // console.log('게임 목록 출력 실패!', responseData.code)
        error(responseData.message)
      }
    } else {
      // console.error('게임 목록 요청 통신 실패', response)
    }
  }

  useEffect(() => {
    roomList()
    console.log(`nickname: ${nickname}`)
  }, [nickname])

  useEffect(() => {
    const audio = new Audio('/assets/sounds/lobby.mp3')
    audio.loop = true
    if (soundOn) {
      audio.play()
    } else {
      audio.pause()
    }

    return () => {
      // 컴포넌트가 unmount될 때 정리
      audio.pause()
      audio.currentTime = 0
    }
  }, [soundOn])

  return (
    <main className={styles.lobby}>
      <div className={styles.top}>
        <div className={styles.logoSound}>
          <img
            className={styles.logo}
            src="/assets/images/logo.png"
            alt="로고"
          />
          {soundOn ? (
            <GiSoundOn className={styles.soundIcon} onClick={backgroundSound} />
          ) : (
            <GiSoundOff
              className={styles.soundIcon}
              onClick={backgroundSound}
            />
          )}
        </div>
        <div className={styles.userInfo}>
          <ProfileModal hoverSound={hoverSound} clickSound={clickSound} />
        </div>
      </div>
      <div className={styles.medium}>
        <div style={{ display: 'flex' }}>
          <CreateRoomModal hoverSound={hoverSound} clickSound={clickSound} />
          <p
            className={styles.buttons}
            onClick={fastStart}
            onMouseEnter={hoverSound}
          >
            빠른 시작
          </p>
          <MdReplay
            className={styles.buttons}
            onMouseEnter={hoverSound}
            onClick={reload}
          />
        </div>
        <RuleModal hoverSound={hoverSound} clickSound={clickSound} />
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
              hoverSound={hoverSound}
              clickSound={clickSound}
            />
          ))}
      </div>
    </main>
  )
}
