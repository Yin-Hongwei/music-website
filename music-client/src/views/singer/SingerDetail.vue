<template>
  <div class='singer-detail'>
    <div class='album-slide'>
      <div class='singer-img'>
        <img :src='attachImageUrl(songDetails.pic)' alt=''>
      </div>
      <ul class='info'>
        <li v-if='songDetails.sex !== 2'>性别：{{getUserSex(songDetails.sex)}}</li>
        <li>生日：{{getBirth(songDetails.birth)}}</li>
        <li>故乡：{{songDetails.location}}</li>
      </ul>
    </div>
    <div class='song-list'>
      <div class='intro'>
        <h2>{{songDetails.name}}</h2>
        <span>{{songDetails.introduction}}</span>
      </div>
      <div class='content'>
        <song-list :songList='currentSongList'></song-list>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '@/mixins'
import SongList from '@/components/SongList'
import { HttpManager } from '@/api'

export default {
  mixins: [mixin],
  components: {
    SongList
  },
  data () {
    return {
      currentSongList: []
    }
  },
  computed: {
    ...mapGetters([
      'songDetails'
    ])
  },
  async mounted () {
    try {
      const result = await HttpManager.getSongOfSingerId(this.songDetails.id)
      this.currentSongList = result
    } catch (error) {
      console.error(error)
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/singer-detail.scss';
</style>
