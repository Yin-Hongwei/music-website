<template>
  <div class="song-list-page">
    <div class="container">
      <div class="handle-box">
        <el-button @click="deleteAll">批量删除</el-button>
        <el-input v-model="searchWord" placeholder="筛选关键词"></el-input>
        <el-button type="primary" @click="centerDialogVisible = true">添加歌单</el-button>
      </div>
      <el-table height="550px" border size="small" :data="data" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40" align="center"></el-table-column>
        <el-table-column label="ID" prop="id" width="50" align="center"></el-table-column>
        <el-table-column label="歌单图片" width="110" align="center">
          <template v-slot="scope">
            <img :src="attachImageUrl(scope.row.pic)" style="width: 80px" />
            <el-upload :action="uploadUrl(scope.row.id)" :show-file-list="false" :on-success="handleImgSuccess"
              :before-upload="beforeImgUpload">
              <el-button>更新图片</el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" width="200"></el-table-column>
        <el-table-column label="简介">
          <template v-slot="scope">
            <p style="height: 100px; overflow: scroll">
              {{ scope.row.introduction }}
            </p>
          </template>
        </el-table-column>
        <el-table-column label="风格" prop="style" width="100"></el-table-column>
        <el-table-column label="内容" width="90" align="center">
          <template v-slot="scope">
            <el-button @click="goContentPage(scope.row.id)">内容</el-button>
          </template>
        </el-table-column>
        <el-table-column label="评论" width="90" align="center">
          <template v-slot="scope">
            <el-button @click="goCommentPage(scope.row.id)">评论</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center">
          <template v-slot="scope">
            <el-button @click="editRow(scope.row)">编辑</el-button>
            <el-button type="danger" @click="deleteRow(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination class="pagination" background layout="total, prev, pager, next" :current-page="currentPage"
        :page-size="pageSize" :total="tableData.length" @current-change="handleCurrentChange">
      </el-pagination>
    </div>

    <!--添加歌单-->
    <el-dialog title="添加歌单" v-model="centerDialogVisible">
      <el-form label-width="70px" :model="registerForm">
        <el-form-item label="歌单名" prop="title">
          <el-input v-model="registerForm.title"></el-input>
        </el-form-item>
        <el-form-item label="歌单介绍" prop="introduction">
          <el-input v-model="registerForm.introduction"></el-input>
        </el-form-item>
        <el-form-item label="风格" prop="style">
          <el-input v-model="registerForm.style"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addsongList">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" v-model="editVisible">
      <el-form :model="editForm">
        <el-form-item label="标题">
          <el-input v-model="editForm.title"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input type="textarea" v-model="editForm.introduction"></el-input>
        </el-form-item>
        <el-form-item label="风格">
          <el-input v-model="editForm.style"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 删除提示框 -->
    <yin-del-dialog :delVisible="delVisible" @confirm="confirm" @cancelRow="delVisible = $event"></yin-del-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import { useAppStore } from "@/stores/app";
import mixin from "@/mixins/mixin";
import {
  attachImageUrl,
  getSongList,
  setSongList,
  updateSongListMsg,
  deleteSongList,
} from "@/api";
import { RouterName } from "@/enums";
import YinDelDialog from "@/components/dialog/YinDelDialog.vue";

interface SongListRow {
  id: number | string;
  title: string;
  pic?: string;
  introduction?: string;
  style?: string;
}

const { routerManager, beforeImgUpload } = mixin();
const appStore = useAppStore();

const tableData = ref<SongListRow[]>([]);
const tempDate = ref<SongListRow[]>([]);
const pageSize = ref(5);
const currentPage = ref(1);

const data = computed(() =>
  tableData.value.slice(
    (currentPage.value - 1) * pageSize.value,
    currentPage.value * pageSize.value,
  ),
);

const searchWord = ref("");
watch(searchWord, () => {
  if (searchWord.value === "") {
    tableData.value = [...tempDate.value];
  } else {
    tableData.value = tempDate.value.filter((item) => item.title.includes(searchWord.value));
  }
});

async function getData() {
  tableData.value = [];
  tempDate.value = [];
  const result = (await getSongList()) as ResponseBody;
  const rows = (Array.isArray(result.data) ? result.data : []) as SongListRow[];
  tableData.value = rows;
  tempDate.value = rows;
  currentPage.value = 1;
}

function handleCurrentChange(val: number) {
  currentPage.value = val;
}

function uploadUrl(id: number | string) {
  return attachImageUrl(`/songList/img/update?id=${id}`);
}

function handleImgSuccess(response: ResponseBody) {
  ElMessage({
    message: response.message,
    type: response.type as "success" | "warning" | "info" | "error",
  });
  if (response.success) void getData();
}

function goContentPage(id: number | string) {
  const breadcrumbList = reactive([
    { path: RouterName.SongList, name: "歌单管理" },
    { path: "", name: "歌单内容" },
  ]);
  appStore.setBreadcrumbList(breadcrumbList);
  routerManager(RouterName.ListSong, { path: RouterName.ListSong, query: { id } });
}

function goCommentPage(id: number | string) {
  const breadcrumbList = reactive([
    { path: RouterName.SongList, name: "歌单管理" },
    { path: "", name: "评论详情" },
  ]);
  appStore.setBreadcrumbList(breadcrumbList);
  routerManager(RouterName.Comment, { path: RouterName.Comment, query: { id, type: 1 } });
}

const centerDialogVisible = ref(false);
const registerForm = reactive({
  title: "",
  introduction: "",
  style: "",
});

async function addsongList() {
  const { title, introduction, style } = registerForm;
  const result = (await setSongList({ title, introduction, style })) as ResponseBody;
  ElMessage({
    message: result.message,
    type: result.type as "success" | "warning" | "info" | "error",
  });

  if (result.success) {
    void getData();
    registerForm.title = "";
    registerForm.introduction = "";
    registerForm.style = "";
  }
  centerDialogVisible.value = false;
}

const editVisible = ref(false);
const editForm = reactive({
  id: "" as string | number,
  title: "",
  pic: "",
  introduction: "",
  style: "",
});

const idx = ref<number | string>(-1);
const multipleSelection = ref<SongListRow[]>([]);
const delVisible = ref(false);

function editRow(row: SongListRow) {
  idx.value = row.id;
  editForm.id = row.id;
  editForm.title = row.title;
  editForm.pic = row.pic != null ? row.pic : "";
  editForm.introduction = row.introduction != null ? row.introduction : "";
  editForm.style = row.style != null ? row.style : "";
  editVisible.value = true;
}

async function saveEdit() {
  const { id, title, introduction, style } = editForm;
  const result = (await updateSongListMsg({ id, title, introduction, style })) as ResponseBody;
  ElMessage({
    message: result.message,
    type: result.type as "success" | "warning" | "info" | "error",
  });

  if (result.success) void getData();
  editVisible.value = false;
}

async function confirm() {
  const result = (await deleteSongList(idx.value)) as ResponseBody;
  ElMessage({
    message: result.message,
    type: result.type as "success" | "warning" | "info" | "error",
  });
  if (result.success) void getData();
  delVisible.value = false;
}

function deleteRow(id: number | string) {
  idx.value = id;
  delVisible.value = true;
}

function handleSelectionChange(val: SongListRow[]) {
  multipleSelection.value = val;
}

function deleteAll() {
  for (const item of multipleSelection.value) {
    deleteRow(item.id);
    void confirm();
  }
  multipleSelection.value = [];
}

getData();
</script>

<style scoped>
.song-list-page {
  display: contents;
}
</style>
