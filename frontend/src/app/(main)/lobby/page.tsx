'use client'

import useUserStore from '@/stores/userStore'

export default function LobbyPage() {
  const { gamerId, nickname } = useUserStore()

  console.log(gamerId)
  console.log(nickname)
  // const router = useRouter();
  // const param = new URLSearchParams(window.location.search);
  // const token:any = param.get('code');
  // const accessToken = token.split('=')[1]
  // useEffect(() => {
  //   console.log(token) //code == token
  //   if(token !== undefined){
  //       localStorage.setItem("access-token", accessToken)
  //       router.push("/lobby")
  //     }
  // }, [accessToken]);

  return (
    <main>
      <h1>로비 페이지</h1>
    </main>
  )
}
