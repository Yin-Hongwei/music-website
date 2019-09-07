<template>
  <div class="head-nav">
    <!--图标-->
    <div class="head-logo" @click="goHomePage">
      <div class="item-logo-hd">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-erji"></use>
        </svg>
      </div>
      Yin-music
    </div>
    <ul class="navbar" ref="change">
      <li :class="{active: item.name === activeName}" v-for="item in navMsg" :key="item.type" @click="goPage(item.type, item.name)">
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
      <li :class="{active: item.name === activeName}" v-for="item in loginMsg" :key="item.type" @click="goPage(item.type, item.name)" v-if="!loginIn">{{item.name}}</li>
    </ul>
    <!--用户-->
    <ul class="menu">
      <li @click="goMySong">{{username}}</li>
      <li @click="goPersonal()">个人资料</li>
      <li @click="goUploadImg()">修改头像</li>
      <li @click="goSign()">退出</li>
    </ul>
    <div id="user" @click="show()" v-if="loginIn">
      <img :src=attachImageUrl(avator) alt="">
    </div>
  </div>
</template>

<script>
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'
export default {
  name: 'head-nav',
  data () {
    return {
      navMsg: [{
        name: '首页',
        type: 1
      }, {
        name: '歌单',
        type: 2
      }, {
        name: '歌手',
        type: 3
      }, {
        name: '我的',
        type: 4
      }],
      loginMsg: [{
        name: '登录',
        type: 5
      }, {
        name: '注册',
        type: 6
      }],
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
  mixins: [mixin],
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
    goPage (type, value) {
      this.changeIndex(value)
      if (type === 1) {
        this.$router.push({path: '/home-page'})
      } else if (type === 2) {
        this.$router.push({path: '/song-list-page'})
      } else if (type === 3) {
        this.$router.push({path: '/singer-page'})
      } else if (type === 4) {
        if (this.loginIn) {
          document.querySelector('.menu').classList.remove('show')
          this.$router.push({path: '/my-song'})
        } else {
          this.$notify({
            title: '请先登录',
            type: 'warning'
          })
        }
      } else if (type === 5) {
        this.$router.push({path: '/'})
      } else if (type === 6) {
        this.$router.push({path: '/register-page'})
      }
    },
    changeIndex (value) {
      this.$store.commit('setActiveName', value)
      window.sessionStorage.setItem('activeName', JSON.stringify(value))
    },
    goHomePage () {
      this.$router.push({path: '/home-page'})
    },
    goMySong () {
      if (this.loginIn) {
        document.querySelector('.menu').classList.remove('show')
        this.$router.push({path: '/my-song'})
      } else {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      }
    },
    goRegister () {
      this.changeIndex('我的')
      this.$router.push({path: '/register-page'})
    },
    goPersonal () {
      document.querySelector('.menu').classList.remove('show')
      this.$router.push({path: '/personal'})
    },
    goUploadImg () {
      document.querySelector('.menu').classList.remove('show')
      this.$router.push({path: '/upLoad-img'})
    },
    goSign () {
      this.$store.commit('setLoginIn', false)
      window.sessionStorage.setItem('loginIn', JSON.stringify(false))
      document.querySelector('.menu').classList.remove('show')
      this.$router.go(0)
    },
    goSearch () {
      this.$store.commit('setSearchword', this.keywords)
      this.$router.push({path: '/search-page', query: {keywords: this.keywords}})
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.item-logo-hd{
  position: absolute;
  top:15px;
  left: 30px;
}
.item-logo-hd .icon {
  width: 50px;
  height: 50px;
  fill: currentColor;
  color: black;
}
.head-nav {
  background-color: #fefefe;
  width: 100%;
  height: 80px;
  position: relative;
  box-shadow: 0 0 10px  black;
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
/*nav*/
.navbar li {
  min-width: 80px;
  height: 55px;
  font-size: 20px;
  text-align: center;
  color: #67757f;
  border-bottom: none;
  cursor: pointer;
}
/*搜索*/
.head-search {
  margin: 0 60px 0 50px;
  display: flex;
  border-radius: 10px;
  overflow: hidden;
}
.head-search input {
  height: 30px;
  width: 270px;
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
  right: 100px;
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
  right: 100px;
  top: 75px;
  box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.4);
  border: 1px solid rgba(0, 0, 0, 0.5);
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
  right:30px;
  top: -5px; /*果断的露出上半部分*/
  width: 10px;
  height: 10px;
  background-color: #ffffff;
  border-left: 1px solid rgba(0, 0, 0, 0.4);
  border-top: 1px solid rgba(0, 0, 0, 0.4);
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
  border-bottom: solid 1px rgba(0, 0, 0, 0.2);
}
.show {
  display: block;
}
.active{
  color: #93d2f8 !important;
  border-bottom: 6px solid #93d2f8 !important;
}
</style>
