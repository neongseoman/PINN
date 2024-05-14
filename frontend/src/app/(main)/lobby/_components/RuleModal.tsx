'use client'

import { useRef } from 'react'
import { ImExit } from 'react-icons/im'
import { PiSealWarningFill } from 'react-icons/pi'
import styles from '../lobby.module.css'

interface RuleModalProps {
  hoverSound: () => void
  clickSound: () => void
}

export default function RuleModal({ hoverSound, clickSound }: RuleModalProps) {
  const dialogRef = useRef<HTMLDialogElement>(null)

  const showModal = () => {
    clickSound()
    dialogRef.current?.showModal()
  }

  const closeModal = () => {
    clickSound()
    dialogRef.current?.close()
  }

  return (
    <div>
      <p
        className={styles.buttons}
        onClick={showModal}
        onMouseEnter={hoverSound}
      >
        {' '}
        게임 설명
      </p>

      <dialog className={styles.ruleWrapper} ref={dialogRef}>
        <div className={styles.ruleContents}>
          <span className={styles.exitIcon} onClick={closeModal}>
            <ImExit />
          </span>
          <p className={styles.ruleTitle}>참여 방법</p>
          <div className={styles.ruleBox}>
            <p className={styles.ruleName}>1. 게임 생성</p>
            <p className={styles.rule}>
              : 내가 원하는 옵션으로 방을 만들어서 방장으로 플레이
            </p>
          </div>
          <div className={styles.ruleBox}>
            <p className={styles.ruleName}>2. 빠른 시작</p>
            <p className={styles.rule}>
              : 인원이 가장 많은 방에 랜덤으로 입장하여 빠르게 플레이
            </p>
          </div>
          <div className={styles.ruleBox}>
            <p className={styles.ruleName}>3. 방 목록</p>
            <p className={styles.rule}>
              : 대기중인 방 목록에서 각 옵션을 확인 후 참여하고 싶은 방에
              입장하여 플레이
            </p>
          </div>
        </div>
        <div className={styles.ruleContents}>
          <p className={styles.ruleTitle}>게임 규칙</p>
          <div className={styles.ruleBox}>
            <p className={styles.ruleName}>최대 라운드 수 - 3</p>
            <p className={styles.rule}>
              : 보물을 찾기 위해 최대 3개의 스팟을 거치게 된다
            </p>
          </div>
          <div className={styles.ruleBox}>
            <p className={styles.ruleName}>스테이지 수 - 2</p>
            <p className={styles.rule}>
              : 스트리트 뷰와 두 번의 힌트를 통해 유추한 위치에 핀으로 지도에
              표시한다
            </p>
          </div>
          <div className={styles.ruleBoxRow}>
            <span className={styles.warningIcon}>
              <PiSealWarningFill />
            </span>
            <div className={styles.ruleTextBox}>
              <p className={styles.ruleText}>
                타이머가 끝날 때까지 핀 제출 버튼을 누르지 않으면
              </p>
              <p className={styles.ruleText}>
                가장 마지막에 핀이 놓였던 위치로 자동 제출 된다
              </p>
            </div>
          </div>
        </div>
        <div className={styles.ruleContents}>
          <p className={styles.ruleTitle}>승리 조건</p>
          <div className={styles.ruleBoxCenter}>
            <p className={styles.rule}>ㅡ</p>
            <p className={styles.ruleText}>
              스테이지 1에서 핀을 확정하면 최대 5000점 획득 가능하고
            </p>
            <p className={styles.ruleText}>
              스테이지 2에서 핀을 확정하면 최대 3500점 획득 가능하다
            </p>
            <p className={styles.rule}>ㅡ</p>
            <p className={styles.ruleText}>
              핀 위치가 해당 스팟과 가까울수록 높은 점수를 획득한다
            </p>
            <p className={styles.rule}>ㅡ</p>
            <p className={styles.ruleText}>
              모든 스팟을 지난 이후, 그동안 얻은 점수의 합으로 최종 순위가
              결정된다
            </p>
          </div>
        </div>
      </dialog>
    </div>
  )
}
