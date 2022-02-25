<template>
  <div class="content">
    <el-table
      style="width: 100%"
      stripe
      highlight-current-row
      :data="dataList"
      @row-click="handleClick"
    >
      <el-table-column type="index" width="50" />
      <el-table-column prop="songName" label="歌曲名" />
      <el-table-column prop="singerName" label="歌手" />
      <el-table-column prop="introduction" label="专辑" />
    </el-table>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mixin from "@/mixins";

export default {
  mixins: [mixin],
  props: {
    songList: Array,
  },
  computed: {
    dataList() {
      const list = [];
      for (let [index, item] of this.songList.entries()) {
        item["songName"] = this.getSongTitle(item.name);
        item["singerName"] = this.getSingerName(item.name);
        item["index"] = index;
        list.push(item);
      }
      return list;
    },
    ...mapGetters(["songId"]), // 音乐 ID
  },
  methods: {
    handleClick(row) {
      this.playMusic({
        id: row.id,
        url: row.url,
        pic: row.pic,
        index: row.index,
        name: row.name,
        lyric: row.lyric,
        currentSongList: this.songList,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.content {
  background-color: $color-white;
  border-radius: $border-radius-songlist;
  padding: 20px 40px;
  min-width: 700px;
}

.content:deep(.el-table__row.current-row) {
  color: $color-blue-active;
  font-weight: bold;
}
</style>
