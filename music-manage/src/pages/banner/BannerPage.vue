<template>
  <div class="page-root">
    <div class="page-panel">
      <div class="page-toolbar">
        <div class="page-toolbar__left">
          <n-button :disabled="!checkedRowKeys.length" @click="deleteAll"
            >批量删除</n-button
          >
        </div>
        <n-button type="primary" @click="openAddDialog">新增轮播图</n-button>
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
      :title="editing ? '编辑轮播图' : '新增轮播图'"
      style="width: 520px"
      @after-leave="resetFormDraft"
    >
      <n-form
        ref="formRef"
        label-placement="left"
        label-width="80"
        :model="form"
        :rules="rules"
      >
        <n-form-item label="图片">
          <n-space align="center">
            <n-image
              v-if="previewSrc"
              class="preview"
              :src="previewSrc"
              object-fit="cover"
            />
            <div v-else class="preview empty">未上传</div>
            <n-upload
              :action="editing ? updatePicUrl(form.id) : uploadUrl"
              :show-file-list="false"
              @before-upload="beforeFormUpload"
              @finish="handleFormUploadFinish"
            >
              <n-button>{{
                previewSrc ? "更换图片" : "上传图片"
              }}</n-button>
            </n-upload>
          </n-space>
        </n-form-item>
        <n-form-item label="标题" path="title">
          <n-input
            v-model:value="form.title"
            maxlength="50"
            show-count
            placeholder="请输入轮播图标题"
          />
        </n-form-item>
        <n-form-item label="跳转地址" path="url">
          <n-input
            v-model:value="form.url"
            placeholder="例如：https://example.com"
          />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="formVisible = false">取 消</n-button>
          <n-button type="primary" :loading="saving" @click="saveForm"
            >确 定</n-button
          >
        </n-space>
      </template>
    </n-modal>
    <YinDelDialog
      :del-visible="delVisible"
      @confirm="confirmDelete"
      @cancel-row="delVisible = $event"
    />
  </div>
</template>
<script setup lang="ts">
import { computed, h, reactive, ref } from "vue";
import type {
  DataTableColumns,
  DataTableRowKey,
  FormInst,
  FormRules,
  UploadFileInfo,
} from "naive-ui";
import { NButton, NImage } from "naive-ui";
import {
  attachImageUrl,
  getBannerAddUploadUrl,
  getBannerUpdatePicUploadUrl,
  getAllBanner,
  deleteBanner,
  updateBannerUrl,
} from "@/api";
import type { ApiResponse } from "@/api/types";
import YinDelDialog from "@/components/YinDelDialog.vue";
import {
  beforeImgUpload,
  message,
  notifyResult,
  parseUploadUrl,
} from "@/utils";
import { useClientList } from "@/composables/useClientList";

interface Banner {
  id: number;
  pic?: string;
  title?: string;
  url?: string;
}

const {
  pageRows,
  pageSize,
  currentPage,
  checkedRowKeys,
  filteredRows,
  setRows,
  handlePageChange,
  handlePageSizeChange,
} = useClientList<Banner>();

const formVisible = ref(false);
const editing = ref(false);
const saving = ref(false);
const delVisible = ref(false);
const deleteIds = ref<number[]>([]);
const localPreview = ref("");
const formRef = ref<FormInst | null>(null);
const form = reactive({
  id: 0,
  pic: "",
  title: "",
  url: "",
});
const uploadUrl = getBannerAddUploadUrl();

const rules: FormRules = {
  title: { required: true, message: "请输入标题", trigger: "blur" },
};

const image = (pic?: string) => (pic?.trim() ? attachImageUrl(pic) : "");
const previewSrc = computed(() => localPreview.value || image(form.pic));

