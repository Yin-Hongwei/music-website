<template>
  <audio :src="attachImageUrl(songUrl)" controls ref="audioRef" preload="true" @canplay="canplay" @timeupdate="timeupdate" @ended="ended">
    <!--（1）属性：controls，preload（2）事件：canplay，timeupdate，ended（3）方法：play()，pause() -->
    <!--controls：向用户显示音频控件（播放/暂停/进度条/音量）-->
    <!--preload：属性规定是否在页面加载后载入音频-->
    <!--canplay：当音频/视频处于加载过程中时，会发生的事件-->
    <!--timeupdate：当目前的播放位置已更改时-->
    <!--ended：当目前的播放列表已结束时-->
  </audio>
</template>

<script lang="ts" setup>
import { ref, computed, watch, onMounted } from "vue";

import { useSongStore } from "../../store/song";
import { attachImageUrl } from "../../utils";

const songStore = useSongStore();
const audioRef = ref<HTMLAudioElement | null>(null);
const pendingUserGesturePlay = ref(false);

const songUrl = computed(() => songStore.songUrl); // 音乐链接
const isPlay = computed(() => songStore.isPlay); // 播放状态
const volume = computed(() => songStore.volume); // 音量
const changeTime = computed(() => songStore.changeTime); // 指定播放时刻
const autoNext = computed(() => songStore.autoNext); // 用于触发自动播放下一首

watch(isPlay, () => togglePlay());
watch(changeTime, () => {
  if (audioRef.value) {
    audioRef.value.currentTime = changeTime.value;
  }
});
watch(volume, (value) => {
  if (audioRef.value) {
    audioRef.value.volume = value;
  }
});

async function tryPlay() {
  if (!audioRef.value) return;
  try {
    await audioRef.value.play();
    pendingUserGesturePlay.value = false;
    songStore.setIsPlay(true);
  } catch {
    // 浏览器自动播放策略导致失败时，等待用户交互后重试
    pendingUserGesturePlay.value = true;
    songStore.setIsPlay(false);
  }
}

// 开始 / 暂停
function togglePlay() {
  if (!audioRef.value) return;
  if (isPlay.value) {
    tryPlay();
  } else {
    audioRef.value.pause();
  }
}

// 获取歌曲链接后准备播放
function canplay() {
  if (!audioRef.value) return;
  // 记录音乐时长
  songStore.setDuration(audioRef.value.duration);
  // 开始播放
  tryPlay();
}

// 音乐播放时记录音乐的播放位置
function timeupdate() {
  if (!audioRef.value) return;
  songStore.setCurTime(audioRef.value.currentTime);
}

// 音乐播放结束时触发
function ended() {
  songStore.setIsPlay(false);
  songStore.setCurTime(0);
  songStore.setAutoNext(!autoNext.value);
}

const retryAfterGesture = () => {
  if (pendingUserGesturePlay.value && audioRef.value && audioRef.value.src) {
    tryPlay();
  }
};

onMounted(() => {
  window.addEventListener("pointerdown", retryAfterGesture, { passive: true });
  window.addEventListener("keydown", retryAfterGesture);
  window.addEventListener("touchstart", retryAfterGesture, { passive: true });
});

</script>

<style scoped>
audio {
  display: none;
}
</style>
