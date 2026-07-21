<template>
  <div class="page-root page-root--with-crumbs">
    <page-breadcrumb :items="breadcrumbList" />

    <div class="page-panel">
      <div class="page-toolbar">
        <div class="page-toolbar__left">
          <n-button :disabled="!checkedRowKeys.length" @click="deleteAll"
            >批量删除</n-button
          >
          <n-input
            v-model:value="searchWord"
            class="page-toolbar__search"
            clearable
            placeholder="筛选歌曲 / 专辑"
          />
        </div>
        <n-button type="primary" @click="openAdd">添加歌曲</n-button>
      </div>

      <div class="table-wrap">
        <n-data-table
          :columns="columns"
          :data="pageRows"
          :row-key="(row) => row.id"
          :checked-row-keys="checkedRowKeys"
          flex-height
          style="height: 100%"
          @update:checked-row-keys="onCheckedKeys"
        />
      </div>

      <div v-if="previewRow" class="mini-player">
        <div class="mini-player__cover">
          <img :src="attachImageUrl(previewRow.pic)" :alt="previewRow.name" />
        </div>
        <div class="mini-player__meta">
          <div class="mini-player__name" :title="previewRow.name">
            {{ previewRow.name }}
          </div>
          <div class="mini-player__album" :title="previewRow.introduction">
            {{ previewRow.introduction || "暂无专辑" }}
          </div>
        </div>
        <audio
          ref="previewAudioRef"
          class="mini-player__audio"
          :src="previewSrc"
          controls
          controlslist="nodownload noplaybackrate"
          @play="previewPlaying = true"
          @pause="previewPlaying = false"
          @ended="previewPlaying = false"
        />
        <n-button quaternary size="small" @click="closePreview">关闭</n-button>
      </div>

      <n-pagination
        class="page-pagination"
        :page="currentPage"
        :page-size="pageSize"
        :item-count="filteredRows.length"
        :page-sizes="[10, 20, 50]"
        show-size-picker
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
      />
    </div>

    <n-modal
      v-model:show="addVisible"
      preset="card"
      title="添加歌曲"
      style="width: 560px"
      @after-leave="resetAddForm"
    >
      <n-form
        ref="addFormRef"
        label-placement="left"
        label-width="80"
        :model="addForm"
        :rules="addRules"
      >
        <n-form-item label="封面">
          <n-space align="center">
            <n-image
              v-if="addCoverPreview"
              class="preview"
              :src="addCoverPreview"
              object-fit="cover"
            />
            <div v-else class="preview empty">未选择</div>
            <n-upload
              :default-upload="false"
              :show-file-list="false"
              accept="image/jpeg,image/png,image/gif,.jpg,.jpeg,.png,.gif"
              @change="selectAddCover"
            >
              <n-button>{{ pendingCoverFile ? "重新选择" : "选择封面" }}</n-button>
            </n-upload>
            <span class="file-hint">确定后生效</span>
          </n-space>
        </n-form-item>
        <n-form-item label="歌曲名" path="name">
          <n-input v-model:value="addForm.name" placeholder="请输入歌曲名" />
        </n-form-item>
        <n-form-item label="专辑">
          <n-input
            v-model:value="addForm.introduction"
            placeholder="所属专辑"
          />
        </n-form-item>
        <n-form-item label="歌词">
          <n-input
            v-model:value="addForm.lyric"
            type="textarea"
            :rows="5"
            placeholder="可选，留空则使用默认占位歌词"
          />
        </n-form-item>
        <n-form-item label="音频文件" path="fileReady">
          <n-space align="center">
            <n-upload
              :default-upload="false"
              :show-file-list="false"
              accept=".mp3,audio/mpeg"
              @change="selectAddSongFile"
            >
              <n-button>{{ pendingSongFile ? "重新选择" : "选择 MP3" }}</n-button>
            </n-upload>
            <span v-if="pendingSongFile" class="file-name">{{
              pendingSongFile.name
            }}</span>
            <span v-else class="file-hint">仅支持 MP3，确定后上传</span>
          </n-space>
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="addVisible = false">取 消</n-button>
          <n-button type="primary" :loading="saving" @click="addSongSubmit"
            >确 定</n-button
          >
        </n-space>
      </template>
    </n-modal>

    <n-modal
      v-model:show="editVisible"
      preset="card"
      title="编辑歌曲"
      style="width: 560px"
      @after-leave="resetEditDraft"
    >
      <n-form
        ref="editFormRef"
        label-placement="left"
        label-width="70"
        :model="editForm"
        :rules="editRules"
      >
        <n-form-item label="封面">
          <n-space align="center">
            <n-image
              v-if="editCoverPreview"
              class="preview"
              :src="editCoverPreview"
              object-fit="cover"
            />
            <div v-else class="preview empty">未选择</div>
            <n-upload
              :default-upload="false"
              :show-file-list="false"
              accept="image/jpeg,image/png,image/gif,.jpg,.jpeg,.png,.gif"
              @change="selectEditCover"
            >
              <n-button>{{
                pendingEditCover ? "重新选择" : "选择封面"
              }}</n-button>
            </n-upload>
            <span class="file-hint">确定后生效</span>
          </n-space>
        </n-form-item>
        <n-form-item label="歌曲" path="name">
          <n-input v-model:value="editForm.name" placeholder="歌曲名" />
        </n-form-item>
        <n-form-item label="专辑">
          <n-input
            v-model:value="editForm.introduction"
            placeholder="所属专辑"
          />
        </n-form-item>
        <n-form-item label="歌词">
          <n-input
            v-model:value="editForm.lyric"
            type="textarea"
            :rows="6"
            placeholder="歌词内容"
          />
        </n-form-item>
        <n-form-item label="音频">
          <n-space align="center">
            <n-upload
              :default-upload="false"
              :show-file-list="false"
              accept=".mp3,audio/mpeg"
              @change="selectEditSongFile"
            >
              <n-button>{{
                pendingEditSong ? "重新选择" : "选择音频"
              }}</n-button>
            </n-upload>
            <span v-if="pendingEditSong" class="file-name">{{
              pendingEditSong.name
            }}</span>
            <span v-else class="file-hint">可选，确定后生效</span>
          </n-space>
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="editVisible = false">取 消</n-button>
          <n-button type="primary" :loading="saving" @click="saveEdit"
            >确 定</n-button
          >
        </n-space>
      </template>
    </n-modal>

    <yin-del-dialog
      :del-visible="delVisible"
      @confirm="confirm"
      @cancel-row="delVisible = $event"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, h, nextTick, reactive, ref } from "vue";
