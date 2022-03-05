<template>
  <div class="search-song-list">
    <play-list :playList="playList" path="song-sheet-detail"></play-list>
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
import PlayList from "@/components/PlayList.vue";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    PlayList,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();

    const playList = ref([]);
    const searchWord = computed(() => store.getters.searchWord);
    watch(searchWord, (value) => {
      getSearchList(value);
    })


    async function getSearchList(value) {
      if (!value) return;
      const result = await HttpManager.getSongListOfLikeTitle(value) as any[];
      if (!result.length) {
        (proxy as any).$notify({
          title: "暂无该歌曲内容",
          type: "warning",
        });
      } else {
        playList.value = result;
      }
    }

    onMounted(() => {
      getSearchList(proxy.$route.query.keywords);
    });

    return {
      playList,
    };
  },
});
</script>

<style scoped>
.search-song-list {
  min-height: 480px;
}
</style>
