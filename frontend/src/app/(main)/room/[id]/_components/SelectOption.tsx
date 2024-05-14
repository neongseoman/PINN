'use client'

import styles from './selectOption.module.css'

import { useState } from "react"

export default function Option() {
    const [selectedTheme, setSelectedTheme] = useState<string>('')
    const [selectedRound, setSelectedRound] = useState<string>('')
    const [selectedStage1, setSelectedStage1] = useState<string>('')
    const [selectedStage2, setSelectedStage2] = useState<string>('')

    // useEffect(() => {
    //     // API 
    //     const themeOptions = []
    // }, [])

    const themeOptions = ['예시1', '예시2', '예시3']
    const roundOptions = ['1', '2', '3', '4', '5']
    const stage1Options = ['20', '30', '40', '50', '60', '70', '80', '90', '100', '110', '120']
    const stage2Options = ['20', '30', '40', '50', '60']

    const handleThemeChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedTheme(event.target.value);
    };

    const handleRoundChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedRound(event.target.value);
    };

    const handleStage1Change = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedStage1(event.target.value);
    };

    const handleStage2Change = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedStage2(event.target.value);
    };

    return (
        <div className={styles.container}>
            <div className={styles.theme}>
                {/* 테마 선택 select box */}
                <label htmlFor="theme">테마</label>
                <select id="theme" value={selectedTheme} onChange={undefined}>
                    {themeOptions.map((option, index) => (
                        <option key={index} value={option}>{option}</option>
                    ))}
                </select>
            </div>
            <div>
                {/* 라운드 선택 select box */}
                <label htmlFor="round">라운드</label>
                <select id="round" value={selectedRound} onChange={handleRoundChange}>
                    {roundOptions.map((option, index) => (
                        <option key={index} value={option}>{option}</option>
                    ))}
                </select>
            </div>
            {/* 스테이지 1 선택 select box */}
            <div>
                <label htmlFor="stage1">스테이지1</label>
                <select id="stage1" value={selectedStage1} onChange={handleStage1Change} disabled>
                    {stage1Options.map((option, index) => (
                        <option key={index} value={option}>{option}초</option>
                    ))}
                </select>
            </div>
            {/* 스테이지 2 선택 select box */}
            <div>
                <label htmlFor="stage2">스테이지2</label>
                <select value={selectedStage2} onChange={handleStage2Change}>
                    {stage2Options.map((option, index) => (
                        <option key={index} value={option}>{option}초</option>
                    ))}
                </select>
            </div>
        </div>
    );
}
