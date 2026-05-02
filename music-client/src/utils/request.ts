import axios, { AxiosRequestConfig } from "axios";
import router from "../router";

/**
 * 与后端统一响应约定一致：code / message / success / data。
 * `type` 为历史字段（部分接口仍下发）；与 `api()` 同文件导出，便于 `import { api, type ApiResponse } from "@/utils/request"`。
 */
export type ApiResponse<T = unknown> = {
  /** 与 manage `ApiResponse` 一致，兼容数字或字符串业务码 */
  code: number | string;
  message: string;
  success: boolean;
  data?: T;
  type?: string;
};

const BASE_URL = process.env.VUE_APP_NODE_HOST || process.env.NODE_HOST;

const request = axios.create({
  baseURL: BASE_URL,
  timeout: 5000,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json;charset=UTF-8",
  },
});

request.interceptors.response.use(
  (response) => {
    if (response.status === 200) {
      return response;
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

export type HttpMethod = "get" | "post" | "delete" | "put";

export interface ApiRequestOptions {
  /** 默认 `get` */
  method?: HttpMethod;
  url: string;
  /** `post` / `put` / `delete` 的 JSON 请求体 */
  data?: object;
  /**
   * GET / DELETE：axios 第二参数；
   * POST / PUT：axios 第三参数（如 `jsonRequestConfig` 或与 manage 一致的 headers）
   */
  config?: AxiosRequestConfig;
}

/**
 * 统一业务请求入口，返回 `ApiResponse<T>`（与 music-manage `utils/request` 的 `api()` 对齐）。
 * 未传 `method` 时为 GET。对 `response.data` 的断言集中在此处，调用方无需再写 `as ApiResponse`。
 */
export function api<T = unknown>(options: ApiRequestOptions): Promise<ApiResponse<T>> {
  const method = (options.method ?? "get").toLowerCase() as HttpMethod;
  const { url, data = {}, config } = options;
  const mapBody = (response: { data: unknown }) => response.data as ApiResponse<T>;

  switch (method) {
    case "post":
      return request.post(url, data, config).then(mapBody);
    case "delete":
      return request.delete(url, config).then(mapBody);
    case "put":
      return request.put(url, data, config).then(mapBody);
    case "get":
    default:
      return request.get(url, config).then(mapBody);
  }
}

/** 二进制响应（非统一 JSON），例如音乐文件下载 */
export function getBlob(url: string, config?: AxiosRequestConfig): Promise<Blob> {
  return request
    .get(url, { ...config, responseType: "blob" })
    .then((response) => response.data as Blob);
}
