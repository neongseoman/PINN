import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface TimerState {
  seconds: number
  isRunning: boolean
  setSeconds: (value: number) => void
  decSeconds: () => void
  setIsRunning: (value: boolean) => void
}

const useTimerStore = create(
  persist<TimerState>(
    (set) => ({
      seconds: 30,
      isRunning: false,
      setSeconds: (value: number) => {
        set({ seconds: value })
      },
      decSeconds: () => {
        set((state) => ({ seconds: state.seconds - 1 }))
      },
      setIsRunning: (value: boolean) => {
        set({ isRunning: value })
      },
    }),
    {
      name: 'timer-storage',
      getStorage: () => sessionStorage,
    },
  ),
)

export default useTimerStore
