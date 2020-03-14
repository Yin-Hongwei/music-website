<template>
  <transition name="slide-fade">
    <div class="the-aside" v-if="showAside">
      <h2 class="title">播放列表</h2>
      <ul class="menus">
        <li v-for="(item, index) in listOfSongs" :class="{'is-play': id === item.id}" :key="index" @click="toplay(item.id, item.url, item.pic, index, item.name, item.lyric)">
          {{replaceFName(item.name)}}
        </li>
      </ul>
    </div>
  </transition>
</template>

<script>
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'

export default {
  name: 'the-aside',
  mixins: [mixin],
  computed: {
    ...mapGetters([
      'id', // 音乐id
      'listOfSongs', // 当前歌单列表
      'showAside' // 是否显示侧边栏
    ])
  },
  mounted () {
    let _this = this
    document.addEventListener('click', function () {
      _this.$store.commit('setShowAside', false)
    }, true)
  }
}
</script>

<style scoped>
.slide-fade-enter-active {
  transition: all .3s ease;
}
.slide-fade-leave-active {
  transition: all .2s ease;
}
.slide-fade-enter, .slide-fade-leave-to {
  transform: translateX(10px);
  opacity: 0;
}

.the-aside {
  font-size: 14px;
  width: 250px;
  height: 370px;
  position: fixed;
  right: 0;
  top: 150px;
  z-index: 99;
  background-color: #ffffff;
  box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.4);
  border: 1px solid rgba(0, 0, 0, 0.5);
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  overflow: hidden;
}
.title {
  padding-left: 20px;
  margin: 10px 0;
  box-sizing: border-box;
}
.menus {
  background-color: #fff;
  width: 100%;
  height: calc(100% - 19px);
  cursor: pointer;
  z-index: 100;
  overflow: scroll;
  white-space: nowrap;
}

.menus li {
  display: block;
  width: 100%;
  height: 40px;
  line-height: 40px;
  padding-left: 20px;
  box-sizing: border-box;
  border-bottom: solid 1px rgba(0, 0, 0, 0.2);
}

.menus li:hover{
  background-color: #95d2f6;
  color: white;
}

.is-play{
  color: #30a4fc;
  font-weight: bold;
}
</style>
