'use client'

import Lottie from 'react-lottie-player'

interface LottieProps {
  animationData: object
  speed: number
  loop: boolean
  play: boolean
  setPlay: (value: boolean) => void
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
