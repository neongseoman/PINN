'use client'

import styles from './lobby.module.css'
import Image from 'next/image'
import RoomCard from './_components/RoomCard';
import CreateRoomModal from './_components/CreateRoomModal';
import RuleModal from './_components/RuleModal';

export default function LobbyPage() {
  const profileModal = () => {
    // 프로필 수정 모달 띄우기
  };

  const fastStart = () => {
    // 빠른 시작
  };

  return (
    <>
      <main className={styles.lobby}>
        <div className={styles.top}>
          <img className={styles.logo} src="/assets/images/logo.png" alt="로고" />
          <div className={styles.userInfo} onClick={profileModal}>
            <p className={styles.username}>유전의 힘</p>
            <Image className={styles.profile} width={25} height={25} src="/assets/images/default_profile.png" alt="프로필 이미지" />
          </div>
        </div>
        <div className={styles.medium}>
          <div style={{ display: 'flex' }}>
            <CreateRoomModal />
            <p className={styles.buttons} onClick={fastStart}>빠른 시작</p>
          </div>
            <RuleModal />
        </div>
        <div className={styles.bottom}>
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
          
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
          <RoomCard />
        </div>
      </main>
    </>
  )
}
