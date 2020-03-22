import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  /**
   * @msg: 添加评论
   * @param {String} type
   * @param {Number} id
   * @param {Number} userId
   * @param {String} content
   * @return: {Promise}
   */
  setComment (type, id, userId, content) {
    // 0 代表歌曲， 1 代表歌单
    var params = new URLSearchParams()
    if (type === 1) {
      params.append('songListId', id)
    } else if (type === 0) {
      params.append('songId', id)
    }
    params.append('userId', userId)
    params.append('type', type)
    params.append('content', content)
    return axios.post(`${host}/comment/add`, params)
  },

  /**
   * @msg: 点赞
   * @param {Number} id
   * @param {Number} up
   * @return: {Promise}
   */
  setLike (id, up) {
    var params = new URLSearchParams()
    params.append('id', id)
    params.append('up', up + 1)
    return axios.post(`${host}/comment/like`, params)
  },

  /**
   * @msg: 返回所有评论
   * @param {String} type
   * @param {Number} id
   * @return: {Promise}
   */
  getAllComment (type, id) {
    let url = ''
    if (type === 1) {
      url = '/comment/songList/detail?songListId='
    } else if (type === 0) {
      url = '/comment/song/detail?songId='
    }
    return axios.get(host + url + id)
  }
}
