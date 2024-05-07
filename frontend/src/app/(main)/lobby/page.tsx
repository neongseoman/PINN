'use client'

import useUserStore from '@/stores/userStore'

export default function LobbyPage() {

  const { gamerId, nickname } = useUserStore()

  console.log(gamerId)
  console.log(nickname)
  return (
    <main>
      <h1>로비 페이지</h1>
    </main>
  )
}
