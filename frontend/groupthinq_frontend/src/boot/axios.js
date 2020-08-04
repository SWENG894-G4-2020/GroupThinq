import Vue from 'vue'
import axios from 'axios'
import auth from 'src/store/auth'

Vue.prototype.$axios = axios

axios.interceptors.request.use(req => {
  if (auth.isLoggedIn()) {
    req.headers.authorization = 'Bearer ' + auth.getEncodedToken()
  }
  return req
}, (error) => {
  return Promise.reject(error)
})

export default { axios }
