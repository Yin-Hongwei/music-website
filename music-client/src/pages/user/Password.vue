<template>
  <el-form
    ref="passwordForm"
    class="password-form"
    label-width="6rem"
    label-position="right"
    :model="form"
    :rules="rules"
  >
    <el-form-item label="当前密码" prop="oldPassword">
      <el-input type="password" show-password v-model="form.oldPassword" />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input type="password" show-password v-model="form.newPassword" />
    </el-form-item>
    <el-form-item label="确认新密码" prop="confirmPassword">
      <el-input type="password" show-password v-model="form.confirmPassword" />
    </el-form-item>
    <el-form-item class="password-form__actions">
      <el-button type="primary" round :loading="saving" @click="confirm">
        {{ saving ? "修改中…" : "修改密码" }}
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts" setup>
import { computed, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useUserStore } from "@/store/user";
import { fetchUpdateUserPassword } from "@/api/user";
import { elMessageTypeFromResponse } from "@/api/types";
import { validatePassword } from "@/utils/validate";
import { RouterName } from "@/enums";
import { useConfigureStore } from "@/store/configure";
import { useAppActions } from "@/composables/useAppActions";

const userStore = useUserStore();
const configureStore = useConfigureStore();
const { routerManager } = useAppActions();
const passwordForm = ref<any>(null);
const saving = ref(false);

const form = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});
const userId = computed(() => userStore.userId);
const userName = computed(() => userStore.username);

const validateCheck = (_rule: any, value: any, callback: any) => {
  if (value === "") {
    callback(new Error("密码不能为空"));
  } else if (value !== form.newPassword) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};
const rules = reactive({
  oldPassword: [{ validator: validatePassword, trigger: "blur", min: 3 }],
  newPassword: [{ validator: validatePassword, trigger: "blur", min: 3 }],
  confirmPassword: [{ validator: validateCheck, trigger: "blur", min: 3 }],
});

async function confirm() {
  if (!passwordForm.value) return;
  try {
    await passwordForm.value.validate();
  } catch {
    return;
  }

  saving.value = true;
  try {
    const result = await fetchUpdateUserPassword({
      id: userId.value,
      username: userName.value,
      oldPassword: form.oldPassword,
      password: form.newPassword,
    });
    ElMessage({
      message: result.message,
      type: elMessageTypeFromResponse(result),
    });
    if (result.success) {
      form.oldPassword = "";
      form.newPassword = "";
      form.confirmPassword = "";
      configureStore.setToken(false);
      userStore.setUserId("");
      routerManager(RouterName.SignIn, { path: RouterName.SignIn });
    }
  } finally {
    saving.value = false;
  }
}
</script>

<style lang="scss" scoped>
.password-form {
  max-width: 100%;
}

.password-form__actions:deep(.el-form-item__content) {
  justify-content: flex-end;
}
</style>
