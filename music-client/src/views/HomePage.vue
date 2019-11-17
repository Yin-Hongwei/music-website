<template>
  <div class="home-page">
    <!--轮播图-->
    <swiper/>
    <!--热门歌单/歌手-->
    <div class="section">
      <div class="section-head">歌单</div>
      <content-list :contentList="songsList"></content-list>
    </div>
    <div class="section">
      <div class="section-head">歌手</div>
      <content-list :contentList="singersList"></content-list>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import Swiper from '../components/Swiper'
import ContentList from '../components/ContentList'

export default {
  name: 'home-page',
  components: {
    Swiper,
    ContentList
  },
  data () {
    return {
      songsList: [],
      singersList: []
    }
  },
  created () {
    this.getSongLists()
    this.getSingerLists()
  },
  methods: {
    // 获取歌单列表
    getSongLists () {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/listSongLists`)
        .then(function (res) {
          _this.songsList = res.data.slice(0, 10)
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 获取歌手
    getSingerLists () {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/listSingers`)
        .then(function (res) {
          _this.singersList = res.data.slice(0, 10)
        })
        .catch(function (error) {
          console.log(error)
        })
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
