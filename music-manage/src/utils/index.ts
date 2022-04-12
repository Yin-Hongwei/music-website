// 解析日期
export function getBirth(cellValue) {
  if (cellValue == null || cellValue == "") return "";
  const date = new Date(cellValue);
  const year = date.getFullYear();
  const month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
  const day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  return year + "-" + month + "-" + day;
}

// 解析歌词
export function parseLyric(text) {
  const lines = text.split("\n");
  const pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g;
  const result = [];

  // 对于歌词格式不对的特殊处理
  if (!/\[.+\]/.test(text)) {
    return [text];
  }
  for (const item of lines) {
    if (pattern.test(item)) {
      const value = item.replace(pattern, ""); // 存歌词
      result.push(value);
    }
  }
  return result;
}
