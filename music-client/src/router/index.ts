import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
const routes: Array<RouteRecordRaw> = [
  {
    path: "/:pathMatch(.*)*",
    redirect: "/404",
  },
  {
    path: "/404",
    component: () => import("@/pages/error/404.vue"),
  },
  {
    path: "/",
    name: "yin-app-layout",
    component: () => import("@/components/layouts/YinAppLayout.vue"),
    children: [
      {
        path: "/",
        name: "home",
        component: () => import("@/pages/home/Home.vue"),
      },
      {
        path: "/sign-in",
        name: "sign-in",
        component: () => import("@/pages/auth/SignIn.vue"),
      },
      {
        path: "/sign-up",
        name: "sign-up",
        component: () => import("@/pages/auth/SignUp.vue"),
      },
      {
        path: "/forgot-password",
        name: "forgot-password",
        component: () => import("@/pages/auth/ForgotPassword.vue"),
      },
      {
        path: "/personal",
        name: "personal",
        meta: {
          requireAuth: true,
        },
        component: () => import("@/pages/user/Personal.vue"),
      },
      {
        path: "/song-sheet",
        name: "song-sheet",
        component: () => import("@/pages/song-sheet/SongSheet.vue"),
      },
      {
        path: "/song-sheet-detail/:id",
        name: "song-sheet-detail",
        component: () => import("@/pages/song-sheet/SongSheetDetail.vue"),
      },
      {
        path: "/singer",
        name: "singer",
        component: () => import("@/pages/singer/Singer.vue"),
      },
      {
        path: "/singer-detail/:id",
        name: "singer-detail",
        component: () => import("@/pages/singer/SingerDetail.vue"),
      },
      {
        path: "/lyric/:id",
        name: "lyric",
        component: () => import("@/pages/lyric/Lyric.vue"),
      },
      {
        path: "/search",
        name: "search",
        component: () => import("@/pages/search/Search.vue"),
      },
      {
        path: "/personal-data",
        name: "personal-data",
        component: () => import("@/pages/user/PersonalData.vue"),
      },
      {
        path: "/setting",
        name: "setting",
        meta: {
          requireAuth: true,
        },
        component: () => import("@/pages/user/Setting.vue"),
        children: [
          {
            path: "/setting/PersonalData",
            name: "personalData",
            meta: {
              requireAuth: true,
            },
            component: () => import("@/pages/user/PersonalData.vue"),
          }
        ]
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
