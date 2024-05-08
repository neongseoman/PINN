'use client'

import useUserStore from '@/stores/userStore';
import { Client, IFrame } from '@stomp/stompjs';
import { useRouter } from 'next/navigation';
import { Dispatch, SetStateAction, useEffect, useRef } from "react";
import { FaQuestionCircle } from "react-icons/fa";
import styles from '../lobby.module.css';

interface PublicRoomModalProps {
  gameId: number
  roomName: string
  setShowModal: Dispatch<SetStateAction<boolean>>
}

export default function PublicRoomModal({ gameId, roomName, setShowModal }: PublicRoomModalProps) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const { nickname } = useUserStore();
  const router = useRouter();
  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SOCKET_URL,
      debug: function (str: string) {
        // console.log(str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  );

  useEffect(() => {
    showModal();
  }, []);
  
  const showModal = () => {
    dialogRef.current?.showModal();
    clientRef.current.activate();
    clientRef.current.onConnect = function (_frame: IFrame) {
      clientRef.current.subscribe(`/game/${gameId}`, () => {})
    };
  };

  const closeModal = () => {
    setShowModal(false)
    dialogRef.current?.close();
  };

  const joinGame = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/lobby/${gameId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken') as string}`
        },
        body: JSON.stringify({
          password: ""
        }),
      });

      if (response.ok) {
        console.log('공개방 입장 요청 통신 성공');
        const responseData = await response.json();
        if (responseData.code === 1000) {
          console.log('공개방 입장 요청 성공!', responseData);
          
          clientRef.current.publish({
            headers: {
              Auth: localStorage.getItem('accessToken') as string,
            },
            destination: `/app/game/enter/${gameId}`,
            body: JSON.stringify({
              senderNickname: nickname,
              senderGameId: gameId
            }),
          })

          console.log(`${gameId}번 방으로 입장합니다`)
          router.push(`/room/${gameId}`)
        } else {
          console.log('공개방 입장 요청 실패!', responseData.code);
          alert(responseData.message);
        }
      } else {
        console.error('공개방 입장 요청 통신 실패', response);
      }
    } catch (error) {
      console.error('에러 발생: ', error);
    }
  };

  return (
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
  )
}
