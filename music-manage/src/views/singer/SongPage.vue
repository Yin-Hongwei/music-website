<template>
  <el-breadcrumb class="crumbs" separator="/">
    <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.name" :to="{ path: item.path, query: item.query }">
      {{ item.name }}
    </el-breadcrumb-item>
  </el-breadcrumb>

  <div class="container">
    <div class="handle-box">
      <el-button @click="deleteAll">批量删除</el-button>
      <el-input v-model="searchWord" placeholder="筛选关键词"></el-input>
      <el-button type="primary" @click="centerDialogVisible = true">添加歌曲</el-button>
    </div>
    <el-table height="550px" border size="small" :data="data" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column label="ID" prop="id" width="50" align="center"></el-table-column>
      <el-table-column label="歌手图片" width="110" align="center">
        <template v-slot="scope">
          <div class="cover-with-play">
            <div class="cover-img-wrap">
              <img :src="attachImageUrl(scope.row.pic)" style="width: 100%" />
            </div>
            <div class="play" @click.stop="playPreview(scope.row)">
              <el-icon class="play-icon" :size="28">
                <VideoPause v-if="previewRow && previewRow.id === scope.row.id && previewPlaying" />
                <VideoPlay v-else />
              </el-icon>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="歌名" prop="name" width="150"></el-table-column>
      <el-table-column label="专辑" prop="introduction" width="150"></el-table-column>
      <el-table-column label="歌词" align="center">
        <template v-slot="scope">
          <ul style="height: 100px; overflow: scroll">
            <li v-for="(item, index) in parseLyric(scope.row.lyric)" :key="index">
              {{ item }}
            </li>
          </ul>
        </template>
      </el-table-column>
      <el-table-column label="时长" prop="duration" width="90" align="center">
        <template v-slot="scope">
          {{ formatDuration(scope.row.duration) }}
        </template>
      </el-table-column>
      <el-table-column label="资源更新" width="120" align="center">
        <template v-slot="scope">
          <el-upload :action="updateSongImg(scope.row.id)" :show-file-list="false" :on-success="handleImgSuccess" :before-upload="beforeImgUpload">
            <el-button>更新图片</el-button>
          </el-upload>
          <br />
          <el-upload :action="updateSongUrl(scope.row.id)" :show-file-list="false" :on-success="handleSongSuccess" :before-upload="beforeSongUpload">
            <el-button>更新歌曲</el-button>
          </el-upload>
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

    <div v-if="previewRow" class="mini-player-bar">
      <span class="mini-player-name" :title="previewRow.name">{{ previewRow.name }}</span>
      <audio
        ref="previewAudioRef"
        class="mini-player-audio"
        :src="previewSrc"
        controls
        controlsList="nodownload noplaybackrate"
        @play="previewPlaying = true"
        @pause="previewPlaying = false"
        @ended="previewPlaying = false"
      />
      <el-button size="small" @click="closePreview">关闭</el-button>
    </div>
  </div>

  <!--添加歌曲-->
  <el-dialog title="添加歌曲" v-model="centerDialogVisible">
    <el-form id="add-song" label-width="120px" :model="registerForm">
      <el-form-item label="歌曲名">
        <el-input type="text" name="name" v-model="registerForm.name"></el-input>
      </el-form-item>
      <el-form-item label="专辑">
        <el-input type="text" name="introduction" v-model="registerForm.introduction"></el-input>
      </el-form-item>
      <el-form-item label="歌词">
        <el-input type="textarea" name="lyric" v-model="registerForm.lyric"></el-input>
      </el-form-item>
      <el-form-item label="歌曲上传">
        <input type="file" name="file" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addSong">确 定</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 编辑弹出框 -->
  <el-dialog title="编辑" v-model="editVisible">
    <el-form :model="editForm">
      <el-form-item label="歌曲">
        <el-input v-model="editForm.name"></el-input>
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="editForm.introduction"></el-input>
      </el-form-item>
      <el-form-item label="歌词">
        <el-input type="textarea" v-model="editForm.lyric"></el-input>
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
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, watch, ref, reactive, computed, nextTick } from "vue";
import { useAppStore } from "@/stores/app";
import { VideoPlay, VideoPause } from "@element-plus/icons-vue";
import mixin from "@/mixins/mixin";
import { RouterName } from "@/enums";
import {
  attachImageUrl,
  getSongImgUpdateUploadUrl,
  getSongUrlUpdateUploadUrl,
  getSongOfSingerId,
  updateSongMsg,
  deleteSong,
} from "@/api";
import { parseLyric } from "@/utils";
import YinDelDialog from "@/components/dialog/YinDelDialog.vue";

type SongRow = {
  id: number | string;
  name: string;
  url?: string;
};

