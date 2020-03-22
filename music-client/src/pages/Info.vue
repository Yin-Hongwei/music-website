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
              <el-select v-model="registerForm.location" placeholder="地区" style="width:100%">
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
import { mapGetters } from 'vuex'
import { cities } from '../assets/data/form'

export default {
  name: 'info',
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
      cities: []
    }
  },
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  created () {
    this.cities = cities
  },
  mounted () {
    this.getMsg(this.userId)
  },
  methods: {
    getMsg (id) {
      this.$api.userAPI.getUserOfId(id)
        .then(res => {
          this.registerForm.username = res.data[0].username
          this.registerForm.password = res.data[0].password
          this.registerForm.sex = res.data[0].sex
          this.registerForm.phoneNum = res.data[0].phoneNum
          this.registerForm.email = res.data[0].email
          this.registerForm.birth = res.data[0].birth
          this.registerForm.introduction = res.data[0].introduction
          this.registerForm.location = res.data[0].location
          this.registerForm.avator = res.data[0].avator
        })
        .catch(err => {
          console.log(err)
        })
    },
    goback () {
      this.$router.go(-1)
    },
    saveMsg () {
      let d = new Date(this.registerForm.birth)
      var datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()

      this.$api.userAPI.updateUserMsg(
        this.userId,
        this.registerForm.username,
        this.registerForm.password,
        this.registerForm.sex,
        this.registerForm.phoneNum,
        this.registerForm.email,
        datetime,
        this.registerForm.introduction,
        this.registerForm.location
      )
        .then(res => {
          if (res.data.code === 1) {
            this.showError = false
            this.showSuccess = true
            this.$store.commit('setUsername', this.registerForm.username)
            this.$notify.success({
              title: '编辑成功',
              showClose: true
            })
            setTimeout(function () {
              this.$router.go(-1)
            }, 2000)
          } else {
            this.showSuccess = false
            this.showError = true
            this.$notify.error({
              title: '编辑失败',
              showClose: true
            })
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
  margin-left: 40px;
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
