import {
  GameInit,
  RoundInit,
  RoundWait,
  StageTwoInit,
} from '@/types/IngameRestTypes'

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
  const resJson = (await res.json()) as GameInit

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

  return resJson
}

export async function getTeamPins(gameId: string, round: string) {
  const res = await fetch(
    process.env.NEXT_PUBLIC_API_URL + '/game/round/guessed',
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
  const resJson = (await res.json()) as RoundWait

  return resJson
}
