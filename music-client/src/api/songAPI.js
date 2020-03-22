import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  /**
   * @msg: 返回指定歌曲ID的歌曲
   * @param {Number} id
   * @return: {Promise}
   */
  getSongOfId (id) {
    return axios.get(`${host}/song/detail?id=${id}`)
  },

  /**
   * @msg: 返回指定歌手ID的歌曲
   * @param {Number} id
   * @return: {Promise}
   */
  getSongOfSingerId (id) {
    return axios.get(`${host}/song/singer/detail?singerId=${id}`)
  },

  /**
   * @msg: 返回指定歌手名的歌曲
   * @param {String} keywords
   * @return: {Promise}
   */
  getSongOfSingerName (keywords) {
    return axios.get(`${host}/song/singerName/detail?name=${keywords}`)
  },

  /**
   * @msg: 下载音乐
   * @param {String} url
   * @return: {Promise}
   */
  download (url) {
    return axios({
      method: 'get',
      url: url,
      responseType: 'blob'
    })
  }
}
