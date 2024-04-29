'use client'

import styles from '../lobby.module.css'
import Image from 'next/image'

export default function RoomCard() {
  const joinGame = () => {
    // 게임 참여하기
    // 비밀방 true -> 비밀번호 확인 모달 띄우기
    // 비밀방 false -> 해당 게임 대기방으로 이동
  };

  return (
    <>
      <div className={styles.card} onClick={joinGame}>
        <Image width={110} height={110} src="/assets/images/themes/EgyptTheme.jpg" alt="테마 이미지" />
        <div className={styles.roomInfo}>
          <p className={styles.title}>방 제목</p>
          <p className={styles.theme}>테마</p>
          <div className={styles.bottomInfo}>
            <p className={styles.round}>라운드 수</p>
            <p className={styles.count}>인원(실시간)</p>
          </div>
        </div>
      </div>
    </>
  )
}
