<template>
  <el-form
    ref="updateForm"
    label-width="70px"
    :model="registerForm"
    :rules="SignUpRules"
  >
    <el-form-item prop="username" label="用户名">
      <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="registerForm.sex">
        <el-radio :label="0">女</el-radio>
        <el-radio :label="1">男</el-radio>
        <el-radio :label="2">保密</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item prop="birth" label="生日">
      <el-date-picker
        type="date"
        placeholder="选择日期"
        v-model="registerForm.birth"
        style="width: 100%"
      ></el-date-picker>
    </el-form-item>
    <el-form-item prop="introduction" label="签名">
      <el-input
        type="textarea"
        placeholder="签名"
        v-model="registerForm.introduction"
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
        >
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item prop="phoneNum" label="手机">
      <el-input placeholder="手机" v-model="registerForm.phoneNum" />
    </el-form-item>
    <el-form-item prop="email" label="邮箱">
      <el-input v-model="registerForm.email" placeholder="邮箱" />
    </el-form-item>
    <el-form-item>
      <el-button @click="goBack(-1)">取消</el-button>
      <el-button type="primary" @click="saveMsg">保存</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts" setup>
import { computed, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useUserStore } from "@/store/user";
import { AREA } from "@/enums";
import { fetchUpdateUserMsg, fetchUserById } from "@/api/user";
import { elMessageTypeFromResponse } from "@/api/types";
import { useAppActions } from "@/composables/useAppActions";

const userStore = useUserStore();
const { goBack } = useAppActions();
const updateForm = ref<any>(null);

// 注册
const registerForm = reactive({
  username: "",
  sex: "",
  phoneNum: "",
  email: "",
  birth: new Date(),
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

async function getUserInfo(id) {
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
}

async function saveMsg() {
  if (!updateForm.value) return;
  try {
    await updateForm.value.validate();
  } catch {
    return;
  }

  const id = userId.value;
  const username = registerForm.username;
  const sex = registerForm.sex;
  const phoneNum = registerForm.phoneNum;
  const email = registerForm.email;
  const birth = registerForm.birth;
  const introduction = registerForm.introduction;
  const location = registerForm.location;
  const result = await fetchUpdateUserMsg({
    id,
    username,
    sex,
    phoneNum,
    email,
    birth,
    introduction,
    location,
  });
  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });
  if (result.success) {
    userStore.setUsername(registerForm.username);
    goBack(-1);
  }
}

getUserInfo(userId.value);
</script>

<style lang="scss" scoped>
.btn ::v-deep .el-form-item__content {
  display: flex;
  justify-content: center;
}
</style>
