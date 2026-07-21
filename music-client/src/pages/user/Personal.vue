<template>
  <div class="page-shell personal">
    <header class="profile-card">
      <el-image
        class="profile-card__avatar"
        fit="cover"
        :src="attachImageUrl(userPic)"
        @click="dialogTableVisible = true"
      />
      <div class="profile-card__meta">
        <h1 class="profile-card__name">{{ personalInfo.username || "用户" }}</h1>
        <p class="profile-card__intro">{{ personalInfo.introduction || "这个人很懒，什么都没写" }}</p>
      </div>
      <el-button class="profile-card__edit" round @click="goPage">
        <Pencil class="btn-icon" />
        修改个人信息
      </el-button>
    </header>

    <el-tabs v-model="activeTab" class="segment-tabs">
      <el-tab-pane :label="`收藏歌曲 (${collectSongList.length})`" name="songs">
        <SongList :songList="collectSongList" :show="true" @changeData="changeData"></SongList>
      </el-tab-pane>
      <el-tab-pane :label="`收藏歌单 (${collectSongSheetList.length})`" name="songSheets">
        <CoverCardGrid :playList="collectSongSheetList" path="song-sheet-detail"></CoverCardGrid>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialogTableVisible" title="修改头像">
      <upload></upload>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from "vue";
import { Pencil } from "lucide-vue-next";

import SongList from "@/components/business/SongList.vue";
import CoverCardGrid from "@/components/business/CoverCardGrid.vue";
import Upload from "@/pages/user/Upload.vue";
import { useUserStore } from "@/store/user";
import { fetchCollectionByUserId, fetchUserById } from "@/api/user";
import { RouterName } from "@/enums";
import { attachImageUrl } from "@/utils";
import { ensureArray } from "@/utils/api-response-data";
import { useAppActions } from "@/composables/useAppActions";

type UserCollectionDetail = {
  songs?: any[];
  songSheets?: any[];
};

const userStore = useUserStore();
const { routerManager } = useAppActions();

const dialogTableVisible = ref(false);
const activeTab = ref("songs");
const collectSongList = ref<any[]>([]);
const collectSongSheetList = ref<any[]>([]);
const personalInfo = reactive({
  username: "",
  userSex: "",
  birth: "",
  location: "",
  introduction: "",
});

const userId = computed(() => userStore.userId);
const userPic = computed(() => userStore.userPic);

watch(userPic, () => {
  dialogTableVisible.value = false;
});

function goPage() {
  routerManager(RouterName.Setting, { path: RouterName.Setting });
}

async function getUserInfo(id: string | number) {
  if (id === "" || id == null) return;
  try {
    const result = await fetchUserById(id);
    const user = result && result.data ? result.data : null;
    if (!user) return;

    personalInfo.username = user.username || "";
    personalInfo.userSex = user.sex || "";
    personalInfo.birth = user.birth || "";
    personalInfo.introduction = user.introduction || "";
    personalInfo.location = user.location || "";
  } catch {
    // 未登录或接口异常时保持默认展示
  }
}

/** 用户收藏：后端一次返回歌曲与歌单实体列表（按收藏时间排序） */
async function getCollection(id: string | number) {
  if (id === "" || id == null) return;
  collectSongList.value = [];
  collectSongSheetList.value = [];
  try {
    const result = await fetchCollectionByUserId(id);
    const raw = result != null ? result.data : undefined;
    if (!raw || typeof raw !== "object" || Array.isArray(raw)) return;
    const data = raw as UserCollectionDetail;
    collectSongList.value = ensureArray(data.songs);
    collectSongSheetList.value = ensureArray(data.songSheets);
  } catch {
    // 未登录或接口异常时保持空列表
  }
}

function changeData() {
  getCollection(userId.value);
}

onMounted(() => {
  getUserInfo(userId.value);
  getCollection(userId.value);
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.personal {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.profile-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  border-radius: 28px;
  background: var(--surface);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.06);
  flex-wrap: wrap;
}

.profile-card__avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  flex-shrink: 0;
  cursor: pointer;
  background: var(--surface-tertiary);
  overflow: hidden;
}

.profile-card__meta {
  flex: 1;
  min-width: 0;
}

.profile-card__name {
  margin: 0;
  font-family: $font-display;
  font-size: clamp(1.5rem, 2.2vw, 1.875rem);
  font-weight: 600;
  letter-spacing: -0.02em;
  color: var(--foreground);
  line-height: 1.2;
}

.profile-card__intro {
  margin: 6px 0 0;
  font-size: 0.875rem;
  line-height: 1.45;
  color: var(--muted);
}

.profile-card__edit {
  flex-shrink: 0;
  background: var(--surface-secondary);
  border-color: transparent;
  color: var(--foreground);

  &:hover {
    background: var(--surface-tertiary);
    border-color: transparent;
    color: var(--foreground);
  }
}

.btn-icon {
  width: 16px;
  height: 16px;
  margin-right: 6px;
}

@media screen and (max-width: $sm) {
  .profile-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
    padding: 20px;
  }

  .profile-card__edit {
    width: 100%;
  }
}
</style>
