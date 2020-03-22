import axios from 'axios'

// this.$store.state.configure.HOST
const host = 'http://localhost:8888'
export default {
  /**
   * @msg: 注册
   * @param {String} username
   * @param {String} password
   * @param {String} sex
   * @param {Number} phoneNum
   * @param {String} email
   * @param {String} datetime
   * @param {String} introduction
   * @param {String} location
   * @return: {Promise}
   */
  loginUp (username, password, sex, phoneNum, email, datetime, introduction, location) {
    var params = new URLSearchParams()
    params.append('username', username)
    params.append('password', password)
    params.append('sex', sex)
    params.append('phone_num', phoneNum)
    params.append('email', email)
    params.append('birth', datetime)
    params.append('introduction', introduction)
    params.append('location', location)
    params.append('avator', '/img/user.jpg')
    return axios.post(`${host}/user/add`, params)
  },
  // 返回所有用户
  getAllUser () {
    return axios.get(`${host}/user`)
  },
  /**
   * @msg: 返回指定ID的用户
   * @param {Number} id
   * @return: {Promise}
   */
  getUserOfId (id) {
    return axios.get(`${host}/user/detail?id=${id}`)
  },
  /**
   * @msg: 更新用户信息
   * @param {Number} userId
   * @return: {Promise}
   */
  updateUserMsg (userId, username, password, sex, phoneNum, email, datetime, introduction, location) {
    var params = new URLSearchParams()
    params.append('id', userId)
    params.append('username', username)
    params.append('password', password)
    params.append('sex', sex)
    params.append('phone_num', phoneNum)
    params.append('email', email)
    params.append('birth', datetime)
    params.append('introduction', introduction)
    params.append('location', location)
    return axios.post(`${host}/user/update`, params)
  },

  // 删除用户
  deleteUser (id) {
    return axios.get(`${host}/user/delete?id=${id}`)
  }
}
