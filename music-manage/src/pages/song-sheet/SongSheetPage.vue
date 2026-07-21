<template>
  <div class="page-root">
    <div class="page-panel">
      <div class="page-toolbar">
        <div class="page-toolbar__left">
          <n-button :disabled="!checkedRowKeys.length" @click="deleteAll">批量删除</n-button>
          <n-input
            v-model:value="searchWord"
            class="page-toolbar__search"
            clearable
            placeholder="筛选歌单"
          />
        </div>
        <n-button type="primary" @click="openAdd">添加歌单</n-button>
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
      v-model:show="formVisible"
      preset="card"
      :title="editing ? '编辑歌单' : '添加歌单'"
      style="width: 520px"
      @after-leave="resetDraft"
    >
      <n-form ref="formRef" label-placement="left" label-width="70" :model="form" :rules="rules">
        <n-form-item label="封面">
          <n-space align="center">
            <n-image v-if="preview" class="preview" :src="preview" object-fit="cover" />
            <div v-else class="preview empty">未选择</div>
            <n-upload :default-upload="false" :show-file-list="false" @change="selectFile">
              <n-button>{{ file ? "重新选择" : editing ? "更换封面" : "上传封面" }}</n-button>
            </n-upload>
          </n-space>
        </n-form-item>
        <n-form-item label="歌单名" path="title">
          <n-input v-model:value="form.title" maxlength="50" show-count />
        </n-form-item>
        <n-form-item label="简介" path="introduction">
          <n-input v-model:value="form.introduction" type="textarea" :rows="3" maxlength="500" show-count />
        </n-form-item>
        <n-form-item label="风格" path="styleIds">
          <n-space :size="8" class="style-tags">
            <n-tag
              v-for="opt in styleOptions"
              :key="String(opt.value)"
              checkable
              :checked="form.styleIds.includes(Number(opt.value))"
              @update:checked="(checked) => toggleStyle(Number(opt.value), checked)"
            >
              {{ opt.label }}
            </n-tag>
          </n-space>
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="formVisible = false">取 消</n-button>
          <n-button type="primary" :loading="saving" @click="save">确 定</n-button>
        </n-space>
      </template>
    </n-modal>

    <yin-del-dialog
      :del-visible="delVisible"
      @confirm="confirmDelete"
      @cancel-row="delVisible = $event"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, reactive, ref } from "vue";
import type {
  DataTableColumns,
  DataTableRowKey,
  FormInst,
  FormRules,
  SelectOption,
  UploadFileInfo,
} from "naive-ui";
import { NButton, NImage, NTag, NSpace } from "naive-ui";
import {
  attachImageUrl,
  getSongList,
  getSongListStyleList,
  setSongList,
  updateSongListMsg,
  uploadSongListImg,
  deleteSongList,
} from "@/api";
import type { ApiResponse } from "@/api/types";
import YinDelDialog from "@/components/YinDelDialog.vue";
import {
  beforeImgUpload,
  message,
  notifyResult,
  parseCreatedId,
  parseUploadUrl,
} from "@/utils";
import { useClientList } from "@/composables/useClientList";
import {
  queryInt,
  queryStr,
  useListQuerySync,
} from "@/composables/useListQuerySync";
import { useAppStore } from "@/stores/app";
import { useAdminNav } from "@/composables/useAdminNav";
import { useRoute, type LocationQueryRaw } from "vue-router";

interface StyleOption {
  id: number;
  name: string;
}

interface SongListRow {
  id: number;
  title: string;
  pic?: string;
  introduction?: string;
  style?: string;
  styles?: StyleOption[];
}

const route = useRoute();

const {
  pageRows,
  pageSize,
  currentPage,
  searchWord,
  checkedRowKeys,
  filteredRows,
  setRows,
  hydrate,
  handlePageChange,
  handlePageSizeChange,
} = useClientList<SongListRow>({
  match: (row, q) =>
    !q ||
    row.title.includes(q) ||
    (row.style || "").includes(q) ||
    (row.styles || []).some((s) => s.name.includes(q)) ||
    (row.introduction || "").includes(q),
});

hydrate({
  searchWord: queryStr(route.query.q),
  page: queryInt(route.query.page, 1),
  pageSize: queryInt(route.query.size, 10),
});

