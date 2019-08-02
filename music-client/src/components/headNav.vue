<template>
  <div class="head">
    <!--图标-->
    <div class="head-logo" @click="goHomePage">
      <div class="item-logo-hd">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-erji"></use>
        </svg>
      </div>
      Y-Music
    </div>
    <ul class="navbar" ref="change">
      <li class="item" @click="goHomePage()">首页</li>
      <li class="item" @click="goSongList()">歌单</li>
      <li class="item" @click="goSinger()">歌手</li>
      <li class="item" @click="goMySong()">我的</li>
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
      <li class="item" @click="goLogin()" v-if="!loginIn">登录</li>
      <li class="item" @click="goRegister()" v-if="!loginIn">注册</li>
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
  name: 'headNav',
  data () {
    return {
      keywords: ''
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'headIndex',
      'avator',
      'username',
      'loginIn'
    ])
  },
  mounted () {
    this.changeIndex(this.headIndex)
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
    changeIndex (index) {
      console.log(index)
      this.$store.commit('setHheadIndex', index)
      window.sessionStorage.setItem('headIndex', JSON.stringify(index))
      let len = this.$refs.change.children.length
      for (let i = 0; i < len; i++) {
        this.$refs.change.children[i].style.color = '#67757f'
        this.$refs.change.children[i].style.borderBottom = 'none'
      }
      this.$refs.change.children[index].style.color = '#93d2f8'
      this.$refs.change.children[index].style.borderBottom = '6px solid #93d2f8'
    },
    goHomePage () {
      this.changeIndex(0)
      this.$router.push({path: '/homePage'})
    },
    goSongList () {
      this.changeIndex(1)
      this.$router.push({path: '/songList'})
    },
    goSinger () {
      this.changeIndex(2)
      this.$router.push({path: '/singer'})
    },
    goMySong () {
      if (this.loginIn) {
        this.changeIndex(3)
        document.querySelector('.menu').classList.remove('show')
        this.$router.push({path: '/mySong'})
      } else {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      }
    },
    goLogin () {
      this.changeIndex(5)
      this.$router.push({path: '/'})
    },
    goRegister () {
      this.changeIndex(6)
      this.$router.push({path: '/register'})
    },
    goPersonal () {
      document.querySelector('.menu').classList.remove('show')
      this.$router.push({path: '/personal'})
    },
    goUploadImg () {
      document.querySelector('.menu').classList.remove('show')
      this.$router.push({path: '/upLoadImg'})
    },
    goSign () {
      this.$store.commit('setLoginIn', false)
      window.sessionStorage.setItem('loginIn', JSON.stringify(false))
      document.querySelector('.menu').classList.remove('show')
      this.$router.go(0)
    },
    goSearch () {
      this.$store.commit('setSearchword', this.keywords)
      this.$router.push({path: '/search', query: {keywords: this.keywords}})
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
.head {
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
</style>
