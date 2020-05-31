import { LocalStorage } from 'quasar'

const auth = {
  storeToken (encodedToken) {
    if (encodedToken) {
      const trimmed = encodedToken.replace('Bearer ', '')
      LocalStorage.set('encodedToken', trimmed)
      const data = JSON.parse(atob(trimmed.split('.')[1]))
      LocalStorage.set('tokenData', data)
    } else {
      throw new Error('No token provided')
    }
  },
  setHeaders (config) {
    config.headers.Authorization = 'Bearer ' + LocalStorage.get('encodedToken')
    return config
  },
  getEncodedToken () {
    return LocalStorage.getItem('encodedToken')
  },
  getTokenData () {
    return LocalStorage.getItem('tokenData')
  },
  isLoggedIn () {
    if (LocalStorage.getItem('encodedToken')) {
      return true
    }
    return false
  }
}

export default auth
