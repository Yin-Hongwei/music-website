import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  // 返回所有歌曲
  getAllSong () {
    return axios.get(`${host}/song`)
  },

  /**
   * @msg: 返回指定歌手ID的歌曲
   * @param {Number} id
   * @return: {Promise}
   */
  getSongOfSingerId (id) {
    return axios.get(`${host}/song/singer/detail?singerId=${id}`)
  },

  // 返回的指定用户ID收藏列表
  getSongOfId (id) {
    return axios.get(`${host}/song/detail?id=${id}`)
  },
  // 返回指定歌手名的歌曲
  getSongOfSingerName (id) {
    return axios.get(`${host}/song/singerName/detail?name=${id}`)
  },
  // 更新歌曲信息
  updateSongMsg (id, singerId, name, introduction, lyric) {
    var params = new URLSearchParams()
    params.append('id', id)
    params.append('singerId', singerId)
    params.append('name', name)
    params.append('introduction', introduction)
    params.append('lyric', lyric)
    return axios.post(`${host}/song/update`, params)
  },
  // 删除歌曲
  deleteSong (id) {
    return axios.get(`${host}/song/delete?id=${id}`)
  }
}
