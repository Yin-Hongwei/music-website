<template>
  <div class="search-song-Lists">
    <content-list :contentList="albumDatas"></content-list>
  </div>
</template>

<script>
import axios from 'axios'
import ContentList from '../ContentList'

export default {
  name: 'search-song-Lists',
  components: {
    ContentList
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
      if (!this.$route.query.keywords) {
        this.$notify({
          title: '您输入内容为空',
          type: 'warning'
        })
      } else if (this.$route.query.keywords) {
        let _this = this
        axios.get(`${_this.$store.state.HOST}/api/songList/likeTitle?title=${_this.$route.query.keywords}`)
          .then(res => {
            _this.albumDatas = res.data
          })
      } else {
        this.$notify({
          title: '暂无该歌曲内容',
          type: 'error'
        })
      }
    }
  }
}
</script>

<style scoped>
.search-song-Lists{
  min-height: 300px;
  margin-top: 50px;
}
</style>
