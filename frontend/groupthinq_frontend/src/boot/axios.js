import Vue from 'vue'
import axios from 'axios'
import { Notify } from 'quasar'
import auth from '../store/auth'

Vue.prototype.$axios = axios

axios.interceptors.response.use((response) => {
  return response
}, (error) => {
  if (!error.response) {
    Notify.create({
      message: 'Network Error',
      color: 'red'
    })
  } else if (error.response.status) {
    Notify.create({
      message: 'Unauthorized',
      color: 'red'
    })
  }
  return Promise.reject(error)
})

axios.interceptors.request.use((config) => {
  if (auth.isLoggedIn()) {
    config.headers.authorization = 'Bearer ' + auth.getEncodedToken()
  }
  return config
}, (error) => {
  return Promise.reject(error)
})

export default { axios }
