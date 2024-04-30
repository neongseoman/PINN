import styles from './team.module.css'

interface TeamProps {
    team: {
        teamNumber: number
        teamColor: string
        teamMember: string[]
        isReady: number
    }
}

export default function Team({ team }: TeamProps) {
    // 투명도 조절
    const transparentColor = (color: string) => {
        return color.replace('1)', '0.5)')
    }
    // team이 비어있는지
    const IsEmptyTeam = team.teamMember.every(member => member === '')

    // team이 준비 중인지
    const readyText = team.isReady === 1 ? '준비 완료' : ''

    return (
        <div className={styles.team} style={{ opacity: IsEmptyTeam ? 0.5 : 1 }}>
            <div className={styles.teamName}>Team {team.teamNumber}</div>
            <div className={styles.users}>
                {team.teamMember.map((member, index) => (
                    <div key={index} style={{ backgroundColor: member === '' ? transparentColor(team.teamColor) : team.teamColor }}>{member}</div>
                ))}
            </div>
            {/* 비어있는 팀  */}
            {IsEmptyTeam && (
                <div className={styles.ready}></div>
            )}
            {/* 준비완료 팀 */}
            {!IsEmptyTeam && team.isReady === 1 && (
                <div className={styles['ready-complete']}>준비 완료</div>
            )}
            {/* 준비중 팀 */}
            {!IsEmptyTeam && team.isReady === 0 && (
                <div className={styles.ready}>준비중</div>
            )}
            {IsEmptyTeam}
        </div>
    )
}
