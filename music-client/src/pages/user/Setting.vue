<template>
  <div class="setting">
    <h1>设置</h1>
    <el-tabs tab-position="left">
      <el-tab-pane label="个人资料" class="content">
        <PersonalData></PersonalData>
      </el-tab-pane>
      <el-tab-pane label="更改密码" class="content">
        <Password></Password>
      </el-tab-pane>
      <el-tab-pane label="账号和安全" class="content">
        <el-button type="danger" :icon="Delete" @click="cancelAccount">注销账号</el-button>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts" setup>
import { computed } from "vue";
import { Delete } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import PersonalData from "./PersonalData.vue";
import Password from "./Password.vue";
import { fetchDeleteUser } from "@/api/user";
import { elMessageTypeFromResponse } from "@/api/types";
import { useConfigureStore } from "@/store/configure";
import { useUserStore } from "@/store/user";
import { RouterName } from "@/enums";
import { useAppActions } from "@/composables/useAppActions";

const userStore = useUserStore();
const configureStore = useConfigureStore();
const { routerManager } = useAppActions();

const userId = computed(() => userStore.userId);

async function cancelAccount() {
  try {
    await ElMessageBox.confirm(
      "账号注销后将无法恢复，是否继续？",
      "确认注销账号",
      {
        confirmButtonText: "确认注销",
        cancelButtonText: "取消",
        type: "warning",
      },
    );
  } catch {
    return;
  }

  const result = await fetchDeleteUser(userId.value);
  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });
  if (result.success) {
    routerManager(RouterName.SignIn, { path: RouterName.SignIn });
    configureStore.setToken(false);
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

h1 {
  border-bottom: 1px solid $color-grey;
}

.content {
  padding-top: 20px;
  text-align: center;
}

@media screen and (min-width: $sm) {
  .setting {
    margin: 30px 10%;
    margin-top: 0;
    padding: 20px;
    min-height: 60vh;
  }
}

@media screen and (max-width: $sm) {
  .setting {
    padding: 20px;
  }
}
</style>
