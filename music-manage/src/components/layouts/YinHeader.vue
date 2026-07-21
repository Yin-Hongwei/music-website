<template>
  <header class="header">
    <n-button
      class="collapse-btn"
      quaternary
      :aria-label="collapsed ? '展开侧栏' : '折叠侧栏'"
      @click="emit('toggle-collapse')"
    >
      <n-icon :size="20">
        <panel-left-open v-if="collapsed" />
        <panel-left-close v-else />
      </n-icon>
    </n-button>

    <div class="logo">{{ musicName }}</div>

    <div class="header-right">
      <div class="user-avatar">
        <img :src="attachImageUrl(userPic)" alt="" />
      </div>
      <n-dropdown
        :options="dropdownOptions"
        trigger="click"
        @select="handleSelect"
      >
        <button type="button" class="user-trigger">
          {{ username }}
          <n-icon :size="14"><chevron-down /></n-icon>
        </button>
      </n-dropdown>
    </div>
  </header>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import type { DropdownOption } from "naive-ui";
import { ChevronDown, PanelLeftClose, PanelLeftOpen } from "lucide-vue-next";
import { useAppStore } from "@/stores/app";
import { attachImageUrl } from "@/api/url";
import { MUSICNAME } from "@/enums";

defineProps<{
  collapsed: boolean;
}>();

const emit = defineEmits<{
  (e: "toggle-collapse"): void;
}>();

const router = useRouter();
const appStore = useAppStore();

const musicName = MUSICNAME;
const username = ref("admin");
const userPic = computed(() => appStore.userPic);

const dropdownOptions: DropdownOption[] = [
  {
    label: "退出登录",
    key: "logout",
  },
];

function handleSelect(key: string | number) {
  if (key === "logout") {
    sessionStorage.removeItem("dataStore");
    router.push({ name: "signIn" });
  }
}
</script>

<style scoped>
.header {
  position: relative;
  z-index: 100;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  width: 100%;
  height: 60px;
  padding: 0 20px 0 0;
  font-size: 18px;
  color: #2c3e50;
  background: #fff;
}

.collapse-btn {
  width: 60px;
  height: 60px;
}

.logo {
  flex: 1;
  min-width: 0;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto;
}

.user-avatar img {
  display: block;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

.user-trigger {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 0;
  border: 0;
  background: transparent;
  font-size: 14px;
  color: #2c3e50;
  cursor: pointer;
}
</style>
