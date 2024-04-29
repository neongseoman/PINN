'use client'

import React, { useState, useEffect } from 'react'
import io from 'socket.io-client'
import styles from './chatting.module.css'

export default function Chatting() {
  const [messages, setMessages] = useState<string[]>([])
  const [newMessage, setNewMessage] = useState<string>('')

  const socket = io('http://localhost:3000')

  useEffect(() => {
    socket.on('message', (message: string) => {
      setMessages((prevMessages) => [...prevMessages, message])
    })

    return () => {
      socket.disconnect()
    }
  }, [])

  const handleSendMessage = () => {
    socket.emit('message', newMessage)
    setNewMessage('')
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
