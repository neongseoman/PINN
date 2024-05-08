'use client'

import useUserStore from '@/stores/userStore';
import { Client, IFrame } from '@stomp/stompjs';
import { Dispatch, SetStateAction, useEffect, useRef } from "react";
import { FaQuestionCircle } from "react-icons/fa";
import styles from '../lobby.module.css';

interface PublicRoomModalProps {
  gameId: number,
  roomName: string,
  setShowModal: Dispatch<SetStateAction<boolean>>,
}

export default function PublicRoomModal({ gameId, roomName, setShowModal }: PublicRoomModalProps) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const { nickname } = useUserStore()

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
  )

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
          // 방 입장
          // 해당 방으로 이동
          if (clientRef.current) {
            clientRef.current.onConnect = function (_frame: IFrame) {
              clientRef.current.subscribe(`${process.env.NEXT_PUBLIC_SOCKET_URL}/game/${gameId}`, (message: any) => {
                // const messageResponse = JSON.parse(message.body) as MessageFormat
                // // console.log(messageResponse)
                // setMessages((prevMessages) => [...prevMessages, messageResponse])
              })
            }
            clientRef.current.publish({
              headers: {
                Auth: localStorage.getItem('accessToken') as string,
              },
              destination: `${process.env.NEXT_PUBLIC_SOCKET_URL}/app/game/enter/${gameId}`,
              body: JSON.stringify({
                senderNickname: nickname,
                senderGameId: gameId
              }),
            })
          }
        } else {
          console.log('공개방 입장 요청 실패!', responseData.code);
        }
      } else {
        console.error('공개방 입장 요청 통신 실패', response);
      }
    } catch (error) {
      console.error('에러 발생: ', error);
    }
  };
    // 입장 버튼 클릭
    // `${process.env.NEXT_PUBLIC_SOCKET_URL}/app/game/enter/${gameId}` websocket 구독

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
