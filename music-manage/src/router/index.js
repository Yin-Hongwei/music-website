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
          component: resolve => require(['../views/InfoPage.vue'], resolve),
          meta: { title: 'Info' }
        },
        {
          path: '/Song',
          component: resolve => require(['../views/SongPage.vue'], resolve),
          meta: { title: 'Song' }
        },
        {
          path: '/Singer',
          component: resolve => require(['../views/SingerPage.vue'], resolve),
          meta: { title: 'Singer' }
        },
        {
          path: '/SongList',
          component: resolve => require(['../views/SongListPage.vue'], resolve),
          meta: { title: 'SongList' }
        },
        {
          path: '/ListSong',
          component: resolve => require(['../views/ListSongPage.vue'], resolve),
          meta: { title: 'ListSong' }
        },
        {
          path: '/Comment',
          component: resolve => require(['../views/CommentPage.vue'], resolve),
          meta: { title: 'Comment' }
        },
        {
          path: '/Consumer',
          component: resolve => require(['../views/ConsumerPage.vue'], resolve),
          meta: { title: 'Consumer' }
        },
        {
          path: '/Collect',
          component: resolve => require(['../views/CollectPage.vue'], resolve),
          meta: { title: 'Collect' }
        }
      ]
    },
    {
      path: '/',
      component: resolve => require(['../views/Login.vue'], resolve)
    }
  ]
})