export default defineComponent({
  components: {
    YinDelDialog,
    VideoPlay,
    VideoPause,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const { routerManager, beforeImgUpload, beforeSongUpload } = mixin();
    const appStore = useAppStore();

    const tableData = ref([]); // 记录歌曲，用于显示
    const tempDate = ref([]); // 记录歌曲，用于搜索时能临时记录一份歌曲列表
    const pageSize = ref(5); // 页数
    const currentPage = ref(1); // 当前页
    const singerId = ref("");
    const singerName = ref("");
    const breadcrumbList = computed(() => appStore.breadcrumbList);

    const previewRow = ref<SongRow | null>(null);
    const previewPlaying = ref(false);
    const previewAudioRef = ref<HTMLAudioElement | null>(null);
    const previewSrc = computed(() => {
      const row = previewRow.value;
      return row && row.url ? attachImageUrl(row.url) : "";
    });
    // 计算当前表格中的数据
    const data = computed(() => {
      return tableData.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value);
    });

    const searchWord = ref(""); // 记录输入框输入的内容
    watch(searchWord, () => {
      if (searchWord.value === "") {
        tableData.value = tempDate.value;
      } else {
        tableData.value = [];
        for (let item of tempDate.value) {
          if (item.name.includes(searchWord.value)) {
            tableData.value.push(item);
          }
        }
      }
    });

    singerId.value = proxy.$route.query.id as string;
    singerName.value = proxy.$route.query.name as string;
    appStore.setIsPlay(false);
    getData();

    // 获取歌曲
    async function getData() {
      tableData.value = [];
      tempDate.value = [];
      const result = (await getSongOfSingerId(singerId.value)) as ResponseBody;
      tableData.value = result.data;
      tempDate.value = result.data;
      currentPage.value = 1;
    }
    async function playPreview(row: SongRow) {
      const rawUrl = typeof row.url === "string" ? row.url.trim() : "";
      if (!rawUrl) {
        (proxy as any).$message({ message: "暂无音频文件", type: "warning" });
        return;
      }
      const isSame = previewRow.value != null && previewRow.value.id === row.id;
      previewRow.value = row;
      await nextTick();
      const el = previewAudioRef.value;
      if (!el) return;
      if (isSame && !el.paused) {
        el.pause();
        return;
      }
      if (isSame && el.paused) {
        void el.play();
        return;
      }
      el.load();
      void el.play().catch(() => {
        /* autoplay / decode blocked until user gesture */
      });
    }

    function closePreview() {
      const a = previewAudioRef.value;
      if (a) a.pause();
      previewRow.value = null;
      previewPlaying.value = false;
    }

    // 更新歌曲图片
    function updateSongImg(id) {
      return getSongImgUpdateUploadUrl(id);
    }
    function updateSongUrl(id) {
      return getSongUrlUpdateUploadUrl(id);
    }
    // 获取当前页
    function handleCurrentChange(val) {
      currentPage.value = val;
    }
    function handleSongSuccess(res) {
      (proxy as any).$message({
        message: res.message,
        type: res.type,
      });
      if (res.success) getData();
    }
    // 更新图片
    function handleImgSuccess(res, file) {
      (proxy as any).$message({
        message: res.message,
        type: res.type,
      });
      if (res.success) getData();
    }

    /**
     * 路由
     */
    function goCommentPage(id) {
      const breadcrumbList = reactive([
        {
          path: RouterName.Singer,
          name: "歌手管理",
        },
        {
          path: RouterName.Song,
          query: {
            id: singerId.value,
            name: singerName.value,
          },
          name: "歌曲信息",
        },
        {
          path: "",
          name: "评论详情",
        },
      ]);
      appStore.setBreadcrumbList(breadcrumbList);
      routerManager(RouterName.Comment, { path: RouterName.Comment, query: { id, type: 0 } });
    }

    /**
     * 添加
     */
    const centerDialogVisible = ref(false);
    const registerForm = reactive({
      name: "",
      singerName: "",
      introduction: "",
      lyric: "",
    });

    async function addSong() {
      const addSongForm = new FormData(document.getElementById("add-song") as HTMLFormElement);
      addSongForm.append("singerId", singerId.value);
      addSongForm.set("name", singerName.value + "-" + addSongForm.get("name"));
      if (!addSongForm.get("lyric")) addSongForm.set("lyric", "[00:00:00]暂无歌词");
      const songFile = addSongForm.get("file");
      if (songFile instanceof File) {
        const durationSeconds = await parseAudioDuration(songFile);
        addSongForm.set("duration", String(durationSeconds));
      }

      const req = new XMLHttpRequest();
      req.onreadystatechange = () => {
        if (req.readyState === 4 && req.status === 200) {
          let res = JSON.parse(req.response);
          (proxy as any).$message({
            message: res.message,
            type: res.type,
          });
          if (res.success) {
            getData();
            registerForm.name = "";
            registerForm.singerName = "";
            registerForm.introduction = "";
            registerForm.lyric = "";
          }
        }
      };
      req.open("post", attachImageUrl(`/song/add`), false);
      req.send(addSongForm);
      centerDialogVisible.value = false;
    }

    function parseAudioDuration(file: File): Promise<number> {
      return new Promise(resolve => {
        const audio = document.createElement("audio");
        const objectUrl = URL.createObjectURL(file);
        audio.preload = "metadata";
        const cleanup = () => {
          URL.revokeObjectURL(objectUrl);
          audio.src = "";
        };
        audio.onloadedmetadata = () => {
          const duration = Number.isFinite(audio.duration) ? Math.round(audio.duration) : 0;
          cleanup();
          resolve(duration > 0 ? duration : 0);
        };
        audio.onerror = () => {
          cleanup();
          resolve(0);
        };
        audio.src = objectUrl;
      });
    }

    function formatDuration(seconds: number) {
      const safeSeconds = Number(seconds);
      if (!Number.isFinite(safeSeconds) || safeSeconds <= 0) {
        return "00:00";
      }
      const total = Math.floor(safeSeconds);
      const min = String(Math.floor(total / 60)).padStart(2, "0");
      const sec = String(total % 60).padStart(2, "0");
      return `${min}:${sec}`;
    }

    /**
     * 编辑
     */
    const editVisible = ref(false);
    const editForm = reactive({
      id: "",
      singerId: "",
      name: "",
      introduction: "",
      createTime: "",
      updateTime: "",
      pic: "",
      lyric: "",
      url: "",
    });

    function editRow(row) {
      idx.value = row.id;
      editForm.id = row.id;
      editForm.singerId = row.singerId;
      editForm.name = row.name;
      editForm.introduction = row.introduction;
      editForm.createTime = row.createTime;
      editForm.updateTime = row.updateTime;
      editForm.pic = row.pic;
      editForm.lyric = row.lyric;
      editForm.url = row.url;
      editVisible.value = true;
    }
    async function saveEdit() {
      let id = editForm.id;
      let singerId = editForm.singerId;
      let name = editForm.name;
      let introduction = editForm.introduction;
      let lyric = editForm.lyric;
      const result = (await updateSongMsg({ id, singerId, name, introduction, lyric })) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) getData();
      editVisible.value = false;
    }

    /**
     * 删除
     */
    const idx = ref(-1); // 记录当前要删除的行
    const multipleSelection = ref([]); // 记录当前要删除的列表
    const delVisible = ref(false); // 显示删除框

    async function confirm() {
      const result = (await deleteSong(idx.value)) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) getData();
      delVisible.value = false;
    }
    function deleteRow(id) {
      idx.value = id;
      delVisible.value = true;
    }
    function handleSelectionChange(val) {
      multipleSelection.value = val;
    }
    function deleteAll() {
      for (let item of multipleSelection.value) {
        deleteRow(item.id);
        confirm();
      }
      multipleSelection.value = [];
    }

    return {
      previewRow,
      previewPlaying,
      previewAudioRef,
      previewSrc,
      playPreview,
      closePreview,
      searchWord,
      data,
      editForm,
      registerForm,
      tableData,
      centerDialogVisible,
      editVisible,
      delVisible,
      pageSize,
      currentPage,
      breadcrumbList,
      deleteAll,
      handleSelectionChange,
      handleCurrentChange,
      handleImgSuccess,
      beforeImgUpload,
      parseLyric,
      formatDuration,
      saveEdit,
      updateSongImg,
      updateSongUrl,
      deleteRow,
      confirm,
      attachImageUrl,
      addSong,
      editRow,
      handleSongSuccess,
      beforeSongUpload,
      goCommentPage,
    };
  },
});
</script>

<style scoped>
.cover-with-play {
  position: relative;
  width: 80px;
  margin: 0 auto;
}

.cover-img-wrap {
  width: 80px;
  height: 80px;
  overflow: hidden;
}

.play {
  position: absolute;
  z-index: 2;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background: rgba(0, 0, 0, 0.12);
}
.play-icon {
  color: #fff;
  filter: drop-shadow(0 0 2px rgba(0, 0, 0, 0.45));
}

.mini-player-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 16px;
  padding: 10px 14px;
  background: var(--el-fill-color-light);
  border-radius: 8px;
  border: 1px solid var(--el-border-color-lighter);
  flex-wrap: wrap;
}

.mini-player-name {
  flex: 0 1 220px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 13px;
  color: var(--el-text-color-primary);
}

.mini-player-audio {
  flex: 1 1 240px;
  min-width: 180px;
  height: 36px;
}
</style>
