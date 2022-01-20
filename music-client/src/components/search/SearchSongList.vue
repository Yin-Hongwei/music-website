<template>
  <div class="search-song-Lists">
    <play-list :playList="albumDatas" path="song-sheet-detail"></play-list>
  </div>
</template>

<script>
import mixin from '../../mixins'
import PlayList from '../PlayList'
import { HttpManager } from '../../api/index'

export default {
  name: 'search-song-sheet',
  mixins: [mixin],
  components: {
    PlayList
  },
  data () {
    return {
      albumDatas: []
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
            this.albumDatas = res
          }
        })
    }
  }
}
</script>

<style scoped>
.search-song-Lists {
  height: 480px !important;
}
</style>
