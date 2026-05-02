import { api } from "@/utils/request";

export async function getLoginStatus(params: { username: string; password: string }) {
  return api({ method: "post", url: "admin/login/status", data: params });
}
