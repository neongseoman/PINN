'use client'

import Lottie from 'react-lottie-player'

interface LottieProps {
  animationData: any
  play: boolean
  loop: boolean
  setPlay: (value: boolean) => void
  speed: number
}

export default function LottieAnimation({
  animationData,
  play,
  setPlay,
  speed,
  loop,
}: LottieProps) {
  return (
    <Lottie
      animationData={animationData}
      play={play}
      loop={loop}
      speed={speed}
      onComplete={() => setPlay(false)}
    />
  )
}
