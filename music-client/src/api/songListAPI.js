import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  /**
   * @msg: 获取全部歌单
   * @return: {Promise}
   */
  getSongList () {
    return axios.get(`${host}/songList`)
  },

  /**
   * @msg: 获取歌单类型
   * @param {String} style
   * @return: {Promise}
   */
  getSongListOfStyle (style) {
    return axios.get(`${host}/songList/style/detail?style=${style}`)
  },

  /**
   * @msg: 返回标题包含文字的歌单
   * @param {String} keywords
   * @return: {Promise}
   */
  getSongListOfLikeTitle (keywords) {
    return axios.get(`${host}/songList/likeTitle/detail?title=${keywords}`)
  }
}
