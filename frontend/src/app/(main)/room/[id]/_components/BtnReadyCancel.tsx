import styles from './btn.module.css'

interface Team {
    teamNumber: number
    teamColor: string
    teamMember: string[]
    isReady: number
}


interface TeamsProp {
    teams: Team[];
    setTeams: React.Dispatch<React.SetStateAction<Team[]>>;
}

export default function BtnReadyCancel({ teams, setTeams }: TeamsProp) {
    const handleReadyClick = () => {
        setTeams(prevTeams => {
            return prevTeams.map(team => {
                if (team.teamNumber === 1) {
                    return { ...team, isReady: 0 }
                }
                return team;
            });
        });
        // ready 상태 변하도록 websocket에서 컨트롤
    }


    return (
        <div>
            <button className={styles['ready-cancel']}>준비 완료</button>
        </div>
    )
}