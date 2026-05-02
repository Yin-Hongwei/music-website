<template>
  <AuthLayout title="重置密码" pageClass="forgot-password-page">
    <div class="fp">
      <p class="fp__intro">
        {{
          showDevIntro
            ? "开发环境：请填写已在本站注册过的邮箱。获取验证码后，右上角通知与下方会显示 6 位数字（后端 mock-without-mail 时不下发真实邮件）。"
            : "验证码将发送至该邮箱，查收后填写即可重置密码（若未收到请查看垃圾箱）。"
        }}
      </p>

      <el-alert v-if="devCodeHint" class="fp__dev" type="success" :closable="true" show-icon @close="devCodeHint = ''">
        <template #title>
          <span class="fp__dev-title">验证码</span>
        </template>
        <span>请填写：<strong class="fp__dev-code">{{ devCodeHint }}</strong></span>
      </el-alert>

      <el-form ref="formRef" class="fp__form" :model="form" :rules="rules" label-position="left" label-width="92px"
        size="large" @submit.prevent="handleSubmit">
        <el-form-item label="邮箱" prop="email">
          <div class="fp__row">
            <el-input v-model="form.email" placeholder="name@example.com" clearable autocomplete="email"
              class="fp__input" />
            <el-button class="fp__send" :disabled="sendDisabled" :loading="sending" @click="sendCode">
              {{ sendButtonText }}
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="验证码" prop="code">
          <el-input v-model="form.code" placeholder="6 位数字" maxlength="6" clearable autocomplete="one-time-code" />
        </el-form-item>

        <el-form-item label="新密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="至少 6 位" clearable />
        </el-form-item>

        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" show-password placeholder="再次输入" clearable />
        </el-form-item>

        <el-form-item class="fp__actions">
          <div class="fp__actions-row">
            <el-button class="fp__action-btn" @click="goSignIn">返回登录</el-button>
            <el-button type="primary" class="fp__action-btn" native-type="submit" :loading="submitting">
              重置密码
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </AuthLayout>
</template>

<script lang="ts" setup>
import { computed, onMounted, onUnmounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElNotification } from "element-plus";
import AuthLayout from "@/components/auth/AuthLayout.vue";
import { fetchResetPassword, fetchSendPasswordResetCode } from "@/api/auth";
import { elMessageTypeFromResponse } from "@/api/types";
import { RouterName } from "@/enums";
import { validatePassword } from "@/utils/validate";

const route = useRoute();
const router = useRouter();

const formRef = ref<{ validate: () => Promise<void>; validateField: (p: string) => Promise<void> } | null>(
  null,
);
const form = reactive({
  email: "",
  code: "",
  password: "",
  confirmPassword: "",
});

const sending = ref(false);
const submitting = ref(false);
const cooldown = ref(0);
const devCodeHint = ref("");
let cooldownTimer: ReturnType<typeof setInterval> | null = null;

const showDevIntro = process.env.NODE_ENV === "development";
/** 与 application-dev 中 app.password-reset.send-cooldown-seconds 对齐，避免前端锁 60s、后端已 15s */
const sendCooldownSeconds = showDevIntro ? 15 : 60;

function extractDebugCode(data: any): string | null {
  if (data == null || typeof data !== "object") {
    return null;
  }
  const raw = data.debugCode;
  if (raw == null) {
    return null;
  }
  if (typeof raw === "number" && Number.isFinite(raw)) {
    const n = Math.floor(Math.abs(raw)) % 1_000_000;
    return String(n).padStart(6, "0");
  }
  const digits = String(raw).replace(/\D/g, "");
  if (digits.length === 0) {
    return null;
  }
  const tail = digits.slice(-6);
  return tail.length === 6 ? tail : tail.padStart(6, "0");
}

const sendButtonText = computed(() => (cooldown.value > 0 ? `${cooldown.value}s 后重发` : "获取验证码"));
const sendDisabled = computed(() => cooldown.value > 0 || sending.value);

const validateConfirm = (_rule: any, value: string, callback: (e?: Error) => void) => {
  if (!value || !value.trim()) {
    callback(new Error("请再次输入密码"));
    return;
  }
  if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"));
    return;
  }
  callback();
};

const rules = {
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "邮箱格式不正确", trigger: ["blur", "change"] },
  ],
  code: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "请输入 6 位数字验证码", trigger: "blur" },
  ],
  password: [
    { validator: validatePassword, trigger: "blur" },
    { min: 6, message: "密码至少 6 位", trigger: "blur" },
  ],
  confirmPassword: [{ validator: validateConfirm, trigger: "blur" }],
};

