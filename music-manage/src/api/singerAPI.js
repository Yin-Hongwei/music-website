import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  // 添加歌手
  setSinger (name, sex, pic, datetime, location, introduction) {
    var params = new URLSearchParams()
    params.append('name', name)
    params.append('sex', sex)
    params.append('pic', pic)
    params.append('birth', datetime)
    params.append('location', location)
    params.append('introduction', introduction)
    return axios.post(`${host}/singer/add`, params)
  },
  /**
   * @msg: 返回所有歌手
   * @return: {Promise}
   */
  getAllSinger () {
    return axios.get(`${host}/singer`)
  },
  // 根据歌手名查找歌手
  getSingerOfName (name) {
    return axios.get(`${host}/singer/name/detail?name=${name}`)
  },
  // 更新歌手信息
  updateSingerMsg (id, name, sex, pic, datetime, location, introduction) {
    let params = new URLSearchParams()
    params.append('id', id)
    params.append('name', name)
    params.append('sex', sex)
    params.append('pic', pic)
    params.append('birth', datetime)
    params.append('location', location)
    params.append('introduction', introduction)
    return axios.post(`${host}/singer/update`, params)
  },
  // 删除歌手
  deleteSinger (id) {
    return axios.get(`${host}/singer/delete?id=${id}`)
  }
}
