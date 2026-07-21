<template>
  <div class="page-shell">
    <!-- <h1 class="section-title">歌手</h1> -->

    <el-space wrap class="pill-filters">
      <button
        v-for="item in singerStyle"
        :key="`${item.id}-${item.name}`"
        type="button"
        class="pill-chip"
        :class="{ 'is-active': item.type === activeType }"
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
      <CoverCardGrid :playList="visiblePlayList" path="singer-detail" />
      <div class="loading-tip loading" v-if="loading">加载中…</div>
      <div class="loading-tip no-more" v-else-if="!hasMore && visiblePlayList.length > 0">没有更多了</div>
    </template>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted, onUnmounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import CoverCardGrid from "@/components/business/CoverCardGrid.vue";
import type { SingerStyleItem } from "@/api/models";
import { fetchAllSingerList, fetchSingerListByType, fetchSingerStyleList } from "@/api/singer";

const route = useRoute();
const router = useRouter();

const ALL_TYPE = -1;
const activeType = ref(ALL_TYPE);
const pageSize = 15;
const currentPage = ref(1);
const allPlayList = ref<any[]>([]);
const visiblePlayList = ref<any[]>([]);
const singerStyle = ref<SingerStyleItem[]>([{ id: 0, name: "全部歌手", type: ALL_TYPE }]);
const hasMore = ref(true);
const loading = ref(false);
const initialLoading = ref(true);

function parseType(value: unknown): number {
  if (value == null || value === "") return ALL_TYPE;
  const raw = Array.isArray(value) ? value[0] : value;
  const type = Number(raw);
  return Number.isFinite(type) ? type : ALL_TYPE;
}

function resolveType(value: unknown): number {
  const type = parseType(value);
  return singerStyle.value.some((item) => item.type === type) ? type : ALL_TYPE;
}

function syncTypeToRoute(type: number) {
  const currentType = parseType(route.query.type);
  if (currentType === type) return;
  const nextQuery = { ...route.query };
  if (type === ALL_TYPE) {
    delete nextQuery.type;
  } else {
    nextQuery.type = String(type);
  }
  router.replace({ query: nextQuery });
}

async function getAllSinger() {
  allPlayList.value = await fetchAllSingerList();
}

async function getSingerByType(type: string | number) {
  allPlayList.value = await fetchSingerListByType(type);
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

async function applyType(type: number) {
  activeType.value = type;
  visiblePlayList.value = [];
  initialLoading.value = true;
  try {
    if (type === ALL_TYPE) {
      await getAllSinger();
    } else {
      await getSingerByType(type);
    }
    resetVisibleList();
  } catch (error) {
    console.error("[Singer] applyType failed:", error);
  } finally {
    initialLoading.value = false;
  }
}

function handleChangeView(item: SingerStyleItem) {
  if (item.type === activeType.value) return;
  syncTypeToRoute(item.type);
}

function handleScroll() {
  if (loading.value || !hasMore.value || initialLoading.value) return;
  const scrollTop = window.scrollY || document.documentElement.scrollTop;
  const viewportHeight = window.innerHeight || document.documentElement.clientHeight;
  const fullHeight = document.documentElement.scrollHeight;
  if (scrollTop + viewportHeight >= fullHeight - 120) {
    loadMoreSinger();
  }
}

async function loadSingerStyle() {
  try {
    const styles = await fetchSingerStyleList();
    singerStyle.value =
      styles.length > 0 ? styles : [{ id: 0, name: "全部歌手", type: ALL_TYPE }];
  } catch (error) {
    console.error("[Singer] loadSingerStyle failed:", error);
  }
}

watch(
  () => route.query.type,
  (value) => {
    const type = resolveType(value);
    if (type !== parseType(value)) {
      syncTypeToRoute(type);
      return;
    }
    if (type === activeType.value) return;
    void applyType(type);
  },
);

onMounted(async () => {
  window.addEventListener("scroll", handleScroll, { passive: true });
  await loadSingerStyle();
  const type = resolveType(route.query.type);
  if (type !== parseType(route.query.type)) {
    syncTypeToRoute(type);
  }
  await applyType(type);
});

onUnmounted(() => {
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
