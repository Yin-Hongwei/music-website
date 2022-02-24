import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  },
  {
    path: '/404',
    component: () => import('@/views/error/404.vue')
  },
  {
    path: '/',
    name: 'yin-container',
    component: () => import('@/views/YinContainer'),
    children: [
      {
        path: '/',
        name: 'home',
        component: () => import('@/views/Home')
      },
      {
        path: '/sign-in',
        name: 'sign-in',
        component: () => import('@/views/SignIn')
      },
      {
        path: '/sign-up',
        name: 'sign-up',
        component: () => import('@/views/SignUp')
      },
      {
        path: '/personal',
        name: 'personal',
        meta: {
          requireAuth: true
        },
        component: () => import('@/views/personal/Personal')
      },
      {
        path: '/song-sheet',
        name: 'song-sheet',
        component: () => import('@/views/song-sheet/SongSheet')
      },
      {
        path: '/song-sheet-detail/:id',
        name: 'song-sheet-detail',
        component: () => import('@/views/song-sheet/SongSheetDetail')
      },
      {
        path: '/singer',
        name: 'singer',
        component: () => import('@/views/singer/Singer')
      },
      {
        path: '/singer-detail/:id',
        name: 'singer-detail',
        component: () => import('@/views/singer/SingerDetail')
      },
      {
        path: '/lyric/:id',
        name: 'lyric',
        component: () => import('@/views/Lyric')
      },
      {
        path: '/search',
        name: 'search',
        component: () => import('@/views/search/Search')
      },
      {
        path: '/personal-data',
        name: 'personal-data',
        component: () => import('@/views/personal/PersonalData')
      },
      {
        path: '/setting',
        name: 'setting',
        meta: {
          requireAuth: true
        },
        component: () => import('@/views/setting/Setting')
      }
    ]
  }
]

// const router = new Router({
//   mode: 'history',
//   scrollBehavior: () => ({ x: 0, y: 0 }),
//   routes: constantRoutes
// })

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
