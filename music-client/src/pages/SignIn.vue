<template>
  <div class="sign-in">
    <yin-login-logo></yin-login-logo>
    <div class="login">
      <div class="login-head">
        <span>帐号登录</span>
      </div>
      <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm" class="demo-ruleForm" >
        <el-form-item prop="username">
          <el-input placeholder="用户名" v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="密码" v-model="loginForm.password" @keyup.enter.native="token"></el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button @click="handleSignUp">注册</el-button>
          <el-button type="primary" @click="handleLoginIn">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import mixin from '../mixins'
import YinLoginLogo from '../components/layouts/YinLoginLogo'
import { HttpManager } from '../api'
import { NAV_NAME, HOME, SIGN_UP } from '../enums'

export default {
  name: 'SignIn',
  mixins: [mixin],
  components: {
    YinLoginLogo
  },
  data () {
    let validateName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('用户名不能为空'))
      } else {
        callback()
      }
    }
    let validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
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
  mounted () {
    this.changeIndex(NAV_NAME.SIGN_IN)
  },
  methods: {
    handleLoginIn () {
      // 获取登录信息
      let params = new URLSearchParams()
      params.append('username', this.loginForm.username)
      params.append('password', this.loginForm.password)
      HttpManager.signIn(params)
        .then(res => {
          if (res.code === 1) {
            this.$message({
              message: '登录成功',
              type: 'success'
            })
            this.setUserInfo(res.userMsg[0])
            setTimeout(() => {
              this.changeIndex(NAV_NAME.HOME)
              this.routerManager(HOME, { path: HOME })
            }, 2000)
          } else {
            this.$notify({
              title: '用户名或密码错误',
              type: 'error'
            })
          }
        })
        .catch(error => {
          console.error(error)
        })
    },
    setUserInfo (item) {
      this.$store.commit('setUserId', item.id)
      this.$store.commit('setUsername', item.username)
      this.$store.commit('setUserPic', item.avator)
      this.$store.commit('setToken', true)
    },
    handleSignUp () {
      this.routerManager(SIGN_UP, { path: SIGN_UP })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/css/sign-in.scss';
</style>
