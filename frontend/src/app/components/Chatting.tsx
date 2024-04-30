'use client'

import { useState, useEffect } from 'react'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import styles from './chatting.module.css'

interface MessageFormat {
  content: string
}

export default function Chatting() {
  const [messages, setMessages] = useState<string[]>([])
  const [newMessage, setNewMessage] = useState<string>('')

  const socketUrl = 'aa'
  const subscribeUrl = 'aa'
  const destinationUrl = 'aa'

  const client = new Client({
    brokerURL: socketUrl,
    connectHeaders: {
      login: 'user',
      passcode: 'password',
    },
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
      client.deactivate() // Cleanup on unmount
    }
  }, [])

  const handleSendMessage = () => {
    client.publish({
      destination: destinationUrl,
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
