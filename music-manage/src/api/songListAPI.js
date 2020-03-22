import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  // 添加歌单
  setSongList (title, pic, introduction, style) {
    let params = new URLSearchParams()
    params.append('title', title)
    params.append('pic', pic)
    params.append('introduction', introduction)
    params.append('style', style)
    return axios.post(`${host}/songList/add`, params)
  },
  /**
   * @msg: 获取全部歌单
   * @return: {Promise}
   */
  getSongList () {
    return axios.get(`${host}/songList`)
  },
  // 更新歌单信息
  updateSongListMsg (id, title, pic, introduction, style) {
    var params = new URLSearchParams()
    params.append('id', id)
    params.append('title', title)
    params.append('pic', pic)
    params.append('introduction', introduction)
    params.append('style', style)
    return axios.post(`${host}/songList/update`, params)
  },
  // 删除歌单
  deleteSongList (id) {
    return axios.get(`${host}/songList/delete?id=${id}`)
  }
}
