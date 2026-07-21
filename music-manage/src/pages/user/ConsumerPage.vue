<template>
  <div class="page-root">
    <div class="page-panel">
      <div class="page-toolbar">
        <div class="page-toolbar__left">
          <n-button :disabled="!checkedRowKeys.length" @click="deleteAll">批量删除</n-button>
          <n-input
            v-model:value="searchWord"
            clearable
            class="page-toolbar__search"
            placeholder="筛选用户"
          />
        </div>
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
        @update:page="handlePageChange"
      />
    </div>

    <n-modal
      v-model:show="editVisible"
      preset="card"
      title="编辑用户"
      style="width: 520px"
      @after-leave="resetAvatarDraft"
    >
      <n-form ref="formRef" label-placement="left" label-width="80" :model="editForm" :rules="rules">
        <n-form-item label="头像">
          <n-space align="center">
            <n-image class="edit-avatar" :src="avatarPreviewSrc" object-fit="cover" />
            <n-upload :default-upload="false" :show-file-list="false" @change="handleAvatarSelect">
              <n-button>更换头像</n-button>
            </n-upload>
          </n-space>
        </n-form-item>
        <n-form-item label="用户名" path="username">
          <n-input v-model:value="editForm.username" />
        </n-form-item>
        <n-form-item label="性别">
          <n-radio-group v-model:value="editForm.sex">
            <n-radio :value="0">女</n-radio>
            <n-radio :value="1">男</n-radio>
            <n-radio :value="2">保密</n-radio>
          </n-radio-group>
        </n-form-item>
        <n-form-item label="手机">
          <n-input v-model:value="editForm.phoneNum" />
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="editForm.email" />
        </n-form-item>
        <n-form-item label="生日">
          <n-date-picker v-model:value="editForm.birth" type="date" clearable style="width: 100%" />
        </n-form-item>
        <n-form-item label="签名">
          <n-input v-model:value="editForm.introduction" type="textarea" :rows="3" />
        </n-form-item>
        <n-form-item label="地区">
          <n-input v-model:value="editForm.location" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="editVisible = false">取 消</n-button>
          <n-button type="primary" :loading="saving" @click="saveEdit">确 定</n-button>
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
  getAllUser,
  updateUserMsg,
  uploadUserAvatar,
  deleteUser,
} from "@/api";
import type { ApiResponse } from "@/api/types";
import YinDelDialog from "@/components/YinDelDialog.vue";
import {
  beforeImgUpload,
  changeUserSex,
  formatBirthForBackend,
  getBirth,
  message,
  notifyResult,
  parseUploadUrl,
  toDateTimestamp,
} from "@/utils";
import { useClientList } from "@/composables/useClientList";

interface ConsumerRow {
  id: number;
  avatar?: string;
  username: string;
  sex?: number | string;
  phoneNum?: string;
  email?: string;
  birth?: string;
  introduction?: string;
  location?: string;
}

const {
  pageRows,
  pageSize,
  currentPage,
  searchWord,
  checkedRowKeys,
  filteredRows,
  setRows,
  handlePageChange,
} = useClientList<ConsumerRow>({
  match: (row, q) => row.username.includes(q),
});

const editVisible = ref(false);
const saving = ref(false);
const delVisible = ref(false);
const deleteIds = ref<number[]>([]);
const pendingAvatar = ref<File | null>(null);
const avatarObjectUrl = ref("");
const formRef = ref<FormInst | null>(null);

const editForm = reactive({
  id: 0,
  avatar: "",
  username: "",
  sex: 2,
  phoneNum: "",
  email: "",
  birth: null as number | null,
  introduction: "",
  location: "",
});

const rules: FormRules = {
  username: { required: true, message: "请输入用户名", trigger: "blur" },
  email: {
    trigger: ["blur", "input"],
    validator: (_rule, value: string) => {
      if (!value || !value.trim()) return true;
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim())
        ? true
        : new Error("请输入正确的邮箱地址");
    },
  },
};