import { useRoute } from "vue-router";
import { Pause, Play } from "lucide-vue-next";
import type {
  DataTableColumns,
  DataTableRowKey,
  FormInst,
  FormRules,
  UploadFileInfo,
} from "naive-ui";
import { NButton } from "naive-ui";
import {
  addSong,
  attachImageUrl,
  deleteSong,
  getSongOfSingerId,
  updateSongMsg,
  uploadSongImg,
  uploadSongUrl,
} from "@/api";
import type { ApiResponse } from "@/api";
import PageBreadcrumb from "@/components/PageBreadcrumb.vue";
import YinDelDialog from "@/components/YinDelDialog.vue";
import { useAdminNav } from "@/composables/useAdminNav";
import { useClientList } from "@/composables/useClientList";
import { useAppStore } from "@/stores/app";
import {
  beforeImgUpload,
  beforeSongUpload,
  message,
  notifyResult,
  parseCreatedId,
  parseLyric,
} from "@/utils";

type SongRow = {
  id: string | number;
  singerId: string | number;
  name: string;
  introduction?: string;
  lyric?: string;
  duration?: number | string;
  pic?: string;
  url?: string;
};

const route = useRoute();
const appStore = useAppStore();
const { routerManager } = useAdminNav();
const singerId = String(route.query.id || "");
const singerName = String(route.query.name || "");

const {
  pageRows,
  pageSize,
  currentPage,
  searchWord,
  checkedRowKeys,
  filteredRows,
  setRows,
  handlePageChange,
  handlePageSizeChange,
} = useClientList<SongRow>({
  match: (row, q) => {
    const keyword = q.toLowerCase();
    return (
      row.name.toLowerCase().includes(keyword) ||
      (row.introduction || "").toLowerCase().includes(keyword)
    );
  },
});

const selectedId = ref<string | number | null>(null);
const delVisible = ref(false);
const addVisible = ref(false);
const editVisible = ref(false);
const saving = ref(false);
const previewRow = ref<SongRow | null>(null);
const previewPlaying = ref(false);
const previewAudioRef = ref<HTMLAudioElement | null>(null);
const pendingSongFile = ref<File | null>(null);
const pendingCoverFile = ref<File | null>(null);
const addCoverObjectUrl = ref("");
const pendingEditSong = ref<File | null>(null);
const pendingEditCover = ref<File | null>(null);
const editCoverObjectUrl = ref("");
const addFormRef = ref<FormInst | null>(null);
const editFormRef = ref<FormInst | null>(null);

