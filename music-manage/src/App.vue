<template>
  <div id="app">
    <router-view v-slot="{ Component }">
      <component v-if="isLoginRoute" :is="Component" />
      <yin-layout v-else-if="Component">
        <component :is="Component" />
      </yin-layout>
    </router-view>
  </div>
</template>

<script lang="ts" setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import YinLayout from "@/components/layouts/YinLayout.vue";
import { useAppStore } from "@/stores/app";

const route = useRoute();
const isLoginRoute = computed(() => route.path === "/");


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
