<template>
  <div class="playbar">
    <YinAudio />
    <div class="playbar__progress-wrap">
      <el-slider
        class="playbar__progress"
        v-model="nowTime"
        :show-tooltip="false"
        @input="dragStart"
        @change="changeTime"
        size="small"
      />
    </div>
    <div class="playbar__content">
      <div class="playbar__meta">
        <button
          type="button"
          class="playbar__cover-wrap"
          aria-label="查看歌词"
          @click="goPlayerPage"
        >
          <el-image class="playbar__cover" fit="cover" :src="attachImageUrl(songPic)" />
        </button>
        <div class="playbar__meta-text">
          <p class="playbar__song">
            {{ songId ? `${songTitle} · ${singerName}` : "未选择歌曲" }}
          </p>
          <p class="playbar__time">{{ startTime }} / {{ endTime }}</p>
        </div>
      </div>

      <div class="playbar__controls">
        <button
          type="button"
          class="playbar__icon-btn hide-mobile"
          :class="{ 'is-accent': playMode !== PLAY_MODE_LIST_LOOP }"
          :title="playModeLabel"
          :aria-label="playModeLabel"
          @click="changePlayState"
        >
          <component :is="playModeIcon" />
        </button>
        <button type="button" class="playbar__icon-btn" aria-label="上一首" @click="prev">
          <SkipBack />
        </button>
        <button
          type="button"
          class="playbar__play-btn"
          aria-label="播放/暂停"
          @click="togglePlay"
        >
          <component :is="isPlay ? Pause : Play" class="playbar__play-icon" />
        </button>
        <button type="button" class="playbar__icon-btn" aria-label="下一首" @click="next">
          <SkipForward />
        </button>
        <div
          class="playbar__volume hide-mobile"
          @mouseenter="showVolume = true"
          @mouseleave="onVolumeLeave"
        >
          <button
            type="button"
            class="playbar__icon-btn"
            aria-label="音量"
            :aria-expanded="showVolume"
            @click="showVolume = !showVolume"
          >
            <component :is="volume !== 0 ? Volume2 : VolumeX" />
          </button>
          <div v-if="showVolume" class="playbar__volume-popup">
            <el-slider
              class="playbar__volume-slider"
              v-model="volume"
              :show-tooltip="false"
              @mousedown="volumeDragging = true"
              @mouseup="onVolumeDragEnd"
            />
          </div>
        </div>
      </div>

      <div class="playbar__actions">
        <button
          type="button"
          class="playbar__icon-btn"
          :class="{ 'is-accent': isCollection }"
          aria-label="喜欢"
          @click="changeCollection"
        >
          <Heart :fill="isCollection ? 'currentColor' : 'none'" />
        </button>
        <button
          type="button"
          class="playbar__icon-btn hide-mobile"
          aria-label="评论"
          @click="openCommentDialog"
        >
          <MessageCircle />
        </button>
        <button
          type="button"
          class="playbar__icon-btn hide-mobile"
          aria-label="下载"
          @click="
            downloadMusic({
              songUrl,
              songName: singerName + '-' + songTitle,
            })
          "
        >
          <Download />
        </button>
        <button
          type="button"
          class="playbar__icon-btn"
          aria-label="播放列表"
          @click="changeAside"
        >
          <ListMusic />
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import {
  Download,
  Heart,
  ListMusic,
  MessageCircle,
  Pause,
  Play,
  Repeat,
  Repeat1,
  Shuffle,
  SkipBack,
  SkipForward,
  Volume2,
  VolumeX,
} from "lucide-vue-next";
import YinAudio from "@/components/player/YinAudio.vue";
import { useConfigureStore } from "../../store/configure";
import { useUserStore } from "../../store/user";
import {
  PLAY_MODE_LIST_LOOP,
  PLAY_MODE_SHUFFLE,
  PLAY_MODE_SINGLE_LOOP,
  useSongStore,
} from "../../store/song";
import {
  fetchCollectionStatus,
  fetchDeleteCollection,
  fetchSetCollection,
} from "../../api/user";
import { elMessageTypeFromResponse } from "@/api/types";
import { formatSeconds } from "../../utils";
import { RouterName } from "../../enums";
import { useAppActions } from "../../composables/useAppActions";

const isDragging = ref(false);
const showVolume = ref(false);
const volumeDragging = ref(false);
const configureStore = useConfigureStore();
const userStore = useUserStore();
const songStore = useSongStore();
const { routerManager, playMusic, checkStatus, downloadMusic, attachImageUrl } =
  useAppActions();

const COLLECTION_TYPE_SONG = "0";
const PLAY_MODE_LIST = [
  PLAY_MODE_LIST_LOOP,
  PLAY_MODE_SINGLE_LOOP,
  PLAY_MODE_SHUFFLE,
];
const PLAY_MODE_LABEL_MAP = {
  [PLAY_MODE_LIST_LOOP]: "列表循环",
  [PLAY_MODE_SINGLE_LOOP]: "单曲循环",
  [PLAY_MODE_SHUFFLE]: "随机播放",
};

