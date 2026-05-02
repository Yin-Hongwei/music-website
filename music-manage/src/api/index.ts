/**
 * 管理端 API 按领域拆分，页面/组件从子模块或本 barrel 引入具名方法。
 * HTTP 统一走 `@/utils/request` 的 `api()`。
 */
export { api, getBaseURL } from "@/utils/request";
export type { ApiRequestOptions, HttpMethod } from "@/utils/request";
export * from "./types";
export * from "./url";
export * from "./admin";
export * from "./user";
export * from "./collection";
export * from "./comment";
export * from "./banner";
export * from "./singer";
export * from "./song";
export * from "./songSheet";
export * from "./listSong";
