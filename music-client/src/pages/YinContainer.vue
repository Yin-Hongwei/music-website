<template>
  <div>
    <yin-audio></yin-audio>
    <yin-header></yin-header>
    <yin-current-play></yin-current-play>
    <router-view/>
    <yin-play-bar></yin-play-bar>
    <yin-scroll-top></yin-scroll-top>
    <yin-footer></yin-footer>
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
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/yin-container.scss';
</style>
