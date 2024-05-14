'use client'

import Lottie from 'react-lottie-player'

interface LottieProps {
  animationData: any
  play: boolean
  setPlay: (value: boolean) => void
  speed: number
}

export default function LottieAnimation({
  animationData,
  play,
  setPlay,
  speed,
}: LottieProps) {
  return (
    <Lottie
      animationData={animationData}
      play={play}
      loop={false}
      speed={speed}
      onComplete={() => setPlay(false)}
    />
  )
}
