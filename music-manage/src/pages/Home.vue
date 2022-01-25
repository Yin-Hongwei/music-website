<template>
  <div>
    <yin-audio></yin-audio>
    <yin-header></yin-header>
    <yin-aside></yin-aside>
    <div class="content-box" :class="{'content-collapse':collapse}">
      <div class="content">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import YinAudio from '../components/layouts/YinAudio'
import YinHeader from '../components/layouts/YinHeader'
import YinAside from '../components/layouts/YinAside'
import _ctrEvent from '../utils/ctr-event'

export default {
  components: {
    YinAudio,
    YinHeader,
    YinAside
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
