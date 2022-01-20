<template>
  <div class="search-song-list">
    <play-list :playList="playList" path="song-sheet-detail"></play-list>
  </div>
</template>

<script>
import mixin from '../../mixins'
import PlayList from '../PlayList'
import { HttpManager } from '../../api/index'

export default {
  name: 'SearchSongList',
  mixins: [mixin],
  components: {
    PlayList
  },
  data () {
    return {
      playList: []
    }
  },
  mounted () {
    this.getSearchList()
  },
  methods: {
    getSearchList () {
      if (!this.$route.query.keywords) return
      HttpManager.getSongListOfLikeTitle(this.$route.query.keywords)
        .then(res => {
          if (!res.length) {
            this.$notify({
              title: '暂无该歌曲内容',
              type: 'warning'
            })
          } else {
            this.playList = res
          }
        })
    }
  }
}
</script>

<style scoped>
.search-song-list {
  height: 480px !important;
}
</style>
