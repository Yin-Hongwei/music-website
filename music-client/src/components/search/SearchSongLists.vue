<template>
  <div class="search-song-Lists">
    <content-list :contentList="albumDatas"></content-list>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import ContentList from '../ContentList'

export default {
  name: 'search-song-Lists',
  data () {
    return {
      albumDatas: []
    }
  },
  computed: {
    ...mapGetters([
      'songsList'
    ])
  },
  components: {
    ContentList
  },
  mounted: function () {
    this.getSearchList()
  },
  methods: {
    getSearchList () {
      console.log(this.$route.query.keywords)
      if (!this.$route.query.keywords) {
        this.$notify({
          title: '您输入内容为空',
          type: 'warning'
        })
      } else if (this.$route.query.keywords) {
        for (let i = 0; i < this.songsList.length; i++) {
          if (this.songsList[i].title.indexOf(this.$route.query.keywords) !== -1) {
            var item = this.songsList[i]
            item.list = i
            this.albumDatas.push(item)
            console.log(item)
          }
        }
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
