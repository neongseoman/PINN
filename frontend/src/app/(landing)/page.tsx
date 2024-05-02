'use client'

import Link from 'next/link'
import styles from './landing.module.css'
import { FaAnglesDown } from "react-icons/fa6";
import { useSearchParams } from 'next/navigation';
import { useRouter } from 'next/navigation';
// import { useState } from 'react';

// const { Kakao } = window;
export default function LandingPage() {
  const router = useRouter();
  // const redirectUri = "http://localhost:8081/oauth/code/kakao"

  const kakaoLogin = async () => {
    window.Kakao.Auth.authorize({redirectUri: process.env.NEXT_PUBLIC_KAKAO_LOGIN_REDIRECT_URI});
    // console.log(redirectUri)
    // window.Kakao.Auth.authorize({redirectUri:redirectUri});
    // if (res) {
    //   const body = await res.json()
    //   console.log(body)
    //   router.push('/lobby');
    // }
    // const body = await res.json()
  }

  const scrollDown = () => {
    // 스크롤 최하단으로
    window.scrollTo({
      top: document.documentElement.scrollHeight,
      behavior: 'smooth'
    });
  }

  return (
    <main className={styles.landing}>
      <p className={styles.name}>지도 게임</p>
      <img className={styles.logo} src="/assets/images/logo.png" alt="로고" />
      {/* <Link href={`https://kauth.kakao.com/oauth/authorize?client_id=${process.env.NEXT_PUBLIC_KAKAO_REST_KEY}&redirect_uri=${process.env.NEXT_PUBLIC_KAKAO_LOGIN_REDIRECT_URI}&response_type=code`}> */}
      <button className={styles.login} onClick={kakaoLogin}><img src="/assets/images/kakao_login.png" alt="카카오 로그인" /></button>
      <button className={styles.scroll} onClick={scrollDown}><FaAnglesDown /></button>
      <div className={styles.guide}>
        <div className={styles.left}>
          <img className={styles.guide1} src="/assets/images/guide1.png" alt="랜딩 페이지 설명 이미지 1" />
          <p className={styles.text}>스트리트 뷰와 힌트를 참고해</p>
          <p className={styles.text}>정답과 근접한 곳을 유추하여</p>
          <p className={styles.text}>지도에 핀을 꽂아요!</p>
        </div>
        <div className={styles.right}>
          <img className={styles.guide2} src="/assets/images/guide2.png" alt="랜딩 페이지 설명 이미지 2" />
          <p className={styles.text}>정답과 가장 가까운 곳에</p>
          <p className={styles.text}>핀을 꽂아서 보물을 획득해요!</p>
        </div>
      </div>
    </main>
  )
}
