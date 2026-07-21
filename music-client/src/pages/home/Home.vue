<template>
  <div class="page-shell home-page">
    <section v-if="loading" class="home-skeleton">
      <el-skeleton animated>
        <template #template>
          <el-skeleton-item variant="image" class="home-skeleton__banner" />
          <div class="home-skeleton__grid">
            <el-skeleton-item
              v-for="n in 5"
              :key="n"
              variant="image"
              class="home-skeleton__cover"
            />
          </div>
        </template>
      </el-skeleton>
    </section>

    <template v-else>
      <el-carousel
        v-if="swiperList.length"
        class="home-banner"
        height="280px"
        :interval="4000"
        arrow="hover"
      >
        <el-carousel-item v-for="(item, index) in swiperList" :key="index">
          <button type="button" class="home-banner__slide" @click="handleBannerClick(item.url)">
            <img :src="attachImageUrl(item.pic)" alt="" />
          </button>
        </el-carousel-item>
      </el-carousel>

      <h1 class="section-title">热门歌单</h1>
      <CoverCardGrid path="song-sheet-detail" :playList="songList" />
    </template>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";

import CoverCardGrid from "@/components/business/CoverCardGrid.vue";
import { fetchBannerList } from "@/api/banner";
import { fetchRecommendedSongList, fetchSongList } from "@/api/songSheet";
import { attachImageUrl } from "@/utils";

const songList = ref([]);
const swiperList = ref([]);
const loading = ref(true);

async function loadHomeData() {
  loading.value = true;
  try {
    const [banners, recommendedSongs] = await Promise.all([
      fetchBannerList(),
      fetchRecommendedSongList(10),
    ]);

    swiperList.value = banners;
    songList.value = recommendedSongs.length ? recommendedSongs : await fetchSongList(10);
  } catch (error) {
    console.error("[Home] loadHomeData failed:", error);
  } finally {
    loading.value = false;
  }
}

loadHomeData();

function handleBannerClick(url?: string) {
  const target = (url || "").trim();
  if (!target) return;
  if (/^https?:\/\//i.test(target)) {
    window.open(target, "_blank");
    return;
  }
  window.location.href = target.startsWith("/") ? target : `/${target}`;
}
</script>

<style lang="scss" scoped>
.home-banner {
  margin-bottom: 2rem;
  border-radius: 16px;
  overflow: hidden;

  :deep(.el-carousel__container) {
    border-radius: 16px;
  }

  :deep(.el-carousel__indicators) {
    transform: none;
  }
}

.home-banner__slide {
  display: block;
  width: 100%;
  height: 100%;
  padding: 0;
  border: 0;
  background: var(--surface-tertiary);
  cursor: pointer;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
  }
}

.home-skeleton__banner {
  width: 100%;
  height: 280px;
  border-radius: 16px;
  margin-bottom: 2rem;
}

.home-skeleton__grid {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.home-skeleton__cover {
  width: calc(20% - 0.8rem);
  aspect-ratio: 1 / 1;
  border-radius: 16px;
}

@media screen and (max-width: 668px) {
  .home-banner {
    :deep(.el-carousel__container) {
      height: 160px !important;
    }
  }

  .home-skeleton__banner {
    height: 160px;
  }

  .home-skeleton__cover {
    width: calc(50% - 0.5rem);
  }
}
</style>
