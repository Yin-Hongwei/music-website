<template>
  <div class="song-sheet-detail">
    <div class="album-slide">
      <div class="album-img">
        <img :src="attachImageUrl(songDetails.pic)" alt="" />
      </div>
      <div class="album-info">
        <h2>简介：</h2>
        <span> {{ songDetails.introduction }} </span>
      </div>
    </div>
    <div class="song-list">
      <div class="album-title">
        <p>{{ songDetails.title }}</p>
      </div>
      <!--评分-->
      <div class="album-score">
        <div>
          <h3>歌单评分：</h3>
          <div>
            <el-rate v-model="rank" disabled></el-rate>
          </div>
        </div>
        <span>{{ rank * 2 }}</span>
        <div>
          <h3>评价：</h3>
          <div @click="pushValue()">
            <el-rate v-model="score" show-text allow-half></el-rate>
          </div>
        </div>
      </div>
      <!--歌曲-->
      <div class="songs-body">
        <song-list :songList="currentSongList"></song-list>
        <comment :playId="songListId" :type="1"></comment>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  computed,
  getCurrentInstance,
} from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import SongList from "@/components/SongList.vue";
import Comment from "@/components/Comment.vue";
import { HttpManager } from "@/api";

interface resContent {
  code: number;
  msg: string;
  data: object;
}

export default defineComponent({
  components: {
    SongList,
    Comment,
  },
  setup() {
    const { proxy } = getCurrentInstance()
    const store = useStore();
    const { checkStatus, attachImageUrl } = mixin();

    const currentSongList = ref([]); // 存放的音乐
    const songListId = ref(""); // 歌单 ID
    const score = ref(0);
    const rank = ref(0);
    const songDetails = computed(() => store.getters.songDetails); // 单个歌单信息
    const userId = computed(() => store.getters.userId);

    songListId.value = songDetails.value.id; // 给歌单ID赋值

    // 收集歌单里面的歌曲
    async function getSongId(id) {
      const result = (await HttpManager.getListSongOfSongId(id)) as any[];
      // 获取歌单里的歌曲信息
      for (const item of result) {
        // 获取单里的歌曲
        const resultSong = await HttpManager.getSongOfId(item.songId);
        currentSongList.value.push(resultSong[0]);
      }
    }
    // 获取评分
    async function getRank(id) {
      const result = (await HttpManager.getRankOfSongListId(id)) as number;
      rank.value = result / 2;
    }
    // 提交评分
    async function pushValue() {
      if (!checkStatus()) return;

      const params = new URLSearchParams();
      params.append("songListId", songListId.value);
      params.append("consumerId", userId.value);
      params.append("score", (score.value * 2).toString());

      try {
        const result = (await HttpManager.setRank(params)) as resContent;
        if (result.code === 1) {
          getRank(songListId);
          (proxy as any).$notify({
            title: "评分成功",
            type: "success",
          });
        } else {
          (proxy as any).$notify({
            title: "评分失败",
            type: "error",
          });
        }
      } catch (error) {
        console.error(error);
      }
    }

    getRank(songListId.value); // 获取评分
    getSongId(songListId.value); // 获取歌单里面的歌曲ID

    return {
      songDetails,
      rank,
      score,
      currentSongList,
      songListId,
      attachImageUrl,
      pushValue,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/song-sheet-detail.scss";
</style>
