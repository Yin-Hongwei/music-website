import axios from "axios";
import { api, getBaseURL } from "@/utils/request";
import type { ApiResponse } from "./types";

export function getAllUser() {
  return api({ url: "user" });
}

export function getUserOfId(id: string | number) {
  return api({ url: `user/detail?id=${id}` });
}

export function updateUserMsg(params: {
  id: number | string;
  username: string;
  sex: number | string;
  phoneNum: string;
  email: string;
  birth: string;
  introduction: string;
  location: string;
}) {
  return api({ method: "post", url: "user/update", data: params });
}

export function deleteUser(id: string | number) {
  return api({ url: `user/delete?id=${id}` });
}

/** 上传用户头像（multipart）；由编辑弹窗在点「确定」时调用 */
export async function uploadUserAvatar(
  id: string | number,
  file: File,
): Promise<ApiResponse<{ url?: string }>> {
  const formData = new FormData();
  formData.append("file", file);
  const { data } = await axios.post(
    `${getBaseURL()}/user/avatar/update?id=${id}`,
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
