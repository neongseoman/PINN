'use client'

import styles from './teamList.module.css'
import Team from './Team'
import { useEffect, useRef, useState } from 'react'
import { Stomp, Client, IFrame, IMessage } from '@stomp/stompjs'

interface TeamsProp {
    teams: {
        teamNumber: number
        teamColor: string
        teamMember: string[]
        isReady: number
    }[]
}

export default function TeamList({ teams }: TeamsProp) {

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