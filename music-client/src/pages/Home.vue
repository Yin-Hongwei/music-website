<template>
  <div class="home">
    <!--轮播图-->
    <swiper/>
    <!--热门歌单/歌手-->
    <div class="section" v-for="(item, index) in songsList" :key="index">
      <div class="section-title">{{item.name}}</div>
      <content-list :contentList="item.list"></content-list>
    </div>
  </div>
</template>

<script>
import Swiper from '../components/Swiper'
import ContentList from '../components/ContentList'
import { getSongList, getAllSinger } from '../api/index'

export default {
  name: 'home',
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
    // 获取歌单列表
    this.getSongList('songList')
    // 获取歌手列表
    this.getSinger('singer')
  },
  methods: {
    getSongList (path) {
      getSongList()
        .then(res => {
          this.songsList[0].list = res.slice(0, 10)
        })
        .catch(err => {
          console.log(err)
        })
    },
    getSinger () {
      getAllSinger().then(res => {
        this.songsList[1].list = res.slice(0, 10)
      })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/home.scss';
</style>
