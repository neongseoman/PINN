import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface IngameInitState {
  teamId: number
  teamColor: string
  theme: string
}

interface IngameStore extends IngameInitState {
  setTeamId: (value: number) => void
  setTeamColor: (value: string) => void
  setTheme: (value: string) => void
  ingameReset: () => void
}

const initState: IngameInitState = {
  teamId: 1,
  teamColor: 'red',
  theme: '랜덤',
}

const useIngameStore = create(
  persist<IngameStore>(
    (set) => ({
      ...initState,
      setTeamId: (value: number) => {
        set({ teamId: value })
      },
      setTeamColor: (value: string) => {
        set({ teamColor: value })
      },
      setTheme: (value: string) => {
        set({ theme: value })
      },
      ingameReset: () => {
        set(initState)
      },
    }),
    {
      name: 'Ingame-storage',
      getStorage: () => sessionStorage,
    },
  ),
)

export default useIngameStore
