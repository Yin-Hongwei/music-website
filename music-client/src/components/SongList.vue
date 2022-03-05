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

<script lang="ts">
import { defineComponent, toRefs, computed } from "vue";
import mixin from "@/mixins/mixin";

export default defineComponent({
  props: {
    songList: Array,
  },
  setup(props) {
    const { getSongTitle, getSingerName, playMusic } = mixin();

    const { songList } = toRefs(props);
    const dataList = computed(() => {
      const list = [];
      songList.value.forEach((item: any, index) => {
        item["songName"] = getSongTitle(item.name);
        item["singerName"] = getSingerName(item.name);
        item["index"] = index;
        list.push(item);
      });
      return list;
    });

    function handleClick(row) {
      playMusic({
        id: row.id,
        url: row.url,
        pic: row.pic,
        index: row.index,
        name: row.name,
        lyric: row.lyric,
        currentSongList: songList.value,
      });
    }

    return {
      dataList,
      handleClick,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.content {
  background-color: $color-white;
  border-radius: $border-radius-songlist;
  padding: 3%;
}

.content:deep(.el-table__row.current-row) {
  color: $color-blue-active;
  font-weight: bold;
}

.content:deep(.el-table__row) {
  cursor: pointer;
}
</style>
