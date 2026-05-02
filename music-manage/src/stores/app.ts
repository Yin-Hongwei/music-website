import { defineStore } from "pinia";

/** 与面包屑 `el-breadcrumb-item` 的 `to` 字段对齐 */
export type BreadcrumbItem = {
  name: string;
  path: string;
  /** 与 `RouteLocationNormalized["query"]` 等来源兼容 */
  query?: unknown;
};

export const useAppStore = defineStore("app", {
  state: () => ({
    userPic: "/img/avatorImages/user.jpg",
    isPlay: false,
    url: "",
    id: "",
    breadcrumbList: [] as BreadcrumbItem[],
  }),
  actions: {
    setUserPic(userPic: string) {
      this.userPic = userPic;
    },
    setIsPlay(isPlay: boolean) {
      this.isPlay = isPlay;
    },
    setUrl(url: string) {
      this.url = url;
    },
    setId(id: string) {
      this.id = id;
    },
    setBreadcrumbList(breadcrumbList: BreadcrumbItem[]) {
      this.breadcrumbList = breadcrumbList;
    },
  },
});
