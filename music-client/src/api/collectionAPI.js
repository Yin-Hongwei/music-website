import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  /**
   * @msg: 添加收藏的歌曲
   * @param {Number} userId
   * @param {String} type 0 代表歌曲， 1 代表歌单
   * @param {Number} songId
   * @return: {Promise}
   */
  setCollection (userId, type, songId) {
    var params = new URLSearchParams()
    params.append('userId', userId)
    params.append('type', type)
    params.append('songId', songId || '')
    return axios.post(`${host}/collection/add`, params)
  },
  /**
   * @msg: 返回的指定用户ID的收藏列表
   * @param {Number} userId
   * @return: {Promise}
   */
  getCollectionOfUser (userId) {
    return axios.get(`${host}/collection/detail?userId=${userId}`)
  }
}
