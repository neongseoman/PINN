'use client'

import Image from "next/image";
import React, { useRef, useState } from "react";
import { TiArrowSortedDown } from "react-icons/ti";
import styles from '../lobby.module.css';

export default function CreateRoomModal() {
  // dialog 참조 ref
  const dialogRef = useRef<HTMLDialogElement>(null);
  // 방 제목
  const [roomName, setRoomName] = useState<string>('');
  // 방 비밀번호
  const [password, setPassword] = useState<string>('');
  // 라운드 수
  const [roundCount, setRoundCount] = useState<number>(3);
  // stage1 시간
  const [stage1Time, setStage1Time] = useState<number>(30);
  // stage2 시간
  const [stage2Time, setStage2Time] = useState<number>(20);
  // 테마
  const [themeId, setThemeId] = useState<number>(1);

  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRoomName(event.target.value);
  };

  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  const handleRoundChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(event.target.value);
    setRoundCount(value);
  };
  
  const handleStage1Change = (event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => {
    const selectedStage = parseInt(event.currentTarget.innerText);
    setStage1Time(selectedStage);
  };

  const handleStage2Change = (event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => {
    const selectedStage = parseInt(event.currentTarget.innerText);
    setStage2Time(selectedStage);
  };

  const handleThemeChange = (selectedTheme: number) => {
    setThemeId(selectedTheme);
  };

  const showModal = () => {
    dialogRef.current?.showModal();
  };

  const closeModal = () => {
    dialogRef.current?.close();
  };

  // 생성 요청 함수
  const handleSubmit = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/lobby/create`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('accessToken') as string}`
        },
        body: JSON.stringify({
          themeId: themeId,
          roomName: roomName,
          roundCount: roundCount,
          stage1Time: stage1Time,
          stage2Time: stage2Time,
          password: password
        }),
      });

      if (response.ok) {
        console.log('게임 생성 요청 통신 성공');
        const responseData = await response.json();
        if (responseData.code === 1000) {
          console.log('게임 생성 성공!', responseData);
          // 방 입장
          // 해당 방으로 이동
        } else {
          console.log('게임 생성 실패!', responseData.code);
        }
      } else {
        console.error('게임 생성 요청 통신 실패', response);
      }
    } catch (error) {
      console.error('에러 발생: ', error);
    }

    if (!roomName || roomName.length > 20 || roomName.length < 1) {
      alert('방 제목은 1글자 이상, 20글자 이하여야 합니다.');
      return;
    } else if (password.length > 8) {
      alert('비밀번호는 8글자 이하여야 합니다.');
      return;
    }
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
            비밀번호 : <input className={styles.inputText} type="text" value={password} placeholder='8글자 이하' onChange={handlePasswordChange} />
          </p>
        </div>
        <div className={styles.infoWrapper}>
          <div className={styles.roundInputBox}>
            <span>라운드 수 : </span>
            <p className={styles.radio}>
              <label><input type="radio" name="options" value="1" checked={roundCount === 1} onChange={handleRoundChange} />1</label>
              <label><input type="radio" name="options" value="2" checked={roundCount === 2} onChange={handleRoundChange} />2</label>
              <label><input type="radio" name="options" value="3" checked={roundCount === 3} onChange={handleRoundChange} />3</label>
            </p>
          </div>
          <div className={styles.stageInputBox}>
            스테이지 1 :
            <div className={styles.dropdown}>
              <button className={styles.dropbtn}>{stage1Time}&nbsp;초&nbsp;&nbsp;<TiArrowSortedDown /></button>
              <div className={styles.dropdownContent}>
                <a href="#" onClick={handleStage1Change}>30</a>
                <a href="#" onClick={handleStage1Change}>40</a>
                <a href="#" onClick={handleStage1Change}>50</a>
                <a href="#" onClick={handleStage1Change}>60</a>
              </div>
            </div>
          </div>
          <div className={styles.stageInputBox}>
            스테이지 2 :
            <div className={styles.dropdown}>
              <button className={styles.dropbtn}>{stage2Time}&nbsp;초&nbsp;&nbsp;<TiArrowSortedDown /></button>
              <div className={styles.dropdownContent}>
                <a href="#" onClick={handleStage2Change}>20</a>
                <a href="#" onClick={handleStage2Change}>30</a>
                <a href="#" onClick={handleStage2Change}>40</a>
                <a href="#" onClick={handleStage2Change}>50</a>
              </div>
            </div>
          </div>
        </div>
        <div className={styles.themeInputBox}>
          <p style={{ margin: '0px' }}>테마 선택</p>
          <div className={styles.themeList}>
            <div className={`${styles.themeItem} ${themeId === 1 ? styles.selectedTheme : ''}`} onClick={() => handleThemeChange(1)}>
              <Image className={styles.shadow} width={100} height={100} src="/assets/images/themes/RandomTheme.jpg" alt="랜덤 이미지" />
              <p className={styles.themeName}>랜덤</p>
            </div>
            <div className={`${styles.themeItem} ${themeId === 2 ? styles.selectedTheme : ''}`} onClick={() => handleThemeChange(2)}>
              <Image className={styles.shadow} width={100} height={100} src="/assets/images/themes/KoreaTheme.jpg" alt="한국 이미지" />
              <p className={styles.themeName}>한국</p>
            </div>
            <div className={`${styles.themeItem} ${themeId === 3 ? styles.selectedTheme : ''}`} onClick={() => handleThemeChange(3)}>
              <Image className={styles.shadow} width={100} height={100} src="/assets/images/themes/GreekTheme.jpg" alt="그리스 이미지" />
              <p className={styles.themeName}>그리스</p>
            </div>
            <div className={`${styles.themeItem} ${themeId === 4 ? styles.selectedTheme : ''}`} onClick={() => handleThemeChange(4)}>
              <Image className={styles.shadow} width={100} height={100} src="/assets/images/themes/EgyptTheme.jpg" alt="이집트 이미지" />
              <p className={styles.themeName}>이집트</p>
            </div>
            <div className={`${styles.themeItem} ${themeId === 5 ? styles.selectedTheme : ''}`} onClick={() => handleThemeChange(5)}>
              <Image className={styles.shadow} width={100} height={100} src="/assets/images/themes/LandmarkTheme.jpg" alt="랜드마크 이미지" />
              <p className={styles.themeName}>랜드마크</p>
            </div>
          </div>
        </div>
        <div className={styles.modalButtons}>
          <p className={styles.exit} onClick={closeModal}>취소</p>
          <p className={styles.start} onClick={handleSubmit}>시작 ➜</p>
        </div>
      </dialog>
    </div>
  );
}