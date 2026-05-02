import { api } from "@/utils/request";
import { routeQueryValue } from "@/utils/routeQuery";

export function getCollectionOfUser(userId: unknown) {
  return api({ url: `collection/detail?userId=${routeQueryValue(userId)}` });
}

export function deleteCollection(userId: unknown, songId: string | number) {
  return api({
    method: "delete",
    url: `collection/delete?userId=${routeQueryValue(userId)}&songId=${songId}`,
  });
}
