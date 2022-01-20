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
        path: '/login-in',
        name: 'login-in',
        component: () => import('@/pages/LoginIn')
      },
      {
        path: '/sign-up',
        name: 'sign-up',
        component: () => import('@/pages/SignUp')
      },
      {
        path: '/my-music',
        name: 'my-music',
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
        component: () => import('@/pages/Search')
      },
      {
        path: '/setting',
        name: 'setting',
        component: () => import('@/pages/Setting')
      }
    ]
  }
]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: (o, from, savedPosition) => ({ x: 0, y: 0 }),
  routes: constantRoutes
})
