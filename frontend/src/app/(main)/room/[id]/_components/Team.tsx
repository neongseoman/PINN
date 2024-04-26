import styles from './team.module.css'

interface TeamProps {
    team: {
        teamNumber: number
        teamMember: string[]
    }
}

export default function Team({ team }: TeamProps) {
    return (
        <div className={styles.team}>
            <div className={styles.teamName}>Team {team.teamNumber}</div>
            <div className={styles.users}>
                {team.teamMember.map((member, index) => (
                    <div key={index}>{member}</div>
                ))}
            </div>

            <div className={styles.ready}>준비중</div>
        </div>
    )
}
