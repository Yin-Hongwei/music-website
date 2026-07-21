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
            name: item.songName || item.name,
            singerName: item.singerName,
            lyric: item.lyric,
            currentSongList: currentPlayList,
            duration: item.duration,
          })
          ">
          {{ item.songName || item.name }}
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
const { playMusic } = useAppActions();

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
  top: var(--header-height);
  bottom: var(--player-height);
  z-index: 99;
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-left: 1px solid var(--border);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
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
  color: var(--foreground);
  font-weight: 600;
}

.control {
  margin: 3px 0;
  color: var(--muted);
}

.menus {
  width: 100%;
  height: calc(100% - 52px);
  cursor: pointer;
  z-index: 100;
  overflow: auto;
  white-space: nowrap;
  li {
    display: block;
    width: 100%;
    height: 40px;
    line-height: 40px;
    border-radius: 12px;
    margin: 0 8px;
    width: calc(100% - 16px);
    padding-left: 12px;
    color: var(--foreground);
    &:hover {
      background-color: rgba(0, 0, 0, 0.04);
    }
  }
}

.is-play {
  color: var(--accent);
  font-weight: 600;
}

</style>
