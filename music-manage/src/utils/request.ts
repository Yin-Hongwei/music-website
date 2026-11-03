import axios from "axios";
import type { ApiResponse } from "@/api/types";
import { message } from "@/utils/message";
import router from "../router";

const BASE_URL = process.env.NODE_HOST;

axios.defaults.timeout = 5000;
axios.defaults.withCredentials = true;
axios.defaults.baseURL = BASE_URL;
axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

/** HTTP 状态码 → 默认提示；后端统一响应里带 message 时优先后者的 */
const STATUS_MESSAGES: Record<number, string> = {
  400: "请求参数错误",
  404: "请求的接口不存在",
  405: "请求方式不支持",
  408: "请求超时",
  500: "服务器内部错误",
  502: "网关错误",
  503: "服务暂不可用",
  504: "网关超时",
};

function backendMessage(res: { data?: { message?: unknown } }): string | null {
  const detail = res.data?.message;
  return typeof detail === "string" && detail ? detail : null;
}

axios.interceptors.response.use(
  (response) => {
    if (response.status === 200) {
      return Promise.resolve(response);
    }
    return Promise.reject(response);
  },
  (error) => {
    const res = error.response;
    if (!res) {
      // 无响应：区分请求超时与网络不通
      message.error(
        error.code === "ECONNABORTED"
          ? "请求超时，请稍后重试"
          : "无法连接服务器，请检查网络或后端服务是否已启动",
      );
      return Promise.reject(error);
    }
    switch (res.status) {
      case 401:
        message.error(backendMessage(res) || "登录已失效，请重新登录");
        sessionStorage.removeItem("adminAuth");
        router.replace({ path: "/", query: {} });
        break;
      case 403:
        message.error(backendMessage(res) || "无权访问该资源");
        setTimeout(() => {
          router.replace({ path: "/", query: {} });
        }, 1000);
        break;
      default:
        message.error(backendMessage(res) || STATUS_MESSAGES[res.status] || `请求失败（HTTP ${res.status}）`);
        break;
    }
    return Promise.reject(res);
  },
);

export function getBaseURL() {
  return BASE_URL;
}

function rawGet(url: string, config?: object) {
  return new Promise((resolve, reject) => {
    axios.get(url, config).then(
      (response) => resolve(response.data),
      (error) => reject(error),
    );
  });
}

function rawPost(url: string, data: object = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error),
    );
  });
}

function rawDelete(url: string, data: object = {}) {
  return new Promise((resolve, reject) => {
    axios.delete(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error),
    );
  });
}

function rawPut(url: string, data: object = {}) {
  return new Promise((resolve, reject) => {
    axios.put(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error),
    );
  });
}

export type HttpMethod = "get" | "post" | "delete" | "put";

export interface ApiRequestOptions {
  /** 默认 `get` */
  method?: HttpMethod;
  url: string;
  /** `post` / `put` / `delete` 的 JSON 请求体 */
  data?: object;
  /** 仅 `get`：作为 axios 第二个参数（如 `{ params: { id: 1 } }`） */
  config?: object;
}

/**
 * 统一业务请求入口，返回 `ApiResponse<T>`。
 * - 未传 `method` 时为 GET
 * - 与历史 `get/post/deletes/put` 行为一致，仍走同一套 axios 实例与拦截器
 */
export function api<T = unknown>(options: ApiRequestOptions): Promise<ApiResponse<T>> {
  const method = (options.method ?? "get").toLowerCase() as HttpMethod;
  const { url, data = {}, config } = options;
  let raw: Promise<unknown>;
  switch (method) {
    case "post":
      raw = rawPost(url, data);
      break;
    case "delete":
      raw = rawDelete(url, data);
      break;
    case "put":
      raw = rawPut(url, data);
      break;
    case "get":
    default:
      raw = rawGet(url, config);
      break;
  }
  return raw as Promise<ApiResponse<T>>;
}
