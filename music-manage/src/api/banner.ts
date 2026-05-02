import { api } from "@/utils/request";

export function getAllBanner() {
  return api({ url: "banner/getAllBanner" });
}

export function updateBannerUrl(params: { id: string | number; url: string; title: string }) {
  return api({ method: "post", url: "banner/updateUrl", data: params });
}

export function deleteBanner(id: string | number) {
  return api({ url: `banner/delete?id=${id}` });
}
