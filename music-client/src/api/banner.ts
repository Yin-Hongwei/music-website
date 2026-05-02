import { api } from "@/utils/request";
import { ensureArray } from "@/utils/api-response-data";
import type { BannerItem } from "@/api/models";

export async function fetchBannerList(): Promise<BannerItem[]> {
  try {
    const result = await api({ url: "banner/getAllBanner" });
    return ensureArray<BannerItem>(result?.data);
  } catch {
    return [];
  }
}
