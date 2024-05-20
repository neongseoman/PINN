'use client'

import { Loader } from '@googlemaps/js-api-loader'
import { useEffect, useRef } from 'react'
import styles from '../result1.module.css'

interface FilteredResult {
    roundNumber: number;
    roundRank: number;
    submitLat: number;
    submitLng: number;
    roundScore: number
}

interface MarkerInfo {
    position: google.maps.LatLngLiteral
    title: string
    icon: string;
}

export default function RoundResultMap({ params, loader, filteredResults }: { params: { gameId: string; }; loader: Loader; filteredResults: FilteredResult[]; }) {
    const mapRef = useRef<any>()
    const mapObjectRef = useRef<google.maps.Map | null>(null)
    const markersRef = useRef<google.maps.Marker[]>([])

    useEffect(() => {
        loader.importLibrary('maps').then(async () => {
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

            const image1 = `/assets/images/svg/number1.svg`
            const image2 = `/assets/images/svg/number2.svg`
            const image3 = `/assets/images/svg/number3.svg`
            const image4 = `/assets/images/svg/number4.svg`
            const image5 = `/assets/images/svg/number5.svg`

            const iconMapping: { [key: number]: string } = {
                1: image1,
                2: image2,
                3: image3,
                4: image4,
                5: image5
            };

            const markerInfos: MarkerInfo[] = filteredResults.map(result => ({
                position: { lat: result.submitLat, lng: result.submitLng },
                title: `라운드 ${result.roundNumber}`,
                icon: iconMapping[result.roundNumber] || '',
            }))

            markerInfos.forEach(markerInfo => {
                const marker = new google.maps.Marker({
                    position: markerInfo.position,
                    map: mapObjectRef.current,
                    title: markerInfo.title,
                    icon: {
                        url: markerInfo.icon,
                        scaledSize: new google.maps.Size(25, 25), // 마커 아이콘 크기 조정
                        anchor: new google.maps.Point(15, 15) // 앵커 포인트 조정
                    }
                })
                markersRef.current.push(marker)
            })

            const lineSymbol = {
                path: "M 0,-1 0,1",
                strokeOpacity: 1,
                scale: 3,
            };

            // 라운드 순서대로 선 그리기
            const polylinePath = filteredResults.map(result => ({
                lat: result.submitLat,
                lng: result.submitLng
            }));

            const line = new google.maps.Polyline({
                path: polylinePath,
                map: mapObjectRef.current,
                strokeOpacity: 0,
                icons: [
                    {
                        icon: lineSymbol,
                        offset: "0",
                        repeat: "15px",
                    },
                ],
            });

        })
    }, [filteredResults])



    return (
        <>
            <div className={styles.map} ref={mapRef} />
        </>
    )
}
