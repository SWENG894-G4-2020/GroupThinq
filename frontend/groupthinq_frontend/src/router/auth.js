import { LocalStorage } from 'quasar'

const auth = {
  storeToken (encodedToken) {
    LocalStorage.set('encodedToken', encodedToken)
  },
  setHeaders (config) {
    config.headers.Authorization = 'Bearer ' + LocalStorage.get('encodedToken')
    return config
  },
  getEncodedToken () {
    return LocalStorage.get('encodedToken')
  }
}

export default auth
