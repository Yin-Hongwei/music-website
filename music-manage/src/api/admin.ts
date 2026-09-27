import { api } from "@/utils/request";

export async function getLoginStatus(params: { username: string; password: string }) {
  return api({ method: "post", url: "admin/login/status", data: params });
}

export async function fetchAdminSession() {
  return api({ url: "admin/session" });
}

export async function fetchAdminLogout() {
  return api({ method: "post", url: "admin/logout", data: {} });
}
