import { api, type ApiResponse } from "@/utils/request";
import { emptyPagedResult, ensureArray, normalizePagedResult, type PagedResult } from "@/utils/api-response-data";
import type { HomeCardItem, SongStyleItem } from "@/api/models";

export async function fetchSongList(limit = 10): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: "songList" });
    return ensureArray<HomeCardItem>(result?.data).slice(0, limit);
  } catch {
    return [];
  }
}

export async function fetchRecommendedSongList(limit = 10): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: `songList/recommend?limit=${limit}` });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

export async function fetchSongSheetList(): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: "songList" });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

export async function fetchSongSheetListPage(page: number, size: number): Promise<PagedResult<HomeCardItem>> {
  try {
    const result = await api({ url: `songList/page?page=${page}&size=${size}` });
    return normalizePagedResult<HomeCardItem>(result?.data, page, size);
  } catch {
    return emptyPagedResult(page, size);
  }
}

export async function fetchSongSheetListByStyle(style: string): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: `songList/style/detail?style=${style}` });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}

export async function fetchSongSheetListByStylePage(
  style: string,
  page: number,
  size: number,
): Promise<PagedResult<HomeCardItem>> {
  try {
    const result = await api({
      url: `songList/style/page/detail?style=${style}&page=${page}&size=${size}`,
    });
    return normalizePagedResult<HomeCardItem>(result?.data, page, size);
  } catch {
    return emptyPagedResult(page, size);
  }
}

export async function fetchSongSheetStyleList(): Promise<SongStyleItem[]> {
  try {
    const result = await api({ url: "songList/style/list" });
    const styles = ensureArray<Record<string, unknown>>(result?.data);
    return styles
      .map((item) => ({
        id: Number(item.id) || 0,
        name: typeof item.name === "string" ? item.name.trim() : "",
      }))
      .filter((item) => item.name.length > 0);
  } catch {
    return [{ id: 0, name: "全部歌单" }];
  }
}

export async function fetchSongListByLikeTitle(keywords: string): Promise<ApiResponse> {
  return await api({ url: `songList/likeTitle/detail?title=${keywords}` });
}
