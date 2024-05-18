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
        if (filteredResults[0].submitLat === 1000) {
            loader.importLibrary('maps').then(async () => {
                console.log(roundQuestion.lat)
                console.log(roundQuestion)
                const position = { lat: 30, lng: 150 }
                const { Map } = (await google.maps.importLibrary(
                    'maps',
                )) as google.maps.MapsLibrary
                const allowedBounds = {
                    north: 85,   // 북쪽 경계
                    south: -85,  // 남쪽 경계
                    west: -179.9,  // 서쪽 경계
                    east: 179.9    // 동쪽 경계
                }

                const map = new Map(mapRef.current, {
                    center: position,
                    disableDefaultUI: true,
                    clickableIcons: false,
                    zoom: 2,
                    restriction: {  // 지도 범위 제한 설정
                        latLngBounds: allowedBounds,
                        strictBounds: true,  // 지도 범위를 엄격하게 제한할지 여부
                    },
                })

                mapObjectRef.current = map

                // 정답 마커 추가
                const image = `/assets/images/svg/treasure.svg`

                const answerMarker = new google.maps.Marker({
                    position: { lat: roundQuestion.lat, lng: roundQuestion.lng },
                    map: mapObjectRef.current,
                    title: '정답',
                    icon: {
                        url: image,
                        scaledSize: new google.maps.Size(30, 30), // 마커 아이콘 크기 조정
                        anchor: new google.maps.Point(15, 15) // 앵커 포인트 조정
                    }
                })

                const markerInfos: MarkerInfo[] = roundResult
                    .filter(result => result.submitLat !== 1000 && result.submitLng !== 1000)
                    .map(result => ({
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
        }
        else {
            loader.importLibrary('maps').then(async () => {
                const position = { lat: filteredResults[0].submitLat, lng: filteredResults[0].submitLng }
                const { Map } = (await google.maps.importLibrary(
                    'maps',
                )) as google.maps.MapsLibrary

                const allowedBounds = {
                    north: 85,   // 북쪽 경계
                    south: -85,  // 남쪽 경계
                    west: -179.9,  // 서쪽 경계
                    east: 179.9    // 동쪽 경계
                }

                const map = new Map(mapRef.current, {
                    center: position,
                    disableDefaultUI: true,
                    clickableIcons: false,
                    zoom: 8,
                    restriction: {  // 지도 범위 제한 설정
                        latLngBounds: allowedBounds,
                        strictBounds: true,  // 지도 범위를 엄격하게 제한할지 여부
                    },
                })

                mapObjectRef.current = map

                // 정답 마커 추가
                const image = `/assets/images/svg/treasure.svg`

                const answerMarker = new google.maps.Marker({
                    position: { lat: roundQuestion.lat, lng: roundQuestion.lng },
                    map: mapObjectRef.current,
                    title: '정답',
                    icon: {
                        url: image,
                        scaledSize: new google.maps.Size(30, 30), // 마커 아이콘 크기 조정
                        anchor: new google.maps.Point(15, 15) // 앵커 포인트 조정
                    }
                })

                const markerInfos: MarkerInfo[] = roundResult
                    .filter(result => result.submitLat !== 1000 && result.submitLng !== 1000)
                    .map(result => ({
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
        }
    }, [roundQuestion, roundResult])

    return (
        <>
            <div className={styles.map} ref={mapRef} />
        </>
    )
}
