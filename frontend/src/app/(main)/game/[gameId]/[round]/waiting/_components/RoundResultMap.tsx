'use client'

import { Loader } from '@googlemaps/js-api-loader'
import { useEffect, useRef } from 'react'
import styles from '../roundResult.module.css'


interface RoundWaitingMapProps {
    loader: Loader
}

// interface MarkerInfo {
//     position: google.maps.LatLngLiteral
//     title: string
// }

// interface AnswerInfo {
//     position: google.maps.LatLngLiteral
// }

export default function RoundResultMap({ loader }: RoundWaitingMapProps) {
    const mapRef = useRef<any>()
    const mapObjectRef = useRef<google.maps.Map | null>(null)
    // const markersRef = useRef<google.maps.Marker[]>([])

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

            // const markerInfos: MarkerInfo[] = roundResult.map(result => ({
            //     position: { lat: result.submitLat, lng: result.submitLng },
            //     title: `팀 ${result.teamId}`,
            //     // teamNumber: result.teamNumber,
            // }))

            // markerInfos.forEach(markerInfo => {
            //     const marker = new google.maps.Marker({
            //         position: markerInfo.position,
            //         map: mapObjectRef.current,
            //         title: markerInfo.title,
            //         icon: {
            //             path: google.maps.SymbolPath.CIRCLE,
            //             scale: 5,
            //             fillColor: 'red',
            //             fillOpacity: 1,
            //             strokeColor: 'black',
            //             strokeWeight: 2,
            //         },
            //     })
            //     markersRef.current.push(marker)
            // })

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
