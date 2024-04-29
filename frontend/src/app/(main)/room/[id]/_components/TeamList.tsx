'use client'

import styles from './teamList.module.css'
import Team from './Team'
import { useEffect, useRef, useState } from 'react'

interface Team {
    teamNumber: number
    teamColor: string
    teamMember: string[]
}

export default function TeamList() {
    const [teams, setTeams] = useState<Team[]>([
        { teamNumber: 1, teamColor: 'rgba(255, 0, 61, 1)', teamMember: ['테스트', '', ''] },
        { teamNumber: 2, teamColor: 'rgba(182, 53, 53, 1)', teamMember: ['', '테스트', ''] },
        { teamNumber: 3, teamColor: 'rgba(255, 111, 0, 1)', teamMember: ['', '', ''] },
        { teamNumber: 4, teamColor: 'rgba(153, 155, 41, 1)', teamMember: ['', '테스트', ''] },
        { teamNumber: 5, teamColor: 'rgba(0, 131, 143, 1)', teamMember: ['', '테스트', ''] },
        { teamNumber: 6, teamColor: 'rgba(105, 53, 170, 1)', teamMember: ['', '', ''] },
        { teamNumber: 7, teamColor: 'rgba(251, 52, 159, 1)', teamMember: ['', '', ''] },
        { teamNumber: 8, teamColor: 'rgba(255, 172, 207, 1)', teamMember: ['', '', ''] },
        { teamNumber: 9, teamColor: 'rgba(188, 157, 157, 1)', teamMember: ['', '', ''] },
        { teamNumber: 10, teamColor: 'rgba(85, 85, 85, 1)', teamMember: ['', '', ''] },
    ])

    // useEffect(() => {
    //     const stompClient = Stomp.over(new WebSocket('ws://localhost:8080/ws'));

    //     stompClient.connect({}, () => {
    //         stompClient.subscribe('/topic/users', (message) => {
    //             const user = JSON.parse(message.body); // 새로 들어온 사용자 정보

    //             // 새로 들어온 사용자를 빈 자리에 배정
    //             const newTeams = teams.map((team) => {
    //                 // 팀에 빈 자리가 있으면 사용자를 추가
    //                 if (team.teamMember.includes('')) {
    //                     team.teamMember[team.teamMember.indexOf('')] = user.name;
    //                 }
    //                 return team;
    //             });

    //             setTeams(newTeams); // 변경된 팀 목록을 상태에 설정
    //         });
    //     });

    //     // STOMP 연결 해제
    //     return () => {
    //         stompClient.disconnect();
    //     };
    // }, [teams]); // teams 상태가 변경될 때마다 재연결

    // const handleTeamDoubleClick = (teamNumber: number) => {
    //     const newTeams = [...teams]
    //     const teamIndex = newTeams.findIndex(team => team.teamNumber === teamNumber)

    //     // 선택한 팀이 비어 있으면 사용자를 추가
    //     if (newTeams[teamIndex].teamMember.some(member => member === '')) {
    //         // 사용자 이름은 현재 세션에 저장된 사용자 이름으로 설정
    //         const currentUser = getCurrentUser(); // 현재 사용자 정보 가져오기
    //         if (currentUser) {
    //             newTeams[teamIndex].teamMember[newTeams[teamIndex].teamMember.indexOf('')] = currentUser.name;
    //             setTeams(newTeams);
    //         } else {
    //             console.error('사용자 정보를 가져올 수 없습니다.')
    //         }
    //     }
    // };

    return (
        <div className={styles.container}>
            {teams.map((team) => (
                <Team key={team.teamNumber} team={team} />
            ))}
        </div>
    )
}