import { get, post } from './request'

const HttpManager = {
  // =======================> 用户 API
  // 登录
  signIn: (params) => post(`user/login/status`, params),
  // 注册
  SignUp: (params) => post(`user/add`, params),
  // 更新用户信息
  updateUserMsg: (params) => post(`user/update`, params),
  // 返回指定ID的用户
  getUserOfId: (id) => get(`user/detail?id=${id}`),

  // =======================> 歌单 API
  // 获取全部歌单
  getSongList: () => get('songList'),
  // 获取歌单类型
  getSongListOfStyle: (style) => get(`songList/style/detail?style=${style}`),
  // 返回标题包含文字的歌单
  getSongListOfLikeTitle: (keywords) => get(`songList/likeTitle/detail?title=${keywords}`),
  // 返回歌单里指定歌单ID的歌曲
  getListSongOfSongId: (songListId) => get(`listSong/detail?songListId=${songListId}`),

  // =======================> 歌手 API
  // 返回所有歌手
  getAllSinger: () => get('singer'),
  // 通过性别对歌手分类
  getSingerOfSex: (sex) => get(`singer/sex/detail?sex=${sex}`),

  // =======================> 收藏 API
  // 返回的指定用户ID的收藏列表
  getCollectionOfUser: (userId) => get(`collection/detail?userId=${userId}`),
  // 添加收藏的歌曲 type: 0 代表歌曲， 1 代表歌单
  setCollection: (params) => post(`collection/add`, params),

  // =======================> 评分 API
  // 提交评分
  setRank: (params) => post(`rankList/add`, params),
  // 获取指定歌单的评分
  getRankOfSongListId: (songListId) => get(`rankList?songListId=${songListId}`),

  // =======================> 评论 API
  // 添加评论
  setComment: (params) => post(`comment/add`, params),
  // 点赞
  setSupport: (params) => post(`comment/like`, params),
  // 返回所有评论
  getAllComment: (type, id) => {
    let url = ''
    if (type === 1) {
      url = `comment/songList/detail?songListId=${id}`
    } else if (type === 0) {
      url = `comment/song/detail?songId=${id}`
    }
    return get(url)
  },

  // =======================> 歌曲 API
  // 返回指定歌曲ID的歌曲
  getSongOfId: (id) => get(`song/detail?id=${id}`),
  // 返回指定歌手ID的歌曲
  getSongOfSingerId: (id) => get(`song/singer/detail?singerId=${id}`),
  // 返回指定歌手名的歌曲
  getSongOfSingerName: (keywords) => get(`song/singerName/detail?name=${keywords}`),
  // 下载音乐
  downloadMusic: (url) => get(url, {}, 'blob')
}

export { HttpManager }
