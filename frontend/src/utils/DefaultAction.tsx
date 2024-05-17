'use client'

import { usePathname, useRouter } from 'next/navigation'
import { useEffect } from 'react'

export default function DefaultAction() {
  const onBackButtonEvent = (e: PopStateEvent) => {
    e.stopPropagation()
  }

  const router = useRouter()
  const pathname = usePathname()
  useEffect(() => {
    // 로그인이 안돼있으면 로그인 화면으로 보내기
    // if (
    //   !localStorage.getItem('accessToken') &&
    //   pathname != '/' &&
    //   pathname != '/login'
    // ) {
    //   router.push('/')
    // }
    // 뒤로 가기 막기
    window.history.pushState(null, '', window.location.pathname)
    window.addEventListener('popstate', onBackButtonEvent)
    return () => {
      window.removeEventListener('popstate', onBackButtonEvent)
    }
  }, [])
  return <></>
}
