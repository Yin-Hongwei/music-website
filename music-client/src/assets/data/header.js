// 左侧导航栏
const navMsg = [{
  name: '首页',
  path: '/home'
}, {
  name: '歌单',
  path: '/song-list'
}, {
  name: '歌手',
  path: '/singer'
}, {
  name: '我的音乐',
  path: '/my-music'
}]

// 右侧导航栏
const loginMsg = [{
  name: '登录',
  path: '/'
}, {
  name: '注册',
  path: '/login-up'
}]

// 用户下拉菜单项
const menuList = [{
  name: '个人信息',
  path: '/info'
}, {
  name: '修改头像',
  path: '/upLoad'
}, {
  name: '退出',
  path: 0
}]

export {
  navMsg,
  loginMsg,
  menuList
}
