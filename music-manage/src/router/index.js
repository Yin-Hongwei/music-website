import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/Home',
      component: resolve => require(['../components/Home.vue'], resolve),
      meta: { title: '自述文件' },
      children: [
        {
          path: '/Info',
          component: resolve => require(['../pages/InfoPage.vue'], resolve),
          meta: { title: 'Info' }
        },
        {
          path: '/song',
          component: resolve => require(['../pages/SongPage.vue'], resolve),
          meta: { title: 'Song' }
        },
        {
          path: '/Singer',
          component: resolve => require(['../pages/SingerPage.vue'], resolve),
          meta: { title: 'Singer' }
        },
        {
          path: '/SongList',
          component: resolve => require(['../pages/SongListPage.vue'], resolve),
          meta: { title: 'SongList' }
        },
        {
          path: '/ListSong',
          component: resolve => require(['../pages/ListSongPage.vue'], resolve),
          meta: { title: 'ListSong' }
        },
        {
          path: '/Comment',
          component: resolve => require(['../pages/CommentPage.vue'], resolve),
          meta: { title: 'Comment' }
        },
        {
          path: '/Consumer',
          component: resolve => require(['../pages/ConsumerPage.vue'], resolve),
          meta: { title: 'Consumer' }
        },
        {
          path: '/Collect',
          component: resolve => require(['../pages/CollectPage.vue'], resolve),
          meta: { title: 'Collect' }
        }
      ]
    },
    {
      path: '/',
      component: resolve => require(['../pages/Login.vue'], resolve)
    }
  ]
})
