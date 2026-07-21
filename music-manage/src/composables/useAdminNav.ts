import { useRouter } from "vue-router";
import type { LocationQueryRaw } from "vue-router";

/** 管理端路由跳转：统一走路由 name */
export function useAdminNav() {
  const router = useRouter();

  function routerManager(name: string, options?: { query?: LocationQueryRaw }) {
    router.push({ name, query: options?.query });
  }

  function goBack(step = -1) {
    router.go(step);
  }

  return { routerManager, goBack };
}
