export { message, notifyResult } from "./message";
export {
  changeSex,
  changeSingerSex,
  changeSingerKind,
  changeSingerType,
  changeUserSex,
  sexTagType,
  singerSexTagType,
  singerKindTagType,
  singerTypeTagType,
} from "./sex";
export {
  beforeImgUpload,
  beforeSongUpload,
  parseUploadUrl,
  parseCreatedId,
} from "./upload";

/** 解析日期（展示用） */
export function getBirth(cellValue: string | number | Date | null | undefined) {
  if (cellValue == null || cellValue === "") return "";
  const date = new Date(cellValue);
  if (Number.isNaN(date.getTime())) return "";
  const year = date.getFullYear();
  const month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
  const day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  return year + "-" + month + "-" + day;
}

/** 提交给后端的生日格式，对齐 spring.jackson.date-format=yyyy-MM-dd HH:mm:ss */
export function formatBirthForBackend(cellValue: string | number | Date | null | undefined) {
  const day = getBirth(cellValue);
  return day ? `${day} 00:00:00` : "";
}

/** Naive DatePicker 值（timestamp）↔ 业务日期 */
export function toDateTimestamp(value: string | number | Date | null | undefined): number | null {
  if (value == null || value === "") return null;
  if (typeof value === "number") return value;
  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? null : date.getTime();
}

/** 解析歌词 */
export function parseLyric(text: string) {
  const lines = text.split("\n");
  const pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g;
  const result: string[] = [];

  if (!/\[.+\]/.test(text)) {
    return [text];
  }
  for (const item of lines) {
    if (pattern.test(item)) {
      const value = item.replace(pattern, "");
      result.push(value);
    }
  }
  return result;
}
