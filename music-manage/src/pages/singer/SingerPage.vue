<template>
  <div class="page-root">
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
            placeholder="筛选歌手"
          />
          <n-select
            v-model:value="filterKind"
            class="page-toolbar__select"
            clearable
            placeholder="类型"
            :options="kindOptions"
          />
          <n-select
            v-model:value="filterSex"
            class="page-toolbar__select"
            clearable
            placeholder="性别"
            :disabled="filterKind !== null && filterKind !== 0"
            :options="sexOptions"
          />
        </div>
        <n-button type="primary" @click="openAdd">添加歌手</n-button>
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
      :title="editing ? '编辑歌手' : '添加歌手'"
      style="width: 520px"
      @after-leave="resetAvatarDraft"
    >
      <n-form
        ref="formRef"
        label-placement="left"
        label-width="80"
        :model="form"
        :rules="rules"
      >
        <n-form-item label="头像">
          <n-space align="center">
            <n-image
              v-if="avatarPreview"
              class="preview"
              :src="avatarPreview"
              object-fit="cover"
            />
            <div v-else class="preview empty">未选择</div>
            <n-upload
              :default-upload="false"
              :show-file-list="false"
              @change="selectAvatar"
            >
              <n-button>{{
                pendingAvatar
                  ? "重新裁剪"
                  : editing
                    ? "更换头像"
                    : "上传头像"
              }}</n-button>
            </n-upload>
          </n-space>
        </n-form-item>
        <n-form-item label="歌手名" path="name">
          <n-input v-model:value="form.name" placeholder="请输入歌手名" />
        </n-form-item>
        <n-form-item label="类型" path="kind">
          <n-radio-group v-model:value="form.kind" @update:value="onKindChange">
            <n-radio :value="0">个人</n-radio>
            <n-radio :value="1">组合</n-radio>
            <n-radio :value="2">其他</n-radio>
          </n-radio-group>
        </n-form-item>
        <n-form-item v-if="form.kind === 0" label="性别" path="sex">
          <n-radio-group v-model:value="form.sex">
            <n-radio :value="0">女</n-radio>
            <n-radio :value="1">男</n-radio>
            <n-radio :value="2">保密</n-radio>
          </n-radio-group>
        </n-form-item>
        <n-form-item label="地区">
          <n-input v-model:value="form.location" placeholder="如：中国四川" />
        </n-form-item>
        <n-form-item v-if="form.kind === 0" label="出生日期">
          <n-date-picker
            v-model:value="form.birth"
            type="date"
            clearable
            style="width: 100%"
          />
        </n-form-item>
        <n-form-item label="简介">
          <n-input
            v-model:value="form.introduction"
            type="textarea"
            :rows="4"
            placeholder="歌手介绍"
          />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="formVisible = false">取 消</n-button>
          <n-button type="primary" :loading="saving" @click="save"
            >确 定</n-button
          >
        </n-space>
      </template>
    </n-modal>

    <yin-del-dialog
      :del-visible="delVisible"
      @confirm="confirmDelete"
      @cancel-row="delVisible = $event"
    />

    <image-crop-dialog
      v-model:show="cropVisible"
      :image-src="cropSourceUrl"
      :file-name="cropFileName"
      title="裁剪歌手头像"
      :aspect-ratio="1"
      @confirm="onCropConfirm"
      @cancel="onCropCancel"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, h, reactive, ref, watch } from "vue";
import { useRoute, type LocationQueryRaw } from "vue-router";
import type {
  DataTableColumns,
  DataTableRowKey,
  FormInst,
  FormRules,
  SelectOption,
  UploadFileInfo,
} from "naive-ui";
import { NButton, NImage, NTag } from "naive-ui";
import {
  attachImageUrl,
  getAllSinger,
  setSinger,
  updateSingerMsg,
  uploadSingerAvatar,
  deleteSinger,
} from "@/api";
import type { ApiResponse } from "@/api/types";
import YinDelDialog from "@/components/YinDelDialog.vue";
import ImageCropDialog from "@/components/ImageCropDialog.vue";
import {
  beforeImgUpload,
  changeSingerKind,
  changeSingerSex,
  formatBirthForBackend,
  getBirth,
  message,
  notifyResult,
  parseCreatedId,
  parseUploadUrl,
  singerKindTagType,
  singerSexTagType,
  toDateTimestamp,
} from "@/utils";
import { useClientList } from "@/composables/useClientList";
import {
  queryInt,
  queryOptionalInt,
  queryStr,
  useListQuerySync,
} from "@/composables/useListQuerySync";
import { useAppStore } from "@/stores/app";
import { useAdminNav } from "@/composables/useAdminNav";

interface SingerRow {
  id: number;
  name: string;
  sex?: number | string | null;
  kind?: number | string;
  pic?: string;
  birth?: string;
  location?: string;
  introduction?: string;
}

const route = useRoute();

const filterKind = ref<number | null>(null);
const filterSex = ref<number | null>(null);

const kindOptions: SelectOption[] = [
  { label: "个人", value: 0 },
  { label: "组合", value: 1 },
  { label: "其他", value: 2 },
];

