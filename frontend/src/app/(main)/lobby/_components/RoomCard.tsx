'use client'

import PrivateRoomModal from '@/app/(main)/lobby/_components/PrivateRoomModal';
import PublicRoomModal from '@/app/(main)/lobby/_components/PublicRoomModal';
import Image from 'next/image';
import { useState } from 'react';
import styles from '../lobby.module.css';

interface RoomCardProps {
  gameId: number,
  themeId: number,
  roomName: string,
  roundCount: number,
  stage1Time: number,
  stage2Time: number,
  password: boolean,
}

export default function RoomCard({ gameId, themeId, roomName, roundCount, stage1Time, stage2Time, password }: RoomCardProps) {
  const [showModal, setShowModal] = useState<boolean>(false);

  const joinRoom = () => {
    setShowModal(true);
  };

  return (
    <>
      <div className={styles.card} onClick={joinRoom}>
        <Image width={110} height={110} src="/assets/images/themes/EgyptTheme.jpg" alt="테마 이미지" />
        <div className={styles.roomInfo}>
          <p className={styles.title}>{roomName}</p>
          <p className={styles.theme}>테마</p>
          <div className={styles.bottomInfo}>
            <p className={styles.round}>라운드 수 {roundCount}</p>
            <p className={styles.count}>인원(실시간)</p>
          </div>
        </div>
      </div>
      {showModal && password ? <PrivateRoomModal gameId={gameId} roomName={roomName} setShowModal={setShowModal} /> : null}
      {showModal && !password ? <PublicRoomModal gameId={gameId} roomName={roomName} setShowModal={setShowModal} /> : null}
    </>
  )
}
