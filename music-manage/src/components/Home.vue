<template>
  <div>
    <song-audio />
    <the-header></the-header>
    <the-aside></the-aside>
    <div class="content-box" :class="{'content-collapse':collapse}">
      <div class="content">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import SongAudio from './SongAudio'
import TheHeader from './TheHeader.vue'
import TheAside from './TheAside.vue'
import _ctrEvent from '../assets/js/ctr-event'

export default {
  components: {
    SongAudio,
    TheHeader,
    TheAside
  },
  data () {
    return {
      collapse: false
    }
  },
  created () {
    _ctrEvent.$on('collapse', msg => {
      this.collapse = msg
    })
  }
}
</script>

<style>
</style>
