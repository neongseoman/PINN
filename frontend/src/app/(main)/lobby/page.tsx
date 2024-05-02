'use client'

import styles from './lobby.module.css'
import Image from 'next/image'
import RoomCard from './_components/RoomCard';
import CreateRoom from './_components/CreateRoom';

export default function LobbyPage() {
  const profileModal = () => {
    // 프로필 수정 모달 띄우기
  };

  const createGame = () => {
    // 게임 생성
  };

  const fastStart = () => {
    // 빠른 시작
  };

  const ruleModal = () => {
    // 게임 설명 모달 띄우기
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
            <CreateRoom />
            <p className={styles.buttons} onClick={fastStart}>빠른 시작</p>
          </div>
          <p className={styles.buttons} onClick={ruleModal}>게임 설명</p>
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
