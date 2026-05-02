<template>
  <header class="yin-header">
    <div class="header-left">
      <svg class="header-logo" aria-hidden="true">
        <use xlink:href="#icon-erji"></use>
      </svg>
      <div class="header-title">{{ musicName }}</div>
      <el-input
        class="header-search"
        v-model="keywords"
        placeholder="搜索歌曲、歌手、歌单"
        @keyup.enter="goSearch"
        :prefix-icon="Search"
      />
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
  </header>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import { ElMessage } from "element-plus";
import { Search } from "@element-plus/icons-vue";
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
// 左侧导航栏
const headerNavList = [
  {
    name: "首页",
    path: "/",
  },
  {
    name: "歌单",
    path: "/song-sheet",
  },
  {
    name: "歌手",
    path: "/singer",
  },
];
// 右侧导航栏
const signList = [
  {
    name: "登录",
    path: "/sign-in",
  },
];
// 用户下拉菜单项
const menuList = [
  {
    name: "个人主页",
    path: "/personal",
  },
  {
    name: "设置",
    path: "/setting",
  },
  {
    name: "退出",
    path: "0",
  },
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

  .yin-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 4px;
    padding: 0 6px;
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

.yin-header {
  position: fixed;
  width: 100%;
  height: 56px;
  line-height: 56px;
  padding: $header-padding;
  margin: $header-margin;
  background-color: $theme-header-color;
  box-shadow: $box-shadow;
  box-sizing: border-box;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
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

/* LOGO */
.header-logo {
  font-size: $font-size-logo;
  font-weight: bold;

  @include icon(1.9rem, $color-black);
  vertical-align: middle;
}

.header-title {
  font-size: $font-size-logo;
  font-weight: bold;
  flex-shrink: 0;
}

.header-search {
  width: min(220px, 100%);
  flex: 0 1 220px;
  min-width: 0;
}

.header-menu {
  flex: 1;
  border-bottom: none;
  min-width: 0;
  height: 56px;
  overflow-x: auto;
  overflow-y: hidden;
}

.header-menu::-webkit-scrollbar {
  display: none;
}

.yin-sign-menu {
  border-bottom: none;
  height: 56px;
}

:deep(.header-menu .el-menu-item),
:deep(.yin-sign-menu .el-menu-item) {
  background-color: transparent !important;
  height: 56px;
  line-height: 56px;
}

/*用户*/
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
</style>
