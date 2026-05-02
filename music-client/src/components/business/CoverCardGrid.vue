<template>
  <div class="play-list">
    <div class="play-title" v-if="title">{{ title }}</div>
    <ul v-if="playList.length" class="play-body">
      <li class="card-frame" v-for="(item, index) in playList" :key="index">
        <div class="card" @click="goAlbum(item)">
          <el-image class="card-img" fit="contain" :src="attachImageUrl(item.pic)" />
          <div class="mask" @click="goAlbum(item)">
            <Play class="mask-icon" />
          </div>
        </div>
        <p class="card-name">{{ item.name || item.title }}</p>
      </li>
    </ul>
    <el-empty v-else description="暂无歌单数据" />
  </div>
</template>

<script setup lang="ts">
import { Play } from "lucide-vue-next";
import { useSongStore } from "../../store/song";
import { useAppActions } from "../../composables/useAppActions";
import { attachImageUrl } from "../../utils";

type CoverCardItem = any;

const props = withDefaults(
  defineProps<{
    title?: string;
    playList?: CoverCardItem[];
    path?: string;
  }>(),
  {
    title: "",
    playList: () => [],
    path: "",
  },
);

const { routerManager } = useAppActions();
const songStore = useSongStore();

function goAlbum(item: CoverCardItem) {
  songStore.setSongDetails(item);
  const routeBase = props.path;
  routerManager(routeBase, { path: `/${routeBase}/${item.id}` });
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.play-list {
  // padding: 0 1rem;
  .play-title {
    height: 60px;
    line-height: 60px;
    font-size: 28px;
    font-weight: 500;
    text-align: center;
    color: $color-black;
    box-sizing: border-box;
  }

  .play-body {
    @include layout(flex-start, stretch, row, wrap);
  }
}

.card-frame {
  .card {
    position: relative;
    height: 0;
    padding-bottom: 100%;
    overflow: hidden;
    border-radius: 5px;

    .card-img {
      width: 100%;
      transition: all 0.4s ease;
    }
  }

  .card-name {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    margin: 0.5rem 0;
  }

  &:hover .card-img {
    // transform: scale(1.2);
  }
}

.mask {
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 5px;
  background-color: rgba(52, 47, 41, 0.4);
  @include layout(center, center);
  transition: all 0.3s ease-in-out;
  opacity: 0;

  .mask-icon {
    width: 32px;
    height: 32px;
    color: rgba(240, 240, 240, 1);
  }

  &:hover {
    opacity: 1;
    cursor: pointer;
  }
}

@media screen and (min-width: $sm) {
  .card-frame {
    width: 18%;
    margin: 0.5rem 1%;
  }
}

@media screen and (max-width: $sm) {
  .card-frame {
    width: 46%;
    margin: 0.5rem 2%;
  }
}
</style>
