<template>
  <div class="home">
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
import Swiper from '../components/Swiper'
import ContentList from '../components/ContentList'

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
      this.$api.songListAPI.getSongList()
        .then(res => {
          this.songsList[0].list = res.data.slice(0, 10)
        })
        .catch(err => {
          console.log(err)
        })
    },
    getSinger () {
      this.$api.singerAPI.getAllSinger()
        .then(res => {
          this.songsList[1].list = res.data.slice(0, 10)
        })
        .catch(err => {
          console.log(err)
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
