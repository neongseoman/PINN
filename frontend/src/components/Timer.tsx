'use client'

import { useEffect, useState } from 'react'
import styles from './timer.module.css'

interface TimerProp {
  initialTime: number
}

export default function Timer({ initialTime }: TimerProp) {
  const [minutes, setMinutes] = useState<number>(Math.floor(initialTime / 60))
  const [seconds, setSeconds] = useState<number>(initialTime % 60)

  useEffect(() => {
    let myInterval = setInterval(() => {
      if (seconds > 0) {
        setSeconds(seconds - 1)
      } else {
        if (minutes <= 0) {
          clearInterval(myInterval)
        } else {
          setMinutes(minutes - 1)
          setSeconds(59)
        }
      }
    }, 1000)
    return () => {
      clearInterval(myInterval)
    }
  }, [minutes, seconds])

  return (
    <>
      <div className={styles.timer}>
        {minutes.toString().padStart(2, '0')} :{' '}
        {seconds.toString().padStart(2, '0')}
      </div>
    </>
  )
}
