import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface Team {
  teamNumber: number
  teamColor: string
  teamMember: string[]
  isReady: number
}

interface TeamState {
  teams: Team[]
  setTeams: (teamNumber: number, teamOrder: number, user: string) => void
  setReady: (teamNumber: number, isReady: number) => void
  setMove: (teamNumber: number, user: string) => void
}

const useTeamStore = create(
  persist<TeamState>(
    (set) => ({
      teams: [
        {
          teamNumber: 1,
          teamColor: 'rgba(255, 0, 61, 1)',
          teamMember: ['test', 'test', 'test'],
          isReady: 0,
        },
        {
          teamNumber: 2,
          teamColor: 'rgba(182, 53, 53, 1)',
          teamMember: ['', 'test', ''],
          isReady: 0,
        },
        {
          teamNumber: 3,
          teamColor: 'rgba(255, 111, 0, 1)',
          teamMember: ['', '', ''],
          isReady: 0,
        },
        {
          teamNumber: 4,
          teamColor: 'rgba(153, 155, 41, 1)',
          teamMember: ['', '', ''],
          isReady: 0,
        },
        {
          teamNumber: 5,
          teamColor: 'rgba(0, 131, 143, 1)',
          teamMember: ['', '', ''],
          isReady: 0,
        },
        {
          teamNumber: 6,
          teamColor: 'rgba(105, 53, 170, 1)',
          teamMember: ['', '', ''],
          isReady: 0,
        },
        {
          teamNumber: 7,
          teamColor: 'rgba(251, 52, 159, 1)',
          teamMember: ['', 'test', ''],
          isReady: 0,
        },
        {
          teamNumber: 8,
          teamColor: 'rgba(255, 172, 207, 1)',
          teamMember: ['', '', ''],
          isReady: 0,
        },
        {
          teamNumber: 9,
          teamColor: 'rgba(188, 157, 157, 1)',
          teamMember: ['', '', ''],
          isReady: 0,
        },
        {
          teamNumber: 10,
          teamColor: 'rgba(85, 85, 85, 1)',
          teamMember: ['', '', ''],
          isReady: 0,
        },
      ],
      // team 입장 / 옮기기
      setTeams: (teamNumber, teamOrder, user) =>
        set((state) => {
          const updatedTeams = state.teams.map((team) => {
            if (team.teamNumber === teamNumber) {
              const updatedTeamMember = [...team.teamMember]
              updatedTeamMember[teamOrder] = user
              return { ...team, teamMember: updatedTeamMember }
            }
            return team
          })
          return { teams: updatedTeams }
        }),
      // team 레디 상태 변경
      setReady: (teamNumber, isReady) =>
        set((state) => {
          const updatedTeams = state.teams.map((team) => {
            if (team.teamNumber === teamNumber) {
              return { ...team, isReady }
            }
            return team
          })
          return { teams: updatedTeams }
        }),

      setMove: (teamNumber, user) =>
        set((state) => {
          const updatedTeams = state.teams.map((team) => {
            if (team.teamNumber === teamNumber) {
              const updatedTeamMember = [...team.teamMember]
              // 사용자를 배정하기 전에 기존 팀에서 사용자를 제거합니다.
              state.teams.forEach((t) => {
                const userIndex = t.teamMember.indexOf(user)
                if (userIndex !== -1) {
                  t.teamMember[userIndex] = ''
                }
              })
              // 다음으로 배정할 인덱스를 찾습니다.
              const nextIndex = updatedTeamMember.findIndex((member) => !member)
              if (nextIndex !== -1) {
                updatedTeamMember[nextIndex] = user
              }
              return { ...team, teamMember: updatedTeamMember }
            }
            return team
          })
          return { teams: updatedTeams }
        }),
    }),
    { name: 'team-storage' },
  ),
)

export default useTeamStore
