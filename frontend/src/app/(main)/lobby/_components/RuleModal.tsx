'use client'

import React, { useRef } from "react";
import styles from '../lobby.module.css'
import ruleStyles from './rule.module.css'
import Image from "next/image";

export default function RuleModal() {
  // dialog 참조 ref
  const dialogRef = useRef<HTMLDialogElement>(null);
  
  // modal 오픈 함수
  const showModal = () => {
    dialogRef.current?.showModal(); // 모달창 노출. show() 호출하면 다이얼로그 노출
  };

  // Modal 닫기 함수
  const closeModal = () => {
    dialogRef.current?.close(); // 모달창 닫기
  };

  return (
    <div>
      <p className={styles.buttons} onClick={showModal}>게임 설명</p>

      <dialog className={styles.ruleWrapper} ref={dialogRef}>
        <div >
          참여 방법
        </div>
      </dialog>
    </div>
  );
}