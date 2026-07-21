<template>
  <div class="page-shell">
    <h1 class="section-title">搜索</h1>

    <el-space wrap class="pill-filters">
      <button
        v-for="item in searchNavList"
        :key="item.value"
        type="button"
        class="pill-chip"
        :class="{ 'is-active': item.name === activeName }"
        @click="handleChangeView(item)"
      >
        {{ item.name }}
      </button>
    </el-space>

    <component class="search-list" :is="currentView" />
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from "vue";
import { useRoute } from "vue-router";

import SearchSong from "./SearchSong.vue";
import SearchSongList from "./SearchSongList.vue";
import { useSearchStore } from "../../store/search";

type SearchNavItem = {
  name: string;
  value: "song" | "songList";
};

const searchNavList: SearchNavItem[] = [
  { name: "歌曲", value: "song" },
  { name: "歌单", value: "songList" },
];

const activeName = ref("歌曲");
const currentViewType = ref<"song" | "songList">("song");
const route = useRoute();
const searchStore = useSearchStore();

const viewMap = {
  song: SearchSong,
  songList: SearchSongList,
};
const currentView = computed(() => viewMap[currentViewType.value]);

function parseKeyword(value: any): string {
  if (Array.isArray(value)) return parseKeyword(value[0]);
  if (typeof value !== "string") return "";
  return value.trim();
}

watch(
  () => route.query.keywords,
  (value) => {
    searchStore.setKeyword(parseKeyword(value));
  },
  { immediate: true },
);

function handleChangeView(item: SearchNavItem) {
  activeName.value = item.name;
  currentViewType.value = item.value;
}
</script>

<style lang="scss" scoped>
.search-list {
  min-height: 320px;
}
</style>
