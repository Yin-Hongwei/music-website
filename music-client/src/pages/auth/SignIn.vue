<template>
  <AuthLayout title="登录" pageClass="sign-in-page">
    <el-tabs v-model="activeTab" class="sign-in-tabs" stretch @tab-change="onTabChange">
      <el-tab-pane label="账号登录" name="account" />
      <el-tab-pane label="邮箱登录" name="email" />
    </el-tabs>
    <el-form ref="signInForm" status-icon :model="registerForm" :rules="signInRules">
      <el-form-item v-show="activeTab === 'account'" prop="username">
        <el-input placeholder="用户名" v-model="registerForm.username" />
      </el-form-item>
      <el-form-item v-show="activeTab === 'email'" prop="email">
        <el-input placeholder="邮箱" v-model="registerForm.email" />
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" placeholder="密码" v-model="registerForm.password" @keyup.enter="handleLoginIn" />
      </el-form-item>
      <el-form-item class="sign-btn--submit">
        <el-button type="primary" class="sign-submit-btn" @click="handleLoginIn">返回登录</el-button>
      </el-form-item>
      <div class="sign-switch sign-switch--footer">
        <div class="sign-switch__left">
          <span class="sign-switch__text">还没有账号？</span>
          <button type="button" class="sign-switch__link sign-switch__link--with-icon" @click="handleSignUp">
            立即注册
          </button>
        </div>
        <button type="button" class="sign-switch__link sign-switch__link--with-icon" @click="handleForgotPassword">
          <KeyRound class="sign-switch__icon" aria-hidden="true" />
          忘记密码
        </button>
      </div>
    </el-form>
  </AuthLayout>
</template>

<script lang="ts" setup>
import { computed, reactive, ref } from "vue";
import { KeyRound, UserPlus } from "lucide-vue-next";
import { ElMessage } from "element-plus";
import AuthLayout from "@/components/auth/AuthLayout.vue";
import { useConfigureStore } from "@/store/configure";
import { useUserStore } from "@/store/user";
import { fetchSignIn } from "@/api/auth";
import { elMessageTypeFromResponse } from "@/api/types";
import { NavLabel, RouterName } from "@/enums";
import { validatePassword } from "@/utils/validate";
import { useAppActions } from "@/composables/useAppActions";
import { useNavigation } from "@/composables/useNavigation";

type LoginTab = "account" | "email";

const { routerManager } = useAppActions();
const { changeActiveNav } = useNavigation();
const signInForm = ref(null);
const userStore = useUserStore();
const configureStore = useConfigureStore();

const activeTab = ref<LoginTab>("account");

const registerForm = reactive({
  username: "",
  email: "",
  password: "",
});

const validateName = (_rule: any, value: string, callback: (e?: Error) => void) => {
  if (!value || !value.trim()) {
    callback(new Error("用户名不能为空"));
  } else {
    callback();
  }
};

const signInRules = computed(() => ({
  username:
    activeTab.value === "account"
      ? [{ validator: validateName, trigger: "blur" }]
      : [],
  email:
    activeTab.value === "email"
      ? [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ]
      : [],
  password: [{ validator: validatePassword, trigger: "blur" }],
}));

function onTabChange() {
  registerForm.username = "";
  registerForm.email = "";
  const form = signInForm.value as { clearValidate: () => void } | null;
  if (form) {
    form.clearValidate();
  }
}

function loginAccount(): string {
  return activeTab.value === "account"
    ? registerForm.username.trim()
    : registerForm.email.trim();
}

async function handleLoginIn() {
  if (!signInForm.value) return;
  try {
    await (signInForm.value as any).validate();
  } catch {
    return;
  }

  const result = await fetchSignIn({
    username: loginAccount(),
    password: registerForm.password,
  });
  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });

  if (result.success) {
    const user = resolveLoginUser(result.data);
    if (!user) {
      ElMessage({
        message: "登录返回数据异常，请重试",
        type: "warning",
      });
      return;
    }
    userStore.setUserId(user.id);
    userStore.setUsername(user.username);
    userStore.setUserPic(user.avatar);
    configureStore.setToken(true);
    changeActiveNav(NavLabel.Home);
    routerManager(RouterName.Home, { path: RouterName.Home });
  }
}

function resolveLoginUser(data: any) {
  if (Array.isArray(data)) {
    return data[0] || null;
  }
  if (data && typeof data === "object") {
    return data;
  }
  return null;
}

function handleSignUp() {
  routerManager(RouterName.SignUp, { path: RouterName.SignUp });
}

function handleForgotPassword() {
  const query: Record<string, string> = {};
  if (activeTab.value === "email" && registerForm.email.trim()) {
    query.email = registerForm.email.trim();
  }
  routerManager(RouterName.ForgotPassword, {
    path: RouterName.ForgotPassword,
    query,
  });
}
</script>

<style lang="scss" scoped>
.sign-in-tabs {
  margin-bottom: 16px;
}

.sign-in-tabs:deep(.el-tabs__header) {
  margin-bottom: 0;
}

.sign-in-tabs:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
}

.sign-in-tabs:deep(.el-tabs__item) {
  flex: 1;
  padding: 0 12px;
}

.sign-in-tabs:deep(.el-tabs__nav) {
  width: 100%;
}

.sign-in-tabs:deep(.el-tabs__active-bar) {
  transition: transform 0.2s ease;
}

.sign-btn--submit {
  margin-bottom: 10px;
}

.sign-submit-btn {
  width: 100%;
}

.sign-btn--submit:deep(.el-form-item__content) {
  display: flex;
  width: 100%;
}

.sign-switch {
  display: flex;
  align-items: center;
  margin-top: 4px;
  font-size: 14px;
  line-height: 20px;
}

.sign-switch--footer {
  justify-content: space-between;
  width: 100%;
  gap: 12px;
  flex-wrap: wrap;
}

.sign-switch__left {
  display: flex;
  align-items: center;
  gap: 6px;
  min-width: 0;
}

.sign-switch__text {
  color: #909399;
}

.sign-switch__link {
  padding: 0;
  border: 0;
  background: transparent;
  color: #409eff;
  font-size: 14px;
  line-height: 20px;
  cursor: pointer;
}

.sign-switch__link--with-icon {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.sign-switch__icon {
  width: 14px;
  height: 14px;
  flex-shrink: 0;
}

.sign-switch__link:hover {
  color: #66b1ff;
}
</style>
