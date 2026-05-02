<template>
  <el-form ref="passwordForm" label-width="70px" :model="form" :rules="rules">
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input type="password" v-model="form.oldPassword" />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input type="password" v-model="form.newPassword" />
    </el-form-item>
    <el-form-item label="密码确认" prop="confirmPassword">
      <el-input type="password" v-model="form.confirmPassword" />
    </el-form-item>
    <el-form-item>
      <el-button @click="clearData">重置</el-button>
      <el-button type="primary" @click="confirm">确认修改</el-button>
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
import { useAppActions } from "@/composables/useAppActions";

const userStore = useUserStore();
const { goBack } = useAppActions();
const passwordForm = ref<any>(null);

const form = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});
const userId = computed(() => userStore.userId);
const userName = computed(() => userStore.username);

const validateCheck = (rule: any, value: any, callback: any) => {
  if (value === "") {
    callback(new Error("密码不能为空"));
  } else if (value !== form.newPassword) {
    callback(new Error("请输入正确密码"));
  } else {
    callback();
  }
};
const rules = reactive({
  oldPassword: [{ validator: validatePassword, trigger: "blur", min: 3 }],
  newPassword: [{ validator: validatePassword, trigger: "blur", min: 3 }],
  confirmPassword: [{ validator: validateCheck, trigger: "blur", min: 3 }],
});

async function clearData() {
  form.oldPassword = "";
  form.newPassword = "";
  form.confirmPassword = "";
}

async function confirm() {
  if (!passwordForm.value) return;
  try {
    await passwordForm.value.validate();
  } catch {
    return;
  }

  const id = userId.value;
  const username = userName.value;
  const oldPassword = form.oldPassword;
  const password = form.newPassword;

  const result = await fetchUpdateUserPassword({
    id,
    username,
    oldPassword,
    password,
  });
  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });
  if (result.success) goBack();
}
</script>

<style></style>
