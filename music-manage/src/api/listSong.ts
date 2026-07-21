import { api } from "@/utils/request";
import { routeQueryValue } from "@/utils/routeQuery";

export function setListSong(params: {
  songId: number;
  songListId: string | number;
}) {
  return api({ method: "post", url: "listSong/add", data: params });
}

/** 歌单内歌曲关联（仅 id 映射） */
export function getListSongOfSongId(songListId: unknown) {
  return api({
    url: `listSong/detail?songListId=${routeQueryValue(songListId)}`,
  });
}

/** 歌单内歌曲详情（推荐列表页使用，避免 N+1） */
export function getSongDetailOfSongListId(songListId: unknown) {
  return api({
    url: `listSong/song/detail?songListId=${routeQueryValue(songListId)}`,
  });
}

/** 从指定歌单删除歌曲 */
export function deleteListSong(
  songListId: unknown,
  songId: string | number,
) {
  return api({
    method: "delete",
    url: `listSong/delete?songListId=${routeQueryValue(songListId)}&songId=${songId}`,
  });
}
