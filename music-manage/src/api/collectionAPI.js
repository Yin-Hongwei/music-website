import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  // 返回的指定用户ID收藏列表
  getCollectionOfUser (userId) {
    return axios.get(`${host}/collection/detail?userId=${userId}`)
  },
  // 删除收藏的歌曲
  deleteCollection (songId) {
    return axios.get(`${host}/collection/delete?songId=${songId}`)
  }
}
