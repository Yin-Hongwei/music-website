<template>
  <div class="home-page">
    <!--轮播图-->
    <swiper/>
    <!--热门歌单/歌手-->
    <div class="section">
      <div class="section-head">歌单</div>
      <content-list :contentList="popularList[0]"></content-list>
    </div>
    <div class="section">
      <div class="section-head">歌手</div>
      <content-list :contentList="popularList[1]"></content-list>
    </div>
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
      popularList: []
    }
  },
  created () {
    this.getSongLists()
    this.getSingerLists()
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
    // 得到歌单和歌曲列表前10项
    getList () {
      this.popularList[0] = this.songsList.slice(0, 10)
      this.popularList[1] = this.singersList.slice(0, 10)
      console.log(this.popularList)
    }
  }
}
</script>

<style scoped>
  * {
    box-sizing: border-box;
  }
  .section {
    width: 100%;
    margin-bottom: 80px;
    padding: 0 120px 50px 120px;
    background-color: #ffffff;
  }
  .section-head {
    height: 100px;
    font-size: 28px;
    font-weight: 500;
    text-align: center;
    line-height: 100px;
    color: black;
  }
</style>
