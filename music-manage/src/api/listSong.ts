import { api } from "@/utils/request";
import { routeQueryValue } from "@/utils/routeQuery";

export function setListSong(params: { songId: number; songListId: string | number }) {
  return api({ method: "post", url: "listSong/add", data: params });
}

export function getListSongOfSongId(songListId: unknown) {
  return api({ url: `listSong/detail?songListId=${routeQueryValue(songListId)}` });
}

export function deleteListSong(songId: string | number) {
  return api({ method: "delete", url: `listSong/delete?songId=${songId}` });
}
