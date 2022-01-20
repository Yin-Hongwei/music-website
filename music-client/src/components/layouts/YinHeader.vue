<template>
  <div class="yin-header">
    <!--图标-->
    <div class="header-logo" @click="goPage()">
      <svg class="icon" aria-hidden="true">
        <use :xlink:href="ERJI"></use>
      </svg>
      <span>{{musicName}}</span>
    </div>
    <ul class="navbar">
      <li :class="{active: item.name === activeNavName}" v-for="item in headerNavList" :key="item.path" @click="goPage(item.path, item.name)">
        {{item.name}}
      </li>
      <li>
        <div class="header-search">
          <input type="text" placeholder="搜索音乐" @keyup.enter="goSearch()" v-model="keywords">
          <div class="search-btn"  @click="goSearch()" >
            <svg class="icon" aria-hidden="true">
              <use :xlink:href="SOUSUO"></use>
            </svg>
          </div>
        </div>
      </li>
      <li v-show="!token" :class="{active: item.name === activeNavName}" v-for="item in signList" :key="item.type" @click="goPage(item.path, item.name)">{{item.name}}</li>
    </ul>
    <!--设置-->
    <div class="header-right" v-show="token">
      <div id="user" ref="user">
        <img :src="attachImageUrl(userPic)" alt="">
      </div>
      <ul class="menu" ref="menu" :class="showMenu ? 'show' : ''">
        <li v-for="(item, index) in menuList" :key="index" @click="goMenuList(item.path)">{{item.name}}</li>
      </ul>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '../../mixins'
import { HEADERNAVLIST, SIGNLIST, MENULIST, ICON, MUSICNAME, HOME, SEARCH, SIGN_OUT, MY_MUSIC, NAV_NAME } from '../../enums'

export default {
  name: 'YinHeader',
  mixins: [mixin],
  data () {
    return {
      musicName: MUSICNAME,
      headerNavList: HEADERNAVLIST, // 左侧导航栏
      signList: SIGNLIST, // 右侧导航栏
      menuList: MENULIST, // 用户下拉菜单项
      keywords: '',
      showMenu: false,
      ERJI: ICON.ERJI,
      SOUSUO: ICON.SOUSUO
    }
  },
  computed: {
    ...mapGetters([
      'activeNavName',
      'userPic',
      'token'
    ])
  },
  mounted () {
    this.$refs.user.addEventListener('click', e => {
      this.showMenu = true
      e.stopPropagation()// 关键在于阻止冒泡
    }, false)
    // 点击“菜单”内部时，阻止事件冒泡。(这样点击内部时，菜单不会关闭)
    this.$refs.menu.addEventListener('click', e => {
      e.stopPropagation()
    }, false)
    document.addEventListener('click', () => {
      this.showMenu = false
    }, false)
  },
  methods: {
    goPage (path, name) {
      if (!this.token && path === MY_MUSIC) {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      } else if (!path && !name) {
        this.changeIndex(NAV_NAME.HOME)
        this.routerManager(HOME, { path: HOME })
      } else {
        this.changeIndex(name)
        this.routerManager(path, { path })
      }
    },
    goMenuList (path) {
      this.showMenu = false

      if (path === 0) this.$store.commit('setIsCollection', false)

      if (path) {
        this.routerManager(path, { path })
      } else {
        this.$store.commit('setToken', false)
        this.routerManager(SIGN_OUT, { path: SIGN_OUT })
      }
    },
    goSearch () {
      this.$store.commit('setSearchWord', this.keywords)
      this.routerManager(SEARCH, { path: SEARCH, query: {keywords: this.keywords} })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/css/yin-header.scss';
</style>
