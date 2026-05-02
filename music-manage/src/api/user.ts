import { api } from "@/utils/request";

export function getAllUser() {
  return api({ url: "user" });
}

export function getUserOfId(id: string | number) {
  return api({ url: `user/detail?id=${id}` });
}

export function deleteUser(id: string | number) {
  return api({ url: `user/delete?id=${id}` });
}
