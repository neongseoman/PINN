import DefaultAction from '@/utils/DefaultAction'
import type { Metadata } from 'next'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'
import './globals.css'

export const metadata: Metadata = {
  title: 'PINN',
  description: '현재 위치를 지도에 찍어보세요',
  icons: {
    icon: '/assets/images/favicon.png',
  },
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="ko-KR">
      <body>
        {children}
        <ToastContainer />

        <DefaultAction />
      </body>
    </html>
  )
}