const addForm = reactive({ name: "", introduction: "", lyric: "", fileReady: 0 });
const editForm = reactive({
  id: "",
  singerId: "",
  name: "",
  introduction: "",
  lyric: "",
  pic: "",
});

const breadcrumbList = computed(() => appStore.breadcrumbList);
const previewSrc = computed(() =>
  previewRow.value?.url ? attachImageUrl(previewRow.value.url) : "",
);
const addCoverPreview = computed(() => addCoverObjectUrl.value || "");
const editCoverPreview = computed(() => {
  if (editCoverObjectUrl.value) return editCoverObjectUrl.value;
  if (editForm.pic) return attachImageUrl(editForm.pic);
  return "";
});

const addRules: FormRules = {
  name: { required: true, message: "请输入歌曲名", trigger: "blur" },
  fileReady: {
    type: "number",
    required: true,
    validator: () =>
      pendingSongFile.value ? true : new Error("请选择歌曲文件"),
    trigger: "change",
  },
};

const editRules: FormRules = {
  name: { required: true, message: "请输入歌曲名", trigger: "blur" },
};

const columns = computed<DataTableColumns<SongRow>>(() => [
  { type: "selection" },
  { title: "ID", key: "id", width: 64 },
  {
    title: "封面",
    key: "pic",
    width: 88,
    render: (row) =>
      h("div", { class: "cover-cell" }, [
        h("img", {
          class: "cover-cell__img",
          src: attachImageUrl(row.pic),
          alt: row.name,
        }),
        h(
          "button",
          {
            class: [
              "cover-cell__play",
              previewRow.value?.id === row.id && previewPlaying.value
                ? "is-active"
                : "",
            ],
            type: "button",
            "aria-label": `播放 ${row.name}`,
            onClick: () => void playPreview(row),
          },
          [
            h(
              previewRow.value?.id === row.id && previewPlaying.value
                ? Pause
                : Play,
              { class: "cover-cell__icon", size: 18 },
            ),
          ],
        ),
      ]),
  },
  {
    title: "歌名",
    key: "name",
    width: 160,
    ellipsis: { tooltip: true },
  },
  {
    title: "专辑",
    key: "introduction",
    width: 140,
    ellipsis: { tooltip: true },
  },
  {
    title: "歌词",
    key: "lyric",
    ellipsis: { tooltip: true },
    render: (row) => {
      const lines = parseLyric(row.lyric || "").filter(Boolean);
      return lines.length ? lines.slice(0, 3).join(" / ") : "暂无歌词";
    },
  },
  {
    title: "时长",
    key: "duration",
    width: 72,
    render: (row) => formatDuration(row.duration),
  },
  {
    title: "操作",
    key: "actions",
    width: 220,
    render: (row) =>
      h("div", { class: "ops-cell" }, [
        h(
          NButton,
          {
            secondary: true,
            size: "small",
            type: "info",
            onClick: () => goCommentPage(row.id),
          },
          () => "评论",
        ),
        h(
          NButton,
          {
            secondary: true,
            size: "small",
            type: "primary",
            onClick: () => editRow(row),
          },
          () => "编辑",
        ),
        h(
          NButton,
          {
            secondary: true,
            size: "small",
            type: "error",
            onClick: () => deleteRow(row.id),
          },
          () => "删除",
        ),
      ]),
  },
]);

function onCheckedKeys(keys: DataTableRowKey[]) {
  checkedRowKeys.value = keys;
}

async function getData() {
  const result = (await getSongOfSingerId(singerId)) as ApiResponse<SongRow[]>;
  if (!result.success) {
    notifyResult(result);
    setRows([]);
    return;
  }
  setRows(Array.isArray(result.data) ? result.data : []);
}

async function playPreview(row: SongRow) {
  if (!row.url?.trim()) {
    message.warning("暂无音频文件");
    return;
  }
  const isSame = previewRow.value?.id === row.id;
  previewRow.value = row;
  await nextTick();
  const audio = previewAudioRef.value;
  if (!audio) return;
  if (isSame) {
    if (audio.paused) void audio.play();
    else audio.pause();
    return;
  }
  audio.load();
  void audio.play().catch(() => undefined);
}

function closePreview() {
  previewAudioRef.value?.pause();
  previewRow.value = null;
  previewPlaying.value = false;
}

function goCommentPage(id: string | number) {
  const singerCrumb = appStore.breadcrumbList[0];
  appStore.setBreadcrumbList([
    {
      path: "/singer",
      name: "歌手管理",
      query: singerCrumb?.query,
    },
    {
      path: "/song",
      query: { id: singerId, name: singerName },
      name: "歌曲信息",
    },
    { path: "", name: "评论详情" },
  ]);
  routerManager("comment", {
    query: { id, type: 0 },
  });
}

