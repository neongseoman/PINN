// KakaoScript.tsx

'use client';

import Script from 'next/script';

function KakaoScript() {
  const onLoad = () => {
    // console.log(window.Kakao.isInitialized())
    console.log(process.env.NEXT_PUBLIC_JAVASCRIPT_KEY)
    window.Kakao.init(process.env.NEXT_PUBLIC_JAVASCRIPT_KEY);
  };

  return (
    <Script
      src="https://developers.kakao.com/sdk/js/kakao.js"
      async
      onLoad={onLoad}
    />
  );
}

export default KakaoScript;