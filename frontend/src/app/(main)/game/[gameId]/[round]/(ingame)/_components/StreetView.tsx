'use client'

import { Loader } from '@googlemaps/js-api-loader'
import { useEffect, useRef } from 'react'

interface StreetViewProps {
  lat: number
  lng: number
  loader: Loader
}

export default function StreetView({ lat, lng, loader }: StreetViewProps) {
  const streetViewRef = useRef<HTMLDivElement | null>(null)

  useEffect(() => {
    loader.importLibrary('maps').then(() => {
      const { StreetViewPanorama } = google.maps
      const panorama = new StreetViewPanorama(streetViewRef.current!, {
        position: { lat, lng },
        disableDefaultUI: true,
        linksControl: true,
        addressControl: false,
      })
    })
  }, [lat, lng])

  return <div ref={streetViewRef} style={{ width: '100%', height: '100%' }} />
}
