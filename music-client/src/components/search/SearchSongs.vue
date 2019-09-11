<template>
  <div class="search-songs">
    <album-content :songList="listOfSongs"></album-content>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {mixin} from '../../mixins'
import AlbumContent from '../AlbumContent'

export default {
  name: 'search-songs',
  computed: {
    ...mapGetters([
      'listOfSongs' // 存放的音乐
    ])
  },
  components: {
    AlbumContent
  },
  mounted: function () {
    this.getSong()
  },
  mixins: [mixin]
}
</script>

<style scoped>
  .search-songs{
    min-height: 300px;
  }
</style>
