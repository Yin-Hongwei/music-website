<template>
  <div class="search-song-list">
    <play-list :playList="playList" path="song-sheet-detail"></play-list>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mixin from "@/mixins";
import PlayList from "@/components/PlayList.vue";
import { HttpManager } from "@/api";

export default {
  mixins: [mixin],
  components: {
    PlayList,
  },
  data() {
    return {
      playList: [],
    };
  },
  computed: {
    ...mapGetters(["searchWord"]),
  },
  watch: {
    searchWord(value) {
      this.getSearchList(value);
    },
  },
  mounted() {
    this.getSearchList(this.$route.query.keywords);
  },
  methods: {
    getSearchList(value) {
      if (!value) return;
      HttpManager.getSongListOfLikeTitle(value).then((res) => {
        if (!res.length) {
          this.$notify({
            title: "暂无该歌曲内容",
            type: "warning",
          });
        } else {
          this.playList = res;
        }
      });
    },
  },
};
</script>

<style scoped>
.search-song-list {
  height: 480px !important;
}
</style>
