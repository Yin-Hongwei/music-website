<template>
  <transition name="aside-fade">
    <div class="yin-play-queue" v-if="showAside">
      <h2 class="title">当前播放</h2>
      <div class="control">
        共 {{ (currentPlayList && currentPlayList.length) || 0 }} 首
      </div>
      <ul class="menus">
        <li v-for="(item, index) in currentPlayList" :class="{ 'is-play': songId === item.id }" :key="index" @click="
          playMusic({
            id: item.id,
            url: item.url,
            pic: item.pic,
            index: index,
            name: item.name,
            lyric: item.lyric,
            currentSongList: currentPlayList,
          })
          ">
          {{ getSongTitle(item.name) }}
        </li>
      </ul>
    </div>
  </transition>
</template>

<script lang="ts" setup>
import { computed, onMounted } from "vue";
import { useConfigureStore } from "../../store/configure";
import { useSongStore } from "../../store/song";
import { useAppActions } from "../../composables/useAppActions";

const configureStore = useConfigureStore();
const songStore = useSongStore();
const { getSongTitle, playMusic } = useAppActions();

const songId = computed(() => songStore.songId); // 音乐 ID
const currentPlayList = computed(() => songStore.currentPlayList); // 当前播放
const showAside = computed(() => configureStore.showAside); // 是否显示侧边栏

const handleDocumentClick = (event: MouseEvent) => {
  const target = event.target as HTMLElement | null;
  if (!target) return;
  if (target.closest(".yin-play-queue")) return;
  if (target.closest(".playbar__toggle-aside-btn")) return;
  configureStore.setShowAside(false);
};

onMounted(() => {
  document.addEventListener("click", handleDocumentClick, true);
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.aside-fade-enter-active {
  transition: all 0.3s ease;
}

.aside-fade-leave-active {
  transition: all 0.2s ease;
}

.aside-fade-enter,
.aside-fade-leave-to {
  transform: translateX(10px);
  opacity: 0;
}

.yin-play-queue {
  font-size: 14px;
  width: $current-play;
  position: fixed;
  right: 0;
  top: $header-height;
  bottom: 0;
  padding-bottom: $footer-height;
  z-index: 99;
  background-color: $color-white;
  box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.4);
  overflow: hidden;
}

.title,
.control,
.menus li {
  padding-left: 20px;
  box-sizing: border-box;
}

.title {
  margin: 10px 0;
}

.control {
  margin: 3px 0;
  color: $color-grey;
}

.menus {
  width: 100%;
  height: calc(100% - 19px);
  cursor: pointer;
  z-index: 100;
  overflow: scroll;
  white-space: nowrap;
  li {
    display: block;
    width: 100%;
    height: 40px;
    line-height: 40px;
    &:hover {
      background-color: $color-light-grey;
    }
  }
}

.is-play {
  color: $color-black;
  font-weight: bold;
}

</style>
