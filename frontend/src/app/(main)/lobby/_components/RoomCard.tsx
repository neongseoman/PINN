'use client'

import PrivateRoomModal from '@/app/(main)/lobby/_components/PrivateRoomModal';
import PublicRoomModal from '@/app/(main)/lobby/_components/PublicRoomModal';
import Image from 'next/image';
import { useEffect, useState } from 'react';
import { IoIosLock } from "react-icons/io";

import styles from '../lobby.module.css';

interface RoomCardProps {
  gameId: number
  themeId: number
  roomName: string
  roundCount: number
  stage1Time: number
  stage2Time: number
  password: boolean
  countPerson: number
}

export default function RoomCard({ gameId, themeId, roomName, roundCount, password, countPerson }: RoomCardProps) {
  const [showModal, setShowModal] = useState<boolean>(false);
  const [themeName, setThemeName] = useState<string>('');
  const [themeImageUrl, setThemeImageUrl] = useState<string>('');

  useEffect(() => {
    setThemeName(getThemeName(themeId));
    setThemeImageUrl(getThemeImageUrl(themeId));
  }, [themeId]);

  const getThemeName = (themeId: number): string => {
    switch (themeId) {
      case 1:
        return "랜덤";
      case 2:
        return "한국";
      case 3:
        return "그리스";
      case 4:
        return "이집트";
      default:
        return "랜드마크";
    }
  };

  const getThemeImageUrl = (themeId: number): string => {
    switch (themeId) {
      case 1:
        return "/assets/images/themes/RandomTheme.jpg";
      case 2:
        return "/assets/images/themes/KoreaTheme.jpg";
      case 3:
        return "/assets/images/themes/GreekTheme.jpg";
      case 4:
        return "/assets/images/themes/EgyptTheme.jpg";
      default:
        return "/assets/images/themes/LandmarkTheme.jpg";
    }
  };

  const joinRoom = () => {
    setShowModal(true);
  };

  return (
    <>
      <div className={styles.card} onClick={joinRoom}>
        <p className={styles.privateIcon}>{password ? <IoIosLock /> : null}</p>
        <Image width={110} height={110} src={themeImageUrl} alt="테마 이미지" />
        <div className={styles.roomInfo}>
          <p className={styles.title}>{roomName}</p>
          <p className={styles.theme}>{themeName}</p>
          <div className={styles.bottomInfo}>
            <p className={styles.round}>라운드 수 {roundCount}</p>
            <p className={styles.count}>인원 {countPerson}/30</p>
          </div>
        </div>
      </div>
      {showModal && password ? <PrivateRoomModal gameId={gameId} roomName={roomName} setShowModal={setShowModal} /> : null}
      {showModal && !password ? <PublicRoomModal gameId={gameId} roomName={roomName} setShowModal={setShowModal} /> : null}
    </>
  )
}
