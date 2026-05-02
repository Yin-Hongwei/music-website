<template>
  <div class="sheet-detail-page">
    <el-row :gutter="24" class="sheet-detail-layout">
      <el-col :xs="24" :sm="9" :md="8" :lg="7" :xl="6">
        <el-card class="left-card" shadow="never">
          <el-image
            class="cover-image"
            fit="cover"
            :src="attachImageUrl(songDetails.pic)"
          />
          <div class="meta-text">Created by {{ creatorName }}</div>
          <div class="meta-text">Last updated {{ lastUpdated }}</div>
          <el-space class="action-row" wrap>
            <el-button type="primary" :icon="VideoPlay" :disabled="!currentSongList.length" @click="handlePlayAll">
              播放全部
            </el-button>
            <el-button :icon="Star" :class="{ 'collect-btn--active': isCollection }" @click="handleCollectSongList">
              {{ isCollection ? "已收藏" : "收藏" }}
            </el-button>
          </el-space>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="15" :md="16" :lg="17" :xl="18">
        <el-card class="right-card" shadow="never">
          <h1 class="sheet-title">{{ songDetails.title || "未命名歌单" }}</h1>
          <p class="sheet-intro">{{ songDetails.introduction || "暂无简介" }}</p>

          <el-tabs v-model="activeTab" class="sheet-tabs">
            <el-tab-pane :label="`歌曲 (${ currentSongList.length })`" name="songs">
              <SongList class="tab-content" :songList="currentSongList"></SongList>
            </el-tab-pane>
            <el-tab-pane :label="`评论 (${ commentList.length })`" name="comments">
              <Comment
                class="tab-content"
                :playId="songListId"
                :type="1"
                :commentList="commentList"
                @refresh="() => getCommentBySongListId(songListId)"
              />
            </el-tab-pane>
            <el-tab-pane :label="`收藏者 (${collectorTotal})`" name="collectors">
              <div v-if="collectorList.length" class="collector-list">
                <div v-for="collector in collectorList" :key="collector.id" class="collector-item">
                  <el-avatar :size="42" :src="attachImageUrl(collector.avatar)"></el-avatar>
                  <div class="collector-meta">
                    <div class="collector-name">{{ collector.username || "匿名用户" }}</div>
                    <div class="collector-intro">{{ collector.introduction || "这个人很懒，什么都没写" }}</div>
                  </div>
                </div>
              </div>
              <el-empty v-else description="暂无收藏者数据"></el-empty>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from "vue";
import { Star, VideoPlay } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import SongList from "@/components/business/SongList.vue";
import Comment from "@/components/business/Comment.vue";
import { attachImageUrl, formatDate } from "@/utils";
import { fetchAllComment } from "@/api/comment";
import { fetchSongDetailOfSongListId } from "@/api/listSong";
import {
  fetchDeleteSongListCollection,
  fetchSetSongListCollection,
  fetchSongListCollectors,
  fetchSongListCollectionStatus,
} from "@/api/user";
import { elMessageTypeFromResponse } from "@/api/types";
import { useAppActions } from "@/composables/useAppActions";
import { usePlayerStore } from "@/store/player";
import { useSongStore } from "@/store/song";
import { useUserStore } from "@/store/user";

const playerStore = usePlayerStore();
const songStore = useSongStore();
const userStore = useUserStore();
const { checkStatus } = useAppActions();

const currentSongList = ref<any[]>([]); // 存放的音乐
const commentList = ref<any[]>([]);
const songListId = ref(""); // 歌单 ID
const activeTab = ref("songs");
const isCollection = ref(false);
const collectorTotal = ref(0);
const collectorList = ref<any[]>([]);
const songDetails = computed(() => songStore.songDetails || {}); // 单个歌单信息
const userId = computed(() => userStore.userId);

// 后端接口直接返回歌单中的歌曲详情列表
async function getSongListBySongListId(id: string | number | undefined) {
  if (!id) {
    currentSongList.value = [];
    return;
  }
  currentSongList.value = await fetchSongDetailOfSongListId(id);
}

