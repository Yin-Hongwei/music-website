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
      <n-input v-model:value="searchWord" clearable placeholder="筛选歌曲" />
    </div>
    <n-data-table
      :columns="columns"
      :data="filteredSongs"
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
import { deleteCollection, getCollectionOfUser } from "@/api";
import type { ApiResponse } from "@/api";
import PageBreadcrumb from "@/components/PageBreadcrumb.vue";
import YinDelDialog from "@/components/YinDelDialog.vue";
import { useAppStore } from "@/stores/app";
import { message, notifyResult } from "@/utils";

type SongRow = {
  id: string | number;
  name?: string;
  singerName?: string;
};

const route = useRoute();
const appStore = useAppStore();
const songs = ref<SongRow[]>([]);
const searchWord = ref("");
const checkedRowKeys = ref<Array<string | number>>([]);
const selectedId = ref<string | number | null>(null);
const delVisible = ref(false);

const breadcrumbList = computed(() => appStore.breadcrumbList);
const filteredSongs = computed(() => {
  const keyword = searchWord.value.trim().toLowerCase();
  return keyword
    ? songs.value.filter(
        (song) =>
          song.name?.toLowerCase().includes(keyword) ||
          song.singerName?.toLowerCase().includes(keyword),
      )
    : songs.value;
});

const columns: DataTableColumns<SongRow> = [
  { type: "selection" },
  { title: "歌曲", key: "name", ellipsis: { tooltip: true } },
  { title: "歌手", key: "singerName", width: 140, ellipsis: { tooltip: true } },
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

function rowKey(row: SongRow) {
  return row.id;
}

function handleCheckedRowKeys(keys: Array<string | number>) {
  checkedRowKeys.value = keys;
}

async function getData() {
  const result = (await getCollectionOfUser(route.query.id)) as ApiResponse<{
    songs?: SongRow[];
  }>;
  if (!result.success) {
    notifyResult(result);
    songs.value = [];
    return;
  }
  songs.value = Array.isArray(result.data?.songs) ? result.data.songs : [];
  checkedRowKeys.value = [];
}

function deleteRow(id: string | number) {
  selectedId.value = id;
  delVisible.value = true;
}

async function confirm() {
  if (selectedId.value == null) return;
  const result = (await deleteCollection(
    route.query.id,
    selectedId.value,
  )) as ApiResponse;
  notifyResult(result);
  delVisible.value = false;
  if (result.success) await getData();
}

async function deleteAll() {
  if (!checkedRowKeys.value.length) {
    message.warning("请先选择要删除的歌曲");
    return;
  }
  for (const songId of checkedRowKeys.value) {
    const result = (await deleteCollection(
      route.query.id,
      songId,
    )) as ApiResponse;
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
</style>
