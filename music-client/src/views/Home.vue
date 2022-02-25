<template>
  <!--轮播图-->
  <el-carousel class="swiper-container" type="card" height="20vw" :interval="4000">
    <el-carousel-item v-for="(item, index) in swiperList" :key="index">
      <img :src="item.picImg" />
    </el-carousel-item>
  </el-carousel>
  <!--热门歌单-->
  <play-list class="section" title="歌单" path="song-sheet-detail" :playList="songList"></play-list>
  <!--热门歌手-->
  <play-list class="section" title="歌手" path="singer-detail" :playList="singerList"></play-list>
</template>

<script setup>
import { ref } from "vue";
import PlayList from "@/components/PlayList";
import { swiperList } from "@/enums";
import { HttpManager } from "@/api";

const songList = ref([]); // 歌单列表
const singerList = ref([]); // 歌手列表

try {
  HttpManager.getSongList().then((res) => {
    songList.value = res.sort().slice(0, 10);
  });

  HttpManager.getAllSinger().then((res) => {
    singerList.value = res.sort().slice(0, 10);
  });
} catch (error) {
  console.error(error);
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.swiper-container {
  width: 90%;
  margin: auto;
  padding-top: 100px;
  img {
    width: 100%;
  }
}

.section {
  width: 100%;
  margin-top: 20px;
  padding: $content-padding;
  background-color: $color-white;
  box-sizing: border-box;
}
</style>
