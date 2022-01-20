// 解析日期
export function getDateTime (date = new Date()) {
  return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
}

// 解析歌词
export function parseLyric (text) {
  let lines = text.split('\n')
  let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g
  let result = []

  // 对于歌词格式不对的特殊处理
  if (!(/\[.+\]/.test(text))) {
    return [[0, text]]
  }

  while (!pattern.test(lines[0])) {
    lines = lines.slice(1)
  }

  lines[lines.length - 1].length === 0 && lines.pop()
  for (let item of lines) {
    let time = item.match(pattern) // 存前面的时间段
    let value = item.replace(pattern, '') // 存歌词
    for (let item1 of time) {
      let t = item1.slice(1, -1).split(':')
      if (value !== '') {
        result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value])
      }
    }
  }
  result.sort((a, b) => a[0] - b[0])
  return result
}

// 解析播放时间
export function formatSeconds (value) {
  let theTime = parseInt(value)
  let theTime1 = 0
  let theTime2 = 0
  if (theTime > 60) {
    theTime1 = parseInt(theTime / 60) // 分
    theTime = parseInt(theTime % 60) // 秒
    // 是否超过一个小时
    if (theTime1 > 60) {
      theTime2 = parseInt(theTime1 / 60) // 小时
      theTime1 = 60 // 分
    }
  }
  // 多少秒
  let result = ''
  if (parseInt(theTime) < 10) {
    result = '0:0' + parseInt(theTime)
  } else {
    result = '0:' + parseInt(theTime)
  }
  // 多少分钟时
  if (theTime1 > 0) {
    if (parseInt(theTime) < 10) {
      result = '0' + parseInt(theTime)
    } else {
      result = parseInt(theTime)
    }
    result = parseInt(theTime1) + ':' + result
  }
  // 多少小时时
  if (theTime2 > 0) {
    if (parseInt(theTime) < 10) {
      result = '0' + parseInt(theTime)
    } else {
      result = parseInt(theTime)
    }
    result = parseInt(theTime2) + ':' + parseInt(theTime1) + ':' + result
  }
  return result
}
