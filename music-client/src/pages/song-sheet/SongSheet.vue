<template>
  <div class="page-shell">
    <!-- <h1 class="section-title">歌单</h1> -->

    <el-space wrap class="pill-filters">
      <button
        v-for="item in songStyle"
        :key="item.id"
        type="button"
        class="pill-chip"
        :class="{ 'is-active': item.id === activeStyleId }"
        @click="handleChangeView(item)"
      >
        {{ item.name }}
      </button>
    </el-space>

    <div v-if="initialLoading" class="browse-skeleton">
      <el-skeleton animated>
        <template #template>
          <div class="browse-skeleton__grid">
            <el-skeleton-item
              v-for="n in 10"
              :key="n"
              variant="image"
              class="browse-skeleton__cover"
            />
          </div>
        </template>
      </el-skeleton>
    </div>

    <template v-else>
      <CoverCardGrid :playList="allPlayList" path="song-sheet-detail" />
      <div class="loading-tip loading" v-if="loading">加载中…</div>
      <div class="loading-tip no-more" v-else-if="!hasMore && allPlayList.length > 0">没有更多了</div>
    </template>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted, onBeforeUnmount } from "vue";
import { useRoute, useRouter } from "vue-router";
import CoverCardGrid from "@/components/business/CoverCardGrid.vue";
import type { SongStyleItem } from "@/api/models";
import { fetchSongSheetListByStylePage, fetchSongSheetListPage, fetchSongSheetStyleList } from "@/api/songSheet";

const route = useRoute();
const router = useRouter();

const activeStyleId = ref(0);
const pageSize = 20;
const currentPage = ref(1);
const songStyle = ref<SongStyleItem[]>([{ id: 0, name: "全部歌单" }]);
const allPlayList = ref<any[]>([]);
const hasMore = ref(true);
const loading = ref(false);
const initialLoading = ref(true);

function parseStyleId(value: unknown): number {
  const raw = Array.isArray(value) ? value[0] : value;
  const id = Number(raw);
  return Number.isFinite(id) && id >= 0 ? id : 0;
}

function resolveStyleId(value: unknown): number {
  const id = parseStyleId(value);
  return songStyle.value.some((item) => item.id === id) ? id : 0;
}

function syncStyleToRoute(styleId: number) {
  const currentId = parseStyleId(route.query.styleId);
  if (currentId === styleId) return;
  const nextQuery = { ...route.query };
  if (styleId === 0) {
    delete nextQuery.styleId;
  } else {
    nextQuery.styleId = String(styleId);
  }
  router.replace({ query: nextQuery });
}

async function getSongList(page: number) {
  return fetchSongSheetListPage(page, pageSize);
}

async function getSongListOfStyle(styleId: number, page: number) {
  return fetchSongSheetListByStylePage(styleId, page, pageSize);
}

async function loadMoreSongSheet() {
  if (loading.value || !hasMore.value) return;
  loading.value = true;
  try {
    const result =
      activeStyleId.value === 0
        ? await getSongList(currentPage.value)
        : await getSongListOfStyle(activeStyleId.value, currentPage.value);
    allPlayList.value = allPlayList.value.concat(result.records);
    hasMore.value = result.hasMore;
    currentPage.value += 1;
  } catch (error) {
    console.error("[SongSheet] loadMoreSongSheet failed:", error);
  } finally {
    loading.value = false;
  }
}

async function applyStyle(styleId: number) {
  activeStyleId.value = styleId;
  allPlayList.value = [];
  currentPage.value = 1;
  hasMore.value = true;
  initialLoading.value = true;
  try {
    await loadMoreSongSheet();
  } catch (error) {
    console.error("[SongSheet] applyStyle failed:", error);
  } finally {
    initialLoading.value = false;
  }
}

function handleChangeView(item: SongStyleItem) {
  if (item.id === activeStyleId.value) return;
  syncStyleToRoute(item.id);
}

function handleScroll() {
  if (loading.value || !hasMore.value || initialLoading.value) return;
  const scrollTop = window.scrollY || document.documentElement.scrollTop;
  const viewportHeight = window.innerHeight || document.documentElement.clientHeight;
  const fullHeight = document.documentElement.scrollHeight;
  const reachBottom = scrollTop + viewportHeight >= fullHeight - 120;
  if (reachBottom) {
    loadMoreSongSheet();
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

watch(
  () => route.query.styleId,
  (value) => {
    const styleId = resolveStyleId(value);
    if (styleId !== parseStyleId(value)) {
      syncStyleToRoute(styleId);
      return;
    }
    if (styleId === activeStyleId.value) return;
    applyStyle(styleId);
  },
);

onMounted(async () => {
  window.addEventListener("scroll", handleScroll, { passive: true });
  await loadSongStyle();
  const styleId = resolveStyleId(route.query.styleId);
  if (styleId !== parseStyleId(route.query.styleId)) {
    syncStyleToRoute(styleId);
  }
  await applyStyle(styleId);
});

onBeforeUnmount(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style scoped>
.browse-skeleton__grid {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.browse-skeleton__cover {
  width: calc(20% - 0.8rem);
  aspect-ratio: 1 / 1;
  border-radius: 16px;
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
  color: var(--accent);
  background: rgba(48, 164, 252, 0.12);
}

.no-more {
  color: var(--muted);
}

@media screen and (max-width: 668px) {
  .browse-skeleton__cover {
    width: calc(50% - 0.5rem);
  }
}
</style>
