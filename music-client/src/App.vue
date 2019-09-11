<template>
  <div id="app">
    <song-audio/>
    <head-nav/>
    <router-view class="music-content"/>
    <play-control/>
    <scroll-top/>
    <music-footer/>
  </div>
</template>

<script>
import ScrollTop from './components/ScrollTop'
import SongAudio from './components/SongAudio'
import HeadNav from './components/HeadNav'
import MusicFooter from './components/MusicFooter'
import PlayControl from './components/PlayControl'

export default {
  name: 'App',
  components: {
    ScrollTop,
    SongAudio,
    HeadNav,
    MusicFooter,
    PlayControl
  }
}
</script>

<style>
#app{
  background-color: #e6ecf0;
  display: flex;
  flex-direction: column;
}
.music-content{
  flex: 1;
}
</style>
