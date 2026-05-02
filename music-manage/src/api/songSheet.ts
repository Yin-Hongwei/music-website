import { api } from "@/utils/request";

export function setSongList(params: { title: string; introduction: string; style: string }) {
  return api({ method: "post", url: "songList/add", data: params });
}

export function getSongList() {
  return api({ url: "songList" });
}

export function updateSongListMsg(params: {
  id: number | string;
  title: string;
  introduction: string;
  style: string;
}) {
  return api({ method: "post", url: "songList/update", data: params });
}

export function deleteSongList(id: string | number) {
  return api({ url: `songList/delete?id=${id}` });
}