const avatarPreviewSrc = computed(
  () => avatarObjectUrl.value || attachImageUrl(editForm.avatar),
);

const columns: DataTableColumns<ConsumerRow> = [
  { type: "selection" },
  { title: "ID", key: "id", width: 64 },
  {
    title: "用户头像",
    key: "avatar",
    width: 90,
    render: (row) =>
      h(NImage, {
        src: attachImageUrl(row.avatar),
        width: 56,
        height: 56,
        objectFit: "cover",
        style: "border-radius: 8px",
      }),
  },
  { title: "用户名", key: "username", width: 100 },
  {
    title: "性别",
    key: "sex",
    width: 60,
    render: (row) => changeUserSex(row.sex),
  },
  { title: "手机号码", key: "phoneNum", width: 120 },
  { title: "邮箱", key: "email", minWidth: 140 },
  {
    title: "生日",
    key: "birth",
    width: 110,
    render: (row) => getBirth(row.birth),
  },
  { title: "签名", key: "introduction", ellipsis: { tooltip: true } },
  { title: "地区", key: "location", width: 80 },
  {
    title: "操作",
    key: "actions",
    width: 160,
    render: (row) =>
      h("div", { class: "ops-cell" }, [
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

function handleAvatarSelect({ file }: { file: UploadFileInfo }) {
  const raw = file.file;
  if (!raw || !beforeImgUpload(raw)) return;
  resetAvatarDraft();
  pendingAvatar.value = raw;
  avatarObjectUrl.value = URL.createObjectURL(raw);
}

async function getData() {
  const result = (await getAllUser()) as ApiResponse<ConsumerRow[]>;
  setRows(Array.isArray(result.data) ? result.data : []);
}

function editRow(row: ConsumerRow) {
  resetAvatarDraft();
  const sexNum = typeof row.sex === "number" ? row.sex : Number(row.sex);
  Object.assign(editForm, {
    id: row.id,
    avatar: row.avatar || "",
    username: row.username,
    sex: Number.isFinite(sexNum) ? sexNum : 2,
    phoneNum: row.phoneNum || "",
    email: row.email || "",
    birth: toDateTimestamp(row.birth),
    introduction: row.introduction || "",
    location: row.location || "",
  });
  editVisible.value = true;
}

async function saveEdit() {
  try {
    await formRef.value?.validate();
  } catch {
    return;
  }

  saving.value = true;
  try {
    if (pendingAvatar.value) {
      const uploadResult = (await uploadUserAvatar(
        editForm.id,
        pendingAvatar.value,
      )) as ApiResponse;
      if (!uploadResult.success) {
        notifyResult(uploadResult);
        return;
      }
      const nextAvatar = parseUploadUrl(uploadResult.data);
      if (nextAvatar) editForm.avatar = nextAvatar;
    }

    const result = (await updateUserMsg({
      id: editForm.id,
      username: editForm.username,
      sex: editForm.sex,
      phoneNum: editForm.phoneNum,
      email: editForm.email,
      birth: formatBirthForBackend(editForm.birth),
      introduction: editForm.introduction,
      location: editForm.location,
    })) as ApiResponse;
    notifyResult(result);
    if (result.success) {
      editVisible.value = false;
      await getData();
    }
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
    message.warning("请先勾选要删除的用户");
    return;
  }
  askDelete(checkedRowKeys.value.map(Number));
}

async function confirmDelete() {
  let ok = 0;
  for (const id of deleteIds.value) {
    const result = (await deleteUser(id)) as ApiResponse;
    if (result.success) ok += 1;
    else notifyResult(result);
  }
  if (ok > 0) {
    message.success(deleteIds.value.length > 1 ? `已删除 ${ok} 个用户` : "删除成功");
    await getData();
  }
  delVisible.value = false;
}

void getData();
</script>

<style scoped>
.page-root {
  height: calc(100vh - 60px - 40px);
  min-height: 480px;
}

.edit-avatar {
  width: 96px;
  height: 96px;
  border-radius: 10px;
  overflow: hidden;
  flex-shrink: 0;
}
</style>