function startCooldown(seconds: number) {
  if (cooldownTimer) {
    clearInterval(cooldownTimer);
    cooldownTimer = null;
  }
  cooldown.value = seconds;
  cooldownTimer = setInterval(() => {
    cooldown.value -= 1;
    if (cooldown.value <= 0 && cooldownTimer) {
      clearInterval(cooldownTimer);
      cooldownTimer = null;
    }
  }, 1000);
}

onUnmounted(() => {
  if (cooldownTimer) {
    clearInterval(cooldownTimer);
  }
});

onMounted(() => {
  const q = route.query.email;
  if (typeof q === "string" && q.trim()) {
    form.email = q.trim();
  }
});

async function sendCode() {
  if (!formRef.value) return;
  try {
    await formRef.value.validateField("email");
  } catch {
    return;
  }

  sending.value = true;
  devCodeHint.value = "";
  try {
    const result = await fetchSendPasswordResetCode(form.email.trim());
    ElMessage({
      message: result.message,
      type: elMessageTypeFromResponse(result),
    });
    if (result.success) {
      startCooldown(sendCooldownSeconds);
      const codeStr = extractDebugCode(result.data);
      if (codeStr) {
        devCodeHint.value = codeStr;
        form.code = codeStr;
        ElNotification({
          title: "验证码",
          message: `请填写：${codeStr}`,
          type: "success",
          duration: showDevIntro ? 0 : 45000,
          position: "top-right",
        });
      } else if (result.message && result.message.includes("若该邮箱已注册")) {
        ElMessage({
          message: "该邮箱可能尚未在本站注册，因此不会下发验证码。请先去注册，或换成已注册邮箱。",
          type: "warning",
          duration: 6000,
        });
      }
    }
  } finally {
    sending.value = false;
  }
}

async function handleSubmit() {
  if (!formRef.value) return;
  try {
    await formRef.value.validate();
  } catch {
    return;
  }

  submitting.value = true;
  try {
    const result = await fetchResetPassword({
      email: form.email.trim(),
      code: form.code.trim(),
      password: form.password,
      confirmPassword: form.confirmPassword,
    });
    ElMessage({
      message: result.message,
      type: elMessageTypeFromResponse(result),
    });
    if (result.success) {
      await router.replace({ path: RouterName.SignIn });
    }
  } finally {
    submitting.value = false;
  }
}

function goSignIn() {
  router.push({ path: RouterName.SignIn });
}
</script>

<style lang="scss" scoped>
.fp {
  text-align: left;
}

.fp__intro {
  margin: 0 0 12px;
  padding: 0 2px;
  font-size: 13px;
  line-height: 1.45;
  color: #909399;
  text-align: center;
}

.fp__dev {
  margin-bottom: 10px;
  border-radius: 8px;
}

.fp__dev-title {
  font-weight: 600;
}

.fp__dev-code {
  font-size: 22px;
  letter-spacing: 0.12em;
  font-variant-numeric: tabular-nums;
}

.fp__form:deep(.el-form-item) {
  align-items: flex-start;
}

.fp__form:deep(.el-form-item:not(.fp__actions)) {
  margin-bottom: 12px;
}

.fp__form:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
  line-height: var(--el-component-size-large);
  padding-bottom: 0;
  padding-right: 6px;
}

.fp__row {
  display: flex;
  gap: 8px;
  width: 100%;
  align-items: stretch;
}

.fp__input {
  flex: 1;
  min-width: 0;
}

.fp__send {
  flex-shrink: 0;
  min-width: 96px;
  border-radius: 8px;
}

.fp__actions {
  margin-bottom: 0;
}

.fp__actions:deep(.el-form-item__content) {
  display: block;
  width: 100%;
}

.fp__actions-row {
  display: flex;
  gap: 8px;
  width: 100%;
  align-items: stretch;
}

.fp__action-btn {
  flex: 1;
  min-width: 0;
  border-radius: 8px;
  font-weight: 500;
  letter-spacing: 0.02em;
}

@media screen and (max-width: 520px) {
  .fp__row {
    flex-direction: column;
  }

  .fp__send {
    width: 100%;
    min-width: 0;
  }
}
</style>

<style lang="scss">
.forgot-password-page .sign {
  width: min(440px, 92vw);
  max-width: 440px;
  box-shadow: 0 12px 40px rgba(64, 158, 255, 0.07);
}
</style>
