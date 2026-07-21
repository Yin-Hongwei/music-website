<template>
  <div class="song-list">
    <el-table
      v-if="dataList.length"
      highlight-current-row
      :data="dataList"
      :show-header="true"
      @row-click="handleClick"
    >
      <el-table-column label="#" width="60" align="center">
        <template #default="scope">
          <span class="row-index">{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="歌曲 & 歌手" min-width="380">
        <template #default="scope">
          <div class="song-info">
            <el-image
              class="song-cover"
              fit="cover"
              :src="attachImageUrl(scope.row.pic)"
            />
            <div class="song-meta">
              <p class="song-name">{{ scope.row.songName || "--" }}</p>
              <p class="singer-name">{{ scope.row.singerName || "--" }}</p>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="专辑" min-width="220">
        <template #default="scope">
          <span class="album-name">{{ scope.row.introduction || "--" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="80" align="center">
        <template #default="scope">
          <div @click.stop>
            <el-dropdown trigger="click">
              <el-button class="more-btn" text>
                <Ellipsis class="menu-trigger-icon" />
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    @click="
                      downloadMusic({
                        songUrl: scope.row.url,
                        songName: [scope.row.singerName, scope.row.songName]
                          .filter(Boolean)
                          .join(' - '),
                      })
                    "
                  >
                    <Download class="dropdown-item-icon" />
                    <span>下载</span>
                  </el-dropdown-item>
                  <el-dropdown-item v-if="show" @click="deleteCollection({ id: scope.row.id })">
                    <Trash2 class="dropdown-item-icon" />
                    <span>删除</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-else description="暂无歌曲数据" />
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { ElMessage } from "element-plus";
import { Download, Ellipsis, Trash2 } from "lucide-vue-next";

import { useUserStore } from "../../store/user";
import { fetchDeleteCollection } from "../../api/user";
import { elMessageTypeFromResponse } from "@/api/types";
import { useAppActions } from "../../composables/useAppActions";
import { attachImageUrl } from "../../utils";

type SongListItem = any;

const props = withDefaults(
  defineProps<{
    songList?: SongListItem[];
    show?: boolean;
  }>(),
  {
    songList: () => [],
    show: false,
  },
);

const emit = defineEmits<{
  (e: "changeData", payload: any): void;
}>();

const { playMusic, checkStatus, downloadMusic } = useAppActions();
const userStore = useUserStore();

const show = computed(() => props.show);

const dataList = computed(() => {
  const list: any[] = [];
  if (!Array.isArray(props.songList)) return list;

  props.songList.forEach((item: SongListItem, index: number) => {
    list.push({
      ...item,
      songName: item.songName || item.name || "",
      singerName: item.singerName || "",
      index,
    });
  });
  return list;
});

function handleClick(row: any) {
  playMusic({
    id: row.id,
    url: row.url,
    pic: row.pic,
    index: row.index,
    name: row.songName || row.name,
    singerName: row.singerName,
    lyric: row.lyric,
    currentSongList: props.songList,
    duration: row.duration,
  });
}

const userId = computed(() => userStore.userId);

async function deleteCollection({ id }: { id: any }) {
  if (!checkStatus()) return;

  const result = await fetchDeleteCollection(userId.value, id);
  ElMessage({
    message: result.message,
    type: elMessageTypeFromResponse(result),
  });

  if (result.data === false) emit("changeData", result.data);
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.song-list {
  background-color: var(--surface);
  border-radius: 12px;
  padding: 4px 8px;
}

.song-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.song-cover {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  flex-shrink: 0;
  overflow: hidden;
  background: var(--surface-tertiary);
}

.song-meta {
  display: flex;
  flex-direction: column;
  line-height: 1.3;
  min-width: 0;
}

.song-name {
  margin: 0;
  color: var(--foreground);
  font-size: 0.9375rem;
  font-weight: 600;
  line-height: 1.3;
}

.singer-name {
  margin: 4px 0 0;
  color: var(--muted);
  font-size: 0.8125rem;
}

.album-name {
  color: var(--foreground);
  font-size: 0.875rem;
}

.row-index {
  color: var(--muted);
  font-size: 0.8125rem;
}

.more-btn {
  color: var(--muted);
  font-size: 16px;
}

.menu-trigger-icon {
  width: 16px;
  height: 16px;
}

.dropdown-item-icon {
  width: 14px;
  height: 14px;
  margin-right: 6px;
}

.song-list:deep(.el-table) {
  --el-table-bg-color: transparent;
  --el-table-tr-bg-color: transparent;
  --el-table-header-bg-color: transparent;
  --el-table-row-hover-bg-color: rgba(0, 0, 0, 0.03);
  background: transparent;
}

.song-list:deep(.el-table__inner-wrapper::before) {
  display: none;
}

.song-list:deep(.el-table th.el-table__cell) {
  background: transparent;
  color: var(--muted);
  font-size: 0.8125rem;
  font-weight: 600;
  border-bottom: 1px solid var(--border);
}

.song-list:deep(.el-table td.el-table__cell) {
  border-bottom: 1px solid var(--border);
}

.song-list:deep(.el-table__row) {
  cursor: pointer;
  height: 72px;
}

.song-list:deep(.el-table__row.current-row) {
  color: var(--accent);
  font-weight: 600;
  background: rgba(48, 164, 252, 0.06) !important;
}
</style>
