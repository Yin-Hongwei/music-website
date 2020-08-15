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
import { mapGetters } from 'vuex'
import mixin from '../mixins'

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

<style lang="scss" scoped>
@import '../assets/css/the-aside.scss';
</style>
