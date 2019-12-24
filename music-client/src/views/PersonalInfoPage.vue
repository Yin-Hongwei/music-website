<template>
  <div>
    <div class="my-bg"></div>
    <div class="my-body">
      <div class="content">
        <p class="title">编辑个人资料</p>
        <hr/>
        <div class="personal">
          <el-form :model="registerForm" class="demo-ruleForm" label-width="80px">
            <el-form-item prop="username" label="用户名">
              <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password" label="密码">
              <el-input type="password" placeholder="密码" v-model="registerForm.password"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="registerForm.sex">
                <el-radio :label="0">女</el-radio>
                <el-radio :label="1">男</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item prop="phoneNum" label="手机">
              <el-input  placeholder="手机" v-model="registerForm.phoneNum" ></el-input>
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
                <el-option
                  v-for="item in cities"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        <div class="btn">
          <div @click="saveMsg()">保存</div>
          <div @click="goback">取消</div>
        </div>
        </div>
      </div>
    </div>
</div>
</template>

<script>
import axios from 'axios'
import { mapGetters } from 'vuex'

export default {
  name: 'personal',
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
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  mounted () {
    this.getMsg(this.userId)
  },
  methods: {
    getMsg (id) {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/commentOfConsumer?id=${id}`)
        .then(response => {
          _this.registerForm.username = response.data[0].username
          _this.registerForm.password = response.data[0].password
          _this.registerForm.sex = response.data[0].sex
          _this.registerForm.phoneNum = response.data[0].phoneNum
          _this.registerForm.email = response.data[0].email
          _this.registerForm.birth = response.data[0].birth
          _this.registerForm.introduction = response.data[0].introduction
          _this.registerForm.location = response.data[0].location
          _this.registerForm.avator = response.data[0].avator
        })
    },
    goback () {
      this.$router.go(-1)
    },
    saveMsg () {
      let _this = this
      let d = new Date(_this.registerForm.birth)
      var datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      var params = new URLSearchParams()
      params.append('id', _this.userId)
      params.append('username', _this.registerForm.username)
      params.append('password', _this.registerForm.password)
      params.append('sex', _this.registerForm.sex)
      params.append('phone_num', _this.registerForm.phoneNum)
      params.append('email', _this.registerForm.email)
      params.append('birth', datetime)
      params.append('introduction', _this.registerForm.introduction)
      params.append('location', _this.registerForm.location)
      params.append('avator', '/img/user.jpg')
      axios.post(`${_this.$store.state.HOST}/api/updateUserMsgs`, params)
        .then(res => {
          if (res.data.code === 1) {
            _this.showError = false
            _this.showSuccess = true
            this.$store.commit('setUsername', _this.registerForm.username)
            window.localStorage.setItem('username', JSON.stringify(_this.registerForm.username))
            this.$notify.success({
              title: '编辑成功',
              showClose: true
            })
            setTimeout(function () {
              _this.$router.go(-1)
            }, 2000)
          } else {
            _this.showSuccess = false
            _this.showError = true
            this.$notify.error({
              title: '编辑失败',
              showClose: true
            })
          }
        })
        .catch(failResponse => {})
    }
  }
}
</script>

<style scoped>
.my-bg {
  width: 100%;
  height: 100px;
  background-color: #93d2f8;
}

.my-body{
  position: relative;
  padding: 0 100px;
}

.content {
  width: 60%;
  margin:50px auto;
  background-color: white;
  border-radius: 10px;
  padding-bottom: 30px;
}

.content p {
  height: 50px;
  line-height: 50px;
  padding-left: 20px;
  font-size: 20px;
  font-weight: 600;
  color: black;
}

.personal {
  padding-right: 50px;
  padding-top: 40px;
}

.btn {
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.btn div {
  display: inline-block;
  width: 100px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  background-color: #30a4fc;
  margin: 0 20px;
  color: white;
}
</style>