const nowTime = ref(0);
const volume = ref(50);

const isCollection = ref(false);
const userId = computed(() => userStore.userId);
const songId = computed(() => songStore.songId);
const token = computed(() => configureStore.token);
const isPlay = computed(() => songStore.isPlay);
const songUrl = computed(() => songStore.songUrl);
const songTitle = computed(() => songStore.songTitle);
const singerName = computed(() => songStore.singerName);
const songPic = computed(() => songStore.songPic);
const curTime = computed(() => songStore.curTime);
const duration = computed(() => songStore.duration);
const startTime = computed(() => formatSeconds(curTime.value));
const endTime = computed(() => formatSeconds(duration.value));
const currentPlayList = computed(() => songStore.currentPlayList);
const currentPlayIndex = computed(() => songStore.currentPlayIndex);
const autoNext = computed(() => songStore.autoNext);
const playMode = computed(() => songStore.playMode);
const playModeIcon = computed(() => {
  if (playMode.value === PLAY_MODE_SINGLE_LOOP) return Repeat1;
  if (playMode.value === PLAY_MODE_SHUFFLE) return Shuffle;
  return Repeat;
});
const playModeLabel = computed(() => PLAY_MODE_LABEL_MAP[playMode.value]);

watch(songId, initCollection);
watch(token, (value) => {
  if (!value) isCollection.value = false;
});
watch(volume, (value) => {
  songStore.setVolume(value / 100);
});
watch(
  [curTime, duration],
  () => {
    if (isDragging.value) return;
    nowTime.value =
      duration.value > 0 ? (curTime.value / duration.value) * 100 : 0;
  },
  { immediate: true },
);
watch(autoNext, () => {
  next();
});

async function initCollection() {
  if (!checkStatus(false)) return;
  const result = await fetchCollectionStatus({
    userId: userId.value,
    type: COLLECTION_TYPE_SONG,
    songId: songId.value,
  });
  isCollection.value = Boolean(result.data);
}

async function changeCollection() {
  if (!checkStatus()) return;
  const result = isCollection.value
    ? await fetchDeleteCollection(userId.value, songId.value)
    : await fetchSetCollection({
        userId: userId.value,
        type: COLLECTION_TYPE_SONG,
        songId: songId.value,
      });
  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });
  if (typeof result.data === "boolean") {
    isCollection.value = result.data;
  }
}

function openCommentDialog() {
  if (!checkStatus()) return;
  if (!songId.value) return;
  configureStore.setShowSongCommentDialog(true);
}

function changeAside() {
  configureStore.setShowAside(!configureStore.showAside);
}

function togglePlay() {
  songStore.setIsPlay(!isPlay.value);
}

function dragStart() {
  isDragging.value = true;
}

function changeTime() {
  isDragging.value = false;
  songStore.setChangeTime(duration.value * (nowTime.value * 0.01));
}

function onVolumeLeave() {
  if (volumeDragging.value) return;
  showVolume.value = false;
}

function onVolumeDragEnd() {
  volumeDragging.value = false;
  showVolume.value = false;
}

function changePlayState() {
  const currentIndex = PLAY_MODE_LIST.indexOf(playMode.value);
  const nextIndex =
    currentIndex >= PLAY_MODE_LIST.length - 1 ? 0 : currentIndex + 1;
  songStore.setPlayMode(PLAY_MODE_LIST[nextIndex]);
}

function getRandomPlayIndex(listLength: number) {
  let index = Math.floor(Math.random() * listLength);
  if (index === currentPlayIndex.value) {
    index = (index + 1) % listLength;
  }
  return index;
}

function toPlay(url: string) {
  if (!url || url === songUrl.value) return;
  const song = currentPlayList.value[currentPlayIndex.value];
  if (!song) return;
  playMusic({
    id: song.id,
    url,
    pic: song.pic,
    index: currentPlayIndex.value,
    name: song.name,
    lyric: song.lyric,
    currentSongList: currentPlayList.value,
    duration: song.duration,
  });
}

function prev() {
  if (playMode.value === PLAY_MODE_SHUFFLE) {
    const playIndex = getRandomPlayIndex(currentPlayList.value.length);
    songStore.setCurrentPlayIndex(playIndex);
    const targetSong = currentPlayList.value[playIndex];
    toPlay(targetSong ? targetSong.url : "");
    return;
  }

  if (currentPlayIndex.value === -1 || currentPlayList.value.length <= 1) return;
  const playIndex =
    currentPlayIndex.value > 0
      ? currentPlayIndex.value - 1
      : currentPlayList.value.length - 1;
  songStore.setCurrentPlayIndex(playIndex);
  const targetSong = currentPlayList.value[playIndex];
  toPlay(targetSong ? targetSong.url : "");
}

