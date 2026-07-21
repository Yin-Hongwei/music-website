import axios from "axios";
import { api, getBaseURL } from "@/utils/request";
import type { ApiResponse } from "./types";

function postMultipart(
  url: string,
  formData: FormData,
): Promise<ApiResponse<{ url?: string; id?: number }>> {
  return axios
    .post(`${getBaseURL()}${url}`, formData, {
      withCredentials: true,
      headers: { "Content-Type": undefined as unknown as string },
      transformRequest: [
        (body, headers) => {
          if (headers) {
            delete headers["Content-Type"];
            if (headers.common) delete headers.common["Content-Type"];
            if (headers.post) delete headers.post["Content-Type"];
          }
          return body;
        },
      ],
    })
    .then(({ data }) => data as ApiResponse<{ url?: string; id?: number }>);
}

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
  duration?: number;
}) {
  return api({ method: "post", url: "song/update", data: params });
}

export function deleteSong(id: string | number) {
  return api({ method: "delete", url: `song/delete?id=${id}` });
}

/** 添加歌曲（multipart：文本字段 + file）→ POST /song/add */
export function addSong(formData: FormData) {
  return postMultipart("/song/add", formData);
}

/** 上传/更换歌曲封面 → POST /song/img/update */
export function uploadSongImg(id: string | number, file: File) {
  const formData = new FormData();
  formData.append("file", file);
  return postMultipart(`/song/img/update?id=${id}`, formData);
}

/** 上传/更换歌曲音频 → POST /song/url/update */
export function uploadSongUrl(
  id: string | number,
  file: File,
  duration?: number,
) {
  const formData = new FormData();
  formData.append("file", file);
  const qs =
    duration != null && Number.isFinite(duration)
      ? `?id=${id}&duration=${Math.max(0, Math.round(duration))}`
      : `?id=${id}`;
  return postMultipart(`/song/url/update${qs}`, formData);
}