async function getCommentBySongListId(id: string | number | undefined) {
  if (!id) {
    commentList.value = [];
    return;
  }
  const result = await fetchAllComment(1, id);
  commentList.value = (result && result.data) || [];
}

async function getSongListCollectors(id: string | number | undefined) {
  if (!id) {
    collectorTotal.value = 0;
    collectorList.value = [];
    return;
  }
  const result = await fetchSongListCollectors(id);
  const data = (result && result.data) || {};
  collectorTotal.value = Number(data.total) || 0;
  collectorList.value = Array.isArray(data.users) ? data.users : [];
}

watch(
  () => songDetails.value.id,
  async (id) => {
    songListId.value = id || "";
    await getSongListBySongListId(id);
    await getCommentBySongListId(id);
    await getSongListCollectors(id);
    await initCollection();
  },
  { immediate: true },
);

async function initCollection() {
  if (!songListId.value || !checkStatus(false)) {
    isCollection.value = false;
    return;
  }

  const result = await fetchSongListCollectionStatus({
    userId: userId.value,
    songListId: songListId.value,
  });
  isCollection.value = Boolean(result.data);
}

async function handleCollectSongList() {
  if (!songListId.value || !checkStatus()) return;

  const result = isCollection.value
    ? await fetchDeleteSongListCollection(userId.value, songListId.value)
    : await fetchSetSongListCollection({
      userId: userId.value,
      songListId: songListId.value,
    });

  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });
  if (typeof result.data === "boolean") {
    isCollection.value = result.data;
  }
  await getSongListCollectors(songListId.value);
}

function handlePlayAll() {
  if (!currentSongList.value.length) return;
  const firstSong: any = currentSongList.value[0];
  playerStore.playSong({
    id: firstSong.id,
    url: firstSong.url,
    pic: firstSong.pic,
    lyric: firstSong.lyric,
    index: 0,
    name: firstSong.name || firstSong.songName || "",
    currentSongList: currentSongList.value,
  });
}

const creatorName = computed(
  () => songDetails.value.username || songDetails.value.creator || "未知用户",
);

const lastUpdated = computed(() => {
  const sourceTime = songDetails.value.updateTime || songDetails.value.createTime;
  return sourceTime ? formatDate(sourceTime) : "未知时间";
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.sheet-detail-page {
  padding: 20px 16px;
}

.sheet-detail-layout {
  max-width: 1320px;
  margin: 0 auto;
  padding: 0;
}

.left-card {
  border-radius: 14px;
}

.cover-image {
  width: 100%;
  aspect-ratio: 1 / 1;
  border-radius: 12px;
}

.meta-text {
  margin-top: 14px;
  color: rgba(0, 0, 0, 0.5);
  font-size: 14px;
}

.action-row {
  margin-top: 18px;
}

.collect-btn--active {
  color: #f59e0b;
  border-color: #f59e0b;
  background: rgba(245, 158, 11, 0.12);
}

.right-card {
  border-radius: 14px;
}

.sheet-title {
  margin: 0;
  font-size: 42px;
  line-height: 1.2;
  font-weight: 700;
}

.sheet-intro {
  margin: 12px 0 4px;
  color: rgba(0, 0, 0, 0.55);
}

.sheet-tabs {
  margin-top: 12px;
}

.tab-content {
  margin-top: 10px;
}

.collector-list {
  margin-top: 8px;
}

.collector-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.collector-meta {
  min-width: 0;
}

.collector-name {
  font-size: 15px;
  font-weight: 600;
}

.collector-intro {
  margin-top: 4px;
  color: rgba(0, 0, 0, 0.5);
  font-size: 13px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

@media screen and (min-width: $sm) {
  .sheet-detail-page {
    padding: 28px 22px;
  }
}

@media screen and (max-width: $sm) {
  .sheet-title {
    font-size: 30px;
  }
}
</style>
