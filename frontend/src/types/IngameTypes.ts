export type IngameResponse = RoundInfo | PinLocationInfo

type IngameResponseCode = 1201 | 1202 | 1203 | 1204 | 1205 | 1205

interface RoundInfo {
  senderDateTime: string
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  code: IngameResponseCode
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
  senderDateTime: string
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  code: IngameResponseCode
  msg: string
  gamerId: number
  submitLat: number
  submitLng: number
  roundNumber: number
  submitStage: number
}
