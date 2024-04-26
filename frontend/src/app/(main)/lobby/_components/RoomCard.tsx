'use client'

import styles from '../lobby.module.css'
import Image from 'next/image'

export default function RoomCard() {

  return (
    <>
      <div className={styles.card}>
        <Image width={140} height={140} src="/assets/images/theme.jpg" alt="테마 이미지" />
        <div className={styles.roomInfo}>
          <p className={styles.title}>방 제목</p>
          <p className={styles.theme}>테마</p>
          <div style={{ display: 'flex' }}>
            <p className={styles.round}>라운드 수</p>
            <p className={styles.count}>인원(실시간)</p>
          </div>
        </div>
      </div>
    </>
  )
}
