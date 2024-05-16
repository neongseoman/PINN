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

            const map = new Map(mapRef.current, {
                center: position,
                disableDefaultUI: true,
                clickableIcons: false,
                zoom: 2,
            })

            mapObjectRef.current = map

            const markerInfos: MarkerInfo[] = filteredResults.map(result => ({
                position: { lat: result.submitLat, lng: result.submitLng },
                title: `라운드 ${result.roundNumber}`,
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
        })
    }, [filteredResults])

    return (
        <>
            <div className={styles.map} ref={mapRef} />
        </>
    )
}
