<template>
  <div id="app">
    <song-audio />
    <router-view></router-view>
  </div>
</template>

<script>
import SongAudio from './components/SongAudio'
import '../static/css/main.css'
import '../static/css/color-dark.css'

export default {
  name: 'App',
  components: {
    SongAudio
  }
}
</script>

<style>
</style>
