<template>
  <header class="yin-header glass-chrome">
    <div class="yin-header__inner">
      <div class="header-left">
        <svg class="header-logo" aria-hidden="true" @click="goHome">
          <use xlink:href="#icon-erji"></use>
        </svg>
        <div class="header-title" @click="goHome">{{ musicName }}</div>
        <el-menu
          class="header-menu"
          mode="horizontal"
          :ellipsis="false"
          :default-active="activeNavName"
          @select="handleHeaderSelect"
        >
          <el-menu-item
            v-for="item in headerNavList"
            :key="item.path"
            :index="item.name"
          >
            {{ item.name }}
          </el-menu-item>
        </el-menu>
        <el-input
          class="header-search"
          v-model="keywords"
          placeholder="搜索歌曲、歌手、歌单"
          @keyup.enter="goSearch"
        >
          <template #prefix>
            <Search class="header-search-icon" />
          </template>
        </el-input>
      </div>

      <div class="header-right">
        <div class="header-extra">
          <el-menu
            v-if="!token"
            class="yin-sign-menu"
            mode="horizontal"
            :ellipsis="false"
            :default-active="activeNavName"
            @select="handleSignSelect"
          >
            <el-menu-item
              v-for="item in signList"
              :key="item.path"
              :index="item.name"
            >
              {{ item.name }}
            </el-menu-item>
          </el-menu>

          <el-dropdown v-else class="user-wrap" trigger="click">
            <el-image class="user" fit="contain" :src="attachImageUrl(userPic)" />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  v-for="(item, index) in menuList"
                  :key="index"
                  @click.stop="goMenuList(item.path)"
                >
                  {{ item.name }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import { ElMessage } from "element-plus";
import { Search } from "lucide-vue-next";
import { useRouter } from "vue-router";

import { useConfigureStore } from "../../store/configure";
import { useSearchStore } from "../../store/search";
import { useUserStore } from "../../store/user";
import { attachImageUrl } from "../../utils";

const router = useRouter();
const userStore = useUserStore();
const configureStore = useConfigureStore();
const searchStore = useSearchStore();

const musicName = "Yin-music";
const headerNavList = [
  { name: "首页", path: "/" },
  { name: "歌单", path: "/song-sheet" },
  { name: "歌手", path: "/singer" },
];
const signList = [{ name: "登录", path: "/sign-in" }];
const menuList = [
  { name: "个人主页", path: "/personal" },
  { name: "设置", path: "/setting" },
  { name: "退出", path: "0" },
];

const keywords = ref("");
const activeNavName = computed(() => configureStore.activeNavName);
const userPic = computed(() => userStore.userPic);
const token = computed(() => configureStore.token);

const HOME_NAME = "首页";
const HOME_PATH = "/";
const SIGN_OUT = "0";

function goPage(toPath: string, activeName: string) {
  configureStore.setActiveNavName(activeName);
  router.push({ path: toPath });
}

function goHome() {
  goPage(HOME_PATH, HOME_NAME);
}

function goMenuList(path: string) {
  if (path === SIGN_OUT) {
    configureStore.setToken(false);
    userStore.setUserId("");
    userStore.setUsername("");
    userStore.setUserPic("");
    goPage(HOME_PATH, HOME_NAME);
  } else {
    router.push({ path });
  }
}

function goSearch() {
  if (keywords.value !== "") {
    searchStore.setKeyword(keywords.value);
    router.push({
      path: "/search",
      query: { keywords: keywords.value },
    });
  } else {
    ElMessage({
      message: "搜索内容不能为空",
      type: "error",
    });
  }
}

function handleHeaderSelect(selectedName: string) {
  const target = headerNavList.find((item) => item.name === selectedName);
  if (target) {
    goPage(target.path, target.name);
  }
}

function handleSignSelect(selectedName: string) {
  const target = signList.find((item) => item.name === selectedName);
  if (target) {
    goPage(target.path, target.name);
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.glass-chrome {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 100;
  width: 100%;
  border-bottom: 1px solid var(--border);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
}

.yin-header__inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1.5rem;
  height: var(--header-height);
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding: 0 28px;
  box-sizing: border-box;
  white-space: nowrap;
}

.header-left {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
  gap: 0.75rem;
}

.header-right {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  min-width: 0;
}

.header-extra {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-logo {
  font-size: $font-size-logo;
  font-weight: bold;
  cursor: pointer;
  @include icon(1.9rem, var(--foreground));
  vertical-align: middle;
}

.header-title {
  font-size: $font-size-logo;
  font-weight: 600;
  flex-shrink: 0;
  cursor: pointer;
  color: var(--foreground);
  letter-spacing: -0.02em;
}

.header-search {
  width: min(220px, 100%);
  flex: 0 1 220px;
  min-width: 0;

  :deep(.el-input__wrapper) {
    border-radius: 999px;
    background: var(--surface-secondary);
    box-shadow: none;
  }
}

.header-search-icon {
  width: 16px;
  height: 16px;
  color: var(--muted);
}

.header-menu {
  flex: 1;
  border-bottom: none !important;
  min-width: 0;
  height: var(--header-height);
  background: transparent !important;
  overflow-x: auto;
  overflow-y: hidden;
}

.header-menu::-webkit-scrollbar {
  display: none;
}

.yin-sign-menu {
  border-bottom: none !important;
  height: var(--header-height);
  background: transparent !important;
}

:deep(.header-menu .el-menu-item),
:deep(.yin-sign-menu .el-menu-item) {
  background-color: transparent !important;
  height: var(--header-height);
  line-height: var(--header-height);
  border-bottom: none !important;
  color: var(--muted);
  font-weight: 500;

  &.is-active {
    color: var(--foreground) !important;
    border-bottom: none !important;
  }

  &:hover {
    color: var(--foreground) !important;
  }
}

.user-wrap {
  position: relative;
  display: flex;
  align-items: center;

  .user {
    width: $header-user-width;
    height: $header-user-width;
    border-radius: $header-user-radius;
    margin-right: 0;
    cursor: pointer;
  }
}

@media screen and (max-width: $sm) {
  .header-logo {
    display: none;
  }

  .header-title {
    margin-left: 0;
    font-size: 12px !important;
    max-width: 76px;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .header-search {
    display: none;
  }

  .yin-header__inner {
    gap: 4px;
    padding: 0 12px;
  }

  .header-left {
    flex: 1;
    min-width: 0;
    overflow: hidden;
  }

  .header-menu {
    flex: 1;
    min-width: 0;
  }

  :deep(.header-menu .el-menu-item),
  :deep(.yin-sign-menu .el-menu-item) {
    padding: 0 7px;
    font-size: 14px;
  }

  .header-extra {
    gap: 6px;
    flex: 0 0 auto;
    position: relative;
    z-index: 3;
  }

  .header-right {
    flex: 0 0 auto;
  }

  .yin-sign-menu {
    min-width: 0;
  }

  .user-wrap .user {
    width: 28px;
    height: 28px;
  }
}
</style>
