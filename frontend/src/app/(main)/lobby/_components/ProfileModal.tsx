'use client'

import { useRef } from 'react'
import styles from '../lobby.module.css'

export default function RuleModal() {
  const dialogRef = useRef<HTMLDialogElement>(null)

  const showModal = () => {
    dialogRef.current?.showModal()
  }

  const closeModal = () => {
    dialogRef.current?.close()
  }

  return (
    <div>
      <p className={styles.buttons} onClick={showModal}>
        프로필 수정
      </p>

      <dialog className={styles.ruleWrapper} ref={dialogRef}></dialog>
    </div>
  )
}
