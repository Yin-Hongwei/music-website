<template>
  <div id="app">
    <song-audio/>
    <the-header/>
    <router-view class="music-content"/>
    <play-control/>
    <scroll-top/>
    <the-footer/>
  </div>
</template>

<script>
import ScrollTop from './components/ScrollTop'
import SongAudio from './components/SongAudio'
import TheHeader from './components/TheHeader'
import TheFooter from './components/TheFooter'
import PlayControl from './components/PlayControl'

export default {
  name: 'App',
  components: {
    ScrollTop,
    SongAudio,
    TheHeader,
    TheFooter,
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