function next() {
  if (playMode.value === PLAY_MODE_SHUFFLE) {
    const playIndex = getRandomPlayIndex(currentPlayList.value.length);
    songStore.setCurrentPlayIndex(playIndex);
    const targetSong = currentPlayList.value[playIndex];
    toPlay(targetSong ? targetSong.url : "");
    return;
  }

  if (currentPlayIndex.value === -1 || currentPlayList.value.length <= 1) return;
  const playIndex =
    currentPlayIndex.value < currentPlayList.value.length - 1
      ? currentPlayIndex.value + 1
      : 0;
  songStore.setCurrentPlayIndex(playIndex);
  const targetSong = currentPlayList.value[playIndex];
  toPlay(targetSong ? targetSong.url : "");
}

function goPlayerPage() {
  if (!songId.value) return;
  routerManager(RouterName.Lyric, {
    path: `${RouterName.Lyric}/${songId.value}`,
  });
}

onMounted(() => {
  if (songId.value) {
    initCollection();
  }
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.playbar {
  position: fixed;
  z-index: 100;
  left: 0;
  right: 0;
  bottom: 0;
  border-top: 1px solid var(--border);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
}

.playbar__progress-wrap {
  position: relative;
  z-index: 1;
  height: 16px;
  margin-top: -8px;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.playbar__progress {
  width: 100%;
  height: 16px;
  margin: 0;

  :deep(.el-slider__runway) {
    height: 4px;
    margin: 0;
    border-radius: 0;
    background: rgba(0, 0, 0, 0.05);
  }

  :deep(.el-slider__bar) {
    height: 4px;
    border-radius: 0;
    background-color: var(--accent);
  }

  :deep(.el-slider__button-wrapper) {
    top: -14px;
    width: 28px;
    height: 28px;
  }

  :deep(.el-slider__button) {
    width: 12px;
    height: 12px;
    border-color: var(--accent);
    opacity: 0;
    transition: opacity 0.15s ease;
  }

  &:hover :deep(.el-slider__button),
  &:active :deep(.el-slider__button) {
    opacity: 1;
  }
}

.playbar__content {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 12px;
  height: calc(var(--player-height) - 4px);
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding: 0 16px;
  box-sizing: border-box;
}

.playbar__meta,
.playbar__controls,
.playbar__actions {
  display: flex;
  align-items: center;
  min-width: 0;
}

.playbar__meta {
  gap: 12px;
  justify-content: flex-start;
}

.playbar__meta-text {
  min-width: 0;
}

.playbar__cover-wrap {
  position: relative;
  width: 56px;
  height: 56px;
  flex: 0 0 56px;
  padding: 0;
  border: 0;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  background: var(--surface-tertiary);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.06);
}

.playbar__cover {
  width: 100%;
  height: 100%;
  display: block;
}

.playbar__song {
  margin: 0;
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--foreground);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.playbar__time {
  margin: 2px 0 0;
  font-size: 0.75rem;
  color: var(--muted);
}

.playbar__controls {
  justify-content: center;
  gap: 4px;
}

.playbar__actions {
  justify-content: flex-end;
  gap: 4px;
}

.playbar__icon-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  padding: 0;
  border: 0;
  border-radius: 999px;
  background: transparent;
  color: var(--foreground);
  cursor: pointer;
  transition: background 0.15s ease, color 0.15s ease;

  &:hover {
    background: rgba(0, 0, 0, 0.04);
  }

  svg {
    width: 16px;
    height: 16px;
  }

  &.is-accent {
    color: var(--accent);
  }
}

.playbar__controls .playbar__icon-btn svg {
  width: 18px;
  height: 18px;
}

.playbar__play-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  margin: 0 2px;
  padding: 0;
  border: 0;
  border-radius: 999px;
  background: var(--foreground);
  color: #fff;
  cursor: pointer;
  transition: background 0.15s ease;

  &:hover {
    background: #000;
  }
}

.playbar__play-icon {
  width: 20px;
  height: 20px;
  fill: currentColor;
}

.playbar__volume {
  position: relative;
}

.playbar__volume-popup {
  position: absolute;
  bottom: 100%;
  left: 50%;
  z-index: 10;
  width: 7rem;
  padding-bottom: 8px;
  transform: translateX(-50%);
}

.playbar__volume-slider {
  padding: 12px;
  border-radius: 12px;
  border: 1px solid var(--border);
  background: var(--surface);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);

  :deep(.el-slider__bar) {
    background-color: var(--accent);
  }

  :deep(.el-slider__button) {
    border-color: var(--accent);
  }
}

.hide-mobile {
  display: inline-flex;
}

@media screen and (min-width: 769px) {
  .playbar__content {
    padding: 0 28px;
  }
}

@media screen and (max-width: 768px) {
  .playbar__content {
    grid-template-columns: minmax(0, 1fr) auto auto;
    gap: 8px;
    padding: 0 10px;
  }

  .playbar__cover-wrap {
    width: 40px;
    height: 40px;
    flex-basis: 40px;
    border-radius: 8px;
  }

  .playbar__time {
    display: none;
  }

  .playbar__controls {
    gap: 0;
  }

  .playbar__play-btn {
    width: 40px;
    height: 40px;
  }

  .playbar__play-icon {
    width: 18px;
    height: 18px;
  }

  .hide-mobile {
    display: none !important;
  }
}
</style>
