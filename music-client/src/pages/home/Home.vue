<template>
  <div>
    <!--轮播图-->
    <el-carousel v-if="swiperList.length" class="swiper-container" type="card" height="20vw" :interval="4000">
      <el-carousel-item v-for="(item, index) in swiperList" :key="index">
        <img :src="attachImageUrl(item.pic)" @click="handleBannerClick(item.url)" />
      </el-carousel-item>
    </el-carousel>
    <!--热门歌单-->
    <CoverCardGrid class="play-list-container" title="热门歌单" path="song-sheet-detail" :playList="songList"/>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";

import CoverCardGrid from "@/components/business/CoverCardGrid.vue";
import { fetchBannerList } from "@/api/banner";
import { fetchRecommendedSongList, fetchSongList } from "@/api/songSheet";
import { attachImageUrl } from "@/utils";

const songList = ref([]); // 歌单列表
const swiperList = ref([]); // 轮播图 每次都在进行查询

async function loadHomeData() {
  try {
    const [banners, recommendedSongs] = await Promise.all([
      fetchBannerList(),
      fetchRecommendedSongList(10),
    ]);

    swiperList.value = banners;
    songList.value = recommendedSongs.length ? recommendedSongs : await fetchSongList(10);
  } catch (error) {
    console.error("[Home] loadHomeData failed:", error);
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
@import "@/assets/css/var.scss";

/*轮播图*/
.swiper-container {
  width: 90%;
  margin: auto;
  padding-top: 20px;

  img {
    width: 100%;
    cursor: pointer;
  }

}

.swiper-container:deep(.el-carousel__indicators.el-carousel__indicators--outside) {
  display: inline-block;
  transform: translateX(30vw);
}

.el-slider__runway {
  background-color: $color-blue;
}
</style>
