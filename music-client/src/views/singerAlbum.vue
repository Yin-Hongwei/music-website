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
            <span>{{singers.name}} : </span>
            <p>{{singers.introduction}}</p>
        </div>
        <div class="content">
          <p class="title">歌单</p>
          <div class="song-item">
            <div class="item-kong"></div>
            <div class="item-n">歌曲名</div>
            <div class="item-s">艺人</div>
            <div class="item-z">专辑</div>
          </div>
          <hr>
          <ul>
            <li v-for="(item, index) in listOfSongs" :key="index">
              <div class="song-item" @click="toplay(item.id, item.url, item.pic, index, item.name, item.lyric)">
                <div class="item-i">{{index + 1}}</div>
                <div class="item-n">{{replaceFName(item.name)}}</div>
                <div class="item-s">{{replaceLName(item.name)}}</div>
                <div class="item-z">{{item.introduction}}</div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'
export default {
  name: 'singerAlbum',
  data () {
    return {
      singers: {},
      textarea: ''
    }
  },
  computed: {
    ...mapGetters([
      'singersList', // 歌手列表
      'index', // 列表中的序号
      'listOfSongs' // 存放的音乐
    ])
  },
  mounted: function () {
    this.singers = this.singersList[this.index]
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
          console.log(111)
          console.log(response.data)
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
  .my-body {
    width: 100%;
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
  .hd-intro span {
    font-size: 50px;
    font-weight: 600;
  }
  .content {
    background-color: white;
    margin: 50px 0;
    border-radius: 12px;
    padding: 20px 40px;
    min-width: 700px;
  }
  .content .title {
    height: 80px;
    line-height: 60px;
    padding-left: 20px;
    font-size: 20px;
    font-weight: 600;
    color: black;
    text-align: center;
  }
  .content ul {
    width: 100%;
    padding-bottom: 50px;
  }
  .content li {
    border-bottom: solid 1px rgba(0, 0, 0, 0.2);
    display: block;
    height: 40px;
    line-height: 40px;
    text-indent: 20px;
    cursor: pointer;
  }
  .song-item {
    display: flex;
    flex-wrap: nowrap;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
  }
  .item-i {
    width: 5%;
  }
  .item-kong{
    width: 8%
  }
  .item-n {
    width: 30%;
  }
  .item-s {
    width: 20%;
  }
  .item-z {
    width: 45%;
  }

</style>
