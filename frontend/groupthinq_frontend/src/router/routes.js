
const routes = [
  {
    // These paths do not require auth
    path: '/',
    component: () => import('layouts/WelcomeLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Landing.vue') },
      { path: 'signup', component: () => import('pages/Signup.vue') },
      { path: 'login', component: () => import('pages/Login.vue') }
    ]
  },
  {
    // These paths require auth
    path: '/main',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Index.vue') },
      { path: 'account', component: () => import('pages/AccountInfo.vue') },
      { path: 'users', component: () => import('pages/Users.vue') },
      { path: 'decisions', component: () => import('pages/Decisions.vue') },
      { path: 'results', component: () => import('pages/Results.vue') },
      { path: 'help', component: () => import('pages/Help.vue') }
    ],
    meta: {
      requiresAuth: true
    }
  }
]

// Always leave this as last one
if (process.env.MODE !== 'ssr') {
  routes.push({
    path: '*',
    component: () => import('pages/Error404.vue')
  })
}

export default routes
