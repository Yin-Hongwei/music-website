import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/info",
    component: () => import("@/views/InfoPage.vue"),
    meta: { title: "Info" },
  },
  {
    path: "/song",
    component: () => import("@/views/singer/SongPage.vue"),
    meta: { title: "Song" },
  },
  {
    path: "/singer",
    component: () => import("@/views/singer/SingerPage.vue"),
    meta: { title: "Singer" },
  },
  {
    path: "/songList",
    component: () => import("@/views/songSheet/SongSheetPage.vue"),
    meta: { title: "SongList" },
  },
  {
    path: "/listSong",
    component: () => import("@/views/ListSongPage.vue"),
    meta: { title: "ListSong" },
  },
  {
    path: "/comment",
    component: () => import("@/views/CommentPage.vue"),
    meta: { title: "Comment" },
  },
  {
    path: "/consumer",
    component: () => import("@/views/user/ConsumerPage.vue"),
    meta: { title: "Consumer" },
  },
  {
    path: "/collect",
    component: () => import("@/views/CollectPage.vue"),
    meta: { title: "Collect" },
  },
  {
    path: "/banner",
    component: () => import("@/views/BannerPage.vue"),
    meta: { title: "Banner" },
  },
  {
    path: "/",
    component: () => import("@/views/Login.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
