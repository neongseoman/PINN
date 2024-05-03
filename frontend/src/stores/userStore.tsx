// 유저 정보 상태관리
// import { create } from 'zustand';

// interface infoState {
//   gamerId: number;
//   setGamerId: (gamerId: number) => void;
//   nickname: string;
//   setNickname: (nickname: string) => void;
// }

// interface userInfoState {
//   info: infoState;
//   addInfo: (gamerInfo: { gamerId: number; nickname: string; }) => void;
// }

// export const userStore = create<userInfoState>((set) => ({
//   info
//   addInfo: ({ gamerId, nickname }) =>
//     set((state) => ({
//       gamerId,
//       nickname
//     })),
// }));