'use client'

import { useEffect, useState } from 'react'
import styles from './roundResult.module.css'
import Image from 'next/image'
import { useRouter } from 'next/navigation'

interface Teams {

}

export default function LoadingPage({ params }: { params: { id: number } }) {
  const [teams, setTeams] = useState()
  const [count, setCount] = useState(5)
  const router = useRouter()

  useEffect(() => {
    // API 호출
  }, [])

  useEffect(() => {
    const timer = setInterval(() => {
      setCount((prevCount) => prevCount - 1)
    }, 1000)

    if (count === 0) {
      clearInterval(timer)
    }

    return () => clearInterval(timer)
  }, [count, router])

  // api받아오기

  return (
    <main className={styles.background}>
      <div className={styles.container}>
        <div className={styles.round}>라운드 {1}</div>
        <div className={styles.result}>Result</div>
        <div className={styles.map}></div>
        <div className={styles['rank-container']}>
          <div className={styles.trophy}>
            <Image src="/assets/images/svg/noto_1st-place-medal.svg" alt="" width={28} height={28} />
            <Image src="/assets/images/svg/noto_2nd-place-medal.svg" alt="" width={28} height={28} />
            <Image src="/assets/images/svg/noto_3rd-place-medal.svg" alt="" width={28} height={28} />
          </div>
          <div className={styles.rank}>
            {/* map함수로 팀원 나열해주기 */}
            <div className={styles.teamList}>
              <div>1. TEAM 1</div>
              <div>00,000</div>
            </div>
            <div className={styles.teamList}>
              <div>2. TEAM 1</div>
              <div>00,000</div>
            </div>
            <div className={styles.teamList}>
              <div>3. TEAM 1</div>
              <div>00,000</div>
            </div>
            <div className={styles.teamList}>
              <div>4. TEAM 1</div>
              <div>00,000</div>
            </div>
            <div className={styles.teamList}>
              <div>5. TEAM 1</div>
              <div>00,000</div>
            </div>
            <div className={styles.teamList}>
              <div>6. TEAM 1</div>
              <div>00,000</div>
            </div>
            <div className={styles.teamList}>
              <div>7. TEAM 1</div>
              <div>00,000</div>
            </div>
            <div className={styles.teamList}>
              <div>8. TEAM 1</div>
              <div>00,000</div>
            </div>
            <div className={styles.teamList}>
              <div>9. TEAM 1</div>
              <div>00,000</div>
            </div>
            <div className={styles.teamList}>
              <div>10. TEAM 1</div>
              <div>00,000</div>
            </div>
          </div>
        </div>
      </div>
    </main>
  )
}
