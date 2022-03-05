<template>
  <yin-login-logo></yin-login-logo>
  <div class="sign-in">
    <div class="sign-in-head">
      <span>帐号登录</span>
    </div>
    <el-form status-icon :model="loginForm" :rules="rules">
      <el-form-item prop="username">
        <el-input
          placeholder="用户名"
          size="large"
          v-model="loginForm.username"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          type="password"
          size="large"
          placeholder="密码"
          v-model="loginForm.password"
          @keyup.enter="handleLoginIn"
        ></el-input>
      </el-form-item>
      <el-form-item class="sign-in-btn">
        <el-button size="large" @click="handleSignUp">注册</el-button>
        <el-button type="primary" size="large" @click="handleLoginIn">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  reactive,
  getCurrentInstance,
  onMounted,
} from "vue";
import mixin from "@/mixins/mixin";
import YinLoginLogo from "@/components/layouts/YinLoginLogo.vue";
import { HttpManager } from "@/api";
import { NAV_NAME, HOME, SIGN_UP } from "@/enums";

interface resSignIn {
  code: string;
  type: string;
  msg: string;
  success: boolean;
  userMsg: any[];
}

export default defineComponent({
  components: {
    YinLoginLogo,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const { routerManager, changeIndex } = mixin();

    const validateName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("用户名不能为空"));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        callback();
      }
    };

    // 登录用户名密码
    const loginForm = reactive({
      username: "",
      password: "",
    });
    const rules = reactive({
      username: [
        { validator: validateName, message: "请输入用户名", trigger: "blur" },
      ],
      password: [
        {
          validator: validatePassword,
          message: "请输入密码",
          trigger: "blur",
        },
      ],
    });

    onMounted(() => {
      changeIndex(NAV_NAME.SIGN_IN);
    });

    async function handleLoginIn() {
      try {
        const params = new URLSearchParams();
        params.append("username", loginForm.username);
        params.append("password", loginForm.password);
        const result = (await HttpManager.signIn(params)) as resSignIn;
        if (result.code != null) {
          (proxy as any).$message({
            message: result.msg,
            type: result.type,
          });
          setUserInfo(result.userMsg[0]);
          setTimeout(() => {
            if (result.success) {
              changeIndex(NAV_NAME.HOME);
              routerManager(HOME, { path: HOME });
            }
          }, 2000);
        } else {
          (proxy as any).$notify({
            title: "用户名或密码错误",
            type: "error",
          });
        }
      } catch (error) {
        console.error(error);
      }
    }

    function setUserInfo(item) {
      proxy.$store.commit("setUserId", item.id);
      proxy.$store.commit("setUsername", item.username);
      proxy.$store.commit("setUserPic", item.avator);
      proxy.$store.commit("setToken", true);
    }
    function handleSignUp() {
      routerManager(SIGN_UP, { path: SIGN_UP });
    }

    return {
      loginForm,
      rules,
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
