<template>
  <div class="page-shell setting">
    <div class="setting-card">
      <h1 class="section-title">设置</h1>
      <el-tabs v-model="activeTab" class="segment-tabs">
        <el-tab-pane label="个人资料" name="profile">
          <PersonalData />
        </el-tab-pane>
        <el-tab-pane label="修改密码" name="password">
          <Password />
        </el-tab-pane>
        <el-tab-pane label="账号" name="danger">
          <div class="danger-panel">
            <p class="danger-panel__hint">注销账号后数据无法恢复，请谨慎操作。</p>
            <el-button
              class="danger-panel__btn"
              round
              type="danger"
              plain
              :loading="deleting"
              @click="cancelAccount"
            >
              <Trash2 class="btn-icon" />
              注销账号
            </el-button>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import { Trash2 } from "lucide-vue-next";
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

const activeTab = ref("profile");
const deleting = ref(false);
const userId = computed(() => userStore.userId);

async function cancelAccount() {
  if (!userId.value) {
    ElMessage({ message: "请先登录", type: "warning" });
    return;
  }

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

  deleting.value = true;
  try {
    const result = await fetchDeleteUser(userId.value);
    ElMessage({
      message: result.message,
      type: elMessageTypeFromResponse(result),
    });
    if (result.success) {
      configureStore.setToken(false);
      userStore.setUserId("");
      userStore.setUsername("");
      userStore.setUserPic("");
      routerManager(RouterName.SignIn, { path: RouterName.SignIn });
    }
  } catch {
    ElMessage({ message: "注销失败，请稍后重试", type: "error" });
  } finally {
    deleting.value = false;
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.setting {
  max-width: 42rem;
}

.setting-card {
  padding: 2rem;
  border-radius: 28px;
  background: var(--surface);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.06);
}

.setting-card .section-title {
  margin-bottom: 1.5rem;
}

.danger-panel {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 1rem;
}

.danger-panel__hint {
  margin: 0;
  font-size: 0.875rem;
  line-height: 1.5;
  color: var(--muted);
}

.btn-icon {
  width: 16px;
  height: 16px;
  margin-right: 6px;
}

@media screen and (max-width: $sm) {
  .setting-card {
    padding: 1.25rem;
  }
}
</style>
