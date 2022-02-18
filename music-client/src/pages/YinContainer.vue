<template>
  <div>
    <yin-header></yin-header>
    <router-view />
    <yin-current-play></yin-current-play>
    <yin-play-bar></yin-play-bar>
    <yin-scroll-top></yin-scroll-top>
    <yin-footer></yin-footer>
    <yin-audio></yin-audio>
  </div>
</template>

<script>
import YinHeader from '../components/layouts/YinHeader'
import YinCurrentPlay from '../components/layouts/YinCurrentPlay'
import YinPlayBar from '../components/layouts/YinPlayBar'
import YinScrollTop from '../components/layouts/YinScrollTop'
import YinFooter from '../components/layouts/YinFooter'
import YinAudio from '../components/layouts/YinAudio'

export default {
  name: 'YinContainer',
  components: {
    YinHeader,
    YinCurrentPlay,
    YinPlayBar,
    YinScrollTop,
    YinFooter,
    YinAudio
  },
  created () {
    if (sessionStorage.getItem('dataStore')) {
      this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(sessionStorage.getItem('dataStore'))))
    }

    // 在页面刷新时将 vuex 里的信息保存到 sessionStorage 里
    window.addEventListener('beforeunload', () => {
      sessionStorage.setItem('dataStore', JSON.stringify(this.$store.state))
    })
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/yin-container.scss';
</style>
