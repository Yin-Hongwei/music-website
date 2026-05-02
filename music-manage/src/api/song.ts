import { api } from "@/utils/request";

export function getAllSong() {
  return api({ url: "song" });
}

export function getSongOfSingerId(id: string | number) {
  return api({ url: `song/singer/detail?singerId=${id}` });
}

export function getSongOfId(id: string | number) {
  return api({ url: `song/detail?id=${id}` });
}

export function getSongOfSingerName(compositeName: string) {
  return api({ url: `song/singerName/detail?name=${compositeName}` });
}

export function updateSongMsg(params: {
  id: number | string;
  singerId: number | string;
  name: string;
  introduction: string;
  lyric: string;
}) {
  return api({ method: "post", url: "song/update", data: params });
}

export function deleteSong(id: string | number) {
  return api({ method: "delete", url: `song/delete?id=${id}` });
}
