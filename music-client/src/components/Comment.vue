<template>
  <div class="comment">
    <h2 class="comment-title">
      <span>评论</span>
      <span class="comment-desc">共 {{ commentList.length }} 条评论</span>
    </h2>
    <el-input class="comment-input" type="textarea" placeholder="期待您的精彩评论..." :rows="2" v-model="textarea" />
    <el-button class="sub-btn" type="primary" @click="submitComment()">发表评论</el-button>
  </div>
  <ul class="popular">
    <li v-for="(item, index) in commentList" :key="index">
      <el-image class="popular-img" fit="contain" :src="attachImageUrl(item.avator)" />
      <div class="popular-msg">
        <ul>
          <li class="name">{{ item.username }}</li>
          <li class="time">{{ formatDate(item.createTime) }}</li>
          <li class="content">{{ item.content }}</li>
        </ul>
      </div>
      <!--这特么是直接拿到了评论的id-->
      <div ref="up" class="comment-ctr" @click="setSupport(item.id, item.up,userId)">
        <div><yin-icon :icon="iconList.Support"></yin-icon> {{ item.up }}</div>
        <el-icon v-if="item.userId === userId" @click="deleteComment(item.id, index)"><delete /></el-icon>
      </div>
    </li>
  </ul>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, ref, toRefs, computed, watch, reactive, onMounted } from "vue";
import { useStore } from "vuex";
import { Delete } from "@element-plus/icons-vue";
import YinIcon from "@/components/layouts/YinIcon.vue";
import mixin from "@/mixins/mixin";
import { HttpManager } from "@/api";
import { Icon } from "@/enums";
import { formatDate } from "@/utils";

export default defineComponent({
  components: {
    YinIcon,
    Delete,
  },
  props: {
    playId: Number || String, // 歌曲ID或歌单ID
    type: Number, // 歌单（1）/歌曲（0）
  },
  setup(props) {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { checkStatus } = mixin();

    const { playId, type } = toRefs(props);
    const commentList = ref([]); // 存放评论内容
    const textarea = ref(""); // 存放输入内容
    const iconList = reactive({
      Support: Icon.Support,
    });
    const userIdVO = computed(() => store.getters.userId);
    const songIdVO = computed(() => store.getters.songId);
    watch(songIdVO, () => {
      getComment();
    });

    // 获取所有评论
    async function getComment() {
      try {
        const result = (await HttpManager.getAllComment(type.value, songIdVO.value)) as ResponseBody;
        commentList.value = result.data;
        for (let index = 0; index < commentList.value.length; index++) {
          // 获取评论用户的昵称和头像
          const resultUser = (await HttpManager.getUserOfId(commentList.value[index].userId)) as ResponseBody;
          commentList.value[index].avator = resultUser.data[0].avator;
          commentList.value[index].username = resultUser.data[0].username;
        }
      } catch (error) {
        console.error(error);
      }
    }

    // 提交评论
    async function submitComment() {
      if (!checkStatus()) return;

      // 0 代表歌曲， 1 代表歌单
      let songListId = null;
      let songId = null;
      let nowType = null;
      if (type.value === 1) {
        nowType = 1;
        songListId = `${playId.value}`;
      } else if (type.value === 0) {
        nowType = 0;
        songId = `${playId.value}`;
      }
      const userId = userIdVO.value;
      const content = textarea.value;
      const result = (await HttpManager.setComment({userId,content,songId,songListId,nowType})) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) {
        textarea.value = "";
        await getComment();
      }
    }

    // 删除评论
    async function deleteComment(id, index) {
      const result = (await HttpManager.deleteComment(id)) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) commentList.value.splice(index, 1);
    }

    // 点赞  还得再查一下
    async function setSupport(id, up, userId) {
      if (!checkStatus()) return;
      let result = null;
      let operatorR = null;
      const commentId = id;
      //当然可以这么左 直接在判断的时候 进行点赞或者取消
      const r = (await HttpManager.testAlreadySupport({commentId,userId})) as ResponseBody;
      (proxy as any).$message({
        message: r.message,
        type: r.type,
        date: r.data
      });

      if (r.data){
        up = up - 1;
        operatorR = (await HttpManager.deleteUserSupport({commentId,userId})) as ResponseBody;
        result = (await HttpManager.setSupport({id,up})) as ResponseBody;
      }else {
        up = up + 1;
        operatorR = (await HttpManager.insertUserSupport({commentId,userId})) as ResponseBody;
        result = (await HttpManager.setSupport({id,up})) as ResponseBody;
      }
      if (result.success&&operatorR.success) {
        // proxy.$refs.up[index].children[0].style.color = "#2796dd";
        await getComment();
      }
    }

    onMounted(() => {
      getComment();
    });

    return {
      userId: userIdVO,
      commentList,
      textarea,
      iconList,
      attachImageUrl: HttpManager.attachImageUrl,
      submitComment,
      setSupport,
      formatDate,
      deleteComment,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

/*评论*/
.comment {
  position: relative;
  margin-bottom: 60px;

  .comment-title {
    height: 50px;
    line-height: 50px;

    .comment-desc {
      font-size: 14px;
      font-weight: 400;
      color: $color-grey;
      margin-left: 10px;
    }
  }

  .comment-input {
    display: flex;
    margin-bottom: 20px;
  }

  .sub-btn {
    position: absolute;
    right: 0;
  }
}

/*热门评论*/
.popular {
  width: 100%;
  > li {
    border-bottom: solid 1px rgba(0, 0, 0, 0.1);
    padding: 15px 0;
    display: flex;
    .popular-img {
      width: 50px;
    }

    .popular-msg {
      padding: 0 20px;
      flex: 1;
      li {
        width: 100%;
      }
      .time {
        font-size: 0.6rem;
        color: rgba(0, 0, 0, 0.5);
      }
      .name {
        color: rgba(0, 0, 0, 0.5);
      }
      .content {
        font-size: 1rem;
      }
    }

    .comment-ctr {
      display: flex;
      align-items: center;
      width: 80px;
      font-size: 1rem;
      cursor: pointer;

      .el-icon {
        margin: 0 10px;
      }

      &:hover,
      :deep(.icon):hover {
        color: $color-grey;
      }
    }
  }
}

.icon {
  @include icon(1em);
}
</style>
