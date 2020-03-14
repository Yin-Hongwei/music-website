import Vue from 'vue'
import Router from 'vue-router'
import LoginPage from '@/pages/LoginPage'
import RegisterPage from '@/pages/RegisterPage'
import HomePage from '@/pages/HomePage'
import SongListPage from '@/pages/SongListPage'
import SingerPage from '@/pages/SingerPage'
import MySongPage from '@/pages/MySongPage'
import SongListAlbumPage from '@/pages/SongListAlbumPage'
import SingerAlbumPage from '@/pages/SingerAlbumPage'
import SearchPage from '@/pages/SearchPage'
import PersonalInfoPage from '@/pages/PersonalInfoPage'
import UpLoadPage from '@/pages/UpLoadPage'
import PlayerPage from '@/pages/PlayerPage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '*',
      redirect: '/404'
    },
    {
      path: '/404',
      component: resolve => require(['../pages/404.vue'], resolve)
    },
    {
      path: '/',
      name: 'login-page',
      component: LoginPage
    },
    {
      path: '/register-page',
      name: 'register-page',
      component: RegisterPage
    },
    {
      path: '/home-page',
      name: 'home-page',
      component: HomePage
    },
    {
      path: '/song-list-page',
      name: 'song-list-page',
      component: SongListPage
    },
    {
      path: '/my-song-page',
      name: 'my-song-page',
      component: MySongPage
    },
    {
      path: '/song-list-album-page/:id',
      name: 'song-list-album-page',
      component: SongListAlbumPage
    },
    {
      path: '/singer-page',
      name: 'singer-page',
      component: SingerPage
    },
    {
      path: '/singer-album-page/:id',
      name: 'singer-album-page',
      component: SingerAlbumPage
    },
    {
      path: '/player-page/:id',
      name: 'player-page',
      component: PlayerPage
    },
    {
      path: '/search-page',
      name: 'search-page',
      component: SearchPage
    },
    {
      path: '/personal-info-page',
      name: 'personal-info-page',
      component: PersonalInfoPage
    },
    {
      path: '/upLoad-page',
      name: 'upLoad-page',
      component: UpLoadPage
    }
  ]
})
