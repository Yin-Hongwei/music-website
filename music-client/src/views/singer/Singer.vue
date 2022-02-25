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

<script setup>
import { ref, computed } from "vue";
import PlayList from "@/components/PlayList";
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
function getAllSinger() {
  HttpManager.getAllSinger()
    .then((res) => {
      currentPage.value = 1;
      allPlayList.value = res;
    })
    .catch((err) => {
      console.error(err);
    });
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
function getSingerSex(sex) {
  HttpManager.getSingerOfSex(sex)
    .then((res) => {
      currentPage.value = 1;
      allPlayList.value = res;
    })
    .catch((err) => {
      console.error(err);
    });
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

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
      line-height: 3rem;
      margin: 1rem 3rem 1rem 3rem;
      font-size: 1.2rem;
      font-weight: 400;
      color: $color-grey;
      border-bottom: none;
      cursor: pointer;
    }
    li.active {
      color: $color-black;
      font-weight: 600;
      border-bottom: 4px solid $color-black;
    }
  }
}

.pagination {
  @include layout(center);
  transform: translateY(15px);
}
</style>
