import auth from 'src/store/auth'

export default ({ router }) => {
  router.beforeEach((to, from, next) => {
    if (to.matched.some(route => route.meta.requiresAuth)) {
      if (auth.isLoggedIn()) {
        next()
      } else {
        next({ path: '/login' })
      }
    } else {
      next()
    }
  })
}
