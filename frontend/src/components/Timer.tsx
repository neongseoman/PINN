'use client'

import styles from './timer.module.css'

interface TimerProp {
  remainSeconds: number
}

export default function Timer({ remainSeconds }: TimerProp) {
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
