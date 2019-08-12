<template>
<div class="sign">
  <loginLogo/>
  <div class="login">
    <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm" label-width="0px" class="demo-ruleForm" >
      <el-form-item prop="username" label="用户名">
        <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password" label="密码">
        <el-input type="password" placeholder="密码" v-model="loginForm.password" @keyup.enter.native="login"></el-input>
      </el-form-item>
      <el-alert
        v-show="showSuccess"
        class="local"
        title="登录成功"
        type="success"
        center
        show-icon>
      </el-alert>
      <el-alert
        v-show="showError"
        class="local"
        title="用户名或密码错误"
        type="error"
        center
        show-icon>
      </el-alert>
      <div class="login-btn">
        <el-button type="primary" @click="login">登录</el-button>
        <el-button type="primary" @click="goRegister()">注册</el-button>
      </div>
    </el-form>
  </div>
</div>
</template>

<script>
import axios from 'axios'
import loginLogo from '../components/loginLogo'
export default {
  name: 'login',
  data: function () {
    var validateName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('用户名不能为空'))
      } else {
        callback()
      }
    }
    var validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      showSuccess: false, // 提示成功
      showError: false, // 提示失败
      loginForm: { // 登录用户名密码
        username: '',
        password: ''
      },
      rules: {
        username: [
          { validator: validateName, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { validator: validatePassword, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  components: {
    loginLogo
  },
  mounted: function () {
    this.getSongLists()
    this.getSingerLists()
  },
  methods: {
    login () {
      let _this = this
      var params = new URLSearchParams()
      params.append('username', _this.loginForm.username)
      params.append('password', _this.loginForm.password)
      console.log(params)
      axios.post(_this.$store.state.HOST + '/api/loginVerify', params)
        .then(response => {
          // console.log('-----------获取登录信息---------------')
          // console.log(response.data)
          if (response.data.code === 1) {
            _this.showError = false
            _this.showSuccess = true
            _this.copyMsg(response.data.userMsg[0])
            _this.$store.commit('setLoginIn', true)
            window.sessionStorage.setItem('loginIn', JSON.stringify(true))
            setTimeout(function () {
              _this.$store.commit('setHheadIndex', 0)
              window.sessionStorage.setItem('headIndex', JSON.stringify(0))
              _this.$router.push({path: '/homePage'})
              _this.$router.go(0)
            }, 2000)
          } else {
            _this.showSuccess = false
            _this.showError = true
          }
        })
        .catch(failResponse => {})
    },
    copyMsg (item) {
      this.$store.commit('setUserId', item.id)
      window.localStorage.setItem('userId', JSON.stringify(item.id))
      this.$store.commit('setUsername', item.username)
      window.localStorage.setItem('username', JSON.stringify(item.username))
      this.$store.commit('setAvator', item.avator)
      window.localStorage.setItem('avator', JSON.stringify(item.avator))
      console.log(item.avator)
    },
    goRegister () {
      this.$router.push({path: '/register'})
    },
    getSongLists () {
      let _this = this
      axios.get(_this.$store.state.HOST + '/listSongLists')
        .then(function (response) {
          _this.$store.commit('setSongsList', response.data)
          window.sessionStorage.setItem('songsList', JSON.stringify(response.data))
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    getSingerLists () {
      let _this = this
      axios.get(_this.$store.state.HOST + '/listSingers')
        .then(function (response) {
          _this.$store.commit('setSingersList', response.data)
          window.sessionStorage.setItem('singersList', JSON.stringify(response.data))
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  }
}
</script>

<style scoped>

.login{
  position: absolute;
  margin-left: 800px;
  top:150px;
  padding: 50px 50px 50px 30px;
  width: 300px;
  background-color: white;
  height: 210px;
  border-radius: 10px;
}
.login-btn {
  display: flex;
  justify-content: space-between;
}
.login-btn button{
  width: 100%;
  height:36px;
  margin-top: 50px;
  margin-left: 20px;
}
.local {
  position: absolute;
  width: 280px;
  margin-left: 20px;
  top: 170px;
}
</style>
