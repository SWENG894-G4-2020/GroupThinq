import auth from '../store/auth'

export default ({ app, router, store, Vue }) => {
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
