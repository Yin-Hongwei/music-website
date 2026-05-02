import { api } from "@/utils/request";
import { ensureArray } from "@/utils/api-response-data";
import type { HomeCardItem } from "@/api/models";

/** 歌单内歌曲列表（对应 manage `listSong` 领域） */
export async function fetchSongDetailOfSongListId(songListId: string | number): Promise<HomeCardItem[]> {
  try {
    const result = await api({ url: `listSong/song/detail?songListId=${songListId}` });
    return ensureArray<HomeCardItem>(result?.data);
  } catch {
    return [];
  }
}
