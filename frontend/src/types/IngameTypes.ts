export type IngameResponse = RoundInfo | PinLocationInfo

interface RoundInfo {
  senderDateTime: number
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  code: number
  msg: string
  gameId: number
  round: number
  questionId: number
  questionName: string
  lat: number
  lng: number
  Hint: string[]
}

interface PinLocationInfo {
  senderDateTime: Date
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  code: number
  msg: string
  gamerId: number
  submitLat: number
  submitLng: number
  roundNumber: number
  submitStage: number
}
