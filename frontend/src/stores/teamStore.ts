// import { StateCreator, create } from 'zustand'
// import { persist } from 'zustand/middleware'

// interface TeamGamers {
//   colorId: number
//   gamerId: number
//   teamId: number
// }

// interface Team {
//   colorCode: string
//   teamNumber: number
//   teamGamers: TeamGamers[]
//   ready: boolean
// }

// interface TeamState {
//   teams: Team[]
//   setTeams: (teamNumber: number, teamOrder: number, user: string) => void
//   setReady: (teamNumber: number, isReady: number) => void
//   setMove: (teamNumber: number, user: string) => void
// }

// const useTeamStore = (gameId: number) =>
//   create(
//     persist<TeamState>(
//       (set) => ({
//         teams: [
//           {
//             teamNumber: 1,
//             colorCode: 'rgba(255, 0, 61, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//           {
//             teamNumber: 2,
//             colorCode: 'rgba(182, 53, 53, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//           {
//             teamNumber: 3,
//             colorCode: 'rgba(255, 111, 0, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//           {
//             teamNumber: 4,
//             colorCode: 'rgba(153, 155, 41, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//           {
//             teamNumber: 5,
//             colorCode: 'rgba(0, 131, 143, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//           {
//             teamNumber: 6,
//             colorCode: 'rgba(105, 53, 170, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//           {
//             teamNumber: 7,
//             colorCode: 'rgba(251, 52, 159, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//           {
//             teamNumber: 8,
//             colorCode: 'rgba(255, 172, 207, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//           {
//             teamNumber: 9,
//             colorCode: 'rgba(188, 157, 157, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//           {
//             teamNumber: 10,
//             colorCode: 'rgba(85, 85, 85, 1)',
//             teamGamers: [],
//             ready: false,
//           },
//         ],
//         // team 입장 / 옮기기
//         setTeams: (teamNumber, teamOrder, user) =>
//           set((state) => {
//             const updatedTeams = state.teams.map((team) => {
//               if (team.teamNumber === teamNumber) {
//                 const updatedTeamMember = [...team.teamGamers]
//                 updatedTeamMember[teamOrder] = user
//                 return { ...team, teamMember: updatedTeamMember }
//               }
//               return team
//             })
//             return { teams: updatedTeams }
//           }),
//         // team 레디 상태 변경
//         setReady: (teamNumber, isReady) =>
//           set((state) => {
//             const updatedTeams = state.teams.map((team) => {
//               if (team.teamNumber === teamNumber) {
//                 return { ...team, isReady }
//               }
//               return team
//             })
//             return { teams: updatedTeams }
//           }),

//         setMove: (teamNumber, user) =>
//           set((state) => {
//             const updatedTeams = state.teams.map((team) => {
//               if (team.teamNumber === teamNumber) {
//                 const updatedTeamMember = [...team.teamMember]
//                 // 사용자를 배정하기 전에 기존 팀에서 사용자를 제거합니다.
//                 state.teams.forEach((t) => {
//                   const userIndex = t.teamMember.indexOf(user)
//                   if (userIndex !== -1) {
//                     t.teamMember[userIndex] = ''
//                   }
//                 })
//                 // 다음으로 배정할 인덱스를 찾습니다.
//                 const nextIndex = updatedTeamMember.findIndex(
//                   (member) => !member,
//                 )
//                 if (nextIndex !== -1) {
//                   updatedTeamMember[nextIndex] = user
//                 }
//                 return { ...team, teamMember: updatedTeamMember }
//               }
//               return team
//             })
//             return { teams: updatedTeams }
//           }),
//       }),
//       { name: `team-storage-${gameId}` },
//     ),
//   )

// export default useTeamStore
