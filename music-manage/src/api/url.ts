import { getBaseURL } from "@/utils/request";

/**
 * 拼接可访问的图片或静态资源地址（相对路径会拼上 API base）
 */
export function attachImageUrl(url: string | undefined | null): string {
  const rawUrl = typeof url === "string" ? url.trim() : "";
  const fallbackPath = "/img/avatarImages/user.jpg";
  const normalizedPath = rawUrl || fallbackPath;
  if (/^https?:\/\//i.test(normalizedPath)) {
    return normalizedPath;
  }
  const baseUrl = (getBaseURL() || "").replace(/\/+$/, "");
  const path = normalizedPath.startsWith("/") ? normalizedPath : `/${normalizedPath}`;
  // Encode each segment for Chinese / spaces; keep `=` unencoded — seed covers
  // often use base64-like names (…==_….jpg) and Spring static resources 404 on %3D.
  const encodedPath = path
    .split("/")
    .map((seg) => (seg ? encodeURIComponent(seg).replace(/%3D/gi, "=") : ""))
    .join("/");
  return `${baseUrl}${encodedPath}`;
}

export function getBannerAddUploadUrl(): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/banner/add`;
}

export function getBannerUpdatePicUploadUrl(id: string | number): string {
  const base = (getBaseURL() || "").replace(/\/+$/, "");
  return `${base}/banner/updatePic?id=${id}`;
}
