<template>
  <yin-login-logo></yin-login-logo>
  <div class='sign-up'>
    <div class='sign-up-head'>
      <span>用户注册</span>
    </div>
    <el-form :model='registerForm' status-icon :rules='rules' label-width='70px' >
      <el-form-item prop='username' label='用户名'>
        <el-input v-model='registerForm.username' placeholder='用户名'></el-input>
      </el-form-item>
      <el-form-item prop='password' label='密码'>
        <el-input type='password' placeholder='密码' v-model='registerForm.password'></el-input>
      </el-form-item>
      <el-form-item prop='sex' label='性别'>
        <el-radio-group v-model='registerForm.sex'>
          <el-radio :label='0'>女</el-radio>
          <el-radio :label='1'>男</el-radio>
          <el-radio :label='2'>保密</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop='phoneNum' label='手机'>
        <el-input placeholder='手机' v-model='registerForm.phoneNum'></el-input>
      </el-form-item>
      <el-form-item prop='email' label='邮箱'>
        <el-input v-model='registerForm.email' placeholder='邮箱'></el-input>
      </el-form-item>
      <el-form-item prop='birth' label='生日'>
        <el-date-picker type='date' placeholder='选择日期' v-model='registerForm.birth' style='width: 100%;'></el-date-picker>
      </el-form-item>
      <el-form-item prop='introduction' label='签名'>
        <el-input type='textarea' placeholder='签名' v-model='registerForm.introduction'></el-input>
      </el-form-item>
      <el-form-item prop='location' label='地区'>
        <el-select v-model='registerForm.location' placeholder='地区' style='width:100%'>
          <el-option v-for='item in area' :key='item.value' :label='item.label' :value='item.value'></el-option>
        </el-select>
      </el-form-item>
      <div class='sign-up-btn'>
        <el-button @click='goBack()'>登录</el-button>
        <el-button type='primary' @click='handleSignUp'>确定</el-button>
      </div>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, getCurrentInstance } from "vue";
import mixin from '@/mixins/mixin'
import YinLoginLogo from '@/components/layouts/YinLoginLogo.vue'
import { HttpManager } from '@/api'
import { getDateTime } from '@/utils'
import { RULES, AREA, SIGN_IN, NAV_NAME } from '@/enums'

interface resSignUp {
  title: string,
  type: string,
  msg: string,
  code: string,
  success: boolean,
}

export default defineComponent({
  components: {
    YinLoginLogo
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const { routerManager, changeIndex } = mixin()

    const rules = ref(RULES);
    const area = ref(AREA);
    const registerForm = reactive({
        username: '',
        password: '',
        sex: '',
        phoneNum: '',
        email: '',
        birth: new Date(),
        introduction: '',
        location: ''
    });

    async function handleSignUp () {
      // TODO：这里需要在前端做必填项校验
      const params = new URLSearchParams()
      params.append('username', registerForm.username)
      params.append('password', registerForm.password)
      params.append('sex', registerForm.sex)
      params.append('phone_num', registerForm.phoneNum)
      params.append('email', registerForm.email)
      params.append('birth', getDateTime(registerForm.birth))
      params.append('introduction', registerForm.introduction)
      params.append('location', registerForm.location)
      try {
        const result = await HttpManager.SignUp(params) as resSignUp;
        if (result.code != null) {
          (proxy as any).$notify({
            title: result.msg,
            type: result.type
          })
          setTimeout(() => {
            if (result.success) {
              routerManager(SIGN_IN, { path: SIGN_IN })
              changeIndex(NAV_NAME.SIGN_IN)
            }
          }, 2000)
        } else {
          (proxy as any).$notify({
            title: '注册失败',
            type: 'error'
          })
        }
      } catch (error) {
        console.error(error)
      }
    }

    return {
      rules,
      area,
      registerForm,
      handleSignUp,
    }
  },
})
</script>

<style lang='scss' scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

@media screen and (min-width: $sm) {
  .login-logo {
    width: 50vw;
  }

  .sign-up {
    width: 300px;
    left: 60vw;
    top: $header-height + 60px;
  }
}

@media screen and (max-width: $sm) {
  .login-logo {
    width: 100vw;
  }
  .sign-up {
    width: 70vw;
    top: $header-height + 20px;
    transform: translateX(1.5vw);
  }
}

.sign-up {
  position: absolute;
  padding: 30px 50px;
  border-radius: 10px;
  background-color: $color-white;

  .sign-up-head {
    text-align: center;
    margin-bottom: 10px;
    font-size: 20px;
    font-weight: 600;
  }

  .sign-up-btn {
    @include layout(space-between);
    button {
      display: block;
      width: 50%;
    }
  }
}
</style>
