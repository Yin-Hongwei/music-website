import { HOME, SONG_SHEET, SINGER, PERSONAL, SIGN_IN, SIGN_UP, SIGN_OUT } from './router-name'

export const NAV_NAME = {
  HOME: '首页',
  SONG_SHEET: '歌单',
  SINGER: '歌手',
  PERSONAL: '个人主页',
  SETTING: '设置',
  SIGN_IN: '登录',
  SIGN_UP: '注册',
  SIGN_OUT: '退出'
}

// 左侧导航栏
export const HEADERNAVLIST = [{
  name: NAV_NAME.HOME,
  path: HOME
}, {
  name: NAV_NAME.SONG_SHEET,
  path: SONG_SHEET
}, {
  name: NAV_NAME.SINGER,
  path: SINGER
}]

// 右侧导航栏
export const SIGNLIST = [{
  name: NAV_NAME.SIGN_IN,
  path: SIGN_IN
}, {
  name: NAV_NAME.SIGN_UP,
  path: SIGN_UP
}]

// 用户下拉菜单项
export const MENULIST = [
  {
    name: NAV_NAME.PERSONAL,
    path: PERSONAL
  },
  // {
  //   name: NAV_NAME.SETTING,
  //   path: SETTING
  // },
  {
    name: NAV_NAME.SIGN_OUT,
    path: SIGN_OUT
  }]
