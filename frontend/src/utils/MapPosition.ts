interface MapCenter {
  [themeName: string]: {
    lat: number
    lng: number
  }
}

export const MapCenter: MapCenter = {
  이집트: { lat: 30.0446, lng: 31.2456 },
  그리스: { lat: 37.9816, lng: 23.7308 },
  한국: { lat: 37.5642135, lng: 127.0016985 },
  랜덤: { lat: 37.5642135, lng: 127.0016985 },
  랜드마크: { lat: 37.5642135, lng: 127.0016985 },
}