const sexOptions: SelectOption[] = [
  { label: "女", value: 0 },
  { label: "男", value: 1 },
  { label: "保密", value: 2 },
];

function rowKind(row: SingerRow): number | null {
  if (row.kind === null || row.kind === undefined || row.kind === "") return null;
  const n = typeof row.kind === "number" ? row.kind : Number(row.kind);
  return Number.isFinite(n) ? n : null;
}

function rowSex(row: SingerRow): number | null {
  if (row.sex === null || row.sex === undefined || row.sex === "") return null;
  const n = typeof row.sex === "number" ? row.sex : Number(row.sex);
  return Number.isFinite(n) ? n : null;
}

const {
  pageRows,
  pageSize,
  currentPage,
  searchWord,
  checkedRowKeys,
  filteredRows,
  setRows,
  applySearch,
  hydrate,
  handlePageChange,
  handlePageSizeChange,
} = useClientList<SingerRow>({
  match: (row, q) => {
    if (q && !row.name.includes(q)) return false;
    if (filterKind.value !== null && rowKind(row) !== filterKind.value) return false;
    if (filterSex.value !== null && rowSex(row) !== filterSex.value) return false;
    return true;
  },
});

// 先回填筛选与分页，再挂 watch，避免初始化把页码冲掉
filterKind.value = queryOptionalInt(route.query.kind);
filterSex.value = queryOptionalInt(route.query.sex);
if (filterKind.value !== null && filterKind.value !== 0) filterSex.value = null;
hydrate({
  searchWord: queryStr(route.query.q),
  page: queryInt(route.query.page, 1),
  pageSize: queryInt(route.query.size, 10),
});

watch(filterKind, (kind) => {
  if (kind !== null && kind !== 0) filterSex.value = null;
  applySearch();
});
watch(filterSex, applySearch);

function buildListQuery(): LocationQueryRaw {
  const query: LocationQueryRaw = {};
  const q = searchWord.value.trim();
  if (q) query.q = q;
  if (currentPage.value > 1) query.page = String(currentPage.value);
  if (pageSize.value !== 10) query.size = String(pageSize.value);
  if (filterKind.value !== null) query.kind = String(filterKind.value);
  if (filterSex.value !== null) query.sex = String(filterSex.value);
  return query;
}

const { buildListQuery: listQuery } = useListQuerySync(buildListQuery, [
  searchWord,
  currentPage,
  pageSize,
  filterKind,
  filterSex,
]);

const appStore = useAppStore();
const { routerManager } = useAdminNav();

const formVisible = ref(false);
const editing = ref(false);
const saving = ref(false);
const delVisible = ref(false);
const deleteIds = ref<number[]>([]);
const pendingAvatar = ref<File | null>(null);
const avatarObjectUrl = ref("");
const cropVisible = ref(false);
const cropSourceUrl = ref("");
const cropFileName = ref("avatar.jpg");
const formRef = ref<FormInst | null>(null);

const form = reactive({
  id: 0,
  name: "",
  kind: 0,
  sex: 1 as number | null,
  pic: "",
  birth: null as number | null,
  location: "",
  introduction: "",
});

const rules: FormRules = {
  name: { required: true, message: "请输入歌手名", trigger: "blur" },
  kind: {
    required: true,
    type: "number",
    message: "请选择类型",
    trigger: "change",
  },
  sex: {
    trigger: "change",
    validator: () => {
      if (form.kind !== 0) return true;
      if (form.sex === 0 || form.sex === 1 || form.sex === 2) return true;
      return new Error("请选择性别");
    },
  },
};

function onKindChange(kind: number) {
  if (kind !== 0) {
    form.sex = null;
    form.birth = null;
  } else if (form.sex === null) {
    form.sex = 1;
  }
}

const avatarPreview = computed(() => {
  if (avatarObjectUrl.value) return avatarObjectUrl.value;
  if (editing.value && form.pic) return attachImageUrl(form.pic);
  return "";
});

