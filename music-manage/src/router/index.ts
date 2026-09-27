import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { fetchAdminSession } from "@/api/admin";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/info",
    name: "info",
    component: () => import("@/pages/InfoPage.vue"),
    meta: { title: "Info", requiresAuth: true },
  },
  {
    path: "/song",
    name: "song",
    component: () => import("@/pages/singer/SongPage.vue"),
    meta: { title: "Song", requiresAuth: true },
  },
  {
    path: "/singer",
    name: "singer",
    component: () => import("@/pages/singer/SingerPage.vue"),
    meta: { title: "Singer", requiresAuth: true },
  },
  {
    path: "/songList",
    name: "songList",
    component: () => import("@/pages/song-sheet/SongSheetPage.vue"),
    meta: { title: "SongList", requiresAuth: true },
  },
  {
    path: "/listSong",
    name: "listSong",
    component: () => import("@/pages/song-sheet/ListSongPage.vue"),
    meta: { title: "ListSong", requiresAuth: true },
  },
  {
    path: "/comment",
    name: "comment",
    component: () => import("@/pages/comment/CommentPage.vue"),
    meta: { title: "Comment", requiresAuth: true },
  },
  {
    path: "/consumer",
    name: "consumer",
    component: () => import("@/pages/user/ConsumerPage.vue"),
    meta: { title: "Consumer", requiresAuth: true },
  },
  {
    path: "/collect",
    name: "collect",
    component: () => import("@/pages/collect/CollectPage.vue"),
    meta: { title: "Collect", requiresAuth: true },
  },
  {
    path: "/banner",
    name: "banner",
    component: () => import("@/pages/banner/BannerPage.vue"),
    meta: { title: "Banner", requiresAuth: true },
  },
  {
    path: "/",
    name: "signIn",
    component: () => import("@/pages/Login.vue"),
    meta: { requiresAuth: false },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

let sessionValidated = false;

router.beforeEach(async (to) => {
  if (to.name === "signIn" || to.meta.requiresAuth === false) {
    return true;
  }
  if (!sessionStorage.getItem("adminAuth")) {
    return { name: "signIn" };
  }
  if (sessionValidated) {
    return true;
  }
  try {
    const result = await fetchAdminSession();
    if (result?.success) {
      sessionValidated = true;
      return true;
    }
  } catch {
    /* 401 / network — fall through to login */
  }
  sessionStorage.removeItem("adminAuth");
  sessionValidated = false;
  return { name: "signIn" };
});

export default router;
