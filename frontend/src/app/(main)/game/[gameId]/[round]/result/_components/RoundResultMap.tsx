'use client'

import { Loader } from '@googlemaps/js-api-loader'
import { useEffect, useRef, useState } from 'react'
import styles from '../roundResult.module.css'
import useIngameStore from '@/stores/ingameStore'

interface RoundQuestion {
    lat: number
    lng: number
}

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
    path: { lat: number; lng: number }[]
    icon: {
        path: google.maps.SymbolPath;
        scale: number;
        fillColor: string;
        fillOpacity: number;
        strokeColor: string;
        strokeWeight: number;
    };
}

export default function RoundResultMap({ params, loader, roundResult, roundQuestion }: { params: { gameId: string; round: string }; loader: Loader; roundResult: RoundResult[]; roundQuestion: RoundQuestion }) {
    const mapRef = useRef<any>()
    const mapObjectRef = useRef<google.maps.Map | null>(null)
    const markersRef = useRef<google.maps.Marker[]>([])
    const { teamId } = useIngameStore()
    const filteredResults: RoundResult[] = roundResult.filter(result => result.teamId === teamId);

    useEffect(() => {
        if (filteredResults.length === 0) {
            return; // 필터링된 결과가 없는 경우 처리
        }
        loader.importLibrary('maps').then(async () => {
            const position = { lat: filteredResults[0].submitLat, lng: filteredResults[0].submitLng }
            const { Map } = (await google.maps.importLibrary(
                'maps',
            )) as google.maps.MapsLibrary

            const map = new Map(mapRef.current, {
                center: position,
                disableDefaultUI: true,
                clickableIcons: false,
                zoom: 8,
            })

            mapObjectRef.current = map

            // 정답 마커 추가
            const image = `/assets/images/svg/treasure.svg`

            const answerMarker = new google.maps.Marker({
                position: { lat: roundQuestion.lat, lng: roundQuestion.lng },
                map: mapObjectRef.current,
                title: '정답',
                icon: image
            })
            const markerInfos: MarkerInfo[] = roundResult.map(result => ({
                position: { lat: result.submitLat, lng: result.submitLng },
                title: result.teamId.toString(),
                path: [
                    { lat: roundQuestion.lat, lng: roundQuestion.lng },
                    { lat: result.submitLat, lng: result.submitLng }
                ],
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: result.teamId === teamId ? 8 : 6,
                    fillColor: result.colorCode,
                    fillOpacity: 1,
                    strokeColor: result.teamId === teamId ? 'yellow' : 'black',
                    strokeWeight: 2,
                },
            }))

            markerInfos.forEach(markerInfo => {
                const marker = new google.maps.Marker({
                    position: markerInfo.position,
                    map: mapObjectRef.current,
                    title: markerInfo.title,
                    icon: markerInfo.icon
                })
                markersRef.current.push(marker)
            })

            const lineSymbol = {
                path: "M 0,-1 0,1",
                strokeOpacity: 1,
                scale: 3,
            };
            markerInfos.forEach(markerInfo => {
                const Line = new google.maps.Polyline({
                    path: markerInfo.path,
                    map: mapObjectRef.current,
                    strokeOpacity: 0,
                    icons: [
                        {
                            icon: lineSymbol,
                            offset: "0",
                            repeat: "15px",
                        },
                    ],
                })
            })


        })
    }, [roundQuestion, roundResult])

    return (
        <>
            <div className={styles.map} ref={mapRef} />
        </>
    )
}
