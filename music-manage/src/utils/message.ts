import { createDiscreteApi } from "naive-ui";
import type { MessageApi } from "naive-ui";
import type { ApiResponse } from "@/api/types";

const { message } = createDiscreteApi(["message"]);

export { message };

type MessageTone = "success" | "warning" | "info" | "error";

function toTone(type?: string, success?: boolean): MessageTone {
  if (type === "success" || type === "warning" || type === "info" || type === "error") {
    return type;
  }
  return success ? "success" : "error";
}

/** 按后端统一响应展示消息 */
export function notifyResult(result: Pick<ApiResponse, "message" | "success" | "type">) {
  const tone = toTone(result.type, result.success);
  (message as MessageApi)[tone](result.message || (result.success ? "操作成功" : "操作失败"));
}
