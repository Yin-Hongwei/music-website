<template>
    <div id="app">
        <song-audio/>
        <router-view></router-view>
    </div>
</template>

<script>
import SongAudio from './components/SongAudio'

export default {
    name: 'App',
    components: {
        SongAudio
    }
}
</script>

<style>
    @import "../static/css/main.css";
    @import "../static/css/color-dark.css";     /*深色主题*/
</style>
