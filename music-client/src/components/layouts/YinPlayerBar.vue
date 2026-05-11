<template>
  <div class="playbar" :class="{ 'is-collapsed': !toggle }">
    <YinAudio />
    <div class="playbar__toggle" :class="{ 'is-rotated': toggle }">
      <ChevronDown class="playbar__icon-btn" @click="toggle = !toggle" />
    </div>
    <el-slider
      class="playbar__progress"
      v-model="nowTime"
      :format-tooltip="formatProgressTooltip"
      @input="dragStart"
      @change="changeTime"
      size="small"
    />
    <div class="playbar__content">
      <div class="playbar__meta">
        <div class="playbar__cover-wrap" @click="goPlayerPage">
          <el-image class="playbar__cover" fit="contain" :src="attachImageUrl(songPic)" />
          <Maximize2 class="playbar__cover-hover-icon" />
        </div>
        <div v-if="songId">
          <div class="playbar__song">{{ songTitle }} - {{ singerName }}</div>
          <div class="playbar__time">{{ startTime }} / {{ endTime }}</div>
        </div>
      </div>
      <div class="playbar__controls">
        <component
          :is="playStateIndex === 0 ? Repeat : Shuffle"
          class="playbar__icon-btn hide-mobile"
          @click="changePlayState"
        />
        <SkipBack class="playbar__icon-btn" @click="prev" />
        <component :is="isPlay ? Pause : Play" class="playbar__icon-btn" @click="togglePlay" />
        <SkipForward class="playbar__icon-btn" @click="next" />
        <ListMusic class="playbar__icon-btn playbar__toggle-aside-btn hide-mobile" @click="changeAside" />
      </div>
      <div class="playbar__actions">
        <span class="playbar__action-item playbar__lyric-entry show-mobile" @click="goPlayerPage">词</span>
        <span class="playbar__action-item" :class="{ 'is-active': isCollection }" @click="changeCollection">
          <Heart class="playbar__action-icon" :fill="isCollection ? 'currentColor' : 'none'" />
        </span>
        <span class="playbar__action-item hide-mobile" @click="openCommentDialog">
          <MessageCircle class="playbar__action-icon" />
        </span>
        <el-dropdown class="playbar__action-item hide-mobile" trigger="click">
          <component :is="volume !== 0 ? Volume2 : VolumeX" class="playbar__action-icon" />
          <template #dropdown>
            <el-dropdown-menu>
              <el-slider class="playbar__volume-slider" style="height: 150px; margin: 10px 0" v-model="volume"
                :vertical="true" />
              <div class="playbar__volume-percent">{{ volume }}%</div>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-dropdown class="playbar__action-item hide-mobile" trigger="click">
          <Ellipsis class="playbar__action-icon" />
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="
                downloadMusic({
                  songUrl,
                  songName: singerName + '-' + songTitle,
                })
                ">
                <Download class="playbar__menu-icon" />
                <span>下载</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import {
  Download,
  Ellipsis,
  Heart,
  ChevronDown,
  ListMusic,
  MessageCircle,
  Maximize2,
  Pause,
  Play,
  Repeat,
  Shuffle,
  SkipBack,
  SkipForward,
  Volume2,
  VolumeX,
} from "lucide-vue-next";
import YinAudio from "@/components/player/YinAudio.vue";
import { useConfigureStore } from "../../store/configure";
import { useUserStore } from "../../store/user";
import { useSongStore } from "../../store/song";
import {
  fetchCollectionStatus,
  fetchDeleteCollection,
  fetchSetCollection,
} from "../../api/user";
import { elMessageTypeFromResponse } from "@/api/types";
import { formatSeconds } from "../../utils";
import { RouterName } from "../../enums";
import { useAppActions } from "../../composables/useAppActions";
const isDragging = ref(false); // 新增标志位
const configureStore = useConfigureStore();
const userStore = useUserStore();
const songStore = useSongStore();
const { routerManager, playMusic, checkStatus, downloadMusic, attachImageUrl } =
  useAppActions();

const COLLECTION_TYPE_SONG = "0";
const PLAY_MODE_LOOP = "loop";
const PLAY_MODE_SHUFFLE = "shuffle";

