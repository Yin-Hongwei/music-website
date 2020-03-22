import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  /**
   * @msg: 返回所有歌手
   * @return: {Promise}
   */
  getAllSinger () {
    return axios.get(`${host}/singer`)
  },

  /**
   * @msg: 通过性别对歌手分类
   * @param {String} sex
   * @return: {Promise}
   */
  getSingerOfSex (sex) {
    return axios.get(`${host}/singer/sex/detail?sex=${sex}`)
  }
}
