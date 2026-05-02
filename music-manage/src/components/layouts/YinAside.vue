<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-el-menu"
      background-color="#ffffff"
      active-text-color="#30a4fc"
      default-active="2"
      router
      :collapse="collapse"
    >
      <el-menu-item
        v-for="item in menuItems"
        :key="item.index"
        :index="item.index"
      >
        <el-icon><component :is="item.icon" /></el-icon>
        <span>{{ item.title }}</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import { PieChart, Mic, Document, User, Picture } from "@element-plus/icons-vue";
import emitter from "@/utils/emitter";

/** 侧栏菜单项：`index` 与 `el-menu` router 模式下的路径一致 */
interface AsideMenuItem {
  index: string;
  title: string;
  icon: typeof PieChart;
}

const defaultAsideMenuItems: AsideMenuItem[] = [
  { index: "info", title: "系统首页", icon: PieChart },
  { index: "consumer", title: "用户管理", icon: User },
  { index: "singer", title: "歌手管理", icon: Mic },
  { index: "songList", title: "歌单管理", icon: Document },
  { index: "banner", title: "Banner管理", icon: Picture },
];

const props = defineProps<{
  /** 不传则使用内置默认菜单 */
  items?: AsideMenuItem[];
}>();

const menuItems = computed(() =>
  props.items === undefined ? defaultAsideMenuItems : props.items,
);

const collapse = ref(false);
emitter.on("collapse", (msg) => {
  collapse.value = msg as boolean;
});
</script>

<style scoped>
.sidebar {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  overflow: hidden;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar > ul {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  border-right: none;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 150px;
}
</style>
