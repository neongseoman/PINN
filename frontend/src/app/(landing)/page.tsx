import Link from 'next/link'
import styles from './landing.module.css'
import { FaAnglesDown } from "react-icons/fa6";

export default function KakaoLogin() {

  return (
    <main className={styles.landing}>
      <p className={styles.logo}>지도 게임</p>
      <p className={styles.name}>PINN</p>
      <Link href={'https://kauth.kakao.com/oauth/authorize?client_id=a4bfe3f2b2f815648b923deb0a3c54c7&redirect_uri=http://www.pinn.kr/api/v1/oauth/kakao&response_type=code'} className={styles.login}>카카오 로그인</Link>
      <button className={styles.scroll}><FaAnglesDown /></button>
    </main>
  )
}
