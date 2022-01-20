<template>
  <div>
    <yin-header></yin-header>
    <router-view/>
    <yin-current-play></yin-current-play>
    <yin-play-bar></yin-play-bar>
    <yin-scroll-top></yin-scroll-top>
    <yin-footer></yin-footer>
    <yin-audio></yin-audio>
  </div>
</template>

<script>
import YinAudio from '../components/common/YinAudio'
import YinHeader from '../components/common/YinHeader'
import YinCurrentPlay from '../components/common/YinCurrentPlay'
import YinPlayBar from '../components/common/YinPlayBar'
import YinScrollTop from '../components/common/YinScrollTop'
import YinFooter from '../components/common/YinFooter'

export default {
  name: 'YinContainer',
  components: {
    YinAudio,
    YinHeader,
    YinCurrentPlay,
    YinPlayBar,
    YinScrollTop,
    YinFooter
  },
  created () {
    console.log(
      `%c Yin-music %c v1.0.0 %c`,
      'background: #35495e; padding: 1px; border-radius: 3px 0 0 3px; color: #fff',
      'background: #41b883; padding: 1px; border-radius: 0 3px 3px 0; color: #fff',
      'background: transparent'
    )
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/yin-container.scss';
</style>