function buildListQuery(): LocationQueryRaw {
  const query: LocationQueryRaw = {};
  const q = searchWord.value.trim();
  if (q) query.q = q;
  if (currentPage.value > 1) query.page = String(currentPage.value);
  if (pageSize.value !== 10) query.size = String(pageSize.value);
  return query;
}

const { buildListQuery: listQuery } = useListQuerySync(buildListQuery, [
  searchWord,
  currentPage,
  pageSize,
]);

const appStore = useAppStore();
const { routerManager } = useAdminNav();

const formVisible = ref(false);
const editing = ref(false);
const saving = ref(false);
const delVisible = ref(false);
const deleteIds = ref<number[]>([]);
const file = ref<File | null>(null);
const objectUrl = ref("");
const formRef = ref<FormInst | null>(null);
const styleLoading = ref(false);
const styleOptions = ref<SelectOption[]>([]);

const form = reactive({
  id: 0,
  title: "",
  pic: "",
  introduction: "",
  styleIds: [] as number[],
});

const rules: FormRules = {
  title: { required: true, message: "请输入歌单名", trigger: "blur" },
  styleIds: {
    type: "array",
    required: true,
    min: 1,
    message: "请至少选择一个风格",
    trigger: ["change", "blur"],
  },
};

const preview = computed(() => {
  if (objectUrl.value) return objectUrl.value;
  if (editing.value && form.pic) return attachImageUrl(form.pic);
  return "";
});

const columns: DataTableColumns<SongListRow> = [
  { type: "selection" },
  { title: "ID", key: "id", width: 64 },
  {
    title: "封面",
    key: "pic",
    width: 80,
    render: (row) =>
      h(NImage, {
        src: attachImageUrl(row.pic),
        width: 48,
        height: 48,
        objectFit: "cover",
        style: "border-radius: 8px",
      }),
  },
  { title: "标题", key: "title", ellipsis: { tooltip: true } },
  {
    title: "风格",
    key: "styles",
    width: 180,
    render: (row) => {
      const styles = row.styles?.length
        ? row.styles
        : (row.style || "")
            .split("-")
            .map((name) => name.trim())
            .filter(Boolean)
            .map((name) => ({ id: 0, name }));
      if (!styles.length) return "—";
      return h(
        NSpace,
        { size: 4, wrap: true },
        {
          default: () =>
            styles.map((s) =>
              h(NTag, { size: "small", bordered: false }, { default: () => s.name }),
            ),
        },
      );
    },
  },
  { title: "简介", key: "introduction", ellipsis: { tooltip: true } },
  {
    title: "操作",
    key: "actions",
    width: 280,
    render: (row) =>
      h("div", { class: "ops-cell" }, [
        h(
          NButton,
          {
            secondary: true,
            size: "small",
            type: "info",
            onClick: () => goContent(row),
          },
          () => "内容",
        ),
        h(
          NButton,
          {
            secondary: true,
            size: "small",
            type: "info",
            onClick: () => goComment(row),
          },
          () => "评论",
        ),
        h(
          NButton,
          {
            secondary: true,
            size: "small",
            type: "primary",
            onClick: () => openEdit(row),
          },
          () => "编辑",
        ),
        h(
          NButton,
          {
            secondary: true,
            size: "small",
            type: "error",
            onClick: () => askDelete([row.id]),
          },
          () => "删除",
        ),
      ]),
  },
];

function onCheckedKeys(keys: DataTableRowKey[]) {
  checkedRowKeys.value = keys;
}

function resetDraft() {
  if (objectUrl.value) URL.revokeObjectURL(objectUrl.value);
  objectUrl.value = "";
  file.value = null;
}

function selectFile({ file: info }: { file: UploadFileInfo }) {
  const raw = info.file;
  if (!raw || !beforeImgUpload(raw)) return;
  resetDraft();
  file.value = raw;
  objectUrl.value = URL.createObjectURL(raw);
}

async function loadStyleOptions() {
  styleLoading.value = true;
  try {
    const result = await getSongListStyleList();
    const list = Array.isArray(result.data) ? result.data : [];
    styleOptions.value = list
      .filter((item) => item.id > 0 && item.name)
      .map((item) => ({ label: item.name, value: item.id }));
  } finally {
    styleLoading.value = false;
  }
}

