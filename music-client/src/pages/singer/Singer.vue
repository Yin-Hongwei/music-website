<template>
  <div class="play-list-container">
    <el-menu
      class="singer-style-menu"
      mode="horizontal"
      :ellipsis="false"
      :default-active="activeName"
      @select="onSingerMenuSelect"
    >
      <el-menu-item v-for="item in singerStyle" :key="`${item.id}-${item.name}`" :index="item.name">
        {{ item.name }}
      </el-menu-item>
    </el-menu>
    <CoverCardGrid :playList="visiblePlayList" path="singer-detail"></CoverCardGrid>
    <div class="loading-tip loading" v-if="loading">加载中...</div>
    <div class="loading-tip no-more" v-else-if="!hasMore && visiblePlayList.length > 0">没有更多了</div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from "vue";
import CoverCardGrid from "@/components/business/CoverCardGrid.vue";
import type { SingerStyleItem } from "@/api/models";
import { fetchAllSingerList, fetchSingerListBySex, fetchSingerStyleList } from "@/api/singer";

// data
const activeName = ref("全部歌手");
const pageSize = 15;
const currentPage = ref(1);
const allPlayList = ref<any[]>([]);
const visiblePlayList = ref<any[]>([]);
const singerStyle = ref<SingerStyleItem[]>([{ id: 0, name: "全部歌手", type: -1 }]);
const hasMore = ref(true);
const loading = ref(false);

// 获取所有歌手
async function getAllSinger() {
  allPlayList.value = await fetchAllSingerList();
}

async function loadSingerData() {
  try {
    singerStyle.value = await fetchSingerStyleList();
    await getAllSinger();
  } catch (error) {
    console.error("[Singer] loadSingerData failed:", error);
  }
  resetVisibleList();
}

function onSingerMenuSelect(index: string) {
  const item = singerStyle.value.find((s) => s.name === index);
  if (item) {
    void handleChangeView(item);
  }
}

async function handleChangeView(item: SingerStyleItem) {
  activeName.value = item.name;
  visiblePlayList.value = [];
  try {
    if (Number(item.type) === -1) {
      await getAllSinger();
    } else {
      await getSingerSex(item.type);
    }
  } catch (error) {
    console.error("[Singer] handleChangeView failed:", error);
  }
}

// 通过性别对歌手分类
async function getSingerSex(sex: string | number) {
  allPlayList.value = await fetchSingerListBySex(sex);
  resetVisibleList();
}

function resetVisibleList() {
  currentPage.value = 1;
  visiblePlayList.value = [];
  hasMore.value = allPlayList.value.length > 0;
  loadMoreSinger();
}

function loadMoreSinger() {
  if (loading.value || !hasMore.value) return;
  loading.value = true;
  const start = (currentPage.value - 1) * pageSize;
  const end = currentPage.value * pageSize;
  const chunk = allPlayList.value.slice(start, end);
  visiblePlayList.value = visiblePlayList.value.concat(chunk);
  currentPage.value += 1;
  hasMore.value = end < allPlayList.value.length;
  loading.value = false;
}

function handleScroll() {
  if (loading.value || !hasMore.value) return;
  const scrollTop = window.scrollY || document.documentElement.scrollTop;
  const viewportHeight = window.innerHeight || document.documentElement.clientHeight;
  const fullHeight = document.documentElement.scrollHeight;
  if (scrollTop + viewportHeight >= fullHeight - 120) {
    loadMoreSinger();
  }
}

onMounted(() => {
  window.addEventListener("scroll", handleScroll, { passive: true });
  loadSingerData();
});

onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style scoped>
.singer-style-menu {
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

.singer-style-menu::-webkit-scrollbar {
  display: none;
}

.singer-style-menu :deep(.el-menu-item) {
  cursor: pointer;
  background-color: transparent !important;
  border-bottom: none !important;
}

.singer-style-menu :deep(.el-menu-item.is-active) {
  border-bottom: none !important;
  background-color: transparent !important;
}

.singer-style-menu :deep(.el-menu-item:hover),
.singer-style-menu :deep(.el-menu-item:focus) {
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
