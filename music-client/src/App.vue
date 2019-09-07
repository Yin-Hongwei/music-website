<template>
  <div id="app">
    <song-audio/>
    <head-nav/>
    <router-view/>
    <play-control/>
    <scroll-top/>
    <foot/>
  </div>
</template>

<script>
import ScrollTop from './components/ScrollTop'
import SongAudio from './components/SongAudio'
import HeadNav from './components/HeadNav'
import Foot from './components/Footer'
import PlayControl from './components/PlayControl'
export default {
  name: 'App',
  components: {
    ScrollTop,
    SongAudio,
    HeadNav,
    Foot,
    PlayControl
  }
}
</script>

<style>
#app{
  background-color: #e6ecf0;
}
</style>