const nowTime = ref(0);
const toggle = ref(true);
const volume = ref(50);
const playStateList = [PLAY_MODE_LOOP, PLAY_MODE_SHUFFLE];
const playStateIndex = ref(0);

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
const playState = computed(() => playStateList[playStateIndex.value]);

watch(songId, initCollection);
watch(token, (value) => {
  if (!value) isCollection.value = false;
});
watch(volume, (value) => {
  songStore.setVolume(value / 100);
});
watch([curTime, duration], () => {
  // 如果用户正在拖动，则禁止回写覆盖，阻止闪烁回弹
  if (isDragging.value) return;
  nowTime.value =
      duration.value > 0 ? (curTime.value / duration.value) * 100 : 0;
}, { immediate: true });
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

// 新增：开始拖拽时将标记设为 true
function dragStart() {
  isDragging.value = true;
}

// 修改原有的 changeTime 函数
function changeTime() {
  isDragging.value = false; // 拖动释放完毕，切回非拖动状态
  songStore.setChangeTime(duration.value * (nowTime.value * 0.01));
}

function formatProgressTooltip(value: number) {
  return `${Math.round(value)}%`;
}

function changePlayState() {
  playStateIndex.value =
    playStateIndex.value >= playStateList.length - 1
      ? 0
      : playStateIndex.value + 1;
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
  });
}

function prev() {
  if (playState.value === PLAY_MODE_SHUFFLE) {
    const playIndex = getRandomPlayIndex(currentPlayList.value.length);
    songStore.setCurrentPlayIndex(playIndex);
    const targetSong = currentPlayList.value[playIndex];
    toPlay(targetSong ? targetSong.url : "");
    return;
  }

  if (currentPlayIndex.value === -1 || currentPlayList.value.length <= 1)
    return;
  const playIndex =
    currentPlayIndex.value > 0
      ? currentPlayIndex.value - 1
      : currentPlayList.value.length - 1;
  songStore.setCurrentPlayIndex(playIndex);
  const targetSong = currentPlayList.value[playIndex];
  toPlay(targetSong ? targetSong.url : "");
}

function next() {
  if (playState.value === PLAY_MODE_SHUFFLE) {
    const playIndex = getRandomPlayIndex(currentPlayList.value.length);
    songStore.setCurrentPlayIndex(playIndex);
    const targetSong = currentPlayList.value[playIndex];
    toPlay(targetSong ? targetSong.url : "");
    return;
  }

  if (currentPlayIndex.value === -1 || currentPlayList.value.length <= 1)
    return;
  const playIndex =
    currentPlayIndex.value < currentPlayList.value.length - 1
      ? currentPlayIndex.value + 1
      : 0;
  songStore.setCurrentPlayIndex(playIndex);
  const targetSong = currentPlayList.value[playIndex];
  toPlay(targetSong ? targetSong.url : "");
}

function goPlayerPage() {
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
@import "@/assets/css/global.scss";

.playbar {
  position: fixed;
  z-index: 100;
  bottom: 0;
  width: 100%;
  transition: all 0.5s;
}

.playbar__toggle {
  position: absolute;
  bottom: $play-bar-height + 5px;
  left: 10px;
  cursor: pointer;
}

.playbar__progress {
  position: absolute;
  margin-top: -10px;
}

.playbar__content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: $play-bar-height;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
  background-color: $theme-play-bar-color;
}

.playbar__meta,
.playbar__controls,
.playbar__actions {
  display: flex;
  align-items: center;
}

.playbar__meta {
  width: 32%;
  min-width: 200px;
  margin-left: 0;
  justify-content: flex-start;
}

.playbar__cover {
  width: 100%;
  height: 100%;
  border-radius: 4px;
  margin-right: 0;
  display: block;
}

.playbar__cover-wrap {
  position: relative;
  width: calc($play-bar-height - 15px);
  height: calc($play-bar-height - 15px);
  flex: 0 0 calc($play-bar-height - 15px);
  margin-right: 10px;
  cursor: pointer;
  border-radius: 4px;
  overflow: hidden;
}

