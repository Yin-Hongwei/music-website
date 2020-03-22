import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  // 给歌单添加歌曲
  setListSong (songId, songListId) {
    let params = new URLSearchParams()
    params.append('songId', songId)
    params.append('songListId', songListId)
    return axios.post(`${host}/ListSong/add`, params)
  },
  /**
   * @msg: 返回歌单里指定歌单ID的歌曲
   * @param {Number} songListId
   * @return: {Promise}
   */
  getListSongOfSongId (songListId) {
    return axios.get(`${host}/listSong/detail?songListId=${songListId}`)
  },
  // 删除歌单里的歌曲
  deleteListSong (songId) {
    return axios.get(`${host}/ListSong/delete?songId=${songId}`)
  }
}
