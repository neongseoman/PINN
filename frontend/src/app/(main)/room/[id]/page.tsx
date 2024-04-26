import styles from './room.module.css'

import TeamList from './_components/TeamList'

export default function RoomPage({ params }: { params: { id: number } }) {
  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles['option-team-container']}>
          <div className={styles['option-wrapper']}>옵션 컴포넌트 입니다</div>
          <TeamList></TeamList>
        </div>
      </div>
    </main>
  )
}
