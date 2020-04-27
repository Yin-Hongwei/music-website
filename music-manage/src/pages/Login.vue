<template>
  <div class="login-wrap">
    <div class="ms-title">Yin-music 后台管理</div>
    <div class="ms-login">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        class="demo-ruleForm"
      >
        <el-form-item prop="username">
          <el-input v-model="ruleForm.username" placeholder="username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            type="password"
            placeholder="password"
            v-model="ruleForm.password"
            @keyup.enter.native="submitForm('ruleForm')"
          ></el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click="submitForm">登录</el-button>
        </div>
        <p style="font-size:12px;line-height:30px;color:#999;">Tips : 用户名和密码要写数据库里的。</p>
      </el-form>
    </div>
  </div>
</template>

<script>
import {mixin} from '../mixins'
import { getLoginStatus } from '../api/index'

export default {
  mixins: [mixin],
  data: function () {
    return {
      ruleForm: {
        username: 'admin',
        password: '123'
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    submitForm () {
      let params = new URLSearchParams()
      params.append('name', this.ruleForm.username)
      params.append('password', this.ruleForm.password)
      getLoginStatus(params)
        .then(res => {
          if (res.code === 1) {
            this.$router.push('/Info')
            this.notify('欢迎回来', 'success')
          } else {
            this.notify('登录失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>

<style scoped>
.login-wrap {
  position: relative;
  background: url('../assets/img/background.jpg');
  background-attachment: fixed;
  background-position: center;
  background-size: cover;
  width: 100%;
  height: 100%;
}
.ms-title {
  position: absolute;
  top: 50%;
  width: 100%;
  margin-top: -230px;
  text-align: center;
  font-size: 30px;
  font-weight: 600;
  color: #fff;
}
.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 300px;
  height: 160px;
  margin: -150px 0 0 -190px;
  padding: 40px;
  border-radius: 5px;
  background: #fff;
}
.login-btn {
  text-align: center;
}
.login-btn button {
  width: 100%;
  height: 36px;
}
</style>
