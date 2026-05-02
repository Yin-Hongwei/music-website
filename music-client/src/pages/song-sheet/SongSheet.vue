<template>
  <div class="play-list-container">
    <el-menu
      class="song-sheet-style-menu"
      mode="horizontal"
      :ellipsis="false"
      :default-active="activeName"
      @select="onStyleMenuSelect"
    >
      <el-menu-item v-for="item in songStyle" :key="`${item.id}-${item.name}`" :index="item.name">
        {{ item.name }}
      </el-menu-item>
    </el-menu>
    <CoverCardGrid :playList="allPlayList" path="song-sheet-detail"></CoverCardGrid>
    <div class="loading-tip loading" v-if="loading">加载中...</div>
    <div class="loading-tip no-more" v-else-if="!hasMore && allPlayList.length > 0">没有更多了</div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import CoverCardGrid from "@/components/business/CoverCardGrid.vue";
import type { SongStyleItem } from "@/api/models";
import { fetchSongSheetListByStylePage, fetchSongSheetListPage, fetchSongSheetStyleList } from "@/api/songSheet";

const activeName = ref("全部歌单");
const activeStyleId = ref(0);
const pageSize = 20;
const currentPage = ref(1);
const songStyle = ref<SongStyleItem[]>([{ id: 0, name: "全部歌单" }]); // 歌单导航栏类别
const allPlayList = ref<any[]>([]);
const hasMore = ref(true);
const loading = ref(false);

// 获取全部歌单
async function getSongList(page: number) {
  return fetchSongSheetListPage(page, pageSize);
}

// 通过类别获取歌单
async function getSongListOfStyle(style: string, page: number) {
  return fetchSongSheetListByStylePage(style, page, pageSize);
}

async function loadMoreSongSheet() {
  if (loading.value || !hasMore.value) return;
  loading.value = true;
  try {
    const result = activeStyleId.value === 0 ? await getSongList(currentPage.value) : await getSongListOfStyle(activeName.value, currentPage.value);
    allPlayList.value = allPlayList.value.concat(result.records);
    hasMore.value = result.hasMore;
    currentPage.value += 1;
  } catch (error) {
    console.error("[SongSheet] loadMoreSongSheet failed:", error);
  } finally {
    loading.value = false;
  }
}

function onStyleMenuSelect(index: string) {
  const item = songStyle.value.find((s) => s.name === index);
  if (item) {
    void handleChangeView(item);
  }
}

// 获取歌单
async function handleChangeView(item: SongStyleItem) {
  activeStyleId.value = item.id;
  activeName.value = item.name;
  allPlayList.value = [];
  currentPage.value = 1;
  hasMore.value = true;
  try {
    await loadMoreSongSheet();
  } catch (error) {
    console.error("[SongSheet] handleChangeView failed:", error);
  }
}

function handleScroll() {
  if (loading.value || !hasMore.value) return;
  const scrollTop = window.scrollY || document.documentElement.scrollTop;
  const viewportHeight = window.innerHeight || document.documentElement.clientHeight;
  const fullHeight = document.documentElement.scrollHeight;
  const reachBottom = scrollTop + viewportHeight >= fullHeight - 120;
  if (reachBottom) {
    loadMoreSongSheet();
  }
}

async function loadSongSheetData() {
  try {
    allPlayList.value = [];
    currentPage.value = 1;
    hasMore.value = true;
    await loadMoreSongSheet();
  } catch (error) {
    console.error("[SongSheet] loadSongSheetData failed:", error);
  }
}

async function loadSongStyle() {
  try {
    const styles = await fetchSongSheetStyleList();
    songStyle.value = styles.length > 0 ? styles : [{ id: 0, name: "全部歌单" }];
  } catch (error) {
    console.error("[SongSheet] loadSongStyle failed:", error);
  }
}

onMounted(() => {
  window.addEventListener("scroll", handleScroll, { passive: true });
  loadSongStyle();
  loadSongSheetData();
});

onBeforeUnmount(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style scoped>
.song-sheet-style-menu {
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

.song-sheet-style-menu::-webkit-scrollbar {
  display: none;
}

.song-sheet-style-menu :deep(.el-menu-item) {
  background-color: transparent !important;
  border-bottom: none !important;
}

.song-sheet-style-menu :deep(.el-menu-item.is-active) {
  border-bottom: none !important;
  background-color: transparent !important;
}

.song-sheet-style-menu :deep(.el-menu-item:hover),
.song-sheet-style-menu :deep(.el-menu-item:focus) {
  background-color: transparent !important;
}

.loading-tip {
  width: fit-content;
  margin: 18px auto 24px;
  padding: 8px 14px;
  border-radius: 999px;
  font-size: 13px;
  line-height: 1;
  text-align: center;
  letter-spacing: 0.5px;
}

.loading {
  color: #409eff;
  background: rgba(64, 158, 255, 0.12);
}

.no-more {
  color: #909399;
}
</style>
