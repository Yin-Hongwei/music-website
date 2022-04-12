<template>
  <yin-login-logo></yin-login-logo>
  <div class="sign-in">
    <div class="sign-in-head">
      <span>帐号登录</span>
    </div>
    <el-form ref="signInForm" status-icon :model="registerForm" :rules="SignInRules">
      <el-form-item prop="username">
        <el-input placeholder="用户名" v-model="registerForm.username"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" placeholder="密码" v-model="registerForm.password" @keyup.enter="handleLoginIn"></el-input>
      </el-form-item>
      <el-form-item class="sign-in-btn">
        <el-button @click="handleSignUp">注册</el-button>
        <el-button type="primary" @click="handleLoginIn">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, getCurrentInstance } from "vue";
import mixin from "@/mixins/mixin";
import YinLoginLogo from "@/components/layouts/YinLoginLogo.vue";
import { HttpManager } from "@/api";
import { NavName, RouterName, SignInRules } from "@/enums";

export default defineComponent({
  components: {
    YinLoginLogo,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const { routerManager, changeIndex } = mixin();

    // 登录用户名密码
    const registerForm = reactive({
      username: "",
      password: "",
    });

    async function handleLoginIn() {
      let canRun = true;
      (proxy.$refs["signInForm"] as any).validate((valid) => {
        if (!valid) return (canRun = false);
      });
      if (!canRun) return;

      const params = new URLSearchParams();
      params.append("username", registerForm.username);
      params.append("password", registerForm.password);

      try {
        const result = (await HttpManager.signIn(params)) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });

        if (result.success) {
          proxy.$store.commit("setUserId", result.data[0].id);
          proxy.$store.commit("setUsername", result.data[0].username);
          proxy.$store.commit("setUserPic", result.data[0].avator);
          proxy.$store.commit("setToken", true);
          changeIndex(NavName.Home);
          routerManager(RouterName.Home, { path: RouterName.Home });
        }
      } catch (error) {
        console.error(error);
      }
    }

    function handleSignUp() {
      routerManager(RouterName.SignUp, { path: RouterName.SignUp });
    }

    return {
      registerForm,
      SignInRules,
      handleLoginIn,
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

  .sign-in {
    width: 300px;
    left: 60vw;
    top: $header-height + 60px;
  }
}

@media screen and (max-width: $sm) {
  .login-logo {
    width: 100vw;
  }
  .sign-in {
    width: 70vw;
    top: $header-height + 20px;
    transform: translateX(1.5vw);
  }
}

.login-logo {
  height: 100vh;
  background-color: $color-blue-light;
  overflow: hidden;
  @include layout(center, center);
  .icon {
    @include icon(6.5em, $color-blue-dark);
    transform: rotate(-30deg);
  }
}

.sign-in {
  position: absolute;
  padding: 30px 50px;
  border-radius: 10px;
  background-color: $color-white;

  .sign-in-head {
    text-align: center;
    margin-bottom: 10px;
    font-size: 20px;
    font-weight: 600;
  }

  .sign-in-btn:deep(.el-form-item__content) {
    @include layout(space-between);
    button {
      width: 50%;
    }
  }
}
</style>
