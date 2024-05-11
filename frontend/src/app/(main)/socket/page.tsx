'use client'

import SocketTest from '@/app/(main)/socket/_components/SocketTest'
import { Client } from '@stomp/stompjs'
import { useRouter } from 'next/navigation'
import { useEffect, useRef, useState } from 'react'

export default function SocketPage() {
  const subscribeInputRef = useRef<HTMLInputElement | null>(null)
  const publishInputRef = useRef<HTMLInputElement | null>(null)
  const [subscribeUrl, setSubscribeUrl] = useState<string | null>()
  const [publishUrl, setPublishUrl] = useState<string | null>()
  const [socketOpen, setSocketOpen] = useState<boolean>(false)
  const [roomNumber, setRoomNumber] = useState<number | null>(0)
  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SERVER_SOCKET_URL,
      debug: function (str: string) {
        console.log(str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )

  const router = useRouter()

  function handleUrlChange(event: any) {
    event.preventDefault()
    if (
      subscribeInputRef.current?.value.trim() &&
      publishInputRef.current?.value.trim()
    ) {
      setSubscribeUrl(subscribeInputRef.current.value)
      setPublishUrl(publishInputRef.current.value)
    } else {
      alert('url 모두 다 입력 바람')
    }
  }

  useEffect(() => {
    if (subscribeUrl && publishUrl) {
      setSocketOpen(true)
    }
  }, [subscribeUrl, publishUrl])

  const createRoom = async () => {
    const res = await fetch(process.env.NEXT_PUBLIC_API_URL + '/lobby/create', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${
          localStorage.getItem('accessToken') as string
        }`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        themeId: 1,
        roomName: '123',
        roundCount: 1,
        stage1Time: 10,
        stage2Time: 10,
        password: '!234',
      }),
    })
    const data = await res.json()
    console.log('방 번호 : ' + data.result.gameId)
    setRoomNumber(data.result.gameId)
    subscribeInputRef.current!.value = `/game/sse/${roomNumber}`
    publishInputRef.current!.value = `/app/game/start`
  }

  function moveRoom() {
    router.push(`/game/${roomNumber}/1`)
  }

  return (
    <main>
      <div style={{ display: 'flex' }}>
        <div style={{ margin: '20px' }}>
          <label htmlFor="subscribeUrl">Subscribe URL</label>
          <input
            type="text"
            name="subscribeUrl"
            id="subscribUrl"
            ref={subscribeInputRef}
          />
        </div>
        <div style={{ margin: '20px' }}>
          <label htmlFor="publishUrl">Publish URL</label>
          <input
            type="text"
            name="publishUrl"
            id="publishUrl"
            ref={publishInputRef}
          />
        </div>
        <button onClick={handleUrlChange}>URL 설정</button>
      </div>
      {socketOpen && (
        <SocketTest
          subscribeUrl={subscribeUrl!}
          publishUrl={publishUrl!}
          room={roomNumber}
        />
      )}

      <button onClick={createRoom}>방만들기</button>
      <button onClick={moveRoom}>방 이동</button>
      <p>당신이 만든 방 번호 {roomNumber}</p>
    </main>
  )
}
