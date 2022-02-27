<template>
  <yin-login-logo></yin-login-logo>
  <div class='sign-in'>
    <div class='sign-in-head'>
      <span>帐号登录</span>
    </div>
    <el-form status-icon :model='loginForm' :rules='rules'>
      <el-form-item prop='username'>
        <el-input placeholder='用户名' size="large" v-model='loginForm.username'></el-input>
      </el-form-item>
      <el-form-item prop='password'>
        <el-input type='password' size="large" placeholder='密码' v-model='loginForm.password' @keyup.enter='handleLoginIn'></el-input>
      </el-form-item>
      <el-form-item class='sign-in-btn'>
        <el-button size="large" @click="handleSignUp">注册</el-button>
        <el-button type="primary" size="large" @click='handleLoginIn'>登录</el-button>
      </el-form-item>
    </el-form>
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
    const validateName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('用户名不能为空'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
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
      const params = new URLSearchParams()
      params.append('username', this.loginForm.username)
      params.append('password', this.loginForm.password)
      HttpManager.signIn(params)
        .then(res => {
          if (res.code != null) {
            this.$message({
              message: res.msg,
              type: res.type
            })
            this.setUserInfo(res.userMsg[0])
            setTimeout(() => {
              if (res.success) {
                this.changeIndex(NAV_NAME.HOME)
                this.routerManager(HOME, { path: HOME })
              }
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

<style lang='scss' scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.login-logo {
  background-color: $color-blue-light;
  height: 100vh;
  width: 50vw;
  min-width: 650px;
  overflow: hidden;
  @include layout(center, center);
  .icon {
    @include icon(6.5em, $color-blue-dark);
    transform: rotate(-30deg);
  }
}

.sign-in {
  position: absolute;
  left: 60vw;
  width: 300px;
  top: $header-height + 60px;
  padding: 30px 50px;
  border-radius: 10px;
  background-color: $color-white;

  .sign-in-head {
    text-align: center;
    margin-bottom: 10px;
    font-size: 20px;
    font-weight: 600;
  }

  .sign-in-btn:deep(.el-form-item__content){
    @include layout(space-between);
    button {
      width: 50%;
    }
  }
}

</style>
