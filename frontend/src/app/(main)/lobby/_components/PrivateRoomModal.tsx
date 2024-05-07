'use client'

import { Dispatch, SetStateAction, useEffect, useRef, useState } from "react";
import { IoIosLock } from "react-icons/io";
import styles from '../lobby.module.css';

interface PrivateRoomModalProps {
  // roomName: string
  setShowModal: Dispatch<SetStateAction<boolean>>
}

// { roomName }: PrivateRoomModal
export default function PrivateRoomModal({ setShowModal }: PrivateRoomModalProps) {
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

  const enterPassword = () => {
    // 비밀번호 입력 후 참여 버튼 클릭
    // `${process.env.NEXT_PUBLIC_API_URL}/app/game/enter/${gameId}` get 요청
    // `${process.env.NEXT_PUBLIC_SOCKET_URL}/game/${gameId}` websocket 구독
  };

  return (
    <>
      <dialog className={styles.privateRoomModalWrapper} ref={dialogRef}>
        <p className={styles.modalTitle}>방 제목</p>
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
