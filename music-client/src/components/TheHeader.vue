<template>
  <div class="the-header">
    <!--图标-->
    <div class="head-logo" @click="goHome">
      <div class="logo-hd">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-erji"></use>
        </svg>
      </div>
      <span>{{musicName}}</span>
    </div>
    <ul class="navbar" ref="change">
      <li :class="{active: item.name === activeName}" v-for="item in navMsg" :key="item.path" @click="goPage(item.path, item.name)">
        {{item.name}}
      </li>
      <li>
        <div class="head-search">
          <input type="text" placeholder="搜索音乐" @keyup.enter="goSearch()" v-model="keywords">
          <div class="search-btn"  @click="goSearch()" >
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-sousuo"></use>
            </svg>
          </div>
        </div>
      </li>
      <li v-if="!loginIn" :class="{active: item.name === activeName}" v-for="item in loginMsg" :key="item.type" @click="goPage(item.path, item.name)">{{item.name}}</li>
    </ul>
    <!--用户-->
    <ul class="menu">
      <li v-for="(item, index) in menuList" :key="index" @click="goMenuList(item.path)">{{item.name}}</li>
    </ul>
    <div id="user" @click="show()" v-if="loginIn">
      <img :src=attachImageUrl(avator) alt="">
    </div>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import { navMsg, loginMsg, menuList } from '../assets/data/header'

export default {
  name: 'the-header',
  mixins: [mixin],
  data () {
    return {
      musicName: 'Yin-music',
      navMsg: [], // 左侧导航栏
      loginMsg: [], // 右侧导航栏
      menuList: [], // 用户下拉菜单项
      keywords: ''
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'activeName',
      'avator',
      'username',
      'loginIn'
    ])
  },
  created () {
    this.navMsg = navMsg
    this.loginMsg = loginMsg
    this.menuList = menuList
  },
  methods: {
    show () {
      document.querySelector('#user').addEventListener('click', function (e) {
        document.querySelector('.menu').classList.add('show')
        e.stopPropagation()// 关键在于阻止冒泡
      }, false)
      // 点击“菜单”内部时，阻止事件冒泡。(这样点击内部时，菜单不会关闭)
      document.querySelector('.menu').addEventListener('click', function (e) {
        e.stopPropagation()
      }, false)
      document.addEventListener('click', function () {
        document.querySelector('.menu').classList.remove('show')
      }, false)
    },
    goHome () {
      this.$router.push({path: '/home'})
    },
    goPage (path, value) {
      document.querySelector('.menu').classList.remove('show')
      this.changeIndex(value)
      if (!this.loginIn && path === '/my-music') {
        this.notify('请先登录', 'warning')
      } else {
        this.$router.push({path: path})
      }
    },
    changeIndex (value) {
      this.$store.commit('setActiveName', value)
    },
    goMenuList (path) {
      document.querySelector('.menu').classList.remove('show')
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

<style scoped>
.the-header {
  background-color: #fefefe;
  width: 100%;
  height: 80px;
  position: relative;
  box-shadow: 0 0 10px black;
}

.head-logo {
  width: 150px;
  line-height: 80px;
  font-size: 28px;
  font-weight: bold;
  margin-left: 90px;
  display: inline-block;
  cursor: pointer;
}

.logo-hd{
  position: absolute;
  top:15px;
  left: 30px;
}

.logo-hd .icon {
  width: 50px;
  height: 50px;
  fill: currentColor;
  color: black;
}

/*nav*/
.navbar {
  box-sizing: border-box;
  height: 80px;
}

.navbar li {
  margin: 0 10px;
  padding: 0 10px;
  height: 48px;
  font-size: 20px;
  text-align: center;
  color: #67757f;
  border-bottom: none;
  cursor: pointer;
}

/*搜索*/
.head-search {
  display: flex;
  border-radius: 5px;
  overflow: hidden;
}

.head-search input {
  height: 30px;
  width: 270px;
  font-size: 16px;
  border: 0;
  text-indent: 10px;
  background-color: #ebeef0;
}

input:focus {
  outline: none;
}

.search-btn{
  background-color: #26a2ff;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 30px;
  cursor: pointer;
}

.icon{
  width: 0.8em;
  height: 0.8em;
  fill: currentColor;
  color: white;
  font-size: 1.5em;
}

/*用户*/
#user {
  border-radius: 50%;
  width: 50px;
  height: 50px;
  overflow: hidden;
  margin: 15px;
  position: absolute;
  right: 150px;
  display: inline-block;
}

#user:hover {
  cursor: pointer;
}

#user img {
  width: 100%;
}

.menu {
  display: none;
  background-color: #fff;
  width: 150px;
  position: absolute;
  right: 140px;
  top: 90px;
  box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.4);
  cursor: pointer;
  z-index: 100;
  text-align: center;
}

.menu li:hover{
  background-color: #95d2f6;
  color: white;
}

.menu :nth-child(1):before {
  content: " ";
  display: block;/*独占一行*/
  position: absolute;
  right: 45px;
  top: -5px; /*果断的露出上半部分*/
  width: 10px;
  height: 10px;
  background-color: #ffffff;
  transform: rotate(45deg); /*一个正方形倾斜四十五度就是三角了但是要把下半部分藏起来*/
}

.menu :nth-child(1):hover:before{
  background-color: #95d2f6;
}

.menu li {
  display: inline-block;
  width: 100%;
  height: 40px;
  line-height: 40px;
}

.show {
  display: block;
}

.active{
  color: #93d2f8 !important;
  border-bottom: 6px solid #93d2f8 !important;
}
</style>
