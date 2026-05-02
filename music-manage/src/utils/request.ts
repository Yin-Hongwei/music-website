import axios from "axios";
import type { ApiResponse } from "@/api/types";
import router from "../router";

const BASE_URL = process.env.NODE_HOST;

axios.defaults.timeout = 5000;
axios.defaults.withCredentials = true;
axios.defaults.baseURL = BASE_URL;
axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

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
      return Promise.reject(error);
    }
    if (res.status) {
      switch (res.status) {
        case 401:
          router.replace({ path: "/", query: {} });
          break;
        case 403:
          setTimeout(() => {
            router.replace({ path: "/", query: {} });
          }, 1000);
          break;
        case 404:
          break;
      }
      return Promise.reject(res);
    }
    return Promise.reject(error);
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
