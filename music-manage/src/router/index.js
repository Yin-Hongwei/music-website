import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/Home',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            meta: { title: '自述文件' },
            children:[
                {
                    path: '/Song',
                    component: resolve => require(['../components/page/SongPage.vue'], resolve),
                    meta: { title: 'Song' }
                },
                {
                    path: '/Singer',
                    component: resolve => require(['../components/page/SingerPage.vue'], resolve),
                    meta: { title: 'Singer' }
                },
                {
                    path: '/SongList',
                    component: resolve => require(['../components/page/SongListPage.vue'], resolve),
                    meta: { title: 'SongList' }
                },
                {
                    path: '/ListSong',
                    component: resolve => require(['../components/page/ListSongPage.vue'], resolve),
                    meta: { title: 'ListSong' }
                },
                {
                    path: '/Comment',
                    component: resolve => require(['../components/page/CommentPage.vue'], resolve),
                    meta: { title: 'Comment' }
                },
                {
                    path: '/Consumer',
                    component: resolve => require(['../components/page/ConsumerPage.vue'], resolve),
                    meta: { title: 'Consumer' }
                },
                {
                    path: '/Collect',
                    component: resolve => require(['../components/page/CollectPage.vue'], resolve),
                    meta: { title: 'Collect' }
                },
                ]
        },
        {
            path: '/',
            component: resolve => require(['../components/page/Login.vue'], resolve)
        },
    ]
})
