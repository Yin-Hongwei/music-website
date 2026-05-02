<template>
  <div class="consumer-page">
    <div class="container">
      <div class="handle-box">
        <el-button @click="deleteAll">批量删除</el-button>
        <el-input v-model="searchWord" placeholder="筛选用户"></el-input>
      </div>

      <el-table height="550px" border size="small" :data="data" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40" align="center"></el-table-column>
        <el-table-column label="ID" prop="id" width="50" align="center"></el-table-column>
        <el-table-column label="用户头像" width="102" align="center">
          <template v-slot="scope">
            <img :src="attachImageUrl(scope.row.avatar)" style="width: 80px" />
          </template>
        </el-table-column>
        <el-table-column label="用户名" prop="username" width="80" align="center"></el-table-column>
        <el-table-column label="性别" width="50" align="center">
          <template v-slot="scope">
            <div>{{ changeSex(scope.row.sex) }}</div>
          </template>
        </el-table-column>
        <el-table-column label="手机号码" prop="phoneNum" width="120" align="center"></el-table-column>
        <el-table-column label="邮箱" prop="email" width="120" align="center"></el-table-column>
        <el-table-column label="生日" width="120" align="center">
          <template v-slot="scope">
            <div>{{ getBirth(scope.row.birth) }}</div>
          </template>
        </el-table-column>
        <el-table-column label="签名" prop="introduction"></el-table-column>
        <el-table-column label="地区" prop="location" width="70" align="center"></el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template v-slot="scope">
            <el-button type="danger" @click="deleteRow(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination class="pagination" background layout="total, prev, pager, next" :current-page="currentPage"
        :page-size="pageSize" :total="tableData.length" @current-change="handleCurrentChange">
      </el-pagination>
    </div>

    <!-- 删除提示框 -->
    <yin-del-dialog :delVisible="delVisible" @confirm="confirm" @cancelRow="delVisible = $event"></yin-del-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import { attachImageUrl, getAllUser, deleteUser } from "@/api";
import YinDelDialog from "@/components/dialog/YinDelDialog.vue";
import { getBirth } from "@/utils";

/** 用户列表行（与 mixin 中 changeSex 约定一致） */
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

function changeSex(value: number | string | undefined) {
  if (value === 0) return "女";
  if (value === 1) return "男";
  if (value === 2) return "组合";
  if (value === 3) return "不明";
  if (value === "男" || value === "女") return value;
}

const tableData = ref<ConsumerRow[]>([]);
const tempDate = ref<ConsumerRow[]>([]);
const pageSize = ref(5);
const currentPage = ref(1);

const data = computed(() =>
  tableData.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value),
);

const searchWord = ref("");
watch(searchWord, () => {
  if (searchWord.value === "") {
    tableData.value = [...tempDate.value];
  } else {
    tableData.value = tempDate.value.filter((item) => item.username.includes(searchWord.value));
  }
});

async function getData() {
  const result = await getAllUser();
  const rows = Array.isArray(result.data) ? (result.data as ConsumerRow[]) : [];
  tableData.value = rows;
  tempDate.value = [...rows];
  currentPage.value = 1;
}

void getData();

function handleCurrentChange(val: number) {
  currentPage.value = val;
}

const idx = ref(-1);
const multipleSelection = ref<ConsumerRow[]>([]);
const delVisible = ref(false);

async function confirm() {
  const result = await deleteUser(idx.value);
  ElMessage({
    message: result.message,
    type: result.type as any,
  });
  if (result.success) await getData();
  delVisible.value = false;
}

function deleteRow(id: number) {
  idx.value = id;
  delVisible.value = true;
}

function handleSelectionChange(val: ConsumerRow[]) {
  multipleSelection.value = val;
}

function deleteAll() {
  for (const item of multipleSelection.value) {
    deleteRow(item.id);
    void confirm();
  }
  multipleSelection.value = [];
}
</script>

<style scoped></style>
