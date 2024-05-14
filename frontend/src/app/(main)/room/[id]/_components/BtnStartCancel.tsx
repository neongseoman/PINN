import styles from './btn.module.css'

// interface TeamGamers {
//     colorId: number
//     gamerId: string
//     teamId: number
//     nickname: string
// }

// interface TeamsProp {
//     teams: {
//         colorCode: string
//         teamNumber: number
//         teamGamer: TeamGamers[]
//         ready: boolean
//     }[]
//     setTeams: React.Dispatch<React.SetStateAction<TeamsProp[]>>;
// }

export default function BtnStartCancel() {

    return (
        <div>
            <button className={styles['start-cancel']}>시작 취소</button>
        </div >
    )
}