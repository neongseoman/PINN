"use client"

import { useCallback, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { string } from 'prop-types'

export default function LobbyPage() {
  const router = useRouter();
  const param = new URLSearchParams(window.location.search);
  const token:any = param.get('code');
  const accessToken = token.split('=')[1]
  useEffect(()=>{
    console.log(token) //code == token
    if(token !== undefined){
        localStorage.setItem("access-token", accessToken)
        router.push("/room")
      }
  }, [accessToken]);


  return (
    <main>
      <h1>로비 페이지</h1>
    </main>
  )
}
