import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { useConfigureStore } from "@/store/configure";
import { useUserStore } from "@/store/user";
import { fetchUserSession } from "@/api/user";
const routes: Array<RouteRecordRaw> = [
  {
    path: "/:pathMatch(.*)*",
    redirect: "/404",
  },
  {
    path: "/404",
    component: () => import("@/pages/error/404.vue"),
  },
  // 认证页独立于应用布局：未登录时不展示顶部导航与底部播放栏
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

/** 无需登录即可访问的路由 */
const PUBLIC_ROUTE_NAMES = new Set(["sign-in", "sign-up", "forgot-password", "404"]);

router.beforeEach(async (to) => {
  if (to.name !== undefined && PUBLIC_ROUTE_NAMES.has(String(to.name))) {
    return true;
  }
  const configureStore = useConfigureStore();
  if (configureStore.token) {
    return true;
  }
  // 刷新后内存态丢失，向服务端探测会话是否仍有效，有效则恢复登录态
  try {
    const result = await fetchUserSession();
    const data = result.success ? result.data : null;
    const user = (Array.isArray(data) ? data[0] : data) as
      | { id?: string | number; username?: string; avatar?: string }
      | null
      | undefined;
    if (user && user.id != null) {
      useUserStore().setUserId(user.id);
      useUserStore().setUsername(user.username ?? "");
      useUserStore().setUserPic(user.avatar ?? "");
      configureStore.setToken(true);
      return true;
    }
  } catch {
    // 探测失败（后端未启动/网络异常）按未登录处理
  }
  return { name: "sign-in" };
});

export default router;
