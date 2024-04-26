'use client'

import styles from './lobby.module.css'
import Image from 'next/image'

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
          <p className={styles.logo}>PINN</p>
          <div className={styles.userInfo}  onClick={profileModal}>
            <p className={styles.username}>유전의 힘</p>
            <Image className={styles.profile} width={35} height={35} src="/images/profile.jpg" alt="프로필 이미지" />
          </div>
        </div>
        <div className={styles.medium}>
          <div style={{ display: 'flex' }}>
            <p className={styles.buttons} onClick={createGame}>게임 생성</p>
            <p className={styles.buttons} onClick={fastStart}>빠른 시작</p>
          </div>
          <p className={styles.buttons} onClick={ruleModal}>게임 설명</p>
        </div>
        <div className={styles.bottom}>
          생성되어 있는 방 목록
        </div>
      </main>
    </>
  )
}
