export default function RoomPage({ params }: { params: { id: number } }) {
  return (
    <main>
      <h1>{params.id}번 방 페이지 </h1>
    </main>
  )
}
