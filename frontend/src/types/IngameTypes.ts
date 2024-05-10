// export type IngameResponse = GameProgressInfo | StageOneInfo | StageTwoInfo

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

// interface StageOneInfo {
//   gameId: number
//   hints: Hint[]
//   lat: number
//   //lng로 통일
//   lon: number
//   // 카멜케이스
//   question_id: number
//   question_name: string
//   round: number
//   sender_date_time: string
//   sender_nickname: string
//   sender_team_id: number
//   code: IngameProgressCode
//   msg: string
// }

// interface StageTwoInfo {
//   senderDateTime: string
//   senderNickname: string
//   senderGameId: number
//   senderTeamId: number
//   gameId: number
//   round: number
//   hints: Hint[]
//   code: IngameProgressCode
//   msg: string
//   questionId: number
//   questionName: string
//   lat: number
//   lng: number
// }
