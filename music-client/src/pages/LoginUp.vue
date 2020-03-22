<template>
<div class="loginUp-page">
  <loginLogo/>
  <div class="loginUp">
    <div class="loginUp-head">
      <span>用户注册</span>
    </div>
    <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" label-width="80px" class="demo-ruleForm">
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
      <div class="login-btn">
        <el-button @click="goback(-1)">取消</el-button>
        <el-button type="primary" @click="loginUp">确定</el-button>
      </div>
    </el-form>
  </div>
</div>
</template>

<script>
import loginLogo from '../components/LoginLogo'
import { mixin } from '../mixins'
import { rules, cities } from '../assets/data/form'

export default {
  name: 'loginUp-page',
  components: {
    loginLogo
  },
  mixins: [mixin],
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
      rules: {},
      cities: []
    }
  },
  created () {
    this.rules = rules
    this.cities = cities
  },
  methods: {
    loginUp () {
      let _this = this
      let d = this.registerForm.birth
      var datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      this.$api.userAPI.loginUp(
        this.registerForm.username,
        this.registerForm.password,
        this.registerForm.sex,
        this.registerForm.phoneNum,
        this.registerForm.email,
        datetime,
        this.registerForm.introduction,
        this.registerForm.location,
        '/img/user.jpg')
        .then(res => {
          console.log(res.data)
          if (res.data.code === 1) {
            _this.notify('注册成功', 'success')
            setTimeout(function () {
              _this.$router.push({path: '/'})
            }, 2000)
          } else {
            _this.notify('注册失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    goback (index) {
      this.$router.go(index)
    }
  }
}
</script>

<style scoped>
.loginUp{
  position: absolute;
  top:150px;
  background-color: white;
  border-radius: 10px;
  width: 350px;
  margin-left: 750px;
  padding: 30px 30px;
}

.loginUp-head {
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

.local {
  position: absolute;
  width: 270px;
  top: 550px;
  margin-left: 80px;
}

</style>
