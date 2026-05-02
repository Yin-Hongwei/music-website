import { api } from "@/utils/request";
import { routeQueryValue } from "@/utils/routeQuery";

export function getCommentOfSongId(songId: unknown) {
  return api({ url: `comment/song/detail?songId=${routeQueryValue(songId)}` });
}

export function getCommentOfSongListId(songListId: unknown) {
  return api({ url: `comment/songList/detail?songListId=${routeQueryValue(songListId)}` });
}

export function deleteComment(id: string | number) {
  return api({ url: `comment/delete?id=${id}` });
}
