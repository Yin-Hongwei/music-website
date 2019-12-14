<template>
<div class="register-page">
  <loginLogo/>
  <div class="register">
    <div class="register-head">
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
        <el-select v-model="registerForm.location" placeholder="地区">
          <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <div class="login-btn">
        <el-button @click="goback(-1)">取消</el-button>
        <el-button type="primary" @click="register">确定</el-button>
      </div>
    </el-form>
  </div>
</div>
</template>

<script>
import axios from 'axios'
import loginLogo from '../components/LoginLogo'

export default {
  name: 'register-page',
  components: {
    loginLogo
  },
  data: function () {
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
      rules: {
        username: [
          { required: true, trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        phoneNum: [
          { essage: '请选择日期', trigger: 'blur' }
        ],
        email: [
          { message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        birth: [
          { required: true, type: 'date', message: '请选择日期', trigger: 'change' }
        ],
        introduction: [
          { message: '请输入介绍', trigger: 'blur' }
        ],
        location: [
          { message: '请输入地区', trigger: 'change' }
        ]
      },
      cities: [{
        value: '北京',
        label: '北京'
      }, {
        value: '天津',
        label: '天津'
      }, {
        value: '河北',
        label: '河北'
      }, {
        value: '山西',
        label: '山西'
      }, {
        value: '内蒙古',
        label: '内蒙古'
      }, {
        value: '辽宁',
        label: '辽宁'
      }, {
        value: '吉林',
        label: '吉林'
      }, {
        value: '黑龙江',
        label: '黑龙江'
      }, {
        value: '上海',
        label: '上海'
      }, {
        value: '江苏',
        label: '江苏'
      }, {
        value: '浙江',
        label: '浙江'
      }, {
        value: '安徽',
        label: '安徽'
      }, {
        value: '福建',
        label: '福建'
      }, {
        value: '江西',
        label: '江西'
      }, {
        value: '山东',
        label: '山东'
      }, {
        value: '河南',
        label: '河南'
      }, {
        value: '湖北',
        label: '湖北'
      }, {
        value: '湖南',
        label: '湖南'
      }, {
        value: '广东',
        label: '广东'
      }, {
        value: '广西',
        label: '广西'
      }, {
        value: '海南',
        label: '海南'
      }, {
        value: '重庆',
        label: '重庆'
      }, {
        value: '四川',
        label: '四川'
      }, {
        value: '贵州',
        label: '贵州'
      }, {
        value: '云南',
        label: '云南'
      }, {
        value: '西藏',
        label: '西藏'
      }, {
        value: '陕西',
        label: '陕西'
      }, {
        value: '甘肃',
        label: '甘肃'
      }, {
        value: '青海',
        label: '青海'
      }, {
        value: '宁夏',
        label: '宁夏'
      }, {
        value: '新疆',
        label: '新疆'
      }, {
        value: '香港',
        label: '香港'
      }, {
        value: '澳门',
        label: '澳门'
      }, {
        value: '台湾',
        label: '台湾'
      }]
    }
  },
  methods: {
    register () {
      let _this = this
      let d = _this.registerForm.birth
      var datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      var params = new URLSearchParams()
      params.append('username', _this.registerForm.username)
      params.append('password', _this.registerForm.password)
      params.append('sex', _this.registerForm.sex)
      params.append('phone_num', _this.registerForm.phoneNum)
      params.append('email', _this.registerForm.email)
      params.append('birth', datetime)
      params.append('introduction', _this.registerForm.introduction)
      params.append('location', _this.registerForm.location)
      params.append('avator', '/img/user.jpg')
      axios.post(`${_this.$store.state.HOST}/api/signup`, params)
        .then(response => {
          if (response.data.code === 1) {
            _this.$notify({
              title: '注册成功',
              type: 'success'
            })
            setTimeout(function () {
              _this.$router.push({path: '/'})
            }, 2000)
          } else {
            _this.$notify({
              title: '注册失败',
              type: 'error'
            })
          }
        })
        .catch(failResponse => {})
    },
    goback (index) {
      this.$router.go(index)
    }
  }
}
</script>

<style scoped>
.register{
  position: absolute;
  top:150px;
  background-color: white;
  border-radius: 10px;
  width: 350px;
  height: 580px;
  margin-left: 750px;
  padding: 30px 30px;
}

.register-head {
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