function revokeObjectUrl(url: string) {
  if (url) URL.revokeObjectURL(url);
}

function openAdd() {
  resetAddForm();
  addVisible.value = true;
}

function resetAddForm() {
  addForm.name = "";
  addForm.introduction = "";
  addForm.lyric = "";
  addForm.fileReady = 0;
  pendingSongFile.value = null;
  pendingCoverFile.value = null;
  revokeObjectUrl(addCoverObjectUrl.value);
  addCoverObjectUrl.value = "";
}

function selectAddCover({ file }: { file: UploadFileInfo }) {
  const raw = file.file;
  if (!raw || !beforeImgUpload(raw)) return;
  revokeObjectUrl(addCoverObjectUrl.value);
  pendingCoverFile.value = raw;
  addCoverObjectUrl.value = URL.createObjectURL(raw);
}

function selectAddSongFile({ file }: { file: UploadFileInfo }) {
  const raw = file.file;
  if (!raw || !beforeSongUpload(raw)) return;
  pendingSongFile.value = raw;
  addForm.fileReady = 1;
}

async function addSongSubmit() {
  try {
    await addFormRef.value?.validate();
  } catch {
    return;
  }
  const file = pendingSongFile.value;
  if (!file) {
    message.warning("请选择歌曲文件");
    return;
  }

  saving.value = true;
  try {
    const formData = new FormData();
    formData.set("name", addForm.name.trim());
    formData.set("introduction", addForm.introduction);
    formData.set("lyric", addForm.lyric || "[00:00:00]暂无歌词");
    formData.set("singerId", singerId);
    formData.set("duration", String(await parseAudioDuration(file)));
    formData.set("file", file);

    const result = await addSong(formData);
    if (!result.success) {
      notifyResult(result);
      return;
    }

    const createdId = parseCreatedId(result.data);
    if (pendingCoverFile.value) {
      if (!createdId) {
        message.warning("歌曲已添加，但未能上传封面（缺少新建 ID）");
      } else {
        const uploadResult = await uploadSongImg(
          createdId,
          pendingCoverFile.value,
        );
        if (!uploadResult.success) {
          notifyResult(uploadResult);
          message.warning("歌曲已添加，封面上传失败，可稍后在编辑中重试");
        }
      }
    }

    message.success(result.message || "添加成功");
    addVisible.value = false;
    await getData();
  } finally {
    saving.value = false;
  }
}

function parseAudioDuration(file: File): Promise<number> {
  return new Promise((resolve) => {
    const audio = document.createElement("audio");
    const objectUrl = URL.createObjectURL(file);
    const cleanup = () => {
      URL.revokeObjectURL(objectUrl);
      audio.src = "";
    };
    audio.onloadedmetadata = () => {
      const duration = Number.isFinite(audio.duration)
        ? Math.round(audio.duration)
        : 0;
      cleanup();
      resolve(Math.max(0, duration));
    };
    audio.onerror = () => {
      cleanup();
      resolve(0);
    };
    audio.src = objectUrl;
  });
}

function formatDuration(seconds: number | string | undefined) {
  const total = Math.floor(Number(seconds));
  if (!Number.isFinite(total) || total <= 0) return "00:00";
  return `${String(Math.floor(total / 60)).padStart(2, "0")}:${String(total % 60).padStart(2, "0")}`;
}

function resetEditDraft() {
  pendingEditSong.value = null;
  pendingEditCover.value = null;
  revokeObjectUrl(editCoverObjectUrl.value);
  editCoverObjectUrl.value = "";
}

function editRow(row: SongRow) {
  resetEditDraft();
  editForm.id = String(row.id);
  editForm.singerId = String(row.singerId);
  editForm.name = row.name;
  editForm.introduction = row.introduction || "";
  editForm.lyric = row.lyric || "";
  editForm.pic = row.pic || "";
  editVisible.value = true;
}

function selectEditCover({ file }: { file: UploadFileInfo }) {
  const raw = file.file;
  if (!raw || !beforeImgUpload(raw)) return;
  revokeObjectUrl(editCoverObjectUrl.value);
  pendingEditCover.value = raw;
  editCoverObjectUrl.value = URL.createObjectURL(raw);
}

function selectEditSongFile({ file }: { file: UploadFileInfo }) {
  const raw = file.file;
  if (!raw || !beforeSongUpload(raw)) return;
  pendingEditSong.value = raw;
}

