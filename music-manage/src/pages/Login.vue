<template>
  <div class="login-page">
    <div class="login-bg" aria-hidden="true">
      <span class="login-bg__orb login-bg__orb--a" />
      <span class="login-bg__orb login-bg__orb--b" />
      <span class="login-bg__orb login-bg__orb--c" />
      <span class="login-bg__grid" />
    </div>
    <div class="login-panel">
      <h1 class="login-title">{{ MUSICNAME }}</h1>
      <p class="login-subtitle">管理员登录</p>
      <div class="login-card">
        <n-form
          ref="formRef"
          size="large"
          :model="ruleForm"
          :rules="rules"
          :show-label="false"
          @keyup.enter="submitForm"
        >
          <n-form-item path="username">
            <n-input
              v-model:value="ruleForm.username"
              placeholder="用户名"
              :input-props="{ autocomplete: 'username' }"
            >
              <template #prefix>
                <n-icon :size="16"><User /></n-icon>
              </template>
            </n-input>
          </n-form-item>
          <n-form-item path="password">
            <n-input
              v-model:value="ruleForm.password"
              type="password"
              show-password-on="click"
              placeholder="密码"
              :input-props="{ autocomplete: 'current-password' }"
            >
              <template #prefix>
                <n-icon :size="16"><Lock /></n-icon>
              </template>
            </n-input>
          </n-form-item>
          <n-form-item :show-feedback="false">
            <n-button
              class="login-btn"
              type="primary"
              strong
              :loading="submitting"
              @click="submitForm"
            >
              登录
            </n-button>
          </n-form-item>
        </n-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import type { FormInst, FormRules } from "naive-ui";
import { Lock, User } from "lucide-vue-next";
import { getLoginStatus } from "@/api/admin";
import type { ApiResponse } from "@/api/types";
import { MUSICNAME } from "@/enums";
import { useAdminNav } from "@/composables/useAdminNav";
import { message, notifyResult } from "@/utils";

const { routerManager } = useAdminNav();
const formRef = ref<FormInst | null>(null);
const submitting = ref(false);
const ruleForm = reactive({ username: "admin", password: "123" });
const rules: FormRules = {
  username: { required: true, message: "请输入用户名", trigger: "blur" },
  password: { required: true, message: "请输入密码", trigger: "blur" },
};

async function submitForm() {
  try {
    await formRef.value?.validate();
  } catch {
    return;
  }
  submitting.value = true;
  try {
    const result = (await getLoginStatus(ruleForm)) as ApiResponse;
    notifyResult(result);
    if (result.success) routerManager("info");
  } catch (error) {
    const body = (error as { data?: ApiResponse }).data;
    message.error(body?.message || "网络异常，请稍后重试");
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.login-page {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  min-height: 100vh;
  height: 100%;
  overflow: hidden;
  background: linear-gradient(160deg, #eef3f8 0%, #d5e0ec 48%, #b8c8dc 100%);
}

.login-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.login-bg__orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(64px);
  opacity: 0.5;
}

.login-bg__orb--a {
  top: -12%;
  left: -8%;
  width: 42vw;
  height: 42vw;
  max-width: 520px;
  max-height: 520px;
  background: #c5d6e8;
  opacity: 0.4;
}

.login-bg__orb--b {
  right: -10%;
  bottom: -18%;
  width: 48vw;
  height: 48vw;
  max-width: 580px;
  max-height: 580px;
  background: #9bb5d0;
  opacity: 0.42;
}

.login-bg__orb--c {
  top: 38%;
  left: 55%;
  width: 22vw;
  height: 22vw;
  max-width: 280px;
  max-height: 280px;
  background: #d0dff0;
  opacity: 0.28;
}

.login-bg__grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(40, 65, 100, 0.07) 1px, transparent 1px),
    linear-gradient(90deg, rgba(40, 65, 100, 0.07) 1px, transparent 1px);
  background-size: 48px 48px;
  mask-image: radial-gradient(ellipse 70% 60% at 50% 45%, #000 20%, transparent 75%);
}

.login-panel {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  width: min(400px, calc(100% - 32px));
}

.login-title {
  margin: 0;
  text-align: center;
  font-size: 26px;
  font-weight: 650;
  line-height: 1.3;
  color: #1e2a3a;
  letter-spacing: 0.01em;
}

.login-subtitle {
  margin: 8px 0 28px;
  text-align: center;
  font-size: 14px;
  line-height: 1.4;
  color: #6b7c90;
}

.login-card {
  padding: 36px 32px 32px;
  border-radius: 14px;
  border: 1px solid rgba(255, 255, 255, 0.7);
  background: rgba(255, 255, 255, 0.82);
  box-shadow:
    0 1px 2px rgba(30, 50, 80, 0.04),
    0 16px 40px rgba(40, 70, 110, 0.1);
  backdrop-filter: blur(12px);
}

.login-card :deep(.n-form-item) {
  margin-right: 0;
  margin-bottom: 20px;
}

.login-card :deep(.n-form-item:last-child) {
  margin-bottom: 0;
}

.login-card :deep(.n-form-item-feedback-wrapper) {
  min-height: 16px !important;
}

.login-card :deep(.n-input) {
  --n-border-radius: 8px !important;
}

.login-btn {
  width: 100%;
  height: 42px;
  margin-top: 4px;
  border: none !important;
  border-radius: 8px !important;
  font-size: 15px;
  letter-spacing: 0.12em;
}
</style>
