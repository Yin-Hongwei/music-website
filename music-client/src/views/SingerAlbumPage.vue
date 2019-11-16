<template>
  <div class="album">
    <div class="my-bg"></div>
    <div class="album-slide">
      <div class="my-slide">
        <div class="hd-img">
          <img :src=attachImageUrl(singers.pic) alt="">
        </div>
        <ul class="info">
          <li>性别：{{attachSex(singers.sex)}}</li>
          <li>生日：{{singers.birth}}</li>
          <li>地址：{{singers.location}}</li>
        </ul>
      </div>
      <div class="my-content">
        <div class="hd-intro">
            <h2>{{singers.name}} : </h2>
            <span>{{singers.introduction}}</span>
        </div>
        <div class="songs-body">
          <album-content :songList="listOfSongs">
            <template slot="title">歌单</template>
          </album-content>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'
import AlbumContent from '../components/AlbumContent'

export default {
  name: 'singer-album-page',
  data () {
    return {
      singers: {},
      textarea: ''
    }
  },
  computed: {
    ...mapGetters([
      'listOfSongs' // 存放的音乐
    ])
  },
  components: {
    AlbumContent
  },
  created () {
    this.singers = this.$route.query.item
    this.getSongList()
  },
  mixins: [mixin],
  methods: {
    getSongList () {
      let _this = this
      axios.get(_this.$store.state.HOST + '/listSongs', {
        params: {
          singerId: _this.singers.id
        }
      })
        .then(function (response) {
          _this.$store.commit('setListOfSongs', response.data)
          window.sessionStorage.setItem('listOfSongs', JSON.stringify(response.data))
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    attachSex (value) {
      if (value === 0) {
        return '女'
      } else if (value === 1) {
        return '男'
      }
    }
  }
}
</script>

<style scoped>
  .my-bg {
    width: 100%;
    height: 200px;
    background-color: #93d2f8;
  }
  /*左*/
  .my-slide {
    float: left;
    width: 400px;
  }
  .hd-img {
    height: 300px;
    width: 300px;
    display: inline-block;
    position: relative;
    top:-100px;
    left: 50px;
    border-radius: 10%;
    overflow: hidden;
    border: 5px solid white;
  }
  .hd-img img {
    width: 100%;
  }
  .info {
    color: black;
    font-size: 20px;
    font-weight: 500;
    padding-left: 100px;
  }
  .info li {
    width: 100%;
    height: 40px;
  }

  /*右*/
  .my-content{
    margin-left: 300px;
    padding: 30px 100px;
  }
  .hd-intro {
    font-size: 20px;
  }
  .hd-intro > span {
    color: rgba(0, 0, 0, 0.5);
  }
  .songs-body {
    margin-top: 50px;
  }

</style>
