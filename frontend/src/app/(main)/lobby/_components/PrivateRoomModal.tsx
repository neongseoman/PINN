'use client'

import { Dispatch, SetStateAction, useEffect, useRef, useState } from "react";
import { IoIosLock } from "react-icons/io";
import styles from '../lobby.module.css';

interface PrivateRoomModalProps {
  gameId: number,
  roomName: string,
  setShowModal: Dispatch<SetStateAction<boolean>>,
}

export default function PrivateRoomModal({ gameId, roomName, setShowModal }: PrivateRoomModalProps) {
  // dialog 참조 ref
  const dialogRef = useRef<HTMLDialogElement>(null);
  // 사용자 입력 비밀번호
  const [password, setPassword] = useState<string>('')

  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  useEffect(() => {
    showModal();
  }, []);

  const showModal = () => {
    dialogRef.current?.showModal();
  };

  const closeModal = () => {
    setShowModal(false)
    dialogRef.current?.close();
  };

  const enterPassword = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/app/game/enter/${gameId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken') as string}`
        },
        body: JSON.stringify({
          password: password
        }),
      });

      if (response.ok) {
        const responseData = await response.json();
        console.log('방 입장 요청 성공!', responseData);
        // 방 입장
        // 해당 방으로 이동
      } else {
        console.error('방 입장 요청 실패!:', response);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };
    // 입장 버튼 클릭
    // `${process.env.NEXT_PUBLIC_API_URL}/app/game/enter/${gameId}` post 요청
    // `${process.env.NEXT_PUBLIC_SOCKET_URL}/game/${gameId}` websocket 구독

  return (
    <>
      <dialog className={styles.privateRoomModalWrapper} ref={dialogRef}>
        <p className={styles.modalTitle}>{roomName}</p>
        <hr className={styles.line}/>
        <div className={styles.passwordWrapper}><IoIosLock /><input className={styles.inputText} type="text" value={password} placeholder='비밀번호를 입력해 주세요' onChange={handlePasswordChange} /></div>
        <div className={styles.modalButtons}>
          <p className={styles.exit} onClick={closeModal}>취소</p>
          <p className={styles.start} onClick={enterPassword}>입장 ➜</p>
        </div>
      </dialog>
    </>
  )
}
