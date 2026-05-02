<template>
  <section class="comment-panel">
    <div class="comment-editor">
      <el-input class="comment-editor__input" type="textarea" placeholder="期待您的精彩评论..." :rows="2" v-model="textarea" /> 
      <el-button class="comment-editor__submit" type="primary" :disabled="!textarea.trim()" @click="submitComment()">发表评论</el-button>
    </div>
    <ul v-if="commentList.length" class="comment-list">
      <li v-for="item in commentList" :key="item.id || item.createTime">
        <el-image class="comment-list__avatar" fit="contain" :src="attachImageUrl(item.avatar)" />
        <div class="comment-list__content">
        <ul>
            <li class="comment-list__name">{{ item.username }}</li>
            <li class="comment-list__time">{{ formatDate(item.createTime) }}</li>
            <li class="comment-list__text">{{ item.content }}</li>
        </ul>
        </div>
        <div class="comment-list__actions" :class="{ 'is-active': isSupported(item.id) }" @click="setSupport(item.id, item.likeCount, userId)">
          <div class="comment-like"><ThumbsUp class="comment-like-icon" /> {{ item.likeCount }}</div>
          <Trash2 v-if="item.userId === userId" class="comment-delete-icon" @click.stop="deleteComment(item.id)" />
        </div>
      </li>
    </ul>
    <el-empty v-else description="暂无评论"></el-empty>
  </section>
</template>

<script lang="ts" setup>
import { ref, toRefs, computed, reactive, watch } from "vue";
import { ElMessage } from "element-plus";
import { ThumbsUp, Trash2 } from "lucide-vue-next";
import { useUserStore } from "../../store/user";
import {
  fetchDeleteComment,
  fetchDeleteUserSupport,
  fetchInsertUserSupport,
  fetchSetComment,
  fetchSetSupport,
  fetchTestAlreadySupport,
} from "../../api/comment";
import { elMessageTypeFromResponse } from "@/api/types";
import { attachImageUrl, formatDate } from "../../utils";
import { useAppActions } from "../../composables/useAppActions";

const userStore = useUserStore();
const { checkStatus } = useAppActions();

const props = defineProps({
  playId: Number || String, // 歌曲ID 或 歌单ID
  type: Number, // 歌单 1 / 歌曲 0
  commentList: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits<{
  (e: "refresh"): void;
}>();

const { playId, type } = toRefs(props);
const textarea = ref(""); // 存放输入内容

const userId = computed(() => userStore.userId);
const supportCommentIdSet = ref<Set<string | number>>(new Set());

function isSupported(commentId: string | number) {
  return supportCommentIdSet.value.has(commentId);
}

async function syncSupportState() {
  const comments = Array.isArray(props.commentList) ? props.commentList : [];
  if (!userId.value || !comments.length) {
    supportCommentIdSet.value = new Set();
    return;
  }

  const nextSet = new Set<string | number>();
  await Promise.all(
    comments.map(async (item: any) => {
      const commentId = item && item.id;
      if (!commentId) return;
      try {
        const result = await fetchTestAlreadySupport({
          commentId,
          userId: userId.value,
        });
        if (result && result.data) {
          nextSet.add(commentId);
        }
      } catch {
        // Ignore single row errors to avoid blocking the whole comment list rendering.
      }
    }),
  );
  supportCommentIdSet.value = nextSet;
}

watch(
  [() => props.commentList, userId],
  () => {
    syncSupportState();
  },
  { immediate: true },
);

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

  const content = textarea.value;
  const result = await fetchSetComment({ userId: userId.value, content, songId, songListId, nowType });
  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });

  if (result.success) {
    textarea.value = "";
    emit("refresh");
  }
}

// 删除评论
async function deleteComment(id) {
  const result = await fetchDeleteComment(id);
  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });

  if (result.success) {
    emit("refresh");
  }
}

async function setSupport(id, likeCount, userId) {
  if (!checkStatus()) return;
  if (!userId) return;

  try {
    let result = null;
    let operatorR = null;
    const commentId = id;
    // 先查询当前用户是否已点赞，再执行点赞/取消点赞
    const r = await fetchTestAlreadySupport({ commentId, userId });
    ElMessage({
      message: r.message,
      type: elMessageTypeFromResponse(r),
    });

    if (r.data) {
      likeCount = likeCount - 1;
      operatorR = await fetchDeleteUserSupport({ commentId, userId });
      result = await fetchSetSupport({ id, likeCount });
      supportCommentIdSet.value.delete(id);
    } else {
      likeCount = likeCount + 1;
      operatorR = await fetchInsertUserSupport({ commentId, userId });
      result = await fetchSetSupport({ id, likeCount });
      supportCommentIdSet.value.add(id);
    }
    if (result.success && operatorR.success) {
      emit("refresh");
    }
  } catch (error) {
    const err = error as { data?: { message?: string }; response?: { data?: { message?: string } } };
    const fallbackMessage =
      (err.data && err.data.message) ||
      (err.response && err.response.data && err.response.data.message) ||
      "点赞操作失败";
    (ElMessage as any)({
      message: fallbackMessage,
      type: "error",
    });
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

/* 评论输入区 */
.comment-editor {
  display: flex;
  align-items: flex-end;
  gap: 12px;

  &__input {
    flex: 1;
    min-width: 0;
  }

  &__submit {
    flex-shrink: 0;
    height: 52px;
  }
}

/* 评论列表 */
.comment-list {
  width: 100%;
  > li {
    border-bottom: solid 1px rgba(0, 0, 0, 0.1);
    padding: 15px 0;
    display: flex;
    .comment-list__avatar {
      width: 50px;
    }

    .comment-list__content {
      padding: 0 20px;
      flex: 1;
      li {
        width: 100%;
      }
      .comment-list__time {
        font-size: 0.6rem;
        color: rgba(0, 0, 0, 0.5);
      }
      .comment-list__name {
        color: rgba(0, 0, 0, 0.5);
      }
      .comment-list__text {
        font-size: 1rem;
      }
    }

    .comment-list__actions {
      display: flex;
      align-items: center;
      width: 80px;
      font-size: 1rem;
      cursor: pointer;
      color: rgba(0, 0, 0, 0.55);

      .comment-like {
        display: inline-flex;
        align-items: center;
        gap: 4px;
      }

      .comment-like-icon {
        width: 16px;
        height: 16px;
      }

      .comment-delete-icon {
        width: 16px;
        height: 16px;
        margin: 0 10px;
      }

      &:hover,
      :deep(.icon):hover {
        color: $color-grey;
      }

      &.is-active {
        color: $color-blue;
      }
    }
  }
}

.icon {
  @include icon(1em);
}
</style>
