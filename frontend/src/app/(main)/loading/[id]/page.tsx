'use client'

import { useEffect, useState } from 'react'
import styles from './loading.module.css'
import Image from 'next/image'
import { useRouter } from 'next/navigation'

export default function LoadingPage({ params }: { params: { id: number } }) {
    // 더미
    const [teams, setTeams] = useState([
        { rank: 2, teamNumber: 1, score: 15000 },
        { rank: 1, teamNumber: 2, score: 14000 },
        { rank: 3, teamNumber: 3, score: 13000 },
        { rank: 5, teamNumber: 4, score: 12000 },
        { rank: 4, teamNumber: 5, score: 11000 },
        { rank: 6, teamNumber: 6, score: 10000 },
        { rank: 7, teamNumber: 7, score: 9000 },
        { rank: 9, teamNumber: 8, score: 8000 },
        { rank: 8, teamNumber: 9, score: 7000 },
        { rank: 10, teamNumber: 10, score: 5000 },
    ])
    const [count, setCount] = useState(3)
    const router = useRouter()

    useEffect(() => {
        // Team API 호출
    }, [])

    useEffect(() => {
        const timer = setInterval(() => {
            setCount((prevCount) => prevCount - 1)
        }, 1000)

        if (count === 0) {
            clearInterval(timer)
            router.push('/loading/4')
        }

        return () => clearInterval(timer)
    }, [count, router])

    // 어디로 라우팅(소켓?연결?)할지만 추가하면 끝

    return (
        <main className={styles.background}>
            <div className={styles.container}>
                <div className={styles.round}>라운드 1</div>
                <div className={styles.count}>{count}</div>
                <div className={styles['rank-container']}>
                    <div className={styles.trophy}>
                        <Image src="/assets/images/svg/noto_1st-place-medal.svg" alt="" width={28} height={28} />
                        <Image src="/assets/images/svg/noto_2nd-place-medal.svg" alt="" width={28} height={28} />
                        <Image src="/assets/images/svg/noto_3rd-place-medal.svg" alt="" width={28} height={28} />
                    </div>
                    <div className={styles.rank}>
                        {teams
                            .sort((a, b) => a.rank - b.rank)
                            .map((team, index) => (
                                <div key={index} className={styles.teamList}>
                                    <div>{team.rank}. TEAM {team.teamNumber}</div>
                                    <div>{team.score.toLocaleString()}</div>
                                </div>
                            ))}
                    </div>
                </div>
            </div>
        </main>
    )
}
