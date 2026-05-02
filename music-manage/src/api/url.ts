import { getBaseURL } from "@/utils/request";

/**
 * 拼接可访问的图片或静态资源地址（相对路径会拼上 API base）
 */
export function attachImageUrl(url: string | undefined | null): string {
  const rawUrl = typeof url === "string" ? url.trim() : "";
  const fallbackPath = "/img/avatorImages/user.jpg";
  const normalizedPath = rawUrl || fallbackPath;
  if (/^https?:\/\//i.test(normalizedPath)) {
    return normalizedPath;
  }
  const baseUrl = (getBaseURL() || "").replace(/\/+$/, "");
  const path = normalizedPath.startsWith("/") ? normalizedPath : `/${normalizedPath}`;
  return `${baseUrl}${path}`;
}

export function getBannerAddUploadUrl(): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/banner/add`;
}

export function getBannerUpdatePicUploadUrl(id: string | number): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/banner/updatePic?id=${id}`;
}

export function getSongUrlUpdateUploadUrl(id: string | number): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/song/url/update?id=${id}`;
}

export function getSongImgUpdateUploadUrl(id: string | number): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/song/img/update?id=${id}`;
}