function toggleStyle(id: number, checked: boolean) {
  if (!Number.isFinite(id) || id <= 0) return;
  const idx = form.styleIds.indexOf(id);
  if (checked && idx < 0) form.styleIds.push(id);
  if (!checked && idx >= 0) form.styleIds.splice(idx, 1);
  void formRef.value?.validate(
    () => undefined,
    (rule) => rule?.key === "styleIds",
  );
}

async function getData() {
  const result = (await getSongList()) as ApiResponse<SongListRow[]>;
  setRows(Array.isArray(result.data) ? result.data : []);
}

function openAdd() {
  resetDraft();
  editing.value = false;
  Object.assign(form, { id: 0, title: "", pic: "", introduction: "", styleIds: [] });
  formVisible.value = true;
}

function openEdit(row: SongListRow) {
  resetDraft();
  editing.value = true;
  const styleIds = (row.styles || [])
    .map((s) => s.id)
    .filter((id) => typeof id === "number" && id > 0);
  Object.assign(form, {
    id: row.id,
    title: row.title,
    pic: row.pic || "",
    introduction: row.introduction || "",
    styleIds,
  });
  formVisible.value = true;
}

async function save() {
  try {
    await formRef.value?.validate();
  } catch {
    return;
  }

  saving.value = true;
  try {
    const styleIds = [...form.styleIds];
    let id = form.id;
    if (!editing.value) {
      const result = (await setSongList({
        title: form.title.trim(),
        introduction: form.introduction.trim(),
        styleIds,
      })) as ApiResponse;
      if (!result.success) {
        notifyResult(result);
        return;
      }
      id = parseCreatedId(result.data) || 0;
      if (file.value) {
        if (!id) {
          message.warning("歌单已创建，但未能上传封面（缺少新建 ID）");
        } else {
          const uploadResult = (await uploadSongListImg(id, file.value)) as ApiResponse;
          if (!uploadResult.success) {
            notifyResult(uploadResult);
            message.warning("歌单已创建，封面上传失败，可稍后在编辑中重试");
          }
        }
      }
      message.success(result.message || "添加成功");
    } else {
      if (file.value) {
        const uploadResult = (await uploadSongListImg(form.id, file.value)) as ApiResponse;
        if (!uploadResult.success) {
          notifyResult(uploadResult);
          return;
        }
        const nextPic = parseUploadUrl(uploadResult.data);
        if (nextPic) form.pic = nextPic;
      }
      const result = (await updateSongListMsg({
        id: form.id,
        title: form.title.trim(),
        introduction: form.introduction.trim(),
        styleIds,
      })) as ApiResponse;
      notifyResult(result);
      if (!result.success) return;
    }
    formVisible.value = false;
    await getData();
  } finally {
    saving.value = false;
  }
}

function askDelete(ids: number[]) {
  deleteIds.value = ids;
  delVisible.value = true;
}

function deleteAll() {
  if (!checkedRowKeys.value.length) {
    message.warning("请先勾选要删除的歌单");
    return;
  }
  askDelete(checkedRowKeys.value.map(Number));
}

async function confirmDelete() {
  let ok = 0;
  for (const id of deleteIds.value) {
    const result = (await deleteSongList(id)) as ApiResponse;
    if (result.success) ok += 1;
    else notifyResult(result);
  }
  if (ok > 0) {
    message.success(deleteIds.value.length > 1 ? `已删除 ${ok} 个歌单` : "删除成功");
    await getData();
  }
  delVisible.value = false;
}

function goContent(row: SongListRow) {
  appStore.setBreadcrumbList([
    { path: "/songList", name: "歌单管理", query: listQuery() },
    { path: "", name: "歌单内容" },
  ]);
  routerManager("listSong", { query: { id: row.id } });
}

function goComment(row: SongListRow) {
  appStore.setBreadcrumbList([
    { path: "/songList", name: "歌单管理", query: listQuery() },
    { path: "", name: "评论详情" },
  ]);
  routerManager("comment", {
    query: { id: row.id, type: 1 },
  });
}

onMounted(() => {
  void loadStyleOptions();
  void getData();
});
</script>

<style scoped>
.page-root {
  height: calc(100vh - 60px - 40px);
  min-height: 480px;
}

.style-tags {
  padding-top: 4px;
  min-height: 32px;
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
