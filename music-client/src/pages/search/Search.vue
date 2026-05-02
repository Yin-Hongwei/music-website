<template>
  <div class="search">
    <el-menu
      class="search-nav-menu"
      mode="horizontal"
      :ellipsis="false"
      :default-active="activeName"
      @select="onSearchMenuSelect"
    >
      <el-menu-item v-for="item in searchNavList" :key="item.value" :index="item.name">
        {{ item.name }}
      </el-menu-item>
    </el-menu>
    <component class="search-list" :is="currentView"></component>
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
  {
    name: "歌曲",
    value: "song",
  },
  {
    name: "歌单",
    value: "songList",
  },
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

function onSearchMenuSelect(index: string) {
  const item = searchNavList.find((n) => n.name === index);
  if (item) {
    handleChangeView(item);
  }
}

function handleChangeView(item: SearchNavItem) {
  activeName.value = item.name;
  currentViewType.value = item.value;
}

</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.search {
  margin: auto;
  width: 900px;

  .search-list {
    min-height: 480px;
  }
}

.search-nav-menu {
  width: 100%;
  height: auto;
  min-height: 48px;
  overflow-x: auto;
  overflow-y: hidden;
  flex-wrap: nowrap;
  background-color: transparent !important;
  border-bottom: none !important;
  border-right: none !important;
}

.search-nav-menu::-webkit-scrollbar {
  display: none;
}

.search-nav-menu :deep(.el-menu-item) {
  cursor: pointer;
  background-color: transparent !important;
  border-bottom: none !important;
}

.search-nav-menu :deep(.el-menu-item.is-active) {
  border-bottom: none !important;
  background-color: transparent !important;
}

.search-nav-menu :deep(.el-menu-item:hover),
.search-nav-menu :deep(.el-menu-item:focus) {
  background-color: transparent !important;
}
</style>
