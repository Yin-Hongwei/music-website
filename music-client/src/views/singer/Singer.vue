<template>
  <div class="singer">
    <ul class="singer-header">
      <li
        v-for="(item, index) in singerStyle"
        :key="index"
        :class="{ active: item.name === activeName }"
        @click="handleChangeView(item)"
      >
        {{ item.name }}
      </li>
    </ul>
    <play-list :playList="data" path="singer-detail"></play-list>
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

<script lang="ts" setup>
import { ref, computed } from "vue";
import PlayList from "@/components/PlayList.vue";
import { singerStyle } from "@/enums";
import { HttpManager } from "@/api";

// data
const activeName = ref("全部歌手");
const pageSize = ref(15); // 页数
const currentPage = ref(1); // 当前页
const allPlayList = ref([]);
// computed
const data = computed(() => {
  return allPlayList.value.slice(
    (currentPage.value - 1) * pageSize.value,
    currentPage.value * pageSize.value
  );
});

// 获取所有歌手
async function getAllSinger() {
  const result = await HttpManager.getAllSinger() as any[];
  currentPage.value = 1;
  allPlayList.value = result;
}

getAllSinger();

// 获取当前页
function handleCurrentChange(val) {
  currentPage.value = val;
}

function handleChangeView(item) {
  activeName.value = item.name;
  allPlayList.value = [];
  if (item.name === "全部歌手") {
    getAllSinger();
  } else {
    getSingerSex(item.type);
  }
}

// 通过性别对歌手分类
async function getSingerSex(sex) {
  const result = await HttpManager.getSingerOfSex(sex) as any[];
  currentPage.value = 1;
  allPlayList.value = result;
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

@media screen and (min-width: $sm) {
  .singer-header {
    li {
      margin: 0.5rem 1rem;
    }
  }
}

@media screen and (max-width: $sm) {
  .singer-header {
    li {
      margin: 0.3rem 0.4rem;
    }
  }
}

div,
ul,
li {
  box-sizing: border-box;
}

.singer {
  margin: 30px 8%;
  margin-top: 0;
  padding-top: $header-height;
  padding-bottom: 50px;
  background-color: $color-white;
  .singer-header {
    width: 100%;
    li {
      display: inline-block;
      font-size: 1rem;
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
}

.pagination {
  @include layout(center);
  transform: translateY(15px);
}
</style>
