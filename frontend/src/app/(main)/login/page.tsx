'use client'

import { useRouter } from 'next/navigation'
import { useEffect } from 'react'

interface User {
  gamerId: number,
  nickname: string,
}

export default function LoginSuccessPage() {
  const router = useRouter()
  const param = new URLSearchParams(window.location.search)
  const token: any = param.get('code')
  const accessToken = token.split('=')[1]

  useEffect(() => {
    console.log(token)  // code == token
    if (token !== undefined) {
      localStorage.setItem('access-token', accessToken)
    }
  }, [accessToken])

  const getUserInfo = async () => {
    const res = await fetch('http://localhost:8081' + '/gamer/userInfo', {
      method: 'GET',
      headers:{'Authorization': `Bearer ${accessToken}`}
    })

    const data = await res.json()
    console.log(data.result)

    const gamerId = data.result.gamerId
    const nickname = data.result.nickname

    localStorage.setItem('gamerId', gamerId)
    localStorage.setItem('nickname', nickname)

    router.push('/lobby')
  }

  getUserInfo()

  return (
    <main>
      {/*<button onClick={getUserInfo}>유저정보 주세요.</button>*/}
      <h1>로그인 성공 페이지</h1>
    </main>
  )
}
