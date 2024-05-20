'use client'

import KakaoScript from '@/utils/KakaoScript'
import { FaAnglesDown, FaAnglesUp } from 'react-icons/fa6'
import styles from './landing.module.css'

declare global {
  interface Window {
    Kakao: any
  }
}

export default function LandingPage() {
  const kakaoLogin = async () => {
    window.Kakao.Auth.authorize({
      redirectUri: process.env.NEXT_PUBLIC_KAKAO_LOGIN_REDIRECT_URI,
    })
    clickSound()
  }

  const hoverSound = () => {
    const audio = new Audio('/assets/sounds/hover.wav')
    audio.play()
  }

  const clickSound = () => {
    const audio = new Audio('/assets/sounds/click.mp3')
    audio.play()
  }

  return (
    <>
      <main className={styles.landing}>
        <img className={styles.logo} src="/assets/images/logo.png" alt="로고" />
        <div className={styles.guide}>
          <div className={styles.top}>
            <img
              className={styles.guide1}
              src="/assets/images/GuideLeft.png"
              alt="랜딩 페이지 설명 이미지 1"
            />
            <div className={styles.textWrapper}>
              <p className={styles.textTitle}>스트리트 뷰와 힌트를 참고해 위치를 유추하여 지도에 핀을 꽂아요!</p>
              <p className={styles.text}>- 한 라운드 내에 두 개의 스테이지가 있어요</p>
              <p className={styles.text}>- 라운드별로 하나의 장소를 맞히게 돼요</p>
              <p className={styles.text}>- 스테이지1에서 스테이지2로 넘어가면 더 많은 힌트를 볼 수 있어요</p>
              <p className={styles.text}>(스테이지1에서 정답을 맞히면 얻을 수 있는 최대 점수가 더 커요)</p>
            </div>
          </div>
          <div className={styles.bottom}>
            <img
              className={styles.guide2}
              src="/assets/images/GuideRight.png"
              alt="랜딩 페이지 설명 이미지 2"
            />
            <div className={styles.textWrapper}>
              <p className={styles.textTitle}>정답과 가장 가까운 곳에 핀을 꽂아서 우승해요!</p>
              <p className={styles.text}>- 팀원들과 핀을 공유하며 답을 찾고, 확정된 답을 최대한 빠르게 제출해요</p>
              <p className={styles.text}>- 우리 팀의 핀을 제출한 이후에는 다른 팀의 핀 위치를 볼 수 있어요</p>
              <p className={styles.text}>- 제출한 핀의 위치와 정답 위치가 가까울수록 높은 점수를 획득해요</p>
              <p className={styles.text}>- 라운드마다 얻은 점수를 합산하여 최종 우승 팀이 결정돼요</p>
            </div>
          </div>
          <p className={styles.textCenter}>더 자세한 설명은 로그인 후에 로비에서 확인할 수 있어요</p>
        </div>
        <img className={styles.login} onClick={kakaoLogin} onMouseEnter={hoverSound} src="/assets/images/KakaoLogin.png" alt="카카오 로그인" />
      </main>
      <KakaoScript />
    </>
  )
}
