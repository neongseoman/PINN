'use client'

import styles from './ingamemap.module.css'
import themeStyles from '@/app/components/theme.module.css'
// import { Loader } from '@googlemaps/js-api-loader'
import { useEffect, useRef, useState } from 'react'

interface IngameMapProps {
  theme: string
  loader: any
}

export default function IngameMap({ theme, loader }: IngameMapProps) {
  const mapRef = useRef<any>()
  const [marker, setMarker] = useState<any>(null)

  // const loader = new Loader({
  //   apiKey: process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string,
  //   version: 'weekly',
  //   // ...additionalOptions,
  // })

  useEffect(() => {
    loader.importLibrary('maps').then(async () => {
      const position = { lat: 37.5642135, lng: 127.0016985 }
      const { Map } = (await google.maps.importLibrary(
        'maps',
      )) as google.maps.MapsLibrary

      const map = new Map(mapRef.current, {
        center: position,
        disableDefaultUI: true,
        zoom: 8,
      })
      map.addListener('click', (e: any) => {
        if (marker) {
          marker.setMap(null)
        }
        const newMarker = new google.maps.Marker({
          position: e.latLng!,
          map: map,
        })
        setMarker(newMarker)
      })
    })
  }, [])

  return (
    <>
      <div className={styles.map} ref={mapRef} />
      <button className={`${styles.guess}  ${themeStyles[theme + '-inverse']}`}>
        제출
      </button>
    </>
  )
}
