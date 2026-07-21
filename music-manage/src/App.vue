<template>
  <n-config-provider
    :theme-overrides="themeOverrides"
    :locale="zhCN"
    :date-locale="dateZhCN"
  >
    <n-message-provider>
      <n-dialog-provider>
        <div id="app-shell">
          <router-view v-slot="{ Component }">
            <component v-if="isLoginRoute" :is="Component" />
            <yin-layout v-else-if="Component">
              <component :is="Component" />
            </yin-layout>
          </router-view>
        </div>
      </n-dialog-provider>
    </n-message-provider>
  </n-config-provider>
</template>

<script lang="ts" setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import { zhCN, dateZhCN } from "naive-ui";
import type { GlobalThemeOverrides } from "naive-ui";
import YinLayout from "@/components/layouts/YinLayout.vue";
import { useAppStore } from "@/stores/app";

const route = useRoute();
const isLoginRoute = computed(() => route.path === "/");

const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: "#18a058",
    primaryColorHover: "#36ad6a",
    primaryColorPressed: "#0c7a43",
    primaryColorSuppl: "#36ad6a",
  },
  Menu: {
    itemHeight: "42px",
  },
  DataTable: {
    thPaddingMedium: "10px 12px",
    tdPaddingMedium: "10px 12px",
  },
};

const appStore = useAppStore();

const raw = sessionStorage.getItem("dataStore");
if (raw) {
  try {
    appStore.$patch(JSON.parse(raw));
  } catch {
    /* ignore invalid session payload */
  }
}

window.addEventListener("beforeunload", () => {
  sessionStorage.setItem("dataStore", JSON.stringify(appStore.$state));
});
</script>

<style>
#app-shell {
  width: 100%;
  height: 100%;
}
</style>
