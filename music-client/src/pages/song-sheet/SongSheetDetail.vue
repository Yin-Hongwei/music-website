<template>
  <div class="page-shell">
    <el-row :gutter="40" class="detail-layout">
      <el-col :xs="24" :lg="7">
        <aside class="detail-aside">
          <el-image
            class="detail-cover cover-lift"
            fit="cover"
            :src="attachImageUrl(songDetails.pic)"
          />
          <p class="detail-aside__meta">Created by {{ creatorName }}</p>
          <p class="detail-aside__meta">Last updated {{ lastUpdated }}</p>
          <el-space class="detail-aside__actions" wrap>
            <el-button type="primary" round :disabled="!currentSongList.length" @click="handlePlayAll">
              <Play class="btn-icon" />
              播放全部
            </el-button>
            <el-button round :class="{ 'collect-btn--active': isCollection }" @click="handleCollectSongList">
              <Star class="btn-icon" :fill="isCollection ? 'currentColor' : 'none'" />
              {{ isCollection ? "已收藏" : "收藏" }}
            </el-button>
          </el-space>
        </aside>
      </el-col>

      <el-col :xs="24" :lg="17">
        <section class="detail-main">
          <h1 class="section-title">{{ songDetails.title || "未命名歌单" }}</h1>
          <p class="detail-meta">{{ songDetails.introduction || "暂无简介" }}</p>

          <el-tabs v-model="activeTab" class="sheet-tabs">
            <el-tab-pane :label="`歌曲 (${currentSongList.length})`" name="songs">
              <SongList class="tab-content" :songList="currentSongList" />
            </el-tab-pane>
            <el-tab-pane :label="`评论 (${commentList.length})`" name="comments">
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
                  <el-avatar :size="42" :src="attachImageUrl(collector.avatar)" />
                  <div class="collector-meta">
                    <div class="collector-name">{{ collector.username || "匿名用户" }}</div>
                    <div class="collector-intro">{{ collector.introduction || "这个人很懒，什么都没写" }}</div>
                  </div>
                </div>
              </div>
              <el-empty v-else description="暂无收藏者数据" />
            </el-tab-pane>
          </el-tabs>
        </section>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from "vue";
import { Play, Star } from "lucide-vue-next";
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

const currentSongList = ref<any[]>([]);
const commentList = ref<any[]>([]);
const songListId = ref("");
const activeTab = ref("songs");
const isCollection = ref(false);
const collectorTotal = ref(0);
const collectorList = ref<any[]>([]);
const songDetails = computed(() => songStore.songDetails || {});
const userId = computed(() => userStore.userId);

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
    name: firstSong.songName || firstSong.name || "",
    singerName: firstSong.singerName || "",
    currentSongList: currentSongList.value,
    duration: firstSong.duration,
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

.detail-layout {
  width: 100%;
}

.detail-aside {
  display: flex;
  flex-direction: column;
  margin-bottom: 1.5rem;
}

.detail-cover {
  width: 100%;
  max-width: 320px;
  border-radius: 24px;
}

.detail-aside__meta {
  margin: 0.75rem 0 0;
  color: var(--muted);
  font-size: 0.875rem;
}

.detail-aside__actions {
  margin-top: 1.25rem;
}

.btn-icon {
  width: 16px;
  height: 16px;
  margin-right: 6px;
}

.collect-btn--active {
  color: var(--accent);
  border-color: var(--accent);
  background: rgba(48, 164, 252, 0.1);
}

.detail-meta {
  margin: -0.5rem 0 1rem;
  color: var(--muted);
  font-size: 0.9375rem;
  line-height: 1.6;
}

.sheet-tabs {
  margin-top: 0.5rem;
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
  border-bottom: 1px solid var(--border);
}

.collector-meta {
  min-width: 0;
}

.collector-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--foreground);
}

.collector-intro {
  margin-top: 4px;
  color: var(--muted);
  font-size: 13px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

@media screen and (max-width: $sm) {
  .detail-cover {
    max-width: 100%;
  }
}
</style>
