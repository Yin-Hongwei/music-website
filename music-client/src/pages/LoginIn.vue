<template>
  <div class="login-in">
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
          <el-input type="password" placeholder="密码" v-model="loginForm.password" @keyup.enter.native="loginIn"></el-input>
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
import YinLoginLogo from '../components/common/YinLoginLogo'
import { HttpManager } from '../api/index'

export default {
  name: 'LoginIn',
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
    this.changeIndex('登录')
  },
  methods: {
    changeIndex (value) {
      this.$store.commit('setActiveName', value)
    },
    handleLoginIn () {
      let params = new URLSearchParams()
      params.append('username', this.loginForm.username)
      params.append('password', this.loginForm.password)
      HttpManager.loginIn(params)
        .then(res => {
          // console.log('-----------获取登录信息---------------')
          if (res.code === 1) {
            this.$message({
              message: '登录成功',
              type: 'success'
            })
            this.setUserMsg(res.userMsg[0])
            this.$store.commit('setLoginIn', true)
            setTimeout(() => {
              this.changeIndex('首页')
              this.$router.push({path: '/'})
              this.$router.go(0)
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
    setUserMsg (item) {
      this.$store.commit('setUserId', item.id)
      this.$store.commit('setUsername', item.username)
      this.$store.commit('setAvator', item.avator)
    },
    handleSignUp () {
      this.$router.push({path: '/sign-up'})
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/login-in.scss';
</style>
