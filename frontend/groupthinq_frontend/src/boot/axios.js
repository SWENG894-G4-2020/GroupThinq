import Vue from 'vue'
import axios from 'axios'
import auth from 'src/store/auth'

Vue.prototype.$axios = axios

axios.interceptors.request.use((config) => {
  if (auth.isLoggedIn()) {
    config.headers.authorization = 'Bearer ' + auth.getEncodedToken()
  }
  return config
}, (error) => {
  return Promise.reject(error)
})

export default { axios }
