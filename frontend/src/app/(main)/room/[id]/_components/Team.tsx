import styles from './team.module.css'
import useUserStore from '@/stores/userStore'

interface TeamProps {
    team: {
        teamNumber: number
        teamColor: string
        teamMember: string[]
        isReady: number
    }
    handleTeamDoubleClick: (teamNumber: number) => void;
}

export default function Team({ team, handleTeamDoubleClick }: TeamProps) {
    const { nickname } = useUserStore()

    // 투명도 조절
    const transparentColor = (color: string) => {
        return color.replace('1)', '0.5)')
    }
    // team이 비어있는지
    const IsEmptyTeam = team.teamMember.every(member => member === '')

    // team이 준비 중인지
    const readyText = team.isReady === 1 ? '준비 완료' : ''

    // 나의 팀
    const myTeam = nickname && team.teamMember.includes(nickname) ? styles.myTeam : styles.team;

    return (
        <div className={IsEmptyTeam ? styles.noMember : myTeam} onDoubleClick={() => handleTeamDoubleClick(team.teamNumber)}>
            <div className={styles.teamName}>Team {team.teamNumber}</div>
            <div className={styles.users}>
                {team.teamMember.map((member, index) => (
                    <div className={styles.nickname}
                        key={index}
                        style={{ backgroundColor: member === '' ? transparentColor(team.teamColor) : team.teamColor }}
                    >
                        {member}
                    </div>
                ))}
            </div>
            {/* 비어있는 팀  */}
            {
                IsEmptyTeam && (
                    <div className={styles.ready}></div>
                )
            }
            {/* 준비완료 팀 */}
            {
                !IsEmptyTeam && team.isReady === 1 && (
                    <div className={styles['ready-complete']}>준비 완료</div>
                )
            }
            {/* 준비중 팀 */}
            {
                !IsEmptyTeam && team.isReady === 0 && (
                    <div className={styles.ready}>준비중</div>
                )
            }
        </div >
    )
}
