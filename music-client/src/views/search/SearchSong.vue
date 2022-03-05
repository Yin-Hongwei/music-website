<template>
  <div class="search-song">
    <song-list :songList="currentSongList"></song-list>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  computed,
  watch,
  onMounted,
  getCurrentInstance,
} from "vue";
import { useStore } from "vuex";
import SongList from "@/components/SongList.vue";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    SongList,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();

    const currentSongList = ref([]); // 存放的音乐
    const searchWord = computed(() => store.getters.searchWord);
    watch(searchWord, (value) => {
      searchSong(value);
    });

    // 搜索音乐
    async function searchSong(value) {
      if (!value) {
        currentSongList.value = [];
        return;
      }
      const result = (await HttpManager.getSongOfSingerName(value)) as any[];
      if (!result.length) {
        currentSongList.value = [];
        (proxy as any).$notify({
          title: "暂时没有相关歌曲",
          type: "warning",
        });
      } else {
        currentSongList.value = result;
      }
    }

    onMounted(() => {
      searchSong(proxy.$route.query.keywords);
    });

    return {
      currentSongList,
    };
  },
});
</script>

<style scoped>
.search-song {
  min-height: 480px;
}
</style>
