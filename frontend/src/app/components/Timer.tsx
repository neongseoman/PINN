'use client'

import styles from './timer.module.css'
import themeStyles from './theme.module.css'
import { useState, useEffect } from 'react'

interface TimerProp {
  theme: string
  initialTime: number
}

export default function Timer({ initialTime, theme }: TimerProp) {
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
      <div className={`${styles.timer}  ${themeStyles[theme]}`}>
        {minutes.toString().padStart(2, '0')} :{' '}
        {seconds.toString().padStart(2, '0')}
      </div>
    </>
  )
}
