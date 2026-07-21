import { message } from "@/utils/message";

const IMAGE_TYPES = ["jpg", "jpeg", "png", "gif"];

export function beforeImgUpload(file: File): boolean {
  const maxMb = 2;
  const isLt = file.size / 1024 / 1024 < maxMb;
  const ext = file.type.replace(/image\//, "");
  const typeOk = IMAGE_TYPES.includes(ext);

  if (!typeOk) {
    message.error(`图片只支持 ${IMAGE_TYPES.join("、")} 格式!`);
  }
  if (!isLt) {
    message.error(`上传图片大小不能超过 ${maxMb}MB!`);
  }
  return typeOk && isLt;
}

export function beforeSongUpload(file: File): boolean {
  const maxMb = 10;
  const isLt = file.size / 1024 / 1024 < maxMb;
  const ext = file.name.substring(file.name.lastIndexOf(".") + 1).toLowerCase();
  const typeOk = ext === "mp3";

  if (!typeOk) {
    message.error("上传文件只能是 mp3 格式！");
  }
  if (!isLt) {
    message.error(`上传文件大小不能超过 ${maxMb}MB!`);
  }
  return typeOk && isLt;
}

export function parseUploadUrl(payload: unknown): string {
  if (typeof payload === "string") return payload;
  if (payload && typeof payload === "object" && "url" in payload) {
    const url = (payload as { url?: unknown }).url;
    return typeof url === "string" ? url : "";
  }
  return "";
}

export function parseCreatedId(payload: unknown): number | null {
  if (payload && typeof payload === "object" && "id" in payload) {
    const id = Number((payload as { id?: unknown }).id);
    return Number.isFinite(id) && id > 0 ? id : null;
  }
  return null;
}
