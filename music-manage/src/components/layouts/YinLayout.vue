<template>
  <div class="layout-root">
    <header class="layout-header">
      <yin-header :collapsed="asideCollapsed" @toggle-collapse="toggleAside" />
    </header>
    <div class="layout-body">
      <aside class="layout-aside" :class="{ 'layout-aside--collapsed': asideCollapsed }">
        <yin-aside :collapse="asideCollapsed" />
      </aside>
      <main class="layout-main">
        <slot />
      </main>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import YinHeader from "./YinHeader.vue";
import YinAside from "./YinAside.vue";

const COLLAPSE_BREAKPOINT = 900;

const asideCollapsed = ref(false);

function toggleAside() {
  asideCollapsed.value = !asideCollapsed.value;
}

onMounted(() => {
  if (window.innerWidth < COLLAPSE_BREAKPOINT) {
    asideCollapsed.value = true;
  }
});
</script>

<style scoped>
.layout-root {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100vh;
  overflow: hidden;
  background: #f0f2f5;
}

.layout-header {
  flex-shrink: 0;
  height: 60px;
  background: #fff;
  border-bottom: 1px solid #efeff5;
}

.layout-body {
  display: flex;
  flex: 1;
  min-height: 0;
  overflow: hidden;
}

.layout-aside {
  flex-shrink: 0;
  width: 180px;
  overflow-x: hidden;
  overflow-y: auto;
  background: #fff;
  border-right: 1px solid #efeff5;
  transition: width 0.2s ease;
}

.layout-aside--collapsed {
  width: 64px;
}

.layout-main {
  flex: 1;
  min-width: 0;
  overflow: auto;
  padding: 20px;
  background: #f0f2f5;
}
</style>
