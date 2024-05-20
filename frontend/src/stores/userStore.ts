// 유저 정보 상태관리

import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface UserState {
  gamerId: string | null
  nickname: string | null
  setGamerId: (gamerId: string) => void
  setNickname: (nickname: string) => void
}

const useUserStore = create(
  persist<UserState>(
    (set) => ({
      gamerId: null,
      nickname: null,
      setGamerId: (gamerId: string) => set({ gamerId }),
      setNickname: (nickname: string) => set({ nickname }),
    }),
    { name: 'user-storage' },
  ),
)

export default useUserStore
