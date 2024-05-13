'use client'

import { useEffect, useState } from 'react'
import styles from './timer.module.css'

interface TimerProp {
  stageTime: number
}

export default function Timer({ stageTime }: TimerProp) {
  const [remainSeconds, setRemainSeconds] = useState<number>(stageTime)

  //init 타임으로 하기
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
  }, [stageTime, remainSeconds])

  //서버에서 시간 받아오기
  useEffect(() => {}, [])

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
