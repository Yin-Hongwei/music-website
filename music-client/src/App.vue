<template>
  <div id="app">
    <song-audio/>
    <headNav/>
    <!--<keep-alive>-->
      <router-view/>
    <!--</keep-alive>-->
    <player/>
    <foot class="app-foot"/>
  </div>
</template>

<script>
import SongAudio from './components/SongAudio'
import headNav from './components/headNav'
import foot from './components/foot'
import player from './components/player'
export default {
  name: 'App',
  components: {
    SongAudio,
    headNav,
    foot,
    player
  }
}
</script>

<style>
#app{
  background-color: #e6ecf0;
}
</style>
