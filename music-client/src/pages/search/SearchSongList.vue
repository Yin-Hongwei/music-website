<template>
  <div class="search-song-list">
    <CoverCardGrid v-if="playList.length" :playList="playList" path="song-sheet-detail"></CoverCardGrid>
    <el-empty v-else description="暂无相关歌单" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

import CoverCardGrid from "../../components/business/CoverCardGrid.vue";
import { fetchSongListByLikeTitle } from "@/api/songSheet";
import { useSearchKeyword } from "../../composables/useSearchKeyword";

type SearchSongListItem = {
  id: string | number;
  title?: string;
  name?: string;
  pic: string;
};

const playList = ref<SearchSongListItem[]>([]);

async function getSearchList(keywords: string) {
  if (keywords === "") {
    playList.value = [];
    return;
  }
  const result = await fetchSongListByLikeTitle(keywords);
  const list = Array.isArray(result && result.data) ? result.data : [];
  if (!list.length) {
    playList.value = [];
  } else {
    playList.value = list;
  }
}

useSearchKeyword(getSearchList);
</script>
