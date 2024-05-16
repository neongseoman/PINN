import { toast, ToastOptions } from 'react-toastify'

const defaultOptions: ToastOptions = {
  position: 'top-center',
  autoClose: 3000,
  hideProgressBar: false,
  closeOnClick: false,
  pauseOnHover: true,
  draggable: true,
  theme: 'light',
}

const useCustomAlert = () => {
  const info = (message: string) => {
    toast.success(message, defaultOptions)
  }

  const success = (message: string) => {
    toast.success(message, defaultOptions)
  }

  const warning = (message: string) => {
    toast.success(message, defaultOptions)
  }

  const error = (message: string) => {
    toast.error(message, defaultOptions)
  }

  return { info, success, warning, error }
}

export default useCustomAlert
