import axios from "axios";
import { api, getBaseURL } from "@/utils/request";
import type { ApiResponse } from "./types";

export function getAllSinger() {
  return api({ url: "singer" });
}

export function setSinger(params: {
  name: string;
  sex: number | string | null;
  kind: number | string;
  birth: string;
  location: string;
  introduction: string;
}) {
  return api({ method: "post", url: "singer/add", data: params });
}

export function updateSingerMsg(params: {
  id: number | string;
  name: string;
  sex: number | string | null;
  kind: number | string;
  birth: string;
  location: string;
  introduction: string;
}) {
  return api({ method: "post", url: "singer/update", data: params });
}

export function deleteSinger(id: string | number) {
  return api({ url: `singer/delete?id=${id}` });
}

/** 上传歌手头像（multipart）；由编辑弹窗在点「确定」时调用 */
export async function uploadSingerAvatar(
  id: string | number,
  file: File,
): Promise<ApiResponse<{ url?: string }>> {
  const formData = new FormData();
  formData.append("file", file);
  const { data } = await axios.post(
    `${getBaseURL()}/singer/avatar/update?id=${id}`,
    formData,
    {
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
    },
  );
  return data as ApiResponse<{ url?: string }>;
}
