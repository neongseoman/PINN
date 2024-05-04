'use client'

import useUserStore from '@/stores/userStore'
import { useRouter, useSearchParams } from 'next/navigation'
import { useEffect } from 'react'

export default function LoginSuccessPage() {
  const router = useRouter()
  const { setGamerId, setNickname } = useUserStore()

  // const param = new URLSearchParams(window.location.search)
  const params = useSearchParams()

  const token: string | null = params.get('code')

  // const accessToken = token.split('=')[1]
  let accessToken: string | null = null

  if (token !== null) {
    accessToken = token.split('=')[1]
  }

  useEffect(() => {
    console.log(token) // code == token
    // if (token !== undefined) {
    if (accessToken !== null) {
      localStorage.setItem('accessToken', accessToken)
    }
  }, [accessToken])

  const getUserInfo = async () => {
    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}` + '/gamer/userInfo',
      {
        method: 'GET',
        headers: { Authorization: `Bearer ${accessToken}` },
      },
    )

    const data = await res.json()
    console.log(data.result)

    const gamerId = data.result.gamerId
    const nickname = data.result.nickname

    setGamerId(gamerId)
    setNickname(nickname)

    console.log(gamerId)
    console.log(nickname)

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
