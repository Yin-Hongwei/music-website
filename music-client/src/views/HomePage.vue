<template>
  <div class="home-page">
    <!--轮播图-->
    <swiper/>
    <!--热门歌单/歌手-->
    <content-list :contentList="popularList" :type="0"></content-list>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {mixin} from '../mixins'
import Swiper from '../components/Swiper'
import ContentList from '../components/ContentList'

export default {
  name: 'Home-page',
  data () {
    return {
      popularList: [{
        name: '歌单',
        list: []
      }, {
        name: '歌手',
        list: []
      }]
    }
  },
  created () {
    this.getSongLists()
    this.getSingerLists()
  },
  mounted () {
    this.getList()
  },
  mixins: [mixin],
  computed: {
    ...mapGetters([
      'songsList',
      'singersList'
    ])
  },
  components: {
    Swiper,
    ContentList
  },
  methods: {
    goRouter (id, index) {
      this.$store.commit('setIndex', index)
      window.sessionStorage.setItem('index', JSON.stringify(index))
      this.$router.push({path: '/song-list-album-page/' + id})
    },
    goSingerAblum (id, index) {
      this.$store.commit('setIndex', index)
      window.sessionStorage.setItem('index', JSON.stringify(index))
      this.$router.push({path: '/singer-album-page/' + id})
    },
    // 得到歌单和歌曲列表前10项
    getList () {
      this.popularList[0].list = this.songsList.slice(0, 10)
      this.popularList[1].list = this.singersList.slice(0, 10)
    }
  }
}
</script>

<style scoped>

</style>
