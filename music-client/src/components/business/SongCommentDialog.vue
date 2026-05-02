<template>
  <div v-if="modelValue" class="song-comment-overlay" @click.self="closeDialog">
    <div class="song-comment-dialog">
      <div class="song-comment-dialog__header">
        <span>歌曲评论</span>
        <button class="song-comment-dialog__close" type="button" @click="closeDialog">
          <el-icon><Close /></el-icon>
        </button>
      </div>
      <div class="song-comment-dialog__body">
        <Comment
          :playId="songId"
          :type="0"
          :commentList="commentList"
          @refresh="() => getCommentBySongId(songId)"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";
import { Close } from "@element-plus/icons-vue";
import Comment from "./Comment.vue";
import { fetchAllComment } from "../../api/comment";

const props = defineProps<{
  modelValue: boolean;
  songId: string | number;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
}>();

const commentList = ref<any[]>([]);
let previousOverflow = "";

function closeDialog() {
  emit("update:modelValue", false);
}

async function getCommentBySongId(id: string | number | undefined) {
  if (!id) {
    commentList.value = [];
    return;
  }
  const result = await fetchAllComment(0, id);
  commentList.value = (result && result.data) || [];
}

watch(
  () => props.songId,
  (id) => {
    if (props.modelValue) {
      getCommentBySongId(id);
    }
  },
);

watch(
  () => props.modelValue,
  (visible) => {
    if (!visible) {
      document.body.style.overflow = previousOverflow;
      return;
    }
    previousOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";
    getCommentBySongId(props.songId);
  },
);
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.song-comment-overlay {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 3000;
  background: rgba(0, 0, 0, 0.45);
  @include layout(center, center);
  padding: 10vh 12px;
}

.song-comment-dialog {
  width: min(700px, calc(100vw - 24px));
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-radius: 8px;
  background: #fff;
}

.song-comment-dialog__header {
  flex-shrink: 0;
  height: 56px;
  padding: 0 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  @include layout(space-between, center);
  font-size: 24px;
}

.song-comment-dialog__close {
  border: 0;
  background: transparent;
  color: #999;
  cursor: pointer;
  font-size: 20px;
}

.song-comment-dialog__body {
  box-sizing: border-box;
  height: calc(80vh - 56px);
  min-height: 280px;
  overflow-y: auto;
  padding: 12px 16px 16px;
}
</style>
