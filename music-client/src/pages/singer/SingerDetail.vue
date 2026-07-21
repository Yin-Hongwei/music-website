<template>
  <div class="page-shell">
    <el-row :gutter="40" class="detail-layout">
      <el-col :xs="24" :lg="7">
        <aside class="detail-aside">
          <el-image
            class="detail-cover cover-lift"
            fit="cover"
            :src="attachImageUrl(songDetails.pic)"
          />
          <div v-if="hasBasicInfo" class="detail-info">
            <h2 class="detail-info__title">基本资料</h2>
            <ul class="detail-info__list">
              <li v-if="sexLabel">性别：{{ sexLabel }}</li>
              <li v-if="birthLabel">生日：{{ birthLabel }}</li>
              <li v-if="locationLabel">地区：{{ locationLabel }}</li>
            </ul>
          </div>
        </aside>
      </el-col>
      <el-col :xs="24" :lg="17">
        <section class="detail-main">
          <h1 class="section-title">{{ songDetails.name }}</h1>
          <p class="detail-meta">{{ songDetails.introduction }}</p>
          <SongList :songList="currentSongList" />
        </section>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from "vue";
import { useRoute } from "vue-router";
import SongList from "@/components/business/SongList.vue";
import { attachImageUrl, getBirth, getSingerSexLabel } from "@/utils";
import { fetchSingerDetail } from "@/api/singer";
import { fetchSongListBySingerId } from "@/api/song";
import { useSongStore } from "@/store/song";

const route = useRoute();
const songStore = useSongStore();

const currentSongList = ref<any[]>([]);
const singerDetail = ref<Record<string, any>>({});

/** Prefer API-loaded detail so refresh / deep-link is not stuck on sessionStorage cache. */
const songDetails = computed(() => {
  if (singerDetail.value?.id != null) return singerDetail.value;
  const cached = songStore.songDetails;
  if (cached && String(cached.id) === String(route.params.id)) return cached;
  return {};
});

const sexLabel = computed(() => {
  const kind = Number(songDetails.value.kind);
  if (Number.isFinite(kind) && kind !== 0) return "";
  return getSingerSexLabel(songDetails.value.sex);
});
const birthLabel = computed(() => {
  const kind = Number(songDetails.value.kind);
  if (Number.isFinite(kind) && kind !== 0) return "";
  return getBirth(songDetails.value.birth) || "";
});
const locationLabel = computed(() => {
  const loc = String(songDetails.value.location || "").trim();
  return loc || "";
});
const hasBasicInfo = computed(
  () => Boolean(sexLabel.value || birthLabel.value || locationLabel.value),
);

async function loadSingerPage(singerId: string | number | undefined) {
  if (singerId == null || singerId === "") {
    singerDetail.value = {};
    currentSongList.value = [];
    return;
  }

  const [detail, songs] = await Promise.all([
    fetchSingerDetail(singerId),
    fetchSongListBySingerId(singerId),
  ]);

  if (detail) {
    singerDetail.value = detail;
    songStore.setSongDetails(detail);
  } else {
    singerDetail.value = {};
  }
  currentSongList.value = songs;
}

watch(
  () => route.params.id,
  async (id) => {
    try {
      await loadSingerPage(Array.isArray(id) ? id[0] : id);
    } catch (error) {
      console.error("[SingerDetail] loadSingerPage failed:", error);
    }
  },
  { immediate: true },
);
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.detail-layout {
  width: 100%;
}

.detail-aside {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  margin-bottom: 1.5rem;
}

.detail-cover {
  width: 100%;
  max-width: 320px;
  border-radius: 24px;
}

.detail-info {
  margin-top: 1.25rem;
  padding: 1rem 1.25rem;
  background: var(--surface);
  border-radius: 16px;
}

.detail-info__title {
  margin: 0 0 0.75rem;
  font-size: 1rem;
  font-weight: 600;
  color: var(--foreground);
}

.detail-info__list {
  margin: 0;
  padding: 0;
  list-style: none;

  li {
    display: block;
    width: 100%;
    padding: 0.35rem 0;
    color: var(--muted);
    font-size: 0.875rem;
    line-height: 1.5;
  }
}

.detail-meta {
  margin: -0.5rem 0 1.5rem;
  color: var(--muted);
  font-size: 0.9375rem;
  line-height: 1.6;
}

@media screen and (max-width: $sm) {
  .detail-cover {
    max-width: 100%;
  }
}
</style>
