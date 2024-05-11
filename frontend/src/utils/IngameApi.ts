import { GameInitInfo, RoundInit, StageTwoInit } from '@/types/IngameRestTypes'

export async function getGameInfo(gameId: string) {
  const res = await fetch(process.env.NEXT_PUBLIC_API_URL + '/game/init', {
    method: 'POST',
    headers: {
      Authorization: ('Bearer ' +
        localStorage.getItem('accessToken')) as string,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ gameId }),
  })
  const resJson = (await res.json()) as GameInitInfo
  if (!resJson.success) {
    alert('gameInfo 실패!')
  }
  return resJson
}

export async function getRoundInfo(gameId: string, round: string) {
  const res = await fetch(
    process.env.NEXT_PUBLIC_API_URL + '/game/round/init',
    {
      method: 'POST',
      headers: {
        Authorization: ('Bearer ' +
          localStorage.getItem('accessToken')) as string,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ gameId, round }),
    },
  )
  const resJson = (await res.json()) as RoundInit
  if (!resJson.success) {
    alert('roundInfo 실패!')
  }
  return resJson
}

export async function getStageTwoHint(gameId: string, round: string) {
  const res = await fetch(
    process.env.NEXT_PUBLIC_API_URL + '/game/round/stage2/init',
    {
      method: 'POST',
      headers: {
        Authorization: ('Bearer ' +
          localStorage.getItem('accessToken')) as string,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ gameId, round }),
    },
  )
  const resJson = (await res.json()) as StageTwoInit
  if (!resJson.success) {
    alert('stageTwo 실패!')
  }
  return resJson
}
