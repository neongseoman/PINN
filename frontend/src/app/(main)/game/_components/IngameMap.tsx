'use client'

import styles from './ingamemap.module.css'
import themeStyles from '@/app/components/theme.module.css'
import { Loader } from '@googlemaps/js-api-loader'
import { useEffect, useRef, useState } from 'react'

interface IngameMapProps {
  theme: string
  loader: Loader
}

interface MyGuess {
  lat: number
  lng: number
}

export default function IngameMap({ theme, loader }: IngameMapProps) {
  const mapRef = useRef<any>()
  const currentMarker = useRef<google.maps.Marker | null>(null)
  const myGuess = useRef<MyGuess | null>(null)

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
        clickableIcons: false,
        zoom: 8,
      })
      map.addListener('click', (e: google.maps.MapMouseEvent) => {
        if (currentMarker.current) {
          currentMarker.current.setMap(null)
        }
        const newMarker = new google.maps.Marker({
          position: e.latLng!,
          map: map,
          icon: {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 10,
            fillColor: 'red',
            fillOpacity: 1,
            strokeColor: 'black',
            strokeWeight: 3,
          },
        })
        currentMarker.current = newMarker
        myGuess.current = {
          lat: e.latLng?.lat() as number,
          lng: e.latLng?.lng() as number,
        }
        console.log(myGuess.current)
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
