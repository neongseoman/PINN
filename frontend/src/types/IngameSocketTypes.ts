type IngameProgressCode = 1201 | 1202 | 1203 | 1204 | 1205 | 1206

export interface GameProgressInfo {
  code: IngameProgressCode
  msg: string
  round: number
}

export type PinRespoonse = PinPickResponse | PinGuessResponse

interface PinPickResponse {
  senderDateTime: string
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  code: 1115
  msg: string
  gamerId: number
  submitLat: number
  submitLng: number
  roundNumber: number
  submitStage: number
}

interface PinGuessResponse {
  senderDateTime: string
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  code: 1116
  msg: string
}
