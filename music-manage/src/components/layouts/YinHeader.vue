<template>
  <div class="header">
    <div class="collapse-btn" @click="collapseChage">
      <el-icon v-if="!collapse"><expand /></el-icon>
      <el-icon v-else><fold /></el-icon>
    </div>
    <div class="logo">{{ nusicName }}</div>
    <div class="header-right">
      <div class="header-user-con">
        <div class="user-avatar">
          <img :src="attachImageUrl(userPic)" />
        </div>
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
          <span class="el-dropdown-link">
            {{ username }}
            <el-icon class="dropdown-caret"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="loginout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, computed, ref, onMounted } from "vue";
import { useAppStore } from "@/stores/app";
import mixin from "@/mixins/mixin";
import { ArrowDown, Expand, Fold } from "@element-plus/icons-vue";
import emitter from "@/utils/emitter";
import { attachImageUrl } from "@/api/url";
import { RouterName, MUSICNAME } from "@/enums";

export default defineComponent({
  components: {
    ArrowDown,
    Expand,
    Fold,
  },
  setup() {
    const { routerManager } = mixin();
    const appStore = useAppStore();

    const collapse = ref(true);
    const username = ref("admin");
    const userPic = computed(() => appStore.userPic);
    const nusicName = ref(MUSICNAME);

    onMounted(() => {
      if (document.body.clientWidth < 1500) {
        collapseChage();
      }
    });

    // 侧边栏折叠
    function collapseChage() {
      collapse.value = !collapse.value;
      emitter.emit("collapse", collapse.value);
    }
    // 用户名下拉菜单选择事件
    function handleCommand(command) {
      if (command === "loginout") {
        routerManager(RouterName.SignIn, { path: RouterName.SignIn });
      }
    }
    return {
      nusicName,
      username,
      userPic,
      collapse,
      collapseChage,
      handleCommand,
      attachImageUrl,
    };
  },
});
</script>
<style scoped>
.header {
  position: relative;
  z-index: 100;
  width: 100%;
  height: 60px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  font-size: 20px;
  color: #2c3e50;
  background: #ffff;
  box-shadow: 0px 0px 8px 2px rgba(0, 0, 0, 0.3);
}

.collapse-btn {
  display: flex;
  padding: 0 21px;
  cursor: pointer;
}

.header .logo {
  width: 250px;
  font-weight: bold;
}

.header-right {
  position: absolute;
  right: 0;
  padding-right: 30px;
}

.header-user-con {
  display: flex;
  align-items: center;
}

.user-name {
  margin-left: 10px;
}

.user-avatar img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.el-dropdown-link {
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.dropdown-caret {
  font-size: 12px;
}

.el-dropdown-menu__item {
  text-align: center;
}
</style>
