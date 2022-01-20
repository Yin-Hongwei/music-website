<template>
<div>
  <yin-login-logo></yin-login-logo>
  <div class="sign-up">
    <div class="sign-up-head">
      <span>用户注册</span>
    </div>
    <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" label-width="70px" class="demo-ruleForm">
      <el-form-item prop="username" label="用户名">
        <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password" label="密码">
        <el-input type="password" placeholder="密码" v-model="registerForm.password"></el-input>
      </el-form-item>
      <el-form-item prop="sex" label="性别">
        <el-radio-group v-model="registerForm.sex">
          <el-radio :label="0">女</el-radio>
          <el-radio :label="1">男</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="phoneNum" label="手机" >
        <el-input  placeholder="手机" v-model="registerForm.phoneNum"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
      </el-form-item>
      <el-form-item prop="birth" label="生日">
        <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%;"></el-date-picker>
      </el-form-item>
      <el-form-item prop="introduction" label="签名">
        <el-input  type="textarea" placeholder="签名" v-model="registerForm.introduction" ></el-input>
      </el-form-item>
      <el-form-item prop="location" label="地区">
        <el-select v-model="registerForm.location" placeholder="地区" style="width:100%">
          <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <div class="sign-up-btn">
        <el-button @click="goback">取消</el-button>
        <el-button type="primary" @click="handleSignUp">确定</el-button>
      </div>
    </el-form>
  </div>
</div>
</template>

<script>
import mixin from '../mixins'
import YinLoginLogo from '../components/common/YinLoginLogo'
import { rules, cities } from '../assets/data/form'
import { HttpManager } from '../api/index'

export default {
  name: 'SignUp',
  mixins: [mixin],
  components: {
    YinLoginLogo
  },
  data () {
    return {
      registerForm: { // 注册
        username: '',
        password: '',
        sex: '',
        phoneNum: '',
        email: '',
        birth: '',
        introduction: '',
        location: ''
      },
      rules: rules,
      cities: cities
    }
  },
  methods: {
    handleSignUp () {
      const params = new URLSearchParams()
      params.append('password', this.registerForm.password)
      params.append('sex', this.registerForm.sex)
      params.append('phone_num', this.registerForm.phoneNum)
      params.append('email', this.registerForm.email)
      params.append('birth', this.getDateTime())
      params.append('introduction', this.registerForm.introduction)
      params.append('location', this.registerForm.location)
      params.append('avator', '/img/user.jpg')
      HttpManager.handleSignUp(params)
        .then(res => {
          if (res.code === 1) {
            this.$notify({
              title: '注册成功',
              type: 'success'
            })
            setTimeout(() => {
              this.$router.push({path: '/'})
            }, 2000)
          } else {
            this.$notify({
              title: '注册失败',
              type: 'error'
            })
          }
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/sign-up.scss';
</style>
