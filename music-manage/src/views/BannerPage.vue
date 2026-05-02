<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" @click="openAddDialog">新增轮播图</el-button>
      </div>

      <el-table height="550px" border size="small" :data="data">
        <el-table-column label="ID" prop="id" width="80" align="center"></el-table-column>
        <el-table-column label="轮播图" width="200" align="center">
          <template v-slot="scope">
            <div class="banner-img">
              <img :src="attachImageUrl(scope.row.pic)" style="width: 100%" />
            </div>
          </template>
        </el-table-column>
        <el-table-column label="标题" width="160" align="center">
          <template v-slot="scope">
            <span>{{ scope.row.title || "--" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="点击跳转地址">
          <template v-slot="scope">
            <span>{{ scope.row.url || "--" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="170" align="center">
          <template v-slot="scope">
            <el-button type="primary" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button type="danger" @click="deleteRow(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination"
        background
        layout="total, prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="tableData.length"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
    <el-dialog title="编辑轮播图" v-model="editVisible" width="520px">
      <div class="edit-content">
        <div class="banner-img banner-img--edit">
          <img :src="attachImageUrl(editForm.pic)" style="width: 100%" />
        </div>
        <el-upload
          :action="updatePicUrl(editForm.id)"
          :show-file-list="false"
          :on-success="handleEditUploadSuccess"
          :before-upload="beforeImgUpload"
        >
          <el-button>更换图片</el-button>
        </el-upload>
        <el-input
          v-model="editForm.title"
          placeholder="请输入轮播图标题"
        />
        <el-input
          v-model="editForm.url"
          placeholder="例如：https://example.com 或 /song-sheet-detail/1"
        />
      </div>
      <template #footer>
        <span>
          <el-button @click="editVisible = false">取消</el-button>
          <el-button type="primary" @click="saveBannerEdit">保存</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog title="新增轮播图" v-model="addVisible" width="520px">
      <div class="edit-content">
        <div class="banner-img banner-img--edit">
          <img :src="attachImageUrl(addForm.pic)" style="width: 100%" />
        </div>
        <el-upload
          :action="uploadUrl"
          :show-file-list="false"
          :on-success="handleAddUploadSuccess"
          :before-upload="beforeImgUpload"
        >
          <el-button>上传图片</el-button>
        </el-upload>
        <el-input
          v-model="addForm.title"
          placeholder="请输入轮播图标题"
        />
        <el-input
          v-model="addForm.url"
          placeholder="例如：https://example.com 或 /song-sheet-detail/1"
        />
      </div>
      <template #footer>
        <span>
          <el-button @click="addVisible = false">取消</el-button>
          <el-button type="primary" @click="saveBannerAdd">保存</el-button>
        </span>
      </template>
    </el-dialog>
    <yin-del-dialog :delVisible="delVisible" @confirm="confirm" @cancelRow="delVisible = $event"></yin-del-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { ElMessage } from "element-plus";
import mixin from "@/mixins/mixin";
import {
  attachImageUrl,
  getBannerAddUploadUrl,
  getBannerUpdatePicUploadUrl,
  getAllBanner,
  deleteBanner,
  updateBannerUrl,
} from "@/api";
import YinDelDialog from "@/components/dialog/YinDelDialog.vue";

const { beforeImgUpload } = mixin();

const tableData = ref<any[]>([]);
const pageSize = ref(5);
const currentPage = ref(1);
const delVisible = ref(false);
const idx = ref(-1);
const editVisible = ref(false);
const addVisible = ref(false);
const editForm = ref({
  id: 0,
  pic: "",
  title: "",
  url: "",
});
const addForm = ref({
  id: 0,
  pic: "",
  title: "",
  url: "",
});

const data = computed(() => {
  return tableData.value.slice(
    (currentPage.value - 1) * pageSize.value,
    currentPage.value * pageSize.value,
  );
});

const uploadUrl = getBannerAddUploadUrl();

async function getData() {
  const result = (await getAllBanner()) as ResponseBody;
  tableData.value = result.data || [];
  currentPage.value = 1;
}

function handleCurrentChange(val: number) {
  currentPage.value = val;
}

async function confirm() {
  const result = (await deleteBanner(idx.value)) as ResponseBody;
  ElMessage({
    message: result.message,
    type: result.type as any,
  });
  if (result.success) {
    await getData();
  }
  delVisible.value = false;
}

function deleteRow(id: number) {
  idx.value = id;
  delVisible.value = true;
}

function openAddDialog() {
  addForm.value = {
    id: 0,
    pic: "",
    title: "",
    url: "",
  };
  addVisible.value = true;
}

function handleAddUploadSuccess(response: ResponseBody) {
  ElMessage({
    message: response.message,
    type: response.type as any,
  });
  if (response.success && response.data) {
    addForm.value.id = Number(response.data.id) || 0;
    addForm.value.pic = response.data.pic || "";
  }
}

async function saveBannerUrl(row: { id: string | number; title?: string; url?: string }) {
  const result = (await updateBannerUrl({
    id: row.id,
    title: row.title || "",
    url: row.url || "",
  })) as ResponseBody;
  ElMessage({
    message: result.message,
    type: result.type as any,
  });
  if (result.success) {
    getData();
  }
}

function openEditDialog(row: { id: string | number; pic?: string; title?: string; url?: string }) {
  editForm.value = {
    id: Number(row.id) || 0,
    pic: row.pic || "",
    title: row.title || "",
    url: row.url || "",
  };
  editVisible.value = true;
}

function handleEditUploadSuccess(response: any) {
  ElMessage({
    message: response.message,
    type: response.type as any,
  });
  if (response.success && response.data && response.data.pic) {
    editForm.value.pic = response.data.pic;
  }
}

async function saveBannerEdit() {
  await saveBannerUrl(editForm.value);
  editVisible.value = false;
}

async function saveBannerAdd() {
  if (!addForm.value.id) {
    ElMessage.warning("请先上传轮播图图片");
    return;
  }
  await saveBannerUrl(addForm.value);
  addVisible.value = false;
}

function updatePicUrl(id: string | number) {
  return getBannerUpdatePicUploadUrl(id);
}

getData();
</script>

<style scoped>
.handle-box {
  margin-bottom: 12px;
}

.banner-img {
  width: 100%;
  height: 100px;
  border-radius: 6px;
  overflow: hidden;
}

.banner-img--edit {
  width: 100%;
  height: 160px;
}

.edit-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
</style>
