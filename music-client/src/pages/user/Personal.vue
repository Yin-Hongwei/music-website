<template>
  <div class="personal">
    <div class="personal-info">
      <el-image class="personal-img" fit="contain" :src="attachImageUrl(userPic)" @click="dialogTableVisible = true" />
      <div class="personal-msg">
        <div class="username">{{ personalInfo.username }}</div>
        <div class="introduction">{{ personalInfo.introduction }}</div>
      </div>
      <el-button class="edit-info" round :icon="Edit" @click="goPage">修改个人信息</el-button>
    </div>
    <div class="personal-body">
      <el-tabs v-model="activeTab">
        <el-tab-pane :label="`收藏歌曲 (${collectSongList.length})`" name="songs">
          <SongList :songList="collectSongList" :show="true" @changeData="changeData"></SongList>
        </el-tab-pane>
        <el-tab-pane :label="`收藏歌单 (${collectSongSheetList.length})`" name="songSheets">
          <CoverCardGrid :playList="collectSongSheetList" path="song-sheet-detail"></CoverCardGrid>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog v-model="dialogTableVisible" title="修改头像">
      <upload></upload>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from "vue";
import { Edit } from "@element-plus/icons-vue";

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
  const result = await fetchUserById(id);
  const user = result && result.data ? result.data : null;
  if (!user) return;

  personalInfo.username = user.username || "";
  personalInfo.userSex = user.sex || "";
  personalInfo.birth = user.birth || "";
  personalInfo.introduction = user.introduction || "";
  personalInfo.location = user.location || "";
}

/** 用户收藏：后端一次返回歌曲与歌单实体列表（按收藏时间排序） */
async function getCollection(id: string | number) {
  collectSongList.value = [];
  collectSongSheetList.value = [];
  const result = await fetchCollectionByUserId(id);
  const raw = result != null ? result.data : undefined;
  if (!raw || typeof raw !== "object" || Array.isArray(raw)) return;
  const data = raw as UserCollectionDetail;
  collectSongList.value = ensureArray(data.songs);
  collectSongSheetList.value = ensureArray(data.songSheets);
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
  padding-top: $header-height + 150px;

  &::before {
    content: "";
    background-color: $color-blue-shallow;
    position: absolute;
    top: 0;
    width: 100%;
    height: $header-height + 150px;
  }
}

.personal-info {
  position: relative;
  margin-bottom: 60px;
  .personal-img {
    height: 200px;
    width: 200px;
    border-radius: 50%;
    border: 5px solid $color-white;
    position: absolute;
    top: -180px;
    left: 50px;
    cursor: pointer;
  }
  .personal-msg {
    margin-left: 300px;
    position: absolute;
    top: -120px;

    .username {
      font-size: 50px;
      font-weight: 600;
    }

    .introduction {
      font-size: 20px;
      font-weight: 500;
    }
  }
  .edit-info {
    position: absolute;
    right: 10vw;
    margin-top: -120px;
  }
}

@media screen and (min-width: $sm) {
  .personal-body {
    padding: 0px 100px;
  }
}

@media screen and (max-width: $sm) {
  .edit-info {
    display: none;
  }
}
</style>
