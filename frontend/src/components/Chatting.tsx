'use client'

import { Client, IFrame } from '@stomp/stompjs'
import { useEffect, useRef, useState } from 'react'
import { IoIosSend } from 'react-icons/io'
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
  const [messages, setMessages] = useState<MessageFormat[]>([])
  const [newMessage, setNewMessage] = useState<string>('')
  const chatContainerRef = useRef<HTMLDivElement | null>(null)
  const myId = useRef<string | null>(null)

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
    myId.current = localStorage.getItem('gamerId')
    clientRef.current.onConnect = function (_frame: IFrame) {
      clientRef.current.subscribe(`/game/${gameId}`, (message: any) => {
        const messageResponse = JSON.parse(message.body) as MessageFormat
        console.log(messageResponse)
        setMessages((prevMessages) => [...prevMessages, messageResponse])
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

  useEffect(() => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight
    }
  }, [messages])

  const handleSendMessage = () => {
    const trimmedMessage = newMessage.trim()
    if (!trimmedMessage) {
      setNewMessage('')
      return
    }

    if (clientRef.current) {
      clientRef.current.publish({
        headers: {
          Auth: localStorage.getItem('accessToken') as string,
        },
        destination: `/app/game/chat/${gameId}`,
        body: JSON.stringify({
          senderNickname: '노란목도리담비',
          senderGameId: 6,
          senderTeamId: 1,
          content: trimmedMessage,
        }),
      })
    }
    setNewMessage('')
  }

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSendMessage()
    }
  }

  return (
    <>
      <div className={styles.chatTitle}>팀 채팅</div>
      <div className={styles.chatList} ref={chatContainerRef}>
        {messages.map((message, index) => (
          <div
            className={
              message.senderGameId != Number(myId.current)
                ? `${styles.chatContainer}`
                : `${styles.myChatContainer}`
            }
            key={index}
          >
            <div className={styles.chatSender}>{message.senderNickname}</div>
            <div
              className={
                message.senderGameId != Number(myId.current)
                  ? `${styles.chatContent}`
                  : `${styles.myChatContent}`
              }
            >
              {message.content}
            </div>
          </div>
        ))}
      </div>
      <div className={styles.send}>
        <input
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="채팅을 입력하세요..."
          onKeyDown={handleKeyDown}
        />
        <div onClick={handleSendMessage}>
          <IoIosSend />
        </div>
      </div>
    </>
  )
}
