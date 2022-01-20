<template>
  <div class="search-song">
    <song-list :songList="listOfSongs"></song-list>
  </div>
</template>

<script>
import mixin from '../../mixins'
import SongList from '../SongList'
import { mapGetters } from 'vuex'

export default {
  name: 'SearchSong',
  mixins: [mixin],
  components: {
    SongList
  },
  computed: {
    ...mapGetters([
      'listOfSongs' // 存放的音乐
    ])
  },
  mounted () {
    this.getSong()
  }
}
</script>

<style scoped>
.search-song {
  min-height: 480px;
}
</style>
