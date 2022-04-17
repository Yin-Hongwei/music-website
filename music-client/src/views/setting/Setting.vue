<template>
  <div class="container">
    <div class="setting">
      <h1>设置</h1>
      <el-tabs tab-position="left">
        <el-tab-pane label="个人资料" class="content">
          <Personal-data></Personal-data>
        </el-tab-pane>
        <el-tab-pane label="更改密码" class="content">
          <Password></Password>
        </el-tab-pane>
        <el-tab-pane label="账号和安全" class="content">
          <el-button type="danger" round @click="cancelAccount">注销账号</el-button>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, computed, reactive } from "vue";
import PersonalData from "./PersonalData.vue";
import Password from "./Password.vue";
import { HttpManager } from "@/api";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import { RouterName } from "@/enums";

export default defineComponent({
  components: {
    PersonalData,
    Password,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { routerManager } = mixin();

    const userId = computed(() => store.getters.userId);

    async function cancelAccount() {
      const result = (await HttpManager.deleteUser(userId.value)) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      routerManager(RouterName.SignIn, { path: RouterName.SignIn });
      proxy.$store.commit("setToken", false);
    }

    return {
      cancelAccount,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.container {
  padding-top: $header-height + 30px;
}

.setting {
  margin: 30px 10%;
  margin-top: 0;
  padding: 20px;
  min-height: 60vh;
  background-color: $color-white;
  border-radius: 12px;
  overflow: hidden;
}
h1 {
  border-bottom: 1px solid $color-grey;
}
.content {
  padding: 20px;
  text-align: center;
}
</style>