const columns: DataTableColumns<Banner> = [
  { type: "selection" },
  { title: "ID", key: "id", width: 80 },
  {
    title: "轮播图",
    key: "pic",
    width: 200,
    render: (r) =>
      h(NImage, {
        src: image(r.pic),
        width: 180,
        height: 100,
        objectFit: "cover",
      }),
  },
  { title: "标题", key: "title", render: (r) => r.title || "--" },
  { title: "点击跳转地址", key: "url", render: (r) => r.url || "--" },
  {
    title: "操作",
    key: "actions",
    width: 160,
    render: (r) =>
      h("div", { class: "ops-cell" }, [
        h(
          NButton,
          {
            secondary: true,
            size: "small",
            type: "primary",
            onClick: () => openEditDialog(r),
          },
          { default: () => "编辑" },
        ),
        h(
          NButton,
          {
            secondary: true,
            size: "small",
            type: "error",
            onClick: () => askDelete([r.id]),
          },
          { default: () => "删除" },
        ),
      ]),
  },
];

function onCheckedKeys(keys: DataTableRowKey[]) {
  checkedRowKeys.value = keys;
}

function revoke(url: string) {
  if (url.startsWith("blob:")) URL.revokeObjectURL(url);
}

function clearLocalPreview() {
  revoke(localPreview.value);
  localPreview.value = "";
}

function resetFormDraft() {
  clearLocalPreview();
  form.id = 0;
  form.pic = "";
  form.title = "";
  form.url = "";
  formRef.value?.restoreValidation();
}

function beforeFormUpload({ file }: { file: UploadFileInfo }) {
  const raw = file.file;
  if (!raw || !beforeImgUpload(raw)) return false;
  clearLocalPreview();
  localPreview.value = URL.createObjectURL(raw);
  return true;
}

function uploadBody(file: UploadFileInfo) {
  return file.response as
    | ApiResponse<{ id?: number; pic?: string }>
    | undefined;
}

function handleFormUploadFinish({ file }: { file: UploadFileInfo }) {
  const result = uploadBody(file);
  if (!result) {
    message.error("上传失败");
    return;
  }
  notifyResult(result);
  if (result.success && result.data) {
    if (!editing.value) {
      form.id = Number(result.data.id) || 0;
    }
    form.pic = result.data.pic || parseUploadUrl(result.data);
    clearLocalPreview();
  }
}

async function getData() {
  const result = (await getAllBanner()) as ApiResponse<Banner[]>;
  setRows(Array.isArray(result.data) ? result.data : []);
}

function askDelete(ids: number[]) {
  deleteIds.value = ids;
  delVisible.value = true;
}

function deleteAll() {
  if (!checkedRowKeys.value.length) {
    message.warning("请先勾选要删除的轮播图");
    return;
  }
  askDelete(checkedRowKeys.value.map(Number));
}

async function confirmDelete() {
  let ok = 0;
  for (const id of deleteIds.value) {
    const result = (await deleteBanner(id)) as ApiResponse;
    if (result.success) ok += 1;
    else notifyResult(result);
  }
  if (ok > 0) {
    message.success(
      deleteIds.value.length > 1 ? `已删除 ${ok} 张轮播图` : "删除成功",
    );
    await getData();
  }
  delVisible.value = false;
}

function openAddDialog() {
  editing.value = false;
  resetFormDraft();
  formVisible.value = true;
}

function openEditDialog(row: Banner) {
  editing.value = true;
  clearLocalPreview();
  form.id = row.id;
  form.pic = row.pic || "";
  form.title = row.title || "";
  form.url = row.url || "";
  formVisible.value = true;
}

async function saveForm() {
  try {
    await formRef.value?.validate();
  } catch {
    return;
  }
  if (!form.id || !form.pic) {
    message.warning("请先上传轮播图图片");
    return;
  }
  saving.value = true;
  try {
    const result = (await updateBannerUrl({
      id: form.id,
      title: form.title || "",
      url: form.url || "",
    })) as ApiResponse;
    notifyResult(result);
    if (result.success) {
      formVisible.value = false;
      await getData();
    }
  } finally {
    saving.value = false;
  }
}

function updatePicUrl(id: number) {
  return getBannerUpdatePicUploadUrl(id);
}

void getData();
</script>
<style scoped>
.preview {
  width: 160px;
  height: 90px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.preview.empty {
  display: grid;
  place-items: center;
  border: 1px dashed #dcdfe6;
  color: #909399;
  font-size: 13px;
  background: #fafafa;
}
</style>
