<template>
  <div class="yin-header">
    <!--图标-->
    <div class="header-logo" @click="goHome">
      <svg class="icon" aria-hidden="true">
        <use :xlink:href="ERJI"></use>
      </svg>
      <span>{{musicName}}</span>
    </div>
    <ul class="navbar">
      <li :class="{active: item.name === activeName}" v-for="item in navMsg" :key="item.path" @click="goPage(item.path, item.name)">
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
      <li v-show="!loginIn" :class="{active: item.name === activeName}" v-for="item in loginMsg" :key="item.type" @click="goPage(item.path, item.name)">{{item.name}}</li>
    </ul>
    <!--设置-->
    <div class="header-right" v-show="loginIn">
      <div id="user" ref="user">
        <img :src="attachImageUrl(avator)" alt="">
      </div>
      <ul class="menu" ref="menu" :class="showMenu ? 'show' : ''">
        <li v-for="(item, index) in menuList" :key="index" @click="goMenuList(item.path)">{{item.name}}</li>
      </ul>
    </div>
  </div>
</template>

<script>
import mixin from '../../mixins'
import { mapGetters } from 'vuex'
import { navMsg, loginMsg, menuList } from '../../assets/data/header'
import { ICON } from '../../assets/icon/index'
import { MUSICNAME } from '../../assets/data/contant'

export default {
  name: 'YinHeader',
  mixins: [mixin],
  data () {
    return {
      musicName: MUSICNAME,
      navMsg: navMsg, // 左侧导航栏
      loginMsg: loginMsg, // 右侧导航栏
      menuList: menuList, // 用户下拉菜单项
      keywords: '',
      showMenu: false,
      ERJI: ICON.ERJI,
      SOUSUO: ICON.SOUSUO
    }
  },
  computed: {
    ...mapGetters([
      'activeName',
      'avator',
      'loginIn'
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
    goHome () {
      this.$router.push({path: '/'})
    },
    goPage (path, value) {
      this.showMenu = false
      this.changeIndex(value)
      if (!this.loginIn && path === '/my-music') {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      } else {
        this.$router.push({path: path})
      }
    },
    changeIndex (value) {
      this.$store.commit('setActiveName', value)
    },
    goMenuList (path) {
      this.showMenu = false

      if (path === 0) this.$store.commit('setIsActive', false)

      if (path) {
        this.$router.push({path: path})
      } else {
        this.$store.commit('setLoginIn', false)
        this.$router.go(0)
      }
    },
    goSearch () {
      this.$store.commit('setSearchword', this.keywords)
      this.$router.push({path: '/search', query: {keywords: this.keywords}})
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../../assets/css/yin-header.scss';
</style>
