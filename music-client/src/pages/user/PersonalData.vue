<template>
  <div class="profile-form">
    <div class="profile-form__avatar-row">
      <el-image
        class="profile-form__avatar"
        fit="cover"
        :src="attachImageUrl(userPic)"
      />
      <el-button round class="profile-form__avatar-btn" @click="avatarDialogVisible = true">
        更换头像
      </el-button>
    </div>

    <el-form
      ref="updateForm"
      class="profile-form__fields"
      label-width="4.5rem"
      label-position="right"
      :model="registerForm"
      :rules="SignUpRules"
    >
      <el-form-item prop="username" label="用户名">
        <el-input v-model="registerForm.username" placeholder="用户名" />
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="registerForm.sex">
          <el-radio :label="0">女</el-radio>
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">保密</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="phoneNum" label="手机号">
        <el-input v-model="registerForm.phoneNum" placeholder="手机号" />
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="registerForm.email" placeholder="邮箱" />
      </el-form-item>
      <el-form-item prop="birth" label="生日">
        <el-date-picker
          type="date"
          placeholder="选择日期"
          v-model="registerForm.birth"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item prop="location" label="地区">
        <el-select
          v-model="registerForm.location"
          placeholder="地区"
          style="width: 100%"
        >
          <el-option
            v-for="item in AREA"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="introduction" label="简介">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="简介"
          v-model="registerForm.introduction"
        />
      </el-form-item>
      <el-form-item class="profile-form__actions">
        <el-button type="primary" round :loading="saving" @click="saveMsg">
          {{ saving ? "保存中…" : "保存资料" }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="avatarDialogVisible" title="修改头像" width="420px">
      <Upload />
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { computed, reactive, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import Upload from "@/pages/user/Upload.vue";
import { useUserStore } from "@/store/user";
import { AREA } from "@/enums";
import { fetchUpdateUserMsg, fetchUserById } from "@/api/user";
import { elMessageTypeFromResponse } from "@/api/types";
import { attachImageUrl } from "@/utils";

const userStore = useUserStore();
const updateForm = ref<any>(null);
const avatarDialogVisible = ref(false);
const saving = ref(false);

const registerForm = reactive({
  username: "",
  sex: "" as string | number,
  phoneNum: "",
  email: "",
  birth: new Date() as Date | string,
  introduction: "",
  location: "",
  userPic: "",
});

const SignUpRules = {
  username: [{ required: true, trigger: "blur", min: 3 }],
  password: [{ required: true, trigger: "blur", min: 3 }],
  sex: [{ required: true, message: "请选择性别", trigger: "change" }],
  phoneNum: [{ message: "请输入手机号码", trigger: "blur" }],
  email: [
    { message: "请输入邮箱地址", trigger: "blur" },
    {
      type: "email",
      message: "请输入正确的邮箱地址",
      trigger: ["blur", "change"],
    },
  ],
  birth: [
    { required: true, type: "date", message: "请选择日期", trigger: "change" },
  ],
  introduction: [{ message: "请输入介绍", trigger: "blur" }],
  location: [{ message: "请输入地区", trigger: "change" }],
};

const userId = computed(() => userStore.userId);
const userPic = computed(() => userStore.userPic);

watch(userPic, () => {
  avatarDialogVisible.value = false;
});

async function getUserInfo(id: string | number) {
  if (id === "" || id == null) return;
  try {
    const result = await fetchUserById(id);
    const user = result && result.data ? result.data : null;
    if (!user) return;
    registerForm.username = user.username;
    registerForm.sex = user.sex;
    registerForm.phoneNum = user.phoneNum;
    registerForm.email = user.email;
    registerForm.birth = user.birth;
    registerForm.introduction = user.introduction;
    registerForm.location = user.location;
    registerForm.userPic = user.avatar;
  } catch {
    // 未登录或接口异常时保持默认表单
  }
}

async function saveMsg() {
  if (!updateForm.value) return;
  try {
    await updateForm.value.validate();
  } catch {
    return;
  }

  saving.value = true;
  try {
    const result = await fetchUpdateUserMsg({
      id: userId.value,
      username: registerForm.username,
      sex: registerForm.sex,
      phoneNum: registerForm.phoneNum,
      email: registerForm.email,
      birth: registerForm.birth,
      introduction: registerForm.introduction,
      location: registerForm.location,
    });
    ElMessage({
      message: result.message,
      type: elMessageTypeFromResponse(result),
    });
    if (result.success) {
      userStore.setUsername(registerForm.username);
    }
  } finally {
    saving.value = false;
  }
}

getUserInfo(userId.value);
</script>

<style lang="scss" scoped>
.profile-form__avatar-row {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 1.5rem;
}

.profile-form__avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: var(--surface-tertiary);
  overflow: hidden;
  flex-shrink: 0;
}

.profile-form__avatar-btn {
  background: var(--surface-secondary);
  border-color: transparent;
  color: var(--foreground);

  &:hover {
    background: var(--surface-tertiary);
    border-color: transparent;
    color: var(--foreground);
  }
}

.profile-form__fields {
  max-width: 100%;
}

.profile-form__actions:deep(.el-form-item__content) {
  justify-content: flex-end;
}
</style>
