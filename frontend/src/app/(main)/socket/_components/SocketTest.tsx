import useUserStore from '@/stores/userStore'
import { Client, IFrame, IMessage } from '@stomp/stompjs'
import { FormEvent, useEffect, useRef, useState } from 'react'

interface SocketTestProp {
  subscribeUrl: string
  publishUrl: string
  room: number | null
}

export default function SocketTest({
  subscribeUrl,
  publishUrl,
  room,
}: SocketTestProp) {
  const [response, setResponse] = useState<any>(null)
  const { gamerId, nickname } = useUserStore()
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

  useEffect(() => {
    clientRef.current.onConnect = function (_frame: IFrame) {
      clientRef.current.subscribe(subscribeUrl, (message: IMessage) => {
        const messageResponse = JSON.parse(message.body)
        setResponse(messageResponse)
      })
      clientRef.current.subscribe('/user/error/' + gamerId, (message) => {
        const messageResponse = JSON.parse(message.body)
        setResponse(messageResponse)
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
  }, [subscribeUrl, publishUrl])

  function handleSubmit(event: FormEvent) {
    event.preventDefault()

    const form = event.target as HTMLFormElement
    const formData = new FormData(form)
    const requestText = formData.get('request') as string
    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: publishUrl,
      body: JSON.stringify(JSON.parse(requestText.trim())),
    })
  }

  function gameStart() {
    clientRef.current.publish({
      headers: {
        Auth: localStorage.getItem('accessToken') as string,
      },
      destination: publishUrl,
      body: JSON.stringify(gameStartRequest),
    })
  }

  const gameStartRequest = {
    senderNickname: nickname,
    senderGameId: room,
    senderTeamId: 1,
    gameId: room,
    roundCount: 3,
    stage1Time: 10,
    stage2Time: 10,
    scorePageTime: 10,
  }

  function handleClear(event: React.MouseEvent<HTMLElement, MouseEvent>) {
    event.preventDefault()
    setResponse('')
  }

  return (
    <div style={{ display: 'flex' }}>
      <div>
        <div>REQUEST</div>
        <form
          onSubmit={handleSubmit}
          style={{
            display: 'flex',
            flexDirection: 'column',
            marginRight: '10px',
          }}
        >
          <textarea
            name="request"
            id="request"
            style={{ width: '300px', height: '300px' }}
          />
          <button>publish</button>
        </form>
      </div>
      <div style={{ display: 'flex', flexDirection: 'column' }}>
        <div>RESPONSE</div>
        <div
          style={{
            border: 'solid 1px',
            width: '300px',
            height: '300px',
          }}
        >
          {response ? (
            <pre>{JSON.stringify(response, null, 2)}</pre>
          ) : (
            'No response yet'
          )}
        </div>
        <button onClick={handleClear}>clear</button>
        <button onClick={gameStart}>방시작</button>
      </div>
    </div>
  )
}
