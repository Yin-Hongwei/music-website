<template>
  <div class="search-song">
    <song-list :songList="currentSongList"></song-list>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mixin from "@/mixins";
import SongList from "@/components/SongList.vue";
import { HttpManager } from "@/api";

export default {
  mixins: [mixin],
  components: {
    SongList,
  },
  data() {
    return {
      currentSongList: [], // 存放的音乐
    };
  },
  computed: {
    ...mapGetters(["searchWord"]),
  },
  watch: {
    searchWord(value) {
      this.searchSong(value);
    },
  },
  mounted() {
    this.searchSong(this.$route.query.keywords);
  },
  methods: {
    // 搜索音乐
    searchSong(value) {
      if (!value) {
        this.currentSongList = [];
        return;
      }
      HttpManager.getSongOfSingerName(value)
        .then((res) => {
          if (!res.length) {
            this.currentSongList = [];
            this.$notify({
              title: "暂时没有相关歌曲",
              type: "warning",
            });
          } else {
            this.currentSongList = res;
          }
        })
        .catch((err) => {
          console.error(err);
        });
    },
  },
};
</script>

<style scoped>
.search-song {
  min-height: 480px;
}
</style>
