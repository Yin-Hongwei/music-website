<template>
  <div class="search-song">
    <SongList v-if="currentSongList.length" :songList="currentSongList"></SongList>
    <el-empty v-else description="暂无相关歌曲" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

import SongList from "../../components/business/SongList.vue";
import { fetchSongBySingerName } from "@/api/song";
import { useSearchKeyword } from "../../composables/useSearchKeyword";

type SearchSongItem = {
  id: string | number;
  name: string;
  url: string;
  pic: string;
  lyric?: string;
  introduction?: string;
};

const currentSongList = ref<SearchSongItem[]>([]); // 存放的音乐

// 搜索音乐
async function searchSong(keywords: string) {
  if (keywords === "") {
    currentSongList.value = [];
    return;
  }
  const result = await fetchSongBySingerName(keywords);
  const list = Array.isArray(result && result.data) ? result.data : [];
  if (!list.length) {
    currentSongList.value = [];
  } else {
    currentSongList.value = list;
  }
}

useSearchKeyword(searchSong);
</script>
