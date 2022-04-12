<template>
  <yin-login-logo></yin-login-logo>
  <div class="sign-up">
    <div class="sign-up-head">
      <span>用户注册</span>
    </div>
    <el-form ref="signUpForm" label-width="70px" status-icon :model="registerForm" :rules="SignUpRules">
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
          <el-radio :label="2">保密</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="phoneNum" label="手机">
        <el-input placeholder="手机" v-model="registerForm.phoneNum"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
      </el-form-item>
      <el-form-item prop="birth" label="生日">
        <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%"></el-date-picker>
      </el-form-item>
      <el-form-item prop="introduction" label="签名">
        <el-input type="textarea" placeholder="签名" v-model="registerForm.introduction"></el-input>
      </el-form-item>
      <el-form-item prop="location" label="地区">
        <el-select v-model="registerForm.location" placeholder="地区" style="width: 100%">
          <el-option v-for="item in AREA" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="sign-up-btn">
        <el-button @click="goBack()">登录</el-button>
        <el-button type="primary" @click="handleSignUp(formRef)">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, getCurrentInstance } from "vue";
import mixin from "@/mixins/mixin";
import YinLoginLogo from "@/components/layouts/YinLoginLogo.vue";
import { HttpManager } from "@/api";
import { getBirth } from "@/utils";
import { AREA, RouterName, NavName, SignUpRules } from "@/enums";

export default defineComponent({
  components: {
    YinLoginLogo,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const { routerManager, changeIndex } = mixin();

    const registerForm = reactive({
      username: "",
      password: "",
      sex: "",
      phoneNum: "",
      email: "",
      birth: new Date(),
      introduction: "",
      location: "",
    });

    async function handleSignUp() {
      let canRun = true;
      (proxy.$refs["signUpForm"] as any).validate((valid) => {
        if (!valid) return (canRun = false);
      });
      if (!canRun) return;

      const params = new URLSearchParams();
      params.append("username", registerForm.username);
      params.append("password", registerForm.password);
      params.append("sex", registerForm.sex);
      params.append("phone_num", registerForm.phoneNum);
      params.append("email", registerForm.email);
      params.append("birth", getBirth(registerForm.birth));
      params.append("introduction", registerForm.introduction);
      params.append("location", registerForm.location);

      try {
        const result = (await HttpManager.SignUp(params)) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });

        if (result.success) {
          changeIndex(NavName.SignIn);
          routerManager(RouterName.SignIn, { path: RouterName.SignIn });
        }
      } catch (error) {
        console.error(error);
      }
    }

    return {
      SignUpRules,
      AREA,
      registerForm,
      handleSignUp,
    };
  },
});
</script>

<style lang="scss" scoped>
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
      width: 47%;
    }
  }
}
</style>
