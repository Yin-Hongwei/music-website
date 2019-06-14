import Vue from 'vue'
import Router from 'vue-router'
import login from '@/views/login'
import register from '@/views/register'
import homePage from '@/views/homePage'
import songList from '@/views/songList'
import singer from '@/views/singer'
import mySong from '@/views/mySong'
import songAlbum from '@/views/songAlbum'
import singerAlbum from '@/views/singerAlbum'
import search from '@/views/search'
import personal from '@/views/personal'
import upLoadImg from '@/views/upLoadImg'
import playerPage from '@/views/playerPage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '*',
      redirect: '/404'
    },
    {
      path: '/404',
      component: resolve => require(['../views/404.vue'], resolve)
    },
    {
      path: '/',
      name: 'login',
      component: login
    },
    {
      path: '/register',
      name: 'register',
      component: register
    },
    {
      path: '/homePage',
      name: 'homePage',
      component: homePage
    },
    {
      path: '/songList',
      name: 'song-list',
      component: songList
    },
    {
      path: '/mySong',
      name: 'my-song',
      component: mySong
    },
    {
      path: '/songAlbum/:id',
      name: 'songAlbum',
      component: songAlbum
    },
    {
      path: '/singer',
      name: 'singer',
      component: singer
    },
    {
      path: '/singerAlbum/:id',
      name: 'singerAlbum',
      component: singerAlbum
    },
    {
      path: '/playerPage/:id',
      name: 'playerPage',
      component: playerPage
    },
    {
      path: '/search',
      name: 'search',
      component: search
    },
    {
      path: '/personal',
      name: 'personal',
      component: personal
    },
    {
      path: '/upLoadImg',
      name: 'upLoadImg',
      component: upLoadImg
    }
  ]
})
