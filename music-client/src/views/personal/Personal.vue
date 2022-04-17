<template>
  <div class="personal">
    <div class="personal-info">
      <el-image class="personal-img" :src="attachImageUrl(userPic)" @click="dialogTableVisible = true" />
      <div class="personal-msg">
        <div class="username">{{ personalInfo.username }}</div>
        <div class="introduction">{{ personalInfo.introduction }}</div>
      </div>
      <el-button class="edit-info" round :icon="Edit" @click="goPage()">修改个人信息</el-button>
    </div>
    <div class="personal-body">
      <song-list :songList="collectSongList">
        <template v-slot:title>我的收藏</template>
      </song-list>
    </div>
    <el-dialog v-model="dialogTableVisible" title="修改头像">
      <upload></upload>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, nextTick, ref, computed, watch, reactive } from "vue";
import { useStore } from "vuex";
import { Edit } from "@element-plus/icons-vue";
import SongList from "@/components/SongList.vue";
import Upload from "../setting/Upload.vue";
import mixin from "@/mixins/mixin";
import { HttpManager } from "@/api";
import { RouterName } from "@/enums";

export default defineComponent({
  components: {
    SongList,
    Upload,
  },
  setup() {
    const store = useStore();

    const { routerManager } = mixin();

    const dialogTableVisible = ref(false);
    const collectSongList = ref([]); // 收藏的歌曲
    const personalInfo = reactive({
      username: "",
      userSex: "",
      birth: "",
      location: "",
      introduction: "",
    });
    const userId = computed(() => store.getters.userId);
    const userPic = computed(() => store.getters.userPic);
    watch(userPic, () => {
      dialogTableVisible.value = false;
    });

    function goPage() {
      routerManager(RouterName.Setting, { path: RouterName.Setting });
    }
    async function getUserInfo(id) {
      const result = (await HttpManager.getUserOfId(id)) as ResponseBody;
      personalInfo.username = result.data[0].username;
      personalInfo.userSex = result.data[0].sex;
      personalInfo.birth = result.data[0].birth;
      personalInfo.introduction = result.data[0].introduction;
      personalInfo.location = result.data[0].location;
    }
    // 获取收藏的歌曲
    async function getCollection(userId) {
      const result = (await HttpManager.getCollectionOfUser(userId)) as ResponseBody;
      const collectIDList = result.data || []; // 存放收藏的歌曲ID
      // 通过歌曲ID获取歌曲信息
      for (const item of collectIDList) {
        const result = (await HttpManager.getSongOfId(item.songId)) as ResponseBody;
        collectSongList.value.push(result.data[0]);
      }
    }

    nextTick(() => {
      getUserInfo(userId.value);
      getCollection(userId.value);
    });

    return {
      Edit,
      userPic,
      dialogTableVisible,
      collectSongList,
      personalInfo,
      attachImageUrl: HttpManager.attachImageUrl,
      goPage,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.personal {
  padding-top: $header-height + 150px;
  background-color: $theme-background-color;

  &::before {
    /*背景*/
    content: "";
    background-color: $theme-color;
    position: absolute;
    top: 0;
    width: 100%;
    height: $header-height + 150px;
  }
}

.personal-info {
  position: relative;
  margin-bottom: 150px;
  // 图片
  .personal-img {
    height: 200px;
    width: 200px;
    border-radius: 50%;
    border: 5px solid $color-white;
    position: absolute;
    top: -100px;
    left: 50px;
    cursor: pointer;
  }
  .personal-msg {
    margin-left: 300px;
    position: absolute;
    top: -40px;

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
    margin-top: -50px;
  }
}

/*歌单内容*/
.personal-body {
  padding: 0px 100px 40px 100px;
}
</style>
