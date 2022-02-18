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
        <song-list :songList='currentSongList'>
          <template slot='title'>歌单</template>
        </song-list>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '../../mixins'
import SongList from '../../components/SongList'
import { HttpManager } from '../../api'

export default {
  name: 'SingerDetail',
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
  mounted () {
    this.getSongList(this.songDetails.id)
  },
  methods: {
    getSongList (id) {
      HttpManager.getSongOfSingerId(id)
        .then(res => {
          this.currentSongList = res
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/singer-detail.scss';
</style>
