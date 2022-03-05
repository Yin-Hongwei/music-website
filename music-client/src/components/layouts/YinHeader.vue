<template>
  <div class="yin-header">
    <!--图标-->
    <div class="header-logo" @click="goPage()">
      <yin-icon :icon="iconList.ERJI"></yin-icon>
      <span>{{ musicName }}</span>
    </div>
    <ul class="navbar">
      <li :class="{active: item.name === activeNavName}" v-for="item in headerNavList" :key="item.path" @click="goPage(item.path, item.name)">
        {{ item.name }}
      </li>
      <!--搜索框-->
      <li class="header-search"> 
        <input type="text" placeholder="搜索音乐" @keyup.enter="goSearch()" v-model="keywords">
      </li>
    </ul>
    <!--设置-->
    <ul class="header-right sign" v-if="!token">
      <li :class="{ active: item.name === activeNavName }" v-for="item in signList" :key="item.type" @click="goPage(item.path, item.name)">
        {{ item.name }}
      </li>
    </ul>
    <el-dropdown class="header-right" v-if="token" trigger="click">
      <div class="user">
        <img :src="attachImageUrl(userPic)" alt="">
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item v-for="(item, index) in menuList" :key="index" @click.stop="goMenuList(item.path)">{{ item.name }}</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  getCurrentInstance,
  computed,
  reactive,
} from "vue";
import { useStore } from "vuex";
import YinIcon from "./YinIcon.vue"
import mixin from "@/mixins/mixin"
import {
  HEADERNAVLIST,
  SIGNLIST,
  MENULIST,
  ICON,
  MUSICNAME,
  HOME,
  SEARCH,
  SIGN_OUT,
  NAV_NAME,
} from "@/enums";

export default defineComponent({
  components: {
    YinIcon
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { changeIndex, routerManager, attachImageUrl } = mixin();

    const musicName = ref(MUSICNAME)
    const headerNavList = ref(HEADERNAVLIST); // 左侧导航栏
    const signList = ref(SIGNLIST); // 右侧导航栏
    const menuList = ref(MENULIST); // 用户下拉菜单项
    const iconList = reactive({
      ERJI: ICON.ERJI
    });
    const keywords = ref("");
    const activeNavName = computed(() => store.getters.activeNavName);
    const userPic = computed(() => store.getters.userPic);
    const token = computed(() => store.getters.token);

    function goPage (path, name) {
      if (!path && !name) {
        changeIndex(NAV_NAME.HOME)
        routerManager(HOME, { path: HOME })
      } else {
        changeIndex(name)
        routerManager(path, { path })
      }
    }

    function goMenuList (path) {
      if (path === 0) proxy.$store.commit("setIsCollection", false)

      if (path) {
        routerManager(path, { path })
      } else {
        proxy.$store.commit("setToken", false)
        routerManager(SIGN_OUT, { path: SIGN_OUT })
      }
    }
    function goSearch () {
      if (keywords.value !== "") {
        proxy.$store.commit("setSearchWord", keywords.value)
        routerManager(SEARCH, { path: SEARCH, query: { keywords: keywords.value } })
      } else {
        (proxy as any).$notify({
          title: "搜索内容不能为空",
          type: "error"
        })
      }
    }

    return {
      musicName,
      headerNavList,
      signList,
      menuList,
      iconList,
      keywords,
      activeNavName,
      userPic,
      token,
      goPage,
      goMenuList,
      goSearch,
      attachImageUrl,
    }
  },
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";
@media screen and (min-width: $sm) {
  .header-logo {
    margin: 0 1rem;
    // .icon {
      // @include icon(($header-height / 3) * 2, $color-black);
    // }
  }
}

@media screen and (max-width: $sm) {
  .header-logo {
    margin: 0 1rem;
    span {
      display: none;
    }
  }
  .header-search {
    display: none;
  }
}
/*头部*/
.yin-header {
  position: fixed;
  // @include layout;
  width: 100%;
  height: $header-height;
  line-height: $header-height;
  padding: $header-padding;
  margin: $header-margin;
  background-color: $theme-header-color;
  @include box-shadow($box-shadow);
  box-sizing: border-box;
  z-index: 100;
  display: flex;
  white-space: nowrap;
  flex-wrap: nowrap;
}

/*左侧LOGO*/
.header-logo {
  font-size: $font-size-logo;
  font-weight: bold;
  cursor: pointer;
  .icon {
    @include icon(1.9rem, $color-black);
    vertical-align: middle;
  }
  span {
    margin-left: 1rem;
  }
}

/*navbar*/
.navbar {
  display: flex;
  white-space: nowrap;
  flex: 1;
  /*搜索输入框*/
  .header-search {
    width: 100%;
    input {
      height: $header-search-height;
      min-width: $header-search-width;
      border-radius: $header-search-radius;
      border: 0;
      font-size: $font-size-default;
      text-indent: 10px;
      background-color: $color-light-grey;
      &:focus {
        outline: none;
      }
    }
  }
}

.navbar,
.header-right.sign {
  li {
    margin: $header-nav-margin;
    padding: $header-nav-padding;
    font-size: $font-size-header;
    color: $color-grey;
    cursor: pointer;
  }
}
/*用户*/
.header-right {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  justify-content: flex-start;

  .user {
    overflow: hidden;
    width: $header-user-width;
    height: $header-user-width;
    border-radius: $header-user-radius;
    margin-right: $header-user-margin;
    cursor: pointer;

    img {
      width: 100%;
    }
  }
  .setting {
    font-size: 30px;
    cursor: pointer;
  }
}

.show {
  display: block;
}

.active {
  color: $theme-color !important;
  border-bottom: 5px solid $theme-color !important;
}

</style>
