<template>
  <el-container>
    <el-aside class="album-slide">
      <el-image class="singer-img" fit="contain" :src="attachImageUrl(songDetails.pic)" />
      <div class="album-info">
        <h2>基本资料</h2>
        <ul>
          <li v-if="songDetails.sex !== 2">性别：{{ getUserSex(songDetails.sex) }}</li>
          <li>生日：{{ getBirth(songDetails.birth) }}</li>
          <li>故乡：{{ songDetails.location }}</li>
        </ul>
      </div>
    </el-aside>
    <el-main class="album-main">
      <h1>{{ songDetails.name }}</h1>
      <p>{{ songDetails.introduction }}</p>
      <song-list :songList="currentSongList"></song-list>
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from "vue";
import SongList from "@/components/business/SongList.vue";
import { attachImageUrl, getBirth, getUserSex } from "@/utils";
import { fetchSongListBySingerId } from "@/api/song";
import { useSongStore } from "@/store/song";

const songStore = useSongStore();

const currentSongList = ref<any[]>([]);
const songDetails = computed(() => songStore.songDetails || {}) as any;

async function loadSingerDetailSongs(singerId: string | number | undefined) {
  if (!singerId) {
    currentSongList.value = [];
    return;
  }
  currentSongList.value = await fetchSongListBySingerId(singerId);
}

watch(
  () => songDetails.value.id,
  async (id) => {
    try {
      await loadSingerDetailSongs(id);
    } catch (error) {
      console.error("[SingerDetail] loadSingerDetailSongs failed:", error);
    }
  },
  { immediate: true },
);
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.album-slide {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 20px;

  .singer-img {
    height: 250px;
    width: 250px;
    border-radius: 10%;
  }

  .album-info {
    width: 60%;
    padding-top: 2rem;
    li {
      width: 100%;
      height: 30px;
      line-height: 30px;
    }
  }
}

.album-main {
  p {
    color: rgba(0, 0, 0, 0.5);
    margin: 10px 0 20px 0px;
  }
}

@media screen and (min-width: $sm) {
  .album-slide {
    position: fixed;
    width: 400px;
  }
  .album-main {
    min-width: 600px;
    padding-right: 10vw;
    margin-left: 400px;
  }
}

@media screen and (max-width: $sm) {
  .album-slide {
    display: none;
  }
}
</style>
