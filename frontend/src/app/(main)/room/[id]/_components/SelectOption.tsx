'use client'

import styles from './selectOption.module.css'
import useUserStore from '@/stores/userStore';
import { useEffect, useState } from "react"

interface Props {
  roomId: string;
}

interface GameInfo {
  gameId: number;
  roomName: string;
  leaderId: string;
  roundCount: number;
  stage1Time: number;
  stage2Time: number;
  themeId: number;
}

export default function Option({ roomId }: Props) {
  const { gamerId, nickname } = useUserStore()

  const [gameInfo, setGameInfo] = useState<GameInfo | null>(null);
  const [selectedTheme, setSelectedTheme] = useState<string>('-');
  const [selectedRound, setSelectedRound] = useState<string>('-');
  const [selectedStage1, setSelectedStage1] = useState<string>('-');
  const [selectedStage2, setSelectedStage2] = useState<string>('-');
  const themeMapping: { [key: number]: string } = {
    1: "랜덤",
    2: "한국",
    3: "그리스",
    4: "이집트",
    5: "랜드마크"
  }


  useEffect(() => {
    const getGameInfo = async () => {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/room/${roomId}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('accessToken') as string
              }`,
          },
        },
      )

      if (response.ok) {
        const responseData = await response.json()
        if (responseData.code === 1000) {
          setGameInfo(responseData.result);
          console.log(responseData.result.themeId)
          setSelectedTheme(themeMapping[responseData.result.themeId]);
          setSelectedRound(responseData.result.roundCount.toString());
          setSelectedStage1(responseData.result.stage1Time.toString());
          setSelectedStage2(responseData.result.stage2Time.toString());
        } else {
          console.log('팀 목록 출력 실패!', responseData.code)
        }
      } else {
        console.error('팀 목록 요청 통신 실패', response)
      }
    }
    getGameInfo()
  }, [roomId])


  const themeOptions = ['랜덤', '한국', '그리스', '이집트', '랜드마크']
  const roundOptions = ['1', '2', '3', '4', '5']
  const stage1Options = ['30', '40', '50', '60']
  const stage2Options = ['20', '30', '40', '50']

  const handleThemeChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedTheme(event.target.value);
  };

  const handleRoundChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedRound(event.target.value);
  };

  const handleStage1Change = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedStage1(event.target.value);
  };

  const handleStage2Change = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedStage2(event.target.value);
  };

  const isLeader = gameInfo?.leaderId === gamerId ? true : false

  return (
    <div className={styles.container}>
      <div className={styles.theme}>
        {/* 테마 선택 select box */}
        <label htmlFor="theme">테마</label>
        <select id="theme" value={selectedTheme} onChange={handleThemeChange} disabled={!isLeader}>
          {themeOptions.map((option, index) => (
            <option key={index} value={option}>{option}</option>
          ))}
        </select>
      </div>
      <div>
        {/* 라운드 선택 select box */}
        <label htmlFor="round">라운드</label>
        <select id="round" value={selectedRound} onChange={handleRoundChange} disabled={!isLeader}>
          {roundOptions.map((option, index) => (
            <option key={index} value={option}>{option}</option>
          ))}
        </select>
      </div>
      {/* 스테이지 1 선택 select box */}
      <div>
        <label htmlFor="stage1">스테이지1</label>
        <select id="stage1" value={selectedStage1} onChange={handleStage1Change} disabled={!isLeader}>
          {stage1Options.map((option, index) => (
            <option key={index} value={option}>{option}초</option>
          ))}
        </select>
      </div>
      {/* 스테이지 2 선택 select box */}
      <div>
        <label htmlFor="stage2">스테이지2</label>
        <select value={selectedStage2} onChange={handleStage2Change} disabled={!isLeader}>
          {stage2Options.map((option, index) => (
            <option key={index} value={option}>{option}초</option>
          ))}
        </select>
      </div>
    </div>
  );
}
