<template>
  <div class="container">
    <div class='personal-data'>
      <h2 class='title'>编辑个人资料</h2>
      <div class='personal'>
        <el-form :model='registerForm' label-width='80px'>
          <el-form-item prop='username' label='用户名'>
            <el-input v-model='registerForm.username' placeholder='用户名'></el-input>
          </el-form-item>
          <el-form-item prop='password' label='密码'>
            <el-input type='password' placeholder='密码' v-model='registerForm.password'></el-input>
          </el-form-item>
          <el-form-item label='性别'>
            <el-radio-group v-model='registerForm.sex'>
              <el-radio :label='0'>女</el-radio>
              <el-radio :label='1'>男</el-radio>
              <el-radio :label='2'>保密</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop='phoneNum' label='手机'>
            <el-input  placeholder='手机' v-model='registerForm.phoneNum' ></el-input>
          </el-form-item>
          <el-form-item prop='email' label='邮箱'>
            <el-input v-model='registerForm.email' placeholder='邮箱'></el-input>
          </el-form-item>
          <el-form-item prop='birth' label='生日'>
            <el-date-picker type='date' placeholder='选择日期' v-model='registerForm.birth' style='width: 100%;'></el-date-picker>
          </el-form-item>
          <el-form-item prop='introduction' label='签名'>
            <el-input  type='textarea' placeholder='签名' v-model='registerForm.introduction' ></el-input>
          </el-form-item>
          <el-form-item prop='location' label='地区'>
            <el-select v-model='registerForm.location' placeholder='地区' style='width:100%'>
              <el-option
                v-for='item in area'
                :key='item.value'
                :label='item.label'
                :value='item.value'>
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div class='btn'>
          <el-button type='primary' @click='saveMsg()'>保存</el-button>
          <el-button @click='goBack(-1)'>取消</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import mixin from '@/mixins'
import { AREA } from '@/enums'
import { HttpManager } from '@/api'
import { getDateTime } from '@/utils'

export default {
  name: 'PersonalData',
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
        location: '',
        userPic: ''
      },
      area: AREA
    }
  },
  mounted () {
    this.getUserInfo(this.userId)
  },
  methods: {
    getUserInfo (id) {
      HttpManager.getUserOfId(id)
        .then(res => {
          this.registerForm.username = res[0].username
          this.registerForm.password = res[0].password
          this.registerForm.sex = res[0].sex
          this.registerForm.phoneNum = res[0].phoneNum
          this.registerForm.email = res[0].email
          this.registerForm.birth = res[0].birth
          this.registerForm.introduction = res[0].introduction
          this.registerForm.location = res[0].location
          this.registerForm.userPic = res[0].avator
        })
        .catch(err => {
          console.error(err)
        })
    },
    saveMsg () {
      const params = new URLSearchParams()
      params.append('id', this.userId)
      params.append('username', this.registerForm.username)
      params.append('password', this.registerForm.password)
      params.append('sex', this.registerForm.sex)
      params.append('phone_num', this.registerForm.phoneNum)
      params.append('email', this.registerForm.email)
      params.append('birth', getDateTime(this.registerForm.birth))
      params.append('introduction', this.registerForm.introduction)
      params.append('location', this.registerForm.location)
      HttpManager.updateUserMsg(params)
        .then(res => {
          if (res.code === 1) {
            this.$store.commit('setUsername', this.registerForm.username)
            this.$notify.success({
              title: '编辑成功',
              showClose: true
            })
            setTimeout(() => {
              this.goBack(-1)
            }, 2000)
          } else {
            this.$notify.error({
              title: '编辑失败',
              showClose: true
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

<style lang='scss' scoped>
@import "@/assets/css/var.scss";

.container {
  padding-top: $header-height + 30px;
}

.personal-data {
  background-color: white;
  margin: 0 20%;
  overflow: hidden;
  border-radius: 12px;

  .title {
    padding-top: 30px;
    margin-left: 50px;
    display: inline-block;
  }

  .personal {
    background-color: white;
    padding: 40px 70px 20px 40px;
  }

  .btn {
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>
