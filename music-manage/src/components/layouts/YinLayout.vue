<template>
  <el-container class="layout-root">
    <el-header class="layout-header" height="60px">
      <yin-header />
    </el-header>
    <el-container class="layout-body">
      <el-aside :width="asideWidth" class="layout-aside">
        <yin-aside />
      </el-aside>
      <el-main class="layout-main">
        <slot />
      </el-main>
    </el-container>
  </el-container>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import YinHeader from "./YinHeader.vue";
import YinAside from "./YinAside.vue";
import emitter from "@/utils/emitter";

const collapse = ref(false);
emitter.on("collapse", (msg) => {
  collapse.value = msg as boolean;
});

/** 与侧栏 `el-menu` 展开 / 折叠宽度一致 */
const asideWidth = computed(() => (collapse.value ? "64px" : "150px"));
</script>

<style scoped>
.layout-root {
  height: 100vh;
  flex-direction: column;
}

.layout-header {
  --el-header-padding: 0;
  padding: 0;
  height: 60px;
  overflow: hidden;
}

.layout-body {
  flex: 1;
  min-height: 0;
}

.layout-aside {
  overflow: hidden;
  transition: width 0.3s ease-in-out;
}

.layout-main {
  min-height: 0;
  overflow-y: auto;
  --el-main-padding: 20px;
}
</style>
