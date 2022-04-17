<template>
  <el-form :model="form" label-width="70px">
    <el-form-item label="旧密码">
      <el-input v-model="form.oldPassword" />
    </el-form-item>
    <el-form-item label="新密码">
      <el-input v-model="form.newPassword" />
    </el-form-item>
    <el-form-item label="密码确认">
      <el-input v-model="form.confirmPassword" />
    </el-form-item>
    <el-form-item>
      <el-button @click="clearData()">重置</el-button>
      <el-button type="primary" @click="confirm()">确认修改</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, computed, reactive } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import { HttpManager } from "@/api";

export default defineComponent({
  setup() {
    const store = useStore();
    const { proxy } = getCurrentInstance();
    const { goBack } = mixin();

    const form = reactive({
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
    });
    const userId = computed(() => store.getters.userId);
    const username = computed(() => store.getters.username);

    async function clearData() {
      form.oldPassword = "";
      form.newPassword = "";
      form.confirmPassword = "";
    }

    async function confirm() {
      const params = new URLSearchParams();
      params.append("id", userId.value);
      params.append("username", username.value);
      params.append("old_password", form.oldPassword);
      params.append("password", form.confirmPassword);

      const result = (await HttpManager.updateUserPassword(params)) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) goBack()
    }

    return {
      form,
      clearData,
      confirm,
    };
  },
});
</script>

<style></style>
