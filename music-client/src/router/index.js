import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const constantRoutes = [
  {
    path: '*',
    redirect: '/404'
  },
  {
    path: '/404',
    component: () => import('@/pages/error/404.vue')
  },
  {
    path: '/',
    name: 'yin-container',
    component: () => import('@/pages/YinContainer'),
    children: [
      {
        path: '/',
        name: 'home',
        component: () => import('@/pages/Home')
      },
      {
        path: '/sign-in',
        name: 'sign-in',
        component: () => import('@/pages/SignIn')
      },
      {
        path: '/sign-up',
        name: 'sign-up',
        component: () => import('@/pages/SignUp')
      },
      {
        path: '/my-music',
        name: 'my-music',
        meta: {
          requireAuth: true
        },
        component: () => import('@/pages/MyMusic')
      },
      {
        path: '/song-sheet',
        name: 'song-sheet',
        component: () => import('@/pages/song-sheet/SongSheet')
      },
      {
        path: '/song-sheet-detail/:id',
        name: 'song-sheet-detail',
        component: () => import('@/pages/song-sheet/SongSheetDetail')
      },
      {
        path: '/singer',
        name: 'singer',
        component: () => import('@/pages/singer/Singer')
      },
      {
        path: '/singer-detail/:id',
        name: 'singer-detail',
        component: () => import('@/pages/singer/SingerDetail')
      },
      {
        path: '/lyric/:id',
        name: 'lyric',
        component: () => import('@/pages/Lyric')
      },
      {
        path: '/search',
        name: 'search',
        component: () => import('@/pages/search/Search')
      },
      {
        path: '/setting',
        name: 'setting',
        meta: {
          requireAuth: true
        },
        component: () => import('@/pages/setting/Setting')
      }
    ]
  }
]

const router = new Router({
  mode: 'history',
  scrollBehavior: () => ({ x: 0, y: 0 }),
  routes: constantRoutes
})

// TODO: 路由拦截

export default router