async function saveEdit() {
  try {
    await editFormRef.value?.validate();
  } catch {
    return;
  }
  saving.value = true;
  try {
    if (pendingEditCover.value) {
      const uploadResult = await uploadSongImg(
        editForm.id,
        pendingEditCover.value,
      );
      if (!uploadResult.success) {
        notifyResult(uploadResult);
        return;
      }
    }

    let nextDuration: number | undefined;
    if (pendingEditSong.value) {
      nextDuration = await parseAudioDuration(pendingEditSong.value);
      const uploadResult = await uploadSongUrl(
        editForm.id,
        pendingEditSong.value,
        nextDuration,
      );
      if (!uploadResult.success) {
        notifyResult(uploadResult);
        return;
      }
    }

    const result = (await updateSongMsg({
      id: editForm.id,
      singerId: editForm.singerId,
      name: editForm.name,
      introduction: editForm.introduction,
      lyric: editForm.lyric,
      ...(nextDuration != null ? { duration: nextDuration } : {}),
    })) as ApiResponse;
    notifyResult(result);
    if (!result.success) return;
    editVisible.value = false;
    await getData();
  } finally {
    saving.value = false;
  }
}

function deleteRow(id: string | number) {
  selectedId.value = id;
  delVisible.value = true;
}

async function confirm() {
  if (selectedId.value == null) return;
  const result = (await deleteSong(selectedId.value)) as ApiResponse;
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
  for (const id of checkedRowKeys.value) {
    const result = (await deleteSong(id)) as ApiResponse;
    if (result.success) ok += 1;
    else notifyResult(result);
  }
  if (ok > 0) {
    message.success(ok > 1 ? `已删除 ${ok} 首歌曲` : "删除成功");
    await getData();
  }
}

appStore.setIsPlay(false);
if (!appStore.breadcrumbList.length && singerName) {
  appStore.setBreadcrumbList([
    { path: "/singer", name: "歌手管理" },
    { path: "", name: `${singerName} · 歌曲` },
  ]);
}
void getData();
</script>

<style scoped>
.page-root--with-crumbs {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 60px - 40px);
  min-height: 480px;
  gap: 12px;
}

.page-root--with-crumbs :deep(.crumbs) {
  margin-bottom: 0;
  flex-shrink: 0;
}

.page-root--with-crumbs .page-panel {
  flex: 1;
  min-height: 0;
}

.page-root--with-crumbs .page-toolbar {
  flex-wrap: nowrap;
}

.page-root--with-crumbs .page-toolbar__search {
  width: min(240px, 36vw) !important;
  max-width: 240px;
  flex: 0 1 240px;
}

.mini-player {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
  padding: 10px 12px;
  border-radius: 8px;
  background: #f7f8fa;
  border: 1px solid #eef0f3;
}

.mini-player__cover {
  width: 40px;
  height: 40px;
  border-radius: 6px;
  overflow: hidden;
  flex-shrink: 0;
  background: #e8eaed;
}

.mini-player__cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.mini-player__meta {
  flex: 0 1 180px;
  min-width: 0;
}

.mini-player__name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 13px;
  font-weight: 500;
  color: #1f2329;
}

.mini-player__album {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-top: 2px;
  font-size: 12px;
  color: #8a8f98;
}

.mini-player__audio {
  flex: 1 1 220px;
  min-width: 160px;
  height: 32px;
}

.file-name {
  max-width: 220px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 13px;
  color: #333;
}

.file-hint {
  font-size: 12px;
  color: #909399;
}

.preview {
  width: 96px;
  height: 96px;
  border-radius: 10px;
  overflow: hidden;
  flex-shrink: 0;
}

.preview.empty {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 12px;
  background: #f5f7fa;
  border: 1px dashed #dcdfe6;
}
</style>

<!-- n-data-table 列 render 产出的节点不带 scoped 属性，需全局样式 -->
<style>
.cover-cell {
  position: relative;
  width: 56px;
  height: 56px;
  margin: 4px 0;
  border-radius: 8px;
  overflow: hidden;
  background: #f0f2f5;
}

.cover-cell__img {
  width: 100%;
  height: 100%;
  display: block;
  object-fit: cover;
}

.cover-cell__play {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0;
  padding: 0;
  border: 0;
  cursor: pointer;
  background: rgba(0, 0, 0, 0.28);
  opacity: 0;
  transition: opacity 0.15s ease;
}

.cover-cell:hover .cover-cell__play,
.cover-cell__play.is-active {
  opacity: 1;
}

.cover-cell__icon {
  color: #fff;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.35));
}
</style>
