<template>
  <AuthLayout title="用户注册" pageClass="sign-up-page">
    <el-form ref="signUpForm" label-width="70px" status-icon :model="registerForm" :rules="SignUpRules">
      <el-form-item prop="username" label="用户名">
        <el-input v-model="registerForm.username" placeholder="用户名" />
      </el-form-item>
      <el-form-item prop="password" label="密码">
        <el-input type="password" placeholder="密码" v-model="registerForm.password" />
      </el-form-item>
      <el-form-item prop="sex" label="性别">
        <el-radio-group v-model="registerForm.sex">
          <el-radio :label="0">女</el-radio>
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">保密</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="phoneNum" label="手机">
        <el-input placeholder="手机" v-model="registerForm.phoneNum" />
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="registerForm.email" placeholder="邮箱" />
      </el-form-item>
      <el-form-item prop="birth" label="生日">
        <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%" />
      </el-form-item>
      <el-form-item prop="introduction" label="签名">
        <el-input type="textarea" placeholder="签名" v-model="registerForm.introduction" />
      </el-form-item>
      <el-form-item prop="location" label="地区">
        <el-select v-model="registerForm.location" :options="AREA" placeholder="地区" style="width: 100%" clearable
          filterable />
      </el-form-item>
      <el-form-item class="sign-btn">
        <el-button @click="goBack">返回登录</el-button>
        <el-button type="primary" @click="handleSignUp">确定</el-button>
      </el-form-item>
    </el-form>
  </AuthLayout>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import AuthLayout from "@/components/auth/AuthLayout.vue";
import { fetchSignUp } from "@/api/auth";
import { elMessageTypeFromResponse } from "@/api/types";
import { AREA, RouterName, NavLabel } from "@/enums";
import { useAppActions } from "@/composables/useAppActions";
import { useNavigation } from "@/composables/useNavigation";

const { routerManager } = useAppActions();
const { changeActiveNav } = useNavigation();
const signUpForm = ref<any>(null);

const registerForm = reactive({
  username: "",
  password: "",
  sex: 2,
  phoneNum: "",
  email: "",
  birth: new Date(),
  introduction: "",
  location: "",
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

async function handleSignUp() {
  if (!signUpForm.value) return;

  try {
    await signUpForm.value.validate();
  } catch {
    return;
  }

  const result = await fetchSignUp({
    username: registerForm.username,
    password: registerForm.password,
    sex: Number(registerForm.sex),
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
    changeActiveNav(NavLabel.SignIn);
    routerManager(RouterName.SignIn, { path: RouterName.SignIn });
  }
}

function goBack() {
  changeActiveNav(NavLabel.SignIn);
  routerManager(RouterName.SignIn, { path: RouterName.SignIn });
}
</script>

<style lang="scss" scoped>
.sign-btn:deep(.el-form-item__content) {
  display: flex;
  gap: 12px;
  width: 100%;
}

.sign-btn:deep(.el-button) {
  flex: 1;
}
</style>
