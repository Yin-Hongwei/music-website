<template>
  <page-breadcrumb :items="breadcrumbList" />

  <div class="container">
    <div class="handle-box">
      <n-button
        type="error"
        :disabled="!checkedRowKeys.length"
        @click="deleteAll"
        >批量删除</n-button
      >
      <n-input v-model:value="searchWord" clearable placeholder="筛选用户" />
    </div>
    <n-data-table
      :columns="columns"
      :data="filteredComments"
      :row-key="rowKey"
      :checked-row-keys="checkedRowKeys"
      :max-height="550"
      bordered
      size="small"
      @update:checked-row-keys="handleCheckedRowKeys"
    />
  </div>

  <yin-del-dialog
    :delVisible="delVisible"
    @confirm="confirm"
    @cancel-row="delVisible = $event"
  />
</template>

<script setup lang="ts">
import { computed, h, ref } from "vue";
import { useRoute } from "vue-router";
import { NButton } from "naive-ui";
import type { DataTableColumns } from "naive-ui";
import {
  deleteComment,
  getCommentOfSongId,
  getCommentOfSongListId,
} from "@/api";
import type { ApiResponse } from "@/api";
import PageBreadcrumb from "@/components/PageBreadcrumb.vue";
import YinDelDialog from "@/components/YinDelDialog.vue";
import { useAppStore } from "@/stores/app";
import { message, notifyResult } from "@/utils";

type CommentRow = {
  id: string | number;
  userId: string | number;
  username?: string;
  content?: string;
  userDeleted?: boolean;
};

const DELETED_USERNAME = "已注销";

const route = useRoute();
const appStore = useAppStore();
const comments = ref<CommentRow[]>([]);
const searchWord = ref("");
const checkedRowKeys = ref<Array<string | number>>([]);
const selectedId = ref<string | number | null>(null);
const delVisible = ref(false);

const breadcrumbList = computed(() => appStore.breadcrumbList);
const filteredComments = computed(() => {
  const keyword = searchWord.value.trim().toLowerCase();
  return keyword
    ? comments.value.filter((comment) =>
        displayUsername(comment).toLowerCase().includes(keyword),
      )
    : comments.value;
});

function isDeletedUser(row: CommentRow) {
  return row.userDeleted === true || row.username === DELETED_USERNAME;
}

function displayUsername(row: CommentRow) {
  if (isDeletedUser(row)) return DELETED_USERNAME;
  return row.username || "";
}

const columns: DataTableColumns<CommentRow> = [
  { type: "selection" },
  { title: "ID", key: "id", width: 70 },
  {
    title: "用户",
    key: "username",
    width: 120,
    render: (row) =>
      h(
        "span",
        {
          class: isDeletedUser(row) ? "comment-user-deleted" : undefined,
        },
        displayUsername(row),
      ),
  },
  { title: "评论内容", key: "content" },
  {
    title: "操作",
    key: "actions",
    width: 90,
    align: "center",
    render: (row) =>
      h(
        NButton,
        {
          secondary: true,
          type: "error",
          size: "small",
          onClick: () => deleteRow(row.id),
        },
        { default: () => "删除" },
      ),
  },
];

function rowKey(row: CommentRow) {
  return row.id;
}

function handleCheckedRowKeys(keys: Array<string | number>) {
  checkedRowKeys.value = keys;
}

async function getData() {
  const request =
    route.query.type === "1"
      ? getCommentOfSongListId(route.query.id)
      : getCommentOfSongId(route.query.id);
  const result = (await request) as ApiResponse<CommentRow[]>;
  if (!result.success) {
    notifyResult(result);
    comments.value = [];
    return;
  }

  comments.value = Array.isArray(result.data) ? result.data : [];
  checkedRowKeys.value = [];
}

function deleteRow(id: string | number) {
  selectedId.value = id;
  delVisible.value = true;
}

async function confirm() {
  if (selectedId.value == null) return;
  const result = (await deleteComment(selectedId.value)) as ApiResponse;
  notifyResult(result);
  delVisible.value = false;
  if (result.success) await getData();
}

async function deleteAll() {
  if (!checkedRowKeys.value.length) {
    message.warning("请先选择要删除的评论");
    return;
  }
  for (const id of checkedRowKeys.value) {
    const result = (await deleteComment(id)) as ApiResponse;
    notifyResult(result);
  }
  await getData();
}

void getData();
</script>

<style scoped>
.handle-box {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.handle-box :deep(.n-input) {
  width: 260px;
}

:deep(.comment-user-deleted) {
  color: #6e6e73;
  opacity: 0.72;
}
</style>
