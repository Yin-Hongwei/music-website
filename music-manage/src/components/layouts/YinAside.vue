<template>
  <n-menu
    class="sidebar-menu"
    :value="activeMenu"
    :collapsed="collapse"
    :collapsed-width="64"
    :collapsed-icon-size="20"
    :options="menuOptions"
    :indent="18"
    @update:value="onSelect"
  />
</template>

<script lang="ts" setup>
import { computed, h } from "vue";
import type { Component } from "vue";
import { useRoute, useRouter } from "vue-router";
import type { MenuOption } from "naive-ui";
import { ChartPie, Mic2, FileText, User, Image } from "lucide-vue-next";

interface AsideMenuItem {
  key: string;
  label: string;
  icon: Component;
}

const defaultAsideMenuItems: AsideMenuItem[] = [
  { key: "info", label: "系统首页", icon: ChartPie },
  { key: "consumer", label: "用户管理", icon: User },
  { key: "singer", label: "歌手管理", icon: Mic2 },
  { key: "songList", label: "歌单管理", icon: FileText },
  { key: "banner", label: "Banner管理", icon: Image },
];

const props = defineProps<{
  collapse: boolean;
  items?: AsideMenuItem[];
}>();

const route = useRoute();
const router = useRouter();

const menuItems = computed(() =>
  props.items === undefined ? defaultAsideMenuItems : props.items,
);

const menuOptions = computed<MenuOption[]>(() =>
  menuItems.value.map((item) => ({
    key: item.key,
    label: item.label,
    icon: () => h(item.icon, { size: 18, strokeWidth: 1.75 }),
  })),
);

const activeMenu = computed(() => {
  const name = route.name;
  if (name === "song") return "singer";
  if (name === "listSong" || name === "comment") return "songList";
  if (name === "collect") return "consumer";
  return typeof name === "string" ? name : route.path;
});

function onSelect(key: string) {
  if (key !== route.name) {
    router.push({ name: key });
  }
}
</script>

<style scoped>
.sidebar-menu {
  height: 100%;
  border-right: none !important;
}

.sidebar-menu :deep(.n-menu-item) {
  margin-top: 4px;
}

.sidebar-menu :deep(.n-menu-item-content) {
  padding-left: 16px !important;
}
</style>
