'use client'

import React, { useRef, useState } from "react";
import styles from '../lobby.module.css'
import { FaArrowCircleRight } from "react-icons/fa";
import { TiArrowSortedDown } from "react-icons/ti";
import Image from "next/image";

export default function CreateRoomModal() {
  // dialog 참조 ref
  const dialogRef = useRef<HTMLDialogElement>(null);
  // 방 제목
  const [roomName, setRoomName] = useState<string>('');
  // 방 비밀번호
  const [roomPassword, setRoomPassword] = useState<string>('');
  // 라운드 수
  const [round, setRound] = useState<number>(3);
  // stage1 시간
  const [stage1, setStage1] = useState<number>(30);
  // stage2 시간
  const [stage2, setStage2] = useState<number>(20);
  // 테마
  const [theme, setTheme] = useState<number>(0);

  // 방 제목 변경 이벤트 핸들러
  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRoomName(event.target.value);
  };

  // 방 비밀번호 변경 이벤트 핸들러
  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRoomPassword(event.target.value);
  };

  // 라운드 수 변경 이벤트 핸들러
  const handleRoundChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(event.target.value);
    setRound(value);
  };
  
  // stage1 시간 변경 이벤트 핸들러
  const handleStage1Change = (event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => {
    const selectedStage = parseInt(event.currentTarget.innerText);
    setStage1(selectedStage);
  };

  // stage2 시간 변경 이벤트 핸들러
  const handleStage2Change = (event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => {
    const selectedStage = parseInt(event.currentTarget.innerText);
    setStage2(selectedStage);
  };

  // 테마 변경 이벤트 핸들러
  const handleThemeChange = (selectedTheme: number) => {
    setTheme(selectedTheme);
  };

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
      <p className={styles.buttons} onClick={showModal}>게임 생성</p>

      <dialog className={styles.createRoomWrapper} ref={dialogRef}>
        <p className={styles.modalTitle}>게임 생성</p>
        <hr className={styles.line} />
        <div className={styles.infoWrapper}>
          <p className={styles.titleInputBox}>
            방 제목 : <input className={styles.inputText} type="text" value={roomName} placeholder='20글자 이하' onChange={handleTitleChange} />
          </p>
          <p className={styles.passwordInputBox}>
            비밀번호 : <input className={styles.inputText} type="text" value={roomPassword} placeholder='8글자 이하' onChange={handlePasswordChange} />
          </p>
        </div>
        <div className={styles.infoWrapper}>
          <div className={styles.roundInputBox}>
            <span>라운드 수 : </span>
            <p className={styles.radio}>
              <label><input type="radio" name="options" value="1" checked={round === 1} onChange={handleRoundChange} />1</label>
              <label><input type="radio" name="options" value="2" checked={round === 2} onChange={handleRoundChange} />2</label>
              <label><input type="radio" name="options" value="3" checked={round === 3} onChange={handleRoundChange} />3</label>
            </p>
          </div>
          <p className={styles.stageInputBox}>
            스테이지 1 :
            <div className={styles.dropdown}>
              <button className={styles.dropbtn}>{stage1}&nbsp;초&nbsp;&nbsp;<TiArrowSortedDown /></button>
              <div className={styles.dropdownContent}>
                <a href="#" onClick={handleStage1Change}>30</a>
                <a href="#" onClick={handleStage1Change}>40</a>
                <a href="#" onClick={handleStage1Change}>50</a>
                <a href="#" onClick={handleStage1Change}>60</a>
              </div>
            </div>
          </p>
          <p className={styles.stageInputBox}>
            스테이지 2 :
            <div className={styles.dropdown}>
              <button className={styles.dropbtn}>{stage2}&nbsp;초&nbsp;&nbsp;<TiArrowSortedDown /></button>
              <div className={styles.dropdownContent}>
                <a href="#" onClick={handleStage2Change}>20</a>
                <a href="#" onClick={handleStage2Change}>30</a>
                <a href="#" onClick={handleStage2Change}>40</a>
                <a href="#" onClick={handleStage2Change}>50</a>
              </div>
            </div>
          </p>
        </div>
        <div className={styles.themeInputBox}>
          <p style={{ marginTop: '0px' }}>테마 선택</p>
          <div className={styles.themeList}>
            <div className={styles.themeItem} onClick={() => handleThemeChange(1)}>
              <Image className={styles.themeImage} width={100} height={100} src="/assets/images/themes/RandomTheme.jpg" alt="랜드마크 이미지" />
              <p className={styles.themeName}>랜덤</p>
            </div>
            <div className={styles.themeItem} onClick={() => handleThemeChange(2)}>
              <Image className={styles.themeImage} width={100} height={100} src="/assets/images/themes/KoreaTheme.jpg" alt="랜드마크 이미지" />
              <p className={styles.themeName}>한국</p>
            </div>
            <div className={styles.themeItem} onClick={() => handleThemeChange(3)}>
              <Image className={styles.themeImage} width={100} height={100} src="/assets/images/themes/GreekTheme.jpg" alt="랜드마크 이미지" />
              <p className={styles.themeName}>그리스</p>
            </div>
            <div className={styles.themeItem} onClick={() => handleThemeChange(4)}>
              <Image className={styles.themeImage} width={100} height={100} src="/assets/images/themes/EgyptTheme.jpg" alt="랜드마크 이미지" />
              <p className={styles.themeName}>이집트</p>
            </div>
            <div className={styles.themeItem} onClick={() => handleThemeChange(5)}>
              <Image className={styles.themeImage} width={100} height={100} src="/assets/images/themes/LandmarkTheme.jpg" alt="랜드마크 이미지" />
              <p className={styles.themeName}>랜드마크</p>
            </div>
          </div>
        </div>
        <div className={styles.modalButtons}>
          <p className={styles.exit} onClick={closeModal}>취소</p>
          <p className={styles.start}>시작 <FaArrowCircleRight /></p>
        </div>
      </dialog>
    </div>
  );
}