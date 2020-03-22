import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  // 是否登录成功
  getLoginStatus (username, password) {
    let params = new URLSearchParams()
    params.append('name', username)
    params.append('password', password)
    return axios.post(`${host}/admin/login/status`, params)
  }
}
