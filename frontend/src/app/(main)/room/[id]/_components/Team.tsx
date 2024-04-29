import styles from './team.module.css'

interface TeamProps {
    team: {
        teamNumber: number
        teamColor: string
        teamMember: string[]
    }
}

export default function Team({ team }: TeamProps) {
    // 투명도 조절
    const transparentColor = (color: string) => {
        return color.replace('1)', '0.5)')
    }
    // team이 비어있는지
    const IsEmptyTeam = team.teamMember.every(member => member === '')

    return (
        <div className={styles.team} style={{ opacity: IsEmptyTeam ? 0.5 : 1 }}>
            <div className={styles.teamName}>Team {team.teamNumber}</div>
            <div className={styles.users}>
                {team.teamMember.map((member, index) => (
                    <div key={index} style={{ backgroundColor: member === '' ? transparentColor(team.teamColor) : team.teamColor }}>{member}</div>
                ))}
            </div>
            <div className={styles.ready}>준비중</div>
        </div>
    )
}
