import Chatting from '@/components/Chatting'

export default function ChatPage() {
  // 임시 게임 아이디
  const gameId = `111`

  // 채팅방 prop
  const chatTitle = '척추 채팅'
  const subscribeUrl = `/game/${gameId}`
  const publishUrl = `/app/game/chat/${gameId}`

  return (
    <div
      style={{
        width: '500px',
        height: '500px',
        backgroundColor: 'black',
        color: 'white',
      }}
    >
      <Chatting
        chatTitle={chatTitle}
        subscribeUrl={subscribeUrl}
        publishUrl={publishUrl}
        gameId={gameId}
      />
    </div>
  )
}
