'use client'

import useUserStore from '@/stores/userStore'
import { useRouter } from 'next/navigation'
import { Suspense, useEffect, useState } from 'react'

export default function LoginPage() {
  const router = useRouter()
  const { setGamerId, setNickname } = useUserStore()
  const [ token, setToken ] = useState<string|null>(null)
  const [ accessToken, setAccessToken ] = useState<string|null>(null)

  useEffect(() => {
    const params = new URLSearchParams(window.location.search)

    setToken(params.get('code'))

    if (token !== null) {
      setAccessToken(token.split('=')[1].split(";")[0])
      // console.log(accessToken)
    }

    if (accessToken !== null) {
      localStorage.setItem('accessToken', accessToken)
      getUserInfo(accessToken)
    }
  }, [token, accessToken])

  const getUserInfo = async (accessToken: string) => {
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/gamer/userInfo`,
      // 'http://localhost:8081/gamer/userInfo',
      {
        method: 'GET',
        headers: { Authorization: `Bearer ${accessToken}` },
      },
    )

    const data = await res.json()

    const gamerIdData = data.result.gamerId
    const nicknameData = data.result.nickname
    
    if (gamerIdData && nicknameData) {
      setGamerId(gamerIdData)
      setNickname(nicknameData)
      router.push('/lobby')
    } else {
      alert('로그인에 실패하였습니다')
      router.push('/')
    }
  }

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <main>
        <h1>로그인 성공 페이지</h1>
      </main>
    </Suspense>
  )
}
