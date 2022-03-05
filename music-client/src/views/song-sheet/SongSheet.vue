<template>
  <div class="song-sheet">
    <ul class="song-sheet-header">
      <li
        v-for="(item, index) in songStyle"
        :key="index"
        :class="{ active: item.name === activeName }"
        @click="handleChangeView(item)"
      >
        {{ item.name }}
      </li>
    </ul>
    <play-list :playList="data" path="song-sheet-detail"></play-list>
    <div class="pagination">
      <el-pagination
        @current-change="handleCurrentChange"
        background
        layout="total, prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="allPlayList.length"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from "vue";
import PlayList from "@/components/PlayList.vue";
import { SONGSTYLE } from "@/enums";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    PlayList,
  },
  setup() {
    const activeName = ref("全部歌单");
    const pageSize = ref(15); // 页数
    const currentPage = ref(1); // 当前页
    const songStyle = ref(SONGSTYLE); // 歌单导航栏类别
    const allPlayList = ref([]); // 歌单
    const data = computed(() => allPlayList.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value));

    // 获取全部歌单
    async function getSongList() {
      allPlayList.value = (await HttpManager.getSongList()) as any[];
      currentPage.value = 1;
    }
    // 通过类别获取歌单
    async function getSongListOfStyle(style) {
      allPlayList.value = (await HttpManager.getSongListOfStyle(style)) as any[];
      currentPage.value = 1;
    }
    
    try {
      getSongList();
    } catch (error) {
      console.error(error);
    }

    // 获取歌单
    async function handleChangeView(item) {
      activeName.value = item.name;
      allPlayList.value = [];
      try {
        if (item.name === "全部歌单") {
          await getSongList();
        } else {
          await getSongListOfStyle(item.name);
        }
      } catch (error) {
        console.error(error);
      }
    }
    // 获取当前页
    function handleCurrentChange(val) {
      currentPage.value = val;
    }
    return {
      activeName,
      songStyle,
      pageSize,
      currentPage,
      allPlayList,
      data,
      handleChangeView,
      handleCurrentChange
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

@media screen and (min-width: $sm) {
  .song-sheet-header {
    li {
      margin: 0.5rem 1rem;
    }
  }
}

@media screen and (max-width: $sm) {
  .song-sheet-header {
    li {
      margin: 0.3rem 0.4rem;
    }
  }
}

.song-sheet {
  margin: 30px 8%;
  margin-top: 0;
  padding-top: $header-height;
  padding-bottom: 50px;
  background-color: $color-white;
}

.song-sheet-header {
  width: 100%;
  li {
    display: inline-block;
    line-height: 2.5rem;
    font-size: 1rem;
    color: $color-grey;
    border-bottom: none;
    cursor: pointer;
  }
  li.active {
    color: $color-black;
    font-weight: 600;
    border-bottom: 0.2rem solid $color-black;
  }
}

.pagination {
  @include layout(center);
  transform: translateY(15px);
}
</style>
