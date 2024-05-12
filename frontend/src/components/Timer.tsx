'use client'

import { useEffect, useState } from 'react'
import styles from './timer.module.css'

interface TimerProp {
  initialTime: number
}

export default function Timer({ initialTime }: TimerProp) {
  const [remainSeconds, setRemainSeconds] = useState<number>(initialTime)

  useEffect(() => {
    const myInterval = setInterval(() => {
      if (remainSeconds > 0) {
        setRemainSeconds((prev) => prev - 1)
      } else {
        clearInterval(myInterval)
      }
    }, 1000)
    return () => {
      clearInterval(myInterval)
    }
  }, [remainSeconds])

  return (
    <>
      <div className={styles.timer}>
        {Math.floor(remainSeconds / 60)
          .toString()
          .padStart(2, '0')}{' '}
        : {(remainSeconds % 60).toString().padStart(2, '0')}
      </div>
    </>
  )
}
