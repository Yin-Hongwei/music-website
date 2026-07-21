/** 歌手性别文案：0女 1男 2保密（仅个人） */
export function changeSingerSex(value: number | string | null | undefined): string {
  if (value === null || value === undefined || value === "") return "—";
  if (value === 0 || value === "0") return "女";
  if (value === 1 || value === "1") return "男";
  if (value === 2 || value === "2") return "保密";
  return "未知";
}

/** 歌手类型文案：0个人 1组合 2其他 */
export function changeSingerKind(value: number | string | undefined): string {
  if (value === 0 || value === "0") return "个人";
  if (value === 1 || value === "1") return "组合";
  if (value === 2 || value === "2") return "其他";
  return "未知";
}

/** @deprecated 使用 changeSingerSex */
export const changeSex = changeSingerSex;

/** 用户性别文案（2 = 保密） */
export function changeUserSex(value: number | string | undefined): string {
  if (value === 0 || value === "0") return "女";
  if (value === 1 || value === "1") return "男";
  if (value === 2 || value === "2") return "保密";
  if (value === "男" || value === "女" || value === "保密") return value;
  return "未知";
}

export type SexTagType =
  | "success"
  | "warning"
  | "info"
  | "error"
  | "primary"
  | "default";

export function singerSexTagType(
  value: number | string | null | undefined,
): SexTagType {
  if (value === 0 || value === "0") return "error";
  if (value === 1 || value === "1") return "primary";
  if (value === 2 || value === "2") return "default";
  return "default";
}

export function singerKindTagType(
  value: number | string | undefined,
): SexTagType {
  if (value === 0 || value === "0") return "info";
  if (value === 1 || value === "1") return "success";
  if (value === 2 || value === "2") return "warning";
  return "default";
}

/** @deprecated 使用 singerSexTagType */
export const sexTagType = singerSexTagType;

/** @deprecated */
export const changeSingerType = changeSingerKind;

/** @deprecated */
export const singerTypeTagType = singerKindTagType;
