'use client'

import { Client, IFrame } from '@stomp/stompjs'
import { useEffect, useRef, useState } from 'react'
import styles from './chatting.module.css'

interface MessageFormat {
  senderDateTime: string
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  code: number
  msg: string
  content: string
}

interface ChattingProps {
  gameId: number
}

export default function Chatting({ gameId }: ChattingProps) {
  const [messages, setMessages] = useState<string[]>([])
  const [newMessage, setNewMessage] = useState<string>('')

  const clientRef = useRef<Client>(
    new Client({
      brokerURL: process.env.NEXT_PUBLIC_SOCKET_URL,
      debug: function (str: string) {
        console.log(str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    }),
  )

  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      clientRef.current.subscribe(`/game/${gameId}`, (message: any) => {
        const messageResponse = JSON.parse(message.body) as MessageFormat
        setMessages((prevMessages) => [
          ...prevMessages,
          messageResponse.content,
        ])
      })
    }

    clientRef.current.onStompError = function (frame: IFrame) {
      console.log('Broker reported error: ' + frame.headers['message'])
      console.log('Additional details: ' + frame.body)
    }

    clientRef.current.activate()

    return () => {
      clientRef.current.deactivate()
    }
  }, [gameId])

  const handleSendMessage = () => {
    if (clientRef.current) {
      clientRef.current.publish({
        destination: `/app/game/chat/${gameId}`,
        // destination: `wss://www.pinn.kr/app/game/chat/${gameId}`,
        body: JSON.stringify({
          senderNickname: 'rockbison',
          senderGameId: 1,
          senderTeamId: 1,
          content: newMessage,
        }),
      })
      setNewMessage('')
    }
  }

  return (
    <div className={styles.room}>
      <ul>
        {messages.map((message, index) => (
          <li className={styles.chat} key={index}>
            {message}
          </li>
        ))}
      </ul>
      <input
        type="text"
        value={newMessage}
        onChange={(e) => setNewMessage(e.target.value)}
      />
      <button onClick={handleSendMessage}>Send</button>
    </div>
  )
}
