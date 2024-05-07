'use client'

import { Dispatch, SetStateAction, useEffect, useRef } from "react";
import { FaQuestionCircle } from "react-icons/fa";
import styles from '../lobby.module.css';

interface PublicRoomModalProps {
  gameId: number,
  roomName: string,
  setShowModal: Dispatch<SetStateAction<boolean>>,
}

export default function PublicRoomModal({ gameId, roomName, setShowModal }: PublicRoomModalProps) {
  // dialog 참조 ref
  const dialogRef = useRef<HTMLDialogElement>(null);

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

  const joinGame = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/app/game/enter/${gameId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken') as string}`
        },
        body: JSON.stringify({
          password: null
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
      <dialog className={styles.publicRoomModalWrapper} ref={dialogRef}>
        <div className={styles.textWrapper}>
          <p className={styles.questionIcon}><FaQuestionCircle /></p>
          <p className={styles.modalTitle}>{roomName}</p>
          <p className={styles.modalTitle}>방에 입장하시겠습니까?</p>
        </div>
        <div className={styles.modalButtons}>
          <p className={styles.exit} onClick={closeModal}>취소</p>
          <p className={styles.start} onClick={joinGame}>입장 ➜</p>
        </div>
      </dialog>
    </>
  )
}
