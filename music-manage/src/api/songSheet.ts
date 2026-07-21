import axios from "axios";
import { api, getBaseURL } from "@/utils/request";
import type { ApiResponse } from "./types";

export interface SongSheetStyleOption {
  id: number;
  name: string;
}

export function getSongListStyleList() {
  return api<SongSheetStyleOption[]>({ url: "songList/style/list" });
}

export function setSongList(params: {
  title: string;
  introduction: string;
  styleIds: number[];
}) {
  return api({ method: "post", url: "songList/add", data: params });
}

export function getSongList() {
  return api({ url: "songList" });
}

export function updateSongListMsg(params: {
  id: number | string;
  title: string;
  introduction: string;
  styleIds: number[];
}) {
  return api({ method: "post", url: "songList/update", data: params });
}

export function deleteSongList(id: string | number) {
  return api({ url: `songList/delete?id=${id}` });
}

/** 上传歌单封面（multipart）；添加/编辑弹窗在点「确定」时调用 → POST /songList/img/update */
export async function uploadSongListImg(
  id: string | number,
  file: File,
): Promise<ApiResponse<{ url?: string }>> {
  const formData = new FormData();
  formData.append("file", file);
  const { data } = await axios.post(
    `${getBaseURL()}/songList/img/update?id=${id}`,
    formData,
    {
      withCredentials: true,
      // 覆盖默认 application/json，交给浏览器带 boundary
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
    },
  );
  return data as ApiResponse<{ url?: string }>;
}
