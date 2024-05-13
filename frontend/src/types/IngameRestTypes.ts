export interface GameInit {
  message: string
  code: number
  result: {
    gameId: number
    roomName: string
    themeId: number
    leaderId: number
    roundCount: number
    stage1Time: number
    stage2Time: number
    startedTime: string
  }
  success: boolean
}

export interface RoundInit {
  message: string
  code: number
  result: {
    gameId: number
    round: number
    questionId: number
    questionName: string
    lat: number
    lng: number
    hints: Hint[]
  }
  success: boolean
}

export interface StageTwoInit {
  message: string
  code: number
  result: {
    gameId: number
    round: number
    stage: number
    hints: Hint[]
  }
  success: boolean
}

export interface Hint {
  hintId: number
  hintTypeId: number
  hintTypeName: string
  hintValue: string
  offerStage: number
}
