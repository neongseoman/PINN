import type { Metadata } from 'next'
import './globals.css'
import KakaoScript from './(landing)/KakaoScript';

export const metadata: Metadata = {
  title: 'PINN',
  description: '현재 위치를 지도에 찍어보세요',
  icons: {
    icon: '/assets/images/favicon.png',
  },
}

declare global{
  interface Window{
    Kakao: any;
  }
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="ko-KR">
      <body>{children}</body>
      <KakaoScript />
    </html>
  )
}