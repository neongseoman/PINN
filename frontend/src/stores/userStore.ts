// 유저 정보 상태관리

import { create } from 'zustand'

interface UserState {
  gamerId: string | null
  nickname: string | null
  setGamerId: (gamerId: string) => void
  setNickname: (nickname: string) => void
}

const useUserStore = create<UserState>((set) => ({
  gamerId: null,
  nickname: null,
  setGamerId: (gamerId: string) => set({ gamerId }),
  setNickname: (nickname: string) => set({ nickname }),
}))

export default useUserStore
