import { getBaseURL } from "@/utils/request";

export function attachImageUrl(url) {
  return url
    ? `${getBaseURL()}${url}`
    : "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png";
}

/** Display title from song payload (`name` is pure title; `songName` kept for playlist detail). */
export function resolveSongTitle(song: {
  name?: string;
  songName?: string;
  songTitle?: string;
} | string | null | undefined) {
  if (typeof song === "string") return song.trim();
  if (!song) return "";
  return String(song.songName || song.songTitle || song.name || "").trim();
}

/** Singer label from API `singerName` (resolved via singerId). */
export function resolveSingerName(song: {
  singerName?: string;
} | string | null | undefined) {
  if (typeof song === "string") return song.trim();
  if (!song) return "";
  return String(song.singerName || "").trim();
}

export function getUserSex(sex: number) {
  if (sex === 0) return "女";
  if (sex === 1) return "男";
  return "";
}

/** 歌手性别：0女 1男 2保密（仅个人） */
export function getSingerSexLabel(sex: number | string | null | undefined) {
  if (sex === null || sex === undefined || sex === "") return "";
  const value = typeof sex === "string" ? Number(sex) : sex;
  if (value === 0) return "女";
  if (value === 1) return "男";
  if (value === 2) return "保密";
  return "";
}

/** 歌手类型：0个人 1组合 2其他 */
export function getSingerKindLabel(kind: number | string | null | undefined) {
  if (kind === null || kind === undefined || kind === "") return "";
  const value = typeof kind === "string" ? Number(kind) : kind;
  if (value === 0) return "个人";
  if (value === 1) return "组合";
  if (value === 2) return "其他";
  return "";
}

// 解析日期
export function getBirth(value) {
  if (value == null || value == "") return "";
  const date = new Date(value);
  const year = date.getFullYear();
  const month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
  const day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  return year + "-" + month + "-" + day;
}

/**
 * 表格时间格式化
 */
export function formatDate(cellValue) {
  if (cellValue == null || cellValue == "") return "";
  const date = new Date(cellValue);
  const year = date.getFullYear();
  const month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
  const day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  const hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
  const minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
  const seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
  return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
}

// 解析歌词
export function parseLyric(text) {
  let lines = text.split("\n");
  const pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g;
  const result = [];

  // 对于歌词格式不对的特殊处理
  if (!/\[.+\]/.test(text)) {
    return [[0, text]];
  }

  while (!pattern.test(lines[0])) {
    lines = lines.slice(1);
  }

  lines[lines.length - 1].length === 0 && lines.pop();
  for (const item of lines) {
    const time = item.match(pattern); // 存前面的时间段
    const value = item.replace(pattern, ""); // 存歌词
    for (const item1 of time) {
      const t = item1.slice(1, -1).split(":");
      if (value !== "") {
        result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value]);
      }
    }
  }
  result.sort((a, b) => a[0] - b[0]);
  return result;
}

// 解析播放时间
export function formatSeconds(value) {
  let theTime = parseInt(value);
  let theTime1 = 0;
  let theTime2 = 0;
  if (theTime > 60) {
    theTime1 = parseInt((theTime / 60).toString()); // 分
    theTime = parseInt((theTime % 60).toString()); // 秒
    // 是否超过一个小时
    if (theTime1 > 60) {
      theTime2 = parseInt((theTime1 / 60).toString()); // 小时
      theTime1 = 60; // 分
    }
  }
  // 多少秒
  let result = "";
  if (parseInt(theTime.toString()) < 10) {
    result = "0:0" + parseInt(theTime.toString());
  } else {
    result = "0:" + parseInt(theTime.toString());
  }
  // 多少分钟时
  if (theTime1 > 0) {
    if (parseInt(theTime.toString()) < 10) {
      result = "0" + parseInt(theTime.toString());
    } else {
      result = parseInt(theTime.toString()).toString();
    }
    result = parseInt(theTime1.toString()) + ":" + result;
  }
  // 多少小时时
  if (theTime2 > 0) {
    if (parseInt(theTime.toString()) < 10) {
      result = "0" + parseInt(theTime.toString());
    } else {
      result = parseInt(theTime.toString()).toString();
    }
    result = parseInt(theTime2.toString()) + ":" + parseInt(theTime1.toString()) + ":" + result;
  }
  return result;
}
