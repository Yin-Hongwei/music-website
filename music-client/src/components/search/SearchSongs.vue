<template>
  <div class="search-songs">
    <album-content :songList="listOfSongs"></album-content>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '../../mixins'
import AlbumContent from '../AlbumContent'

export default {
  name: 'search-songs',
  mixins: [mixin],
  components: {
    AlbumContent
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

<style lang="scss" scoped>
@import '../../assets/css/search-songs.scss';
</style>
