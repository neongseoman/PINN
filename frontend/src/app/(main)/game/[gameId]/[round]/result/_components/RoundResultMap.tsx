'use client'

import { Loader } from '@googlemaps/js-api-loader'
import { useEffect, useRef, useState } from 'react'
import styles from '../roundResult.module.css'
import { getRoundInfo } from '@/utils/IngameApi'
import { RoundInit } from '@/types/IngameRestTypes'

interface RoundResult {
    teamId: number
    roundNumber: number
    roundRank: number
    totalRank: number
    roundScore: number
    totalScore: number
    submitLat: number
    submitLng: number
    colorCode: string
}

interface RoundResultMapProps {
    loader: Loader
    roundResult: RoundResult[]
}

interface MarkerInfo {
    position: google.maps.LatLngLiteral
    title: string
}

interface AnswerInfo {
    position: google.maps.LatLngLiteral
}

export default function RoundResultMap({ params, loader, roundResult }: { params: { gameId: string; round: string }; loader: Loader; roundResult: RoundResult[] }) {
    const mapRef = useRef<any>()
    const mapObjectRef = useRef<google.maps.Map | null>(null)
    const markersRef = useRef<google.maps.Marker[]>([])
    const [answerLat, setAnswerLat] = useState<number>(1)
    const [answerLng, setAnswerLng] = useState<number>(1)

    // 라운드 시작 정보 받아오기
    async function roundStartRender() {
        const roundInfo = (await getRoundInfo(
            params.gameId,
            params.round,
        )) as RoundInit
        if (!roundInfo.success) {
            alert(roundInfo.message)
            return
        }
        setAnswerLat(roundInfo.result.lat)
        setAnswerLng(roundInfo.result.lng)
    }

    useEffect(() => {
        roundStartRender()
        loader.importLibrary('maps').then(async () => {
            const position = { lat: answerLat, lng: answerLng }
            const { Map } = (await google.maps.importLibrary(
                'maps',
            )) as google.maps.MapsLibrary

            const map = new Map(mapRef.current, {
                center: position,
                disableDefaultUI: true,
                clickableIcons: false,
                zoom: 2,
            })

            mapObjectRef.current = map

            // 정답 마커 추가
            const answerMarker = new google.maps.Marker({
                position: position,
                map: mapObjectRef.current,
                title: '정답',
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: 6,
                    fillColor: 'black',
                    fillOpacity: 1,
                    strokeColor: 'white',
                    strokeWeight: 2,
                },
            })

            const markerInfos: MarkerInfo[] = roundResult.map(result => ({
                position: { lat: result.submitLat, lng: result.submitLng },
                title: `팀 ${result.teamId}`,
                // color: colorCode
                // teamNumber: result.teamNumber,
            }))

            markerInfos.forEach(markerInfo => {
                const marker = new google.maps.Marker({
                    position: markerInfo.position,
                    map: mapObjectRef.current,
                    title: markerInfo.title,
                    icon: {
                        path: google.maps.SymbolPath.CIRCLE,
                        scale: 5,
                        fillColor: 'red',
                        fillOpacity: 1,
                        strokeColor: 'black',
                        strokeWeight: 2,
                    },
                })
                markersRef.current.push(marker)
            })




            const currentZoom = mapObjectRef.current.getZoom();
            if (currentZoom !== undefined && currentZoom > 2) {
                mapObjectRef.current.setZoom(currentZoom - 1);
            }

        })
    }, [])

    return (
        <>
            <div className={styles.map} ref={mapRef} />
        </>
    )
}
