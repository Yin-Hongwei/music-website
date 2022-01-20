<template>
  <transition name="aside-fade">
    <div class="yin-current-play" v-if="showAside">
      <h2 class="title">当前播放</h2>
      <div class="control">共 {{currentPlay && currentPlay.length || 0}} 首</div>
      <ul class="menus">
        <li
          v-for="(item, index) in currentPlay"
          :class="{'is-play': id === item.id}"
          :key="index"
          @click="toplay({
            id:item.id,
            url: item.url,
            pic: item.pic,
            index: index,
            name: item.name,
            lyric: item.lyric
          })">
          {{replaceFName(item.name)}}
        </li>
      </ul>
    </div>
  </transition>
</template>

<script>
import mixin from '../../mixins'
import { mapGetters } from 'vuex'

export default {
  name: 'YinCurrentPlay',
  mixins: [mixin],
  computed: {
    ...mapGetters([
      'id', // 音乐id
      'currentPlay', // 当前播放
      'showAside' // 是否显示侧边栏
    ])
  },
  mounted () {
    document.addEventListener('click', () => {
      this.$store.commit('setShowAside', false)
    }, true)
  }
}
</script>

<style lang="scss" scoped>
@import '../../assets/css/yin-current-play.scss';
</style>
