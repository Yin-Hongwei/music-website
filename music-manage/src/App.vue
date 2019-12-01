<template>
  <div id="app">
    <song-audio />
    <router-view></router-view>
  </div>
</template>

<script>
import SongAudio from './components/SongAudio'
import './assets/css/main.css'

export default {
  name: 'App',
  components: {
    SongAudio
  }
}
</script>

<style>
</style>
