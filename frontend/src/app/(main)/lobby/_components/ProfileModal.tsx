'use client'

import useCustomAlert from '@/components/useCustomAlert'
import useUserStore from '@/stores/userStore'
import { useRef, useState } from 'react'
import { FaEdit } from 'react-icons/fa'
import styles from '../lobby.module.css'

interface ProfileModalProps {
  hoverSound: () => void
  clickSound: () => void
}

export default function ProfileModal({
  hoverSound,
  clickSound,
}: ProfileModalProps) {
  const { nickname, setNickname } = useUserStore()
  const dialogRef = useRef<HTMLDialogElement>(null)
  const [newNickname, setNewNickname] = useState<string>(nickname || '')
  const { error } = useCustomAlert()

  const handleNicknameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setNewNickname(event.target.value)
  }

  const showModal = () => {
    clickSound()
    dialogRef.current?.showModal()
  }

  const closeModal = () => {
    setNewNickname('')
    dialogRef.current?.close()
  }

  const editNickname = async () => {
    if (!newNickname.trim()) {
      error('닉네임을 입력해 주세요')
      setNewNickname('')
    } else {
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_URL}/gamer/nickname`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${
                localStorage.getItem('accessToken') as string
              }`,
            },
            body: JSON.stringify({
              nickname: newNickname,
            }),
          },
        )

        if (response.ok) {
          console.log('닉네임 변경 통신 성공')
          const responseData = await response.json()
          if (responseData.code === 1000) {
            console.log('닉네임 변경 성공!', responseData)
            setNickname(newNickname)
            closeModal()
          } else {
            console.log('닉네임 변경 실패!', responseData.code)
            error(responseData.message)
          }
        } else {
          console.error('닉네임 변경 통신 실패', response)
        }
      } catch (error) {
        console.error('에러 발생: ', error)
      }
    }

    clickSound()
    console.log(`nickname: ${nickname}`)
    console.log(`newNickname: ${newNickname}`)
  }

  return (
    <div>
      <p
        className={styles.username}
        onMouseEnter={hoverSound}
        onClick={showModal}
      >
        {nickname}
      </p>

      <dialog className={styles.privateRoomModalWrapper} ref={dialogRef}>
        <p className={styles.modalTitle}>닉네임 수정</p>
        <hr className={styles.line} />
        <div className={styles.passwordWrapper}>
          <FaEdit />
          <input
            className={styles.inputNickname}
            type="text"
            value={newNickname || ''}
            placeholder={nickname || ''}
            onChange={handleNicknameChange}
          />
        </div>
        <div className={styles.modalButtons}>
          <p className={styles.exit} onClick={closeModal}>
            취소
          </p>
          <p className={styles.start} onClick={editNickname}>
            저장
          </p>
        </div>
      </dialog>
    </div>
  )
}
