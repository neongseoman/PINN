'use client'

import useCustomAlert from '@/components/useCustomAlert'
import Image from 'next/image'
import { useRouter } from 'next/navigation'
import React, { useRef, useState } from 'react'
import { TiArrowSortedDown } from 'react-icons/ti'
import styles from '../lobby.module.css'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import useUserStore from '@/stores/userStore'

interface CreateRoomModalProps {
  hoverSound: () => void
  clickSound: () => void
}

export default function CreateRoomModal({
  hoverSound,
  clickSound,
}: CreateRoomModalProps) {
  const dialogRef = useRef<HTMLDialogElement>(null)
  const [roomName, setRoomName] = useState<string>('')
  const [password, setPassword] = useState<string>('')
  const [roundCount, setRoundCount] = useState<number>(3)
  const [stage1Time, setStage1Time] = useState<number>(30)
  const [stage2Time, setStage2Time] = useState<number>(20)
  const [themeId, setThemeId] = useState<number>(1)
  const router = useRouter()
  const { error } = useCustomAlert()
  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SERVER_SOCKET_URL,
      debug: function (str: string) {
        // console.log(str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )
  const { nickname } = useUserStore()

  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const inputTitle = event.target.value
    if (inputTitle.length <= 20) {
      setRoomName(inputTitle)
    }
  }

  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const inputPassword = event.target.value
    if (inputPassword.length <= 8) {
      setPassword(inputPassword)
    }
  }

  const handleRoundChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(event.target.value)
    setRoundCount(value)
  }

  const handleStage1Change = (
    event: React.MouseEvent<HTMLAnchorElement, MouseEvent>,
  ) => {
    const selectedStage = parseInt(event.currentTarget.innerText)
    setStage1Time(selectedStage)
  }

  const handleStage2Change = (
    event: React.MouseEvent<HTMLAnchorElement, MouseEvent>,
  ) => {
    const selectedStage = parseInt(event.currentTarget.innerText)
    setStage2Time(selectedStage)
  }

  const handleThemeChange = (selectedTheme: number) => {
    setThemeId(selectedTheme)
  }

  const showModal = () => {
    clickSound()
    dialogRef.current?.showModal()
    // clientRef.current.activate()
    // clientRef.current.onConnect = function (_frame: IFrame) {
    //   clientRef.current.subscribe(`/game/${gameId}`, () => {})
    // }
  }

  const closeModal = () => {
    dialogRef.current?.close()
  }

  // 생성 요청 함수
  const handleSubmit = async () => {
    if (!roomName || roomName.length > 20 || roomName.length < 1) {
      error('방 제목을 입력해 주세요!')
      return
    } else if (password.length > 8) {
      error('비밀번호는 8글자 이하여야 합니다.')
      return
    }
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/lobby/create`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('accessToken') as string
              }`,
          },
          body: JSON.stringify({
            themeId: themeId,
            roomName: roomName,
            roundCount: roundCount,
            stage1Time: stage1Time,
            stage2Time: stage2Time,
            password: password,
          }),
        },
      )

      if (response.ok) {
        // console.log('게임 생성 요청 통신 성공')
        const responseData = await response.json()
        if (responseData.code === 1000) {
          // console.log('게임 생성 성공!', responseData)
          const gameId = responseData.result.gameId
          const subscribeRoomUrl = `/game/${gameId}`

          clientRef.current.onConnect = function (_frame: IFrame) {
            clientRef.current.subscribe(subscribeRoomUrl, (message: IMessage) => {
              const enterResponse = JSON.parse(message.body)

              router.push(`/room/${gameId}`)
              clientRef.current.deactivate()
            })

            clientRef.current.publish({
              headers: {
                Auth: localStorage.getItem('accessToken') as string,
              },
              destination: `/app/game/enter/${gameId}`,
              body: JSON.stringify({
                senderNickname: nickname,
                senderGameId: gameId,
              }),
            })

            // console.log(`${gameId}번 방으로 입장합니다`)
          }

          clientRef.current.onStompError = function (frame: IFrame) {
            // console.log('Broker reported error: ' + frame.headers['message'])
            // console.log('Additional details: ' + frame.body)
          }

          clientRef.current.activate()
        } else {
          error(responseData.message)
        }
      } else {
        // console.error('게임 생성 요청 통신 실패', response)
      }
    } catch (error) {
      // console.error('에러 발생: ', error)
    }
    clickSound()
  }

  return (
    <div>
      <p
        className={styles.buttons}
        onClick={showModal}
        onMouseEnter={hoverSound}
      >
        게임 생성
      </p>

      <dialog className={styles.createRoomWrapper} ref={dialogRef}>
        <p className={styles.modalTitle}>게임 생성</p>
        <hr className={styles.line} />
        <div className={styles.infoWrapper}>
          <p className={styles.titleInputBox}>
            방 제목 :{' '}
            <input
              className={styles.inputText}
              type="text"
              value={roomName}
              placeholder="20글자 이하"
              onChange={handleTitleChange}
            />
          </p>
          <p className={styles.passwordInputBox}>
            비밀번호 :{' '}
            <input
              className={styles.inputText}
              type="text"
              value={password}
              placeholder="8글자 이하"
              onChange={handlePasswordChange}
            />
          </p>
        </div>
        <div className={styles.infoWrapper}>
          <div className={styles.roundInputBox}>
            <span>라운드 수 : </span>
            <p className={styles.radio}>
              <label>
                <input
                  className={styles.radioButton}
                  type="radio"
                  name="options"
                  value="1"
                  checked={roundCount === 1}
                  onChange={handleRoundChange}
                />
                <span className={styles.radioText}>1</span>
              </label>
              <label>
                <input
                  className={styles.radioButton}
                  type="radio"
                  name="options"
                  value="2"
                  checked={roundCount === 2}
                  onChange={handleRoundChange}
                />
                <span className={styles.radioText}>2</span>
              </label>
              <label>
                <input
                  className={styles.radioButton}
                  type="radio"
                  name="options"
                  value="3"
                  checked={roundCount === 3}
                  onChange={handleRoundChange}
                />
                <span className={styles.radioText}>3</span>
              </label>
              <label>
                <input
                  className={styles.radioButton}
                  type="radio"
                  name="options"
                  value="4"
                  checked={roundCount === 4}
                  onChange={handleRoundChange}
                />
                <span className={styles.radioText}>4</span>
              </label>
              <label>
                <input
                  className={styles.radioButton}
                  type="radio"
                  name="options"
                  value="5"
                  checked={roundCount === 5}
                  onChange={handleRoundChange}
                />
                <span className={styles.radioText}>5</span>
              </label>
            </p>
          </div>
          <div className={styles.stageInputBox}>
            스테이지 1 :
            <div className={styles.dropdown}>
              <button className={styles.dropbtn}>
                {stage1Time}&nbsp;초&nbsp;&nbsp;
                <TiArrowSortedDown />
              </button>
              <div className={styles.dropdownContent}>
                <a href="#" onClick={handleStage1Change}>
                  30
                </a>
                <a href="#" onClick={handleStage1Change}>
                  40
                </a>
                <a href="#" onClick={handleStage1Change}>
                  50
                </a>
                <a href="#" onClick={handleStage1Change}>
                  60
                </a>
              </div>
            </div>
          </div>
          <div className={styles.stageInputBox}>
            스테이지 2 :
            <div className={styles.dropdown}>
              <button className={styles.dropbtn}>
                {stage2Time}&nbsp;초&nbsp;&nbsp;
                <TiArrowSortedDown />
              </button>
              <div className={styles.dropdownContent}>
                <a href="#" onClick={handleStage2Change}>
                  20
                </a>
                <a href="#" onClick={handleStage2Change}>
                  30
                </a>
                <a href="#" onClick={handleStage2Change}>
                  40
                </a>
                <a href="#" onClick={handleStage2Change}>
                  50
                </a>
              </div>
            </div>
          </div>
        </div>
        <div className={styles.themeInputBox}>
          <p style={{ margin: '0px' }}>테마 선택</p>
          <div className={styles.themeList}>
            <div
              className={`${styles.themeItem} ${themeId === 1 ? styles.selectedTheme : ''
                }`}
              onClick={() => handleThemeChange(1)}
            >
              <Image
                className={styles.shadow}
                width={100}
                height={100}
                src="/assets/images/themes/RandomTheme.jpg"
                alt="랜덤 이미지"
                priority
              />
              <p className={styles.themeName}>랜덤</p>
            </div>
            <div
              className={`${styles.themeItem} ${themeId === 2 ? styles.selectedTheme : ''
                }`}
              onClick={() => handleThemeChange(2)}
            >
              <Image
                className={styles.shadow}
                width={100}
                height={100}
                src="/assets/images/themes/KoreaTheme.jpg"
                alt="한국 이미지"
                priority
              />
              <p className={styles.themeName}>한국</p>
            </div>
            <div
              className={`${styles.themeItem} ${themeId === 3 ? styles.selectedTheme : ''
                }`}
              onClick={() => handleThemeChange(3)}
            >
              <Image
                className={styles.shadow}
                width={100}
                height={100}
                src="/assets/images/themes/GreekTheme.jpg"
                alt="그리스 이미지"
                priority
              />
              <p className={styles.themeName}>그리스</p>
            </div>
            <div
              className={`${styles.themeItem} ${themeId === 4 ? styles.selectedTheme : ''
                }`}
              onClick={() => handleThemeChange(4)}
            >
              <Image
                className={styles.shadow}
                width={100}
                height={100}
                src="/assets/images/themes/EgyptTheme.jpg"
                alt="이집트 이미지"
                priority
              />
              <p className={styles.themeName}>이집트</p>
            </div>
            <div
              className={`${styles.themeItem} ${themeId === 5 ? styles.selectedTheme : ''
                }`}
              onClick={() => handleThemeChange(5)}
            >
              <Image
                className={styles.shadow}
                width={100}
                height={100}
                src="/assets/images/themes/LandmarkTheme.jpg"
                alt="랜드마크 이미지"
                priority
              />
              <p className={styles.themeName}>랜드마크</p>
            </div>
          </div>
        </div>
        <div className={styles.modalButtons}>
          <p className={styles.exit} onClick={closeModal}>
            취소
          </p>
          <p className={styles.start} onClick={handleSubmit}>
            시작 ➜
          </p>
        </div>
      </dialog>
    </div>
  )
}
