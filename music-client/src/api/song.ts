import { api, getBlob, type ApiResponse } from "@/utils/request";
import { ensureArray } from "@/utils/api-response-data";
import type { HomeCardItem } from "@/api/models";

export async function fetchSongListBySingerId(singerId: string | number): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: `song/singer/detail?singerId=${singerId}` });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

/** 下载：返回二进制 Blob，供前端创建对象 URL */
export async function fetchDownloadMusic(songUrl: string): Promise<Blob> {
  return getBlob(songUrl);
}

export async function fetchSongBySingerName(keywords: string): Promise<ApiResponse> {
  return await api({ url: `song/singerName/detail?name=${keywords}` });
}
