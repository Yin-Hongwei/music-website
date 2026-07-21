import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/info",
    name: "info",
    component: () => import("@/pages/InfoPage.vue"),
    meta: { title: "Info" },
  },
  {
    path: "/song",
    name: "song",
    component: () => import("@/pages/singer/SongPage.vue"),
    meta: { title: "Song" },
  },
  {
    path: "/singer",
    name: "singer",
    component: () => import("@/pages/singer/SingerPage.vue"),
    meta: { title: "Singer" },
  },
  {
    path: "/songList",
    name: "songList",
    component: () => import("@/pages/song-sheet/SongSheetPage.vue"),
    meta: { title: "SongList" },
  },
  {
    path: "/listSong",
    name: "listSong",
    component: () => import("@/pages/song-sheet/ListSongPage.vue"),
    meta: { title: "ListSong" },
  },
  {
    path: "/comment",
    name: "comment",
    component: () => import("@/pages/comment/CommentPage.vue"),
    meta: { title: "Comment" },
  },
  {
    path: "/consumer",
    name: "consumer",
    component: () => import("@/pages/user/ConsumerPage.vue"),
    meta: { title: "Consumer" },
  },
  {
    path: "/collect",
    name: "collect",
    component: () => import("@/pages/collect/CollectPage.vue"),
    meta: { title: "Collect" },
  },
  {
    path: "/banner",
    name: "banner",
    component: () => import("@/pages/banner/BannerPage.vue"),
    meta: { title: "Banner" },
  },
  {
    path: "/",
    name: "signIn",
    component: () => import("@/pages/Login.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
