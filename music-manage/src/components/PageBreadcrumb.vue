<template>
  <n-breadcrumb class="crumbs" separator="/">
    <n-breadcrumb-item
      v-for="item in items"
      :key="item.name"
      :clickable="Boolean(item.path)"
      @click="onClick(item)"
    >
      {{ item.name }}
    </n-breadcrumb-item>
  </n-breadcrumb>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import type { LocationQueryRaw } from "vue-router";

export interface BreadcrumbItem {
  path?: string;
  name: string;
  query?: LocationQueryRaw;
}

defineProps<{
  items: BreadcrumbItem[];
}>();

const router = useRouter();

function onClick(item: BreadcrumbItem) {
  if (!item.path) return;
  router.push({ path: item.path, query: item.query });
}
</script>
