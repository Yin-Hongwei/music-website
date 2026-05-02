import type { AxiosRequestConfig } from "axios";
import type { ApiResponse } from "@/utils/request";

export const jsonRequestConfig: AxiosRequestConfig = {
  headers: {
    "Content-Type": "application/json;charset=UTF-8",
  },
};

export function elMessageTypeFromResponse(
  r: Pick<ApiResponse, "success" | "type">,
): "success" | "warning" | "error" | "info" {
  const t = r.type;
  if (t === "success" || t === "warning" || t === "error" || t === "info") {
    return t;
  }
  return r.success ? "success" : "error";
}
