<template>
<div class="login-in">
  <login-logo/>
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
        <el-button @click="goLoginUp()">注册</el-button>
        <el-button type="primary" @click="loginIn">登录</el-button>
      </div>
    </el-form>
  </div>
</div>
</template>

<script>
import { mixin } from '../mixins'
import LoginLogo from '../components/LoginLogo'

export default {
  name: 'login-in',
  components: {
    LoginLogo
  },
  mixins: [mixin],
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
    loginIn () {
      let _this = this
      _this.$api.userAPI.loginIn(this.loginForm.username, this.loginForm.password)
        .then(res => {
          // console.log('-----------获取登录信息---------------')
          if (res.data.code === 1) {
            _this.notify('登录成功', 'success')
            _this.setUserMsg(res.data.userMsg[0])
            _this.$store.commit('setLoginIn', true)
            setTimeout(function () {
              _this.changeIndex('首页')
              _this.$router.push({path: '/home'})
              _this.$router.go(0)
            }, 2000)
          } else {
            _this.notify('用户名或密码错误', 'error')
          }
        })
        .catch(failResponse => {})
    },
    setUserMsg (item) {
      this.$store.commit('setUserId', item.id)
      this.$store.commit('setUsername', item.username)
      this.$store.commit('setAvator', item.avator)
    },
    goLoginUp () {
      this.$router.push({path: '/login-up'})
    }
  }
}
</script>

<style scoped>
.login{
  position: absolute;
  margin-left: 800px;
  top: 150px;
  padding: 30px 50px;
  width: 300px;
  background-color: white;
  height: 210px;
  border-radius: 10px;
}

.login-head {
  text-align: center;
  margin-bottom: 10px;
  font-size: 20px;
  font-weight: 600;
}

.login-btn {
  display: flex;
  justify-content: space-between;
}

.login-btn button{
  display: block;
  width: 50%;
}
</style>