const columns: DataTableColumns<SingerRow> = [
  { type: "selection" },
  { title: "ID", key: "id", width: 64 },
  {
    title: "头像",
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
  { title: "歌手名", key: "name", width: 150, ellipsis: { tooltip: true } },
  {
    title: "类型",
    key: "kind",
    width: 70,
    render: (row) =>
      h(
        NTag,
        { size: "small", type: singerKindTagType(row.kind), bordered: false },
        () => changeSingerKind(row.kind),
      ),
  },
  {
    title: "性别",
    key: "sex",
    width: 70,
    render: (row) =>
      h(
        NTag,
        { size: "small", type: singerSexTagType(row.sex), bordered: false },
        () => changeSingerSex(row.sex),
      ),
  },
  {
    title: "出生",
    key: "birth",
    width: 110,
    render: (row) =>
      rowKind(row) === 0 ? getBirth(row.birth) : "—",
  },
  { title: "地区", key: "location", width: 100, ellipsis: { tooltip: true } },
  { title: "简介", key: "introduction", ellipsis: { tooltip: true } },
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
            onClick: () => goSongPage(row),
          },
          () => "歌曲",
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

function resetAvatarDraft() {
  if (avatarObjectUrl.value) URL.revokeObjectURL(avatarObjectUrl.value);
  avatarObjectUrl.value = "";
  pendingAvatar.value = null;
}

function revokeCropSource() {
  if (cropSourceUrl.value) URL.revokeObjectURL(cropSourceUrl.value);
  cropSourceUrl.value = "";
}

function selectAvatar({ file }: { file: UploadFileInfo }) {
  const raw = file.file;
  if (!raw || !beforeImgUpload(raw)) return;
  revokeCropSource();
  const ext = raw.name.includes(".")
    ? raw.name.slice(raw.name.lastIndexOf(".")).toLowerCase()
    : ".jpg";
  // canvas 无法可靠导出 gif，统一落到 jpg/png
  const safeExt =
    ext === ".png" ? ".png" : ext === ".jpeg" || ext === ".jpg" ? ".jpg" : ".jpg";
  cropFileName.value = `singer-avatar${safeExt}`;
  cropSourceUrl.value = URL.createObjectURL(raw);
  cropVisible.value = true;
}

function onCropConfirm(file: File) {
  revokeCropSource();
  resetAvatarDraft();
  pendingAvatar.value = file;
  avatarObjectUrl.value = URL.createObjectURL(file);
}

function onCropCancel() {
  revokeCropSource();
}

async function getData() {
  const result = (await getAllSinger()) as ApiResponse<SingerRow[]>;
  setRows(Array.isArray(result.data) ? result.data : []);
}

function openAdd() {
  resetAvatarDraft();
  editing.value = false;
  Object.assign(form, {
    id: 0,
    name: "",
    kind: 0,
    sex: 1,
    pic: "",
    birth: Date.now(),
    location: "",
    introduction: "",
  });
  formVisible.value = true;
}

function openEdit(row: SingerRow) {
  resetAvatarDraft();
  editing.value = true;
  const kindNum = typeof row.kind === "number" ? row.kind : Number(row.kind);
  const sexNum =
    row.sex === null || row.sex === undefined || row.sex === ""
      ? null
      : typeof row.sex === "number"
        ? row.sex
        : Number(row.sex);
  const kind = Number.isFinite(kindNum) ? kindNum : 0;
  Object.assign(form, {
    id: row.id,
    name: row.name,
    kind,
    sex: kind === 0 ? (sexNum !== null && Number.isFinite(sexNum) ? sexNum : 1) : null,
    pic: row.pic || "",
    birth: kind === 0 ? toDateTimestamp(row.birth) : null,
    location: row.location || "",
    introduction: row.introduction || "",
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
    if (!editing.value) {
      const result = (await setSinger({
        name: form.name,
        kind: form.kind,
        sex: form.kind === 0 ? form.sex : null,
        birth: form.kind === 0 ? formatBirthForBackend(form.birth) : null,
        location: form.location,
        introduction: form.introduction,
      })) as ApiResponse;
      if (!result.success) {
        notifyResult(result);
        return;
      }
      const id = parseCreatedId(result.data) || 0;
      if (pendingAvatar.value) {
        if (!id) {
          message.warning("歌手已创建，但未能上传头像（缺少新建 ID）");
        } else {
          const uploadResult = (await uploadSingerAvatar(
            id,
            pendingAvatar.value,
          )) as ApiResponse;
          if (!uploadResult.success) {
            notifyResult(uploadResult);
            message.warning("歌手已创建，头像上传失败，可稍后在编辑中重试");
          }
        }
      }
      message.success(result.message || "添加成功");
    } else {
      if (pendingAvatar.value) {
        const uploadResult = (await uploadSingerAvatar(
          form.id,
          pendingAvatar.value,
        )) as ApiResponse;
        if (!uploadResult.success) {
          notifyResult(uploadResult);
          return;
        }
        const nextPic = parseUploadUrl(uploadResult.data);
        if (nextPic) form.pic = nextPic;
      }
      const result = (await updateSingerMsg({
        id: form.id,
        name: form.name,
        kind: form.kind,
        sex: form.kind === 0 ? form.sex : null,
        birth: form.kind === 0 ? formatBirthForBackend(form.birth) : null,
        location: form.location,
        introduction: form.introduction,
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
    message.warning("请先勾选要删除的歌手");
    return;
  }
  askDelete(checkedRowKeys.value.map(Number));
}

async function confirmDelete() {
  let ok = 0;
  for (const id of deleteIds.value) {
    const result = (await deleteSinger(id)) as ApiResponse;
    if (result.success) ok += 1;
    else notifyResult(result);
  }
  if (ok > 0) {
    message.success(
      deleteIds.value.length > 1 ? `已删除 ${ok} 位歌手` : "删除成功",
    );
    await getData();
  }
  delVisible.value = false;
}

function goSongPage(row: SingerRow) {
  appStore.setBreadcrumbList([
    { path: "/singer", name: "歌手管理", query: listQuery() },
    { path: "", name: `${row.name} · 歌曲` },
  ]);
  routerManager("song", {
    query: { id: row.id, name: row.name },
  });
}

void getData();
</script>

<style scoped>
.page-root {
  height: calc(100vh - 60px - 40px);
  min-height: 480px;
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
