import styles from './room.module.css'

import TeamList from './_components/TeamList'
import Option from './_components/option'

export default function RoomPage({ params }: { params: { id: number } }) {
  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles['option-team-container']}>
          <Option></Option>
          <TeamList></TeamList>
        </div>
        <div className={styles['chat-ready-out']}>
          <div className={styles.chat}>chatting</div>
          <div className={styles['ready-out']}>
            <button className={styles.ready}>준비</button>
            <button className={styles.out}>나가기</button>
          </div>
        </div>
      </div>
    </main>
  )
}