.playbar__cover-hover-icon {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%) scale(0.92);
  width: 26px;
  height: 26px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  stroke-width: 2.4;
  opacity: 0;
  transition: opacity 0.2s ease, transform 0.2s ease;
  pointer-events: none;
}

.playbar__cover-wrap::before {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.35);
  opacity: 0;
  transition: opacity 0.2s ease;
  z-index: 1;
  pointer-events: none;
}

.playbar__cover-wrap:hover::before {
  opacity: 1;
}

.playbar__cover-wrap:hover .playbar__cover-hover-icon {
  opacity: 1;
  transform: translate(-50%, -50%) scale(1);
  z-index: 2;
}

.playbar__song {
  font-size: 14px;
}

.playbar__time {
  font-size: 12px;
  color: $color-grey;
}

.playbar__controls {
  flex: 1;
  justify-content: center;
  gap: 28px;
}

.playbar__actions {
  width: 32%;
  justify-content: flex-end;
  gap: 16px;
}

.playbar__action-item {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  margin: 0;
  line-height: 1;
  vertical-align: middle;
  cursor: pointer;
}

.playbar__action-icon,
.playbar__action-item svg {
  display: block;
  width: 16px;
  height: 16px;
  color: inherit;
}

.playbar__action-item.is-active .playbar__action-icon {
  color: #ff4d4f;
}

.playbar__menu-icon {
  width: 14px;
  height: 14px;
  margin-right: 6px;
}

.playbar__volume-percent {
  width: 100%;
  text-align: center;
}

.playbar__icon-btn,
.playbar__icon-btn svg {
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.playbar__controls .playbar__icon-btn:nth-child(3),
.playbar__controls .playbar__icon-btn:nth-child(3) svg {
  width: 30px;
  height: 30px;
}

.show-mobile {
  display: none;
}

.hide-mobile {
  display: inline-flex;
}

.playbar__lyric-entry {
  width: 28px;
  height: 28px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
  color: #409eff;
  border: 1px solid rgba(64, 158, 255, 0.4);
  background: rgba(64, 158, 255, 0.1);
  line-height: 1;
}

.is-rotated {
  transform: rotate(180deg);
}

.is-collapsed {
  bottom: -($play-bar-height) + 5px;
}

.icon {
  @include icon(1.1em, $color-black);
}

.is-active.icon {
  color: $color-red;
}

@media screen and (min-width: 769px) {
  .playbar__content {
    padding: 0 28px;
  }

  .playbar__meta {
    width: 30%;
  }

  .playbar__controls {
    gap: 48px;
  }

  .playbar__actions {
    width: 30%;
    gap: 20px;
  }

  .playbar__icon-btn,
  .playbar__icon-btn svg {
    width: 30px;
    height: 30px;
  }

  .playbar__controls .playbar__icon-btn:nth-child(3),
  .playbar__controls .playbar__icon-btn:nth-child(3) svg {
    width: 38px;
    height: 38px;
  }
}

@media screen and (max-width: 768px) {
  .playbar__toggle {
    left: 10px;
  }

  .playbar__content {
    display: grid;
    grid-template-columns: minmax(0, 1fr) auto auto;
    align-items: center;
    column-gap: 10px;
    padding: 0 10px;
  }

  .playbar__meta {
    width: auto;
    min-width: 0;
    margin-left: 14px;
    overflow: hidden;
  }

  .playbar__cover {
    width: 100%;
    height: 100%;
  }

  .playbar__cover-wrap {
    width: 26px;
    height: 26px;
    flex: 0 0 26px;
    margin-right: 8px;
  }

  .playbar__song {
    max-width: 100%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    font-size: 16px;
  }

  .playbar__time {
    display: none;
  }

  .playbar__controls {
    flex: 0 0 auto;
    gap: 28px;
    margin: 0;
    justify-content: center;
  }

  .playbar__actions {
    width: auto;
    gap: 8px;
    margin-right: 2px;
    justify-content: flex-end;
  }

  .show-mobile {
    display: inline-flex !important;
  }

  .playbar__actions .playbar__action-item:not(:first-child) {
    display: none !important;
  }

  .hide-mobile {
    display: none !important;
  }
}
</style>
