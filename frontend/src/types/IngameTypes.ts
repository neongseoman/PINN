export type IngameResponse = GameProgressInfo | StageOneInfo | StageTwoInfo
// | PinLocationInfo

type IngameResponseCode = 1201 | 1202 | 1203 | 1204 | 1205 | 1206

interface Hint {
  hintId: number
  hintTypeId: number
  hintTypeName: string
  hintValue: string
  offerStage: number
}

// GAME-001 004 005 006
interface GameProgressInfo {
  code: IngameResponseCode
  msg: string
}

// GAME-002
interface StageOneInfo {
  gameId: number
  hints: Hint[]
  lat: number
  //lng로 통일
  lon: number
  // 카멜케이스
  question_id: number
  question_name: string
  round: number
  sender_date_time: string
  sender_nickname: string
  sender_team_id: number
  code: IngameResponseCode
  msg: string
}

// GAME-003
interface StageTwoInfo {
  senderDateTime: string
  senderNickname: string
  senderGameId: number
  senderTeamId: number
  gameId: number
  round: number
  hints: Hint[]
  code: IngameResponseCode
  msg: string
  questionId: number
  questionName: string
  lat: number
  lng: number
}

// interface PinLocationInfo {
//   senderDateTime: string
//   senderNickname: string
//   senderGameId: number
//   senderTeamId: number
//   code: IngameResponseCode
//   msg: string
//   gamerId: number
//   submitLat: number
//   submitLng: number
//   roundNumber: number
//   submitStage: number
// }
