<template>
  <div>
    <!--轮播图-->
    <swiper/>
    <!--热门歌单-->
    <div class="body-section">
      <div class="section-head">歌单</div>
      <div class="section-content">
        <div class="content-item" v-for="(item, index) in PopularListSings" :key="index">
          <div class="kuo">
            <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
            <div class="mask"  @click="goRouter(item.id, index)">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-bofang"></use>
              </svg>
            </div>
          </div>
          <p class="item-name">{{item.title}}</p>
        </div>
      </div>
    </div>

    <!--歌手-->
    <div class="body-section">
      <div class="section-head">歌手</div>
      <div class="section-content">
        <div class="content-item" v-for="(item, index) in PopularSinger" :key="index">
          <div class="kuo">
            <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
            <div class="mask" @click="goSingerAblum(item.id, index)">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-bofang"></use>
              </svg>
            </div>
          </div>
          <p class="item-name">{{item.name}}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {mixin} from '../mixins'
import swiper from '../components/swiper'
export default {
  name: 'discover',
  data () {
    return {
      PopularListSings: [], // 热门歌单
      PopularSinger: [] // 热门歌手
    }
  },
  components: {
    swiper
  },
  mounted: function () {
    this.getSongLists()
    this.getSingerLists()
    this.getAllComment()
  },
  mixins: [mixin],
  methods: {
    goRouter (id, index) {
      this.$store.commit('setIndex', index)
      window.sessionStorage.setItem('index', JSON.stringify(index))
      this.$router.push({path: '/songAlbum/' + id})
    },
    goSingerAblum (id, index) {
      this.$store.commit('setIndex', index)
      window.sessionStorage.setItem('index', JSON.stringify(index))
      this.$router.push({path: '/singerAlbum/' + id})
    },
    getSongLists () {
      let _this = this
      axios.get(_this.$store.state.HOST + '/listSongLists')
        .then(function (response) {
          // console.log('-----------获得歌单列表------------')
          // console.log(response.data)
          _this.$store.commit('setSongsList', response.data)
          window.sessionStorage.setItem('songsList', JSON.stringify(response.data))
          for (let i = 0; i < 10; i++) {
            if (response.data[i]) {
              _this.PopularListSings.push(response.data[i])
            }
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    getSingerLists () {
      let _this = this
      axios.get(_this.$store.state.HOST + '/listSingers')
        .then(function (response) {
          // console.log('-----------获得歌手列表------------')
          // console.log(response.data)
          _this.$store.commit('setSingersList', response.data)
          window.sessionStorage.setItem('singersList', JSON.stringify(response.data))
          for (let i = 0; i < 10; i++) {
            _this.PopularSinger.push(response.data[i])
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
.body-section {
  margin-bottom: 80px;
  padding-bottom: 50px;
  padding-left: 100px;
  padding-right: 100px;
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
.section-content {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
.content-item {
  padding: 0 20px;
  width: 13vw;
  min-width: 140px;
  position: relative;
}
.kuo {
  overflow: hidden;
}
.kuo, .mask {
  width: 13vw;
  height: 13vw;
  min-width: 140px;
  min-height: 140px;
  border-radius: 6px;
}
.mask{
  position: absolute;
  top:0;
  background-color: rgba(52,47,41,.4);
  transition:all .3s ease-in-out;
  opacity: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.mask:hover{
  opacity: 1;
  cursor: pointer;
}
.item-img {
  width: 100%;
}
.item-name {
  overflow:hidden;
  text-overflow:ellipsis;
  display:-webkit-box;
  -webkit-box-orient:vertical;
  -webkit-line-clamp:2;
  margin: 10px 8px;
}

.icon {
  width: 2em;
  height: 2em;
  fill: currentColor;
  color: rgba(240,240,240,1);
  font-size: 1.6em;
}
</style>
