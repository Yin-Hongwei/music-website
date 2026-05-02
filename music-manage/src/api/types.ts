/** 与后端统一响应 `code/message/success/data` 对齐；保留 `type` 以兼容管理端历史消息展示 */
export interface ApiResponse<T = unknown> {
  code: number | string;
  message: string;
  success: boolean;
  data?: T;
  /** Element Plus message type，部分接口仍返回 */
  type?: string;
}
