<template>
  <div class="cover-grid">
    <h2 v-if="title" class="cover-grid__caption">{{ title }}</h2>
    <ul v-if="playList.length" class="cover-grid__body">
      <li class="cover-tile" v-for="(item, index) in playList" :key="item.id ?? index">
        <button type="button" class="cover-tile__btn" @click="goAlbum(item)">
          <el-image class="cover-lift" fit="cover" :src="attachImageUrl(item.pic)" />
          <p class="cover-tile__title">{{ item.name || item.title }}</p>
        </button>
      </li>
    </ul>
    <el-empty v-else description="暂无数据" />
  </div>
</template>

<script setup lang="ts">
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

.cover-grid {
  width: 100%;
}

.cover-grid__caption {
  margin: 0 0 1rem;
  font-size: 1.125rem;
  font-weight: 600;
  color: var(--foreground);
  letter-spacing: -0.01em;
}

.cover-grid__body {
  @include layout(flex-start, stretch, row, wrap);
  gap: 0;
  list-style: none;
  margin: 0;
  padding: 0;
  width: 100%;
}

.cover-tile {
  display: block;
  box-sizing: border-box;
}

.cover-tile__btn {
  display: block;
  width: 100%;
  padding: 0;
  border: 0;
  background: transparent;
  text-align: left;
  cursor: pointer;
}

.cover-tile__title {
  margin: 0.625rem 0 0;
  font-size: 0.875rem;
  font-weight: 500;
  line-height: 1.35;
  color: var(--foreground);
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.cover-lift {
  :deep(img) {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

@media screen and (min-width: $sm) {
  .cover-tile {
    width: 18%;
    margin: 0 1% 1.25rem;
  }
}

@media screen and (max-width: $sm) {
  .cover-tile {
    width: 46%;
    margin: 0 2% 1rem;
  }
}
</style>
