import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface IngameInitState {
  teamId: number
  currentRound: number
  currentStage: number
  stage1Time: number
  stage2Time: number
}

interface IngameStore extends IngameInitState {
  setTeamId: (value: number) => void
  setCurrentStage: (value: number) => void
  setStage1Time: (value: number) => void
  setStage2Time: (value: number) => void
  ingameReset: () => void
}

const initState: IngameInitState = {
  teamId: 1,
  currentRound: 1,
  currentStage: 1,
  stage1Time: 30,
  stage2Time: 30,
}

const useIngameStore = create(
  persist<IngameStore>(
    (set) => ({
      ...initState,
      setCurrentStage: (value: number) => {
        set({ currentStage: value })
      },
      setTeamId: (value: number) => {
        set({ teamId: value })
      },
      setStage1Time: (value: number) => {
        set({ stage1Time: value })
      },
      setStage2Time: (value: number) => {
        set({ stage2Time: value })
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
