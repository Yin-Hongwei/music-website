<template>
  <div class="home-page">
    <!--轮播图-->
    <swiper/>
    <!--热门歌单/歌手-->
    <div class="section" v-for="(item, index) in songsList" :key="index">
      <div class="section-head">{{item.name}}</div>
      <content-list :contentList="item.list"></content-list>
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
      songsList: [
        {name: '歌单', list: []},
        {name: '歌手', list: []}
      ]
    }
  },
  created () {
    this.getSongLists('listSongLists')
    this.getSongLists('listSingers')
  },
  methods: {
    getSongLists (path) {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/${path}`)
        .then(function (res) {
          if (path === 'listSongLists') {
            // 获取歌单列表
            _this.songsList[0].list = res.data.slice(0, 10)
          } else if (path === 'listSingers') {
            // 获取歌手列表
            _this.songsList[1].list = res.data.slice(0, 10)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  }
}
</script>

<style scoped>
div {
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
