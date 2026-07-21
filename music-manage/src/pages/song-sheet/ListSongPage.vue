<template>
  <page-breadcrumb :items="breadcrumbList" />

  <div class="container">
    <div class="handle-box">
      <n-button
        type="error"
        :disabled="!checkedRowKeys.length"
        @click="deleteAll"
      >
        批量删除
      </n-button>
      <n-input v-model:value="searchWord" clearable placeholder="筛选已添加歌曲" />
      <n-button type="primary" @click="openAdd">添加歌曲</n-button>
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

  <n-modal
    v-model:show="addVisible"
    preset="card"
    title="添加歌曲"
    style="width: 480px"
    @after-leave="resetAdd"
  >
    <n-form label-placement="left" label-width="80">
      <n-form-item label="选择歌曲">
        <n-select
          v-model:value="selectedSongId"
          filterable
          clearable
          placeholder="搜索并选择要加入歌单的歌曲"
          :options="songOptions"
          :loading="songOptionsLoading"
          :disabled="songOptionsLoading"
        />
      </n-form-item>
    </n-form>
    <template #footer>
      <n-space justify="end">
        <n-button @click="addVisible = false">取 消</n-button>
        <n-button type="primary" :loading="saving" @click="saveSong">
          确 定
        </n-button>
      </n-space>
    </template>
  </n-modal>

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
import type { DataTableColumns, SelectOption } from "naive-ui";
import {
  deleteListSong,
  getAllSong,
  getSongDetailOfSongListId,
  setListSong,
} from "@/api";
import type { ApiResponse } from "@/api";
import PageBreadcrumb from "@/components/PageBreadcrumb.vue";
import YinDelDialog from "@/components/YinDelDialog.vue";
import { useAppStore } from "@/stores/app";
import { message, notifyResult } from "@/utils";

type SongRow = {
  id: string | number;
  name?: string;
  singerName?: string;
  songName?: string;
};

const route = useRoute();
const appStore = useAppStore();
const songs = ref<SongRow[]>([]);
const searchWord = ref("");
const checkedRowKeys = ref<Array<string | number>>([]);
const selectedId = ref<string | number | null>(null);
const delVisible = ref(false);
const addVisible = ref(false);
const selectedSongId = ref<number | null>(null);
const saving = ref(false);
const songOptionsLoading = ref(false);
const allSongOptions = ref<SelectOption[]>([]);

const songListId = computed(() => route.query.id);
const breadcrumbList = computed(() => appStore.breadcrumbList);
const existingSongIds = computed(
  () => new Set(songs.value.map((s) => Number(s.id))),
);
const songOptions = computed(() =>
  allSongOptions.value.filter(
    (opt) => !existingSongIds.value.has(Number(opt.value)),
  ),
);
const filteredSongs = computed(() => {
  const keyword = searchWord.value.trim().toLowerCase();
  return keyword
    ? songs.value.filter(
        (song) =>
          song.name?.toLowerCase().includes(keyword) ||
          song.songName?.toLowerCase().includes(keyword) ||
          song.singerName?.toLowerCase().includes(keyword),
      )
    : songs.value;
});

const columns: DataTableColumns<SongRow> = [
  { type: "selection" },
  { title: "ID", key: "id", width: 70, align: "center" },
  { title: "歌曲", key: "name", ellipsis: { tooltip: true } },
  { title: "歌手", key: "singerName", width: 120, ellipsis: { tooltip: true } },
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

function songLabel(song: SongRow) {
  const title = song.songName || song.name || `歌曲 #${song.id}`;
  return song.singerName ? `${song.singerName} - ${title}` : title;
}

async function getData() {
  const result = (await getSongDetailOfSongListId(
    songListId.value,
  )) as ApiResponse<SongRow[]>;
  if (!result.success) {
    notifyResult(result);
    songs.value = [];
    return;
  }
  songs.value = Array.isArray(result.data) ? result.data : [];
  checkedRowKeys.value = [];
}

async function loadSongOptions() {
  songOptionsLoading.value = true;
  try {
    const result = (await getAllSong()) as ApiResponse<SongRow[]>;
    if (!result.success) {
      notifyResult(result);
      allSongOptions.value = [];
      return;
    }
    allSongOptions.value = (Array.isArray(result.data) ? result.data : []).map(
      (song) => ({
        label: songLabel(song),
        value: Number(song.id),
      }),
    );
  } finally {
    songOptionsLoading.value = false;
  }
}

async function openAdd() {
  selectedSongId.value = null;
  addVisible.value = true;
  if (!allSongOptions.value.length) {
    await loadSongOptions();
  }
}

function resetAdd() {
  selectedSongId.value = null;
}

function deleteRow(id: string | number) {
  selectedId.value = id;
  delVisible.value = true;
}

async function confirm() {
  if (selectedId.value == null) return;
  const result = (await deleteListSong(
    songListId.value,
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
  let ok = 0;
  for (const songId of checkedRowKeys.value) {
    const result = (await deleteListSong(
      songListId.value,
      songId,
    )) as ApiResponse;
    if (result.success) ok += 1;
    else notifyResult(result);
  }
  if (ok > 0) {
    message.success(
      checkedRowKeys.value.length > 1 ? `已删除 ${ok} 首歌曲` : "删除成功",
    );
    await getData();
  }
}

async function saveSong() {
  if (selectedSongId.value == null) {
    message.warning("请选择要添加的歌曲");
    return;
  }
  saving.value = true;
  try {
    const result = (await setListSong({
      songId: selectedSongId.value,
      songListId: String(songListId.value || ""),
    })) as ApiResponse;
    notifyResult(result);
    if (!result.success) return;
    addVisible.value = false;
    await getData();
  } finally {
    saving.value = false;
  }
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
