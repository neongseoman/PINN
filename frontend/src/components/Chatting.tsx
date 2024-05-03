'use client'

import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { useEffect, useState } from 'react'
import styles from './chatting.module.css'

interface MessageFormat {
  content: string
}

interface ChattingProps {
  subscribeUrl: string
  publishUrl: string
}

export default function Chatting({ subscribeUrl, publishUrl }: ChattingProps) {
  const [messages, setMessages] = useState<string[]>([])
  const [newMessage, setNewMessage] = useState<string>('')

  const client = new Client({
    brokerURL: process.env.NEXT_PUBLIC_SOCKET_URL,
    debug: function (str: string) {
      console.log(str)
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  })

  useEffect(() => {
    client.onConnect = function (_frame: IFrame) {
      client.subscribe(subscribeUrl, (message: IMessage) => {
        const messageResponse = JSON.parse(message.body) as MessageFormat
        setMessages((prevMessages) => [
          ...prevMessages,
          messageResponse.content,
        ])
      })
    }

    client.onStompError = function (frame: IFrame) {
      console.log('Broker reported error: ' + frame.headers['message'])
      console.log('Additional details: ' + frame.body)
    }

    client.activate()
    return () => {
      client.deactivate()
    }
  }, [])

  const handleSendMessage = () => {
    client.publish({
      destination: publishUrl,
      body: JSON.stringify({ newMessage: newMessage }),
    })
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
