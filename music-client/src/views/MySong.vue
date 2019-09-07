<template>
  <div id="album">
    <div class="album-bg"></div>
    <div class="album-slide">
      <div class="album-slide">
        <div class="album-img">
          <img :src=attachImageUrl(avator) alt="">
        </div>
        <ul class="album-info">
          <li>昵称： {{username}}</li>
          <li>性别： {{userSex}}</li>
          <li>生日： {{birth}}</li>
          <li>地区： {{location}}</li>
        </ul>
      </div>
    </div>
    <div class="album-content">
      <div class="album-title">个性签名: {{introduction}}</div>
      <div class="content">
        <p class="title">我的收藏</p>
        <div class="song-item">
          <div class="item-kong"></div>
          <div class="item-n">歌曲名</div>
          <div class="item-s">艺人</div>
          <div class="item-z">专辑</div>
        </div>
        <hr>
        <ul>
          <li v-for="(item, index) in collectList" :key="index">
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
</template>

<script>
import axios from 'axios'
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'
export default {
  name: 'my-song',
  data () {
    return {
      avator: '',
      username: '',
      userSex: '',
      birth: '',
      location: '',
      introduction: '',
      collectList: [] // 收藏的歌曲
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'collection', // 收藏的歌曲ID
      'id',
      'listOfSongs' // 存放的音乐
    ])
  },
  mounted () {
    this.getMsg(this.userId)
    this.getCollection(this.userId)
  },
  mixins: [mixin],
  methods: {
    getMsg (id) {
      let _this = this
      axios.get(_this.$store.state.HOST + '/commentOfConsumer?id=' + id)
        .then(response => {
          _this.username = response.data[0].username
          _this.getuserSex(response.data[0].sex)
          _this.birth = response.data[0].birth
          _this.introduction = response.data[0].introduction
          _this.location = response.data[0].location
          _this.avator = response.data[0].avator
        })
    },
    getuserSex (sex) {
      if (sex === 0) {
        this.userSex = '女'
      } else if (sex === 1) {
        this.userSex = '男'
      }
    },
    getCollection (userId) {
      let _this = this
      axios.get(_this.$store.state.HOST + '/myCollection?userId=' + userId)
        .then(function (response) {
          console.log('--------------收藏的歌曲ID-----------------')
          console.log(response.data)
          _this.$store.commit('setCollection', response.data)
          window.sessionStorage.setItem('collection', JSON.stringify(response.data))
          // 通过歌曲ID获取歌曲信息
          for (let i = 0, len = _this.collection.length; i < len; i++) {
            _this.getCollectSongs(_this.collection[i].songId)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 获取收藏的歌曲
    getCollectSongs (id) {
      let _this = this
      axios.get(_this.$store.state.HOST + '/listSongsOfSongs?id=' + id)
        .then(function (response) {
          _this.collectList.push(response.data[0])
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  }
}
</script>

<style scoped>
  /*歌单背景*/
  .album-bg {
    width: 100%;
    height: 200px;
    background-color: #93d2f8;
  }
  /*歌单左侧*/
  .album-slide {
    float: left;
    width: 400px;
  }
  /*歌单图像*/
  .album-img {
    height: 200px;
    width: 200px;
    display: inline-block;
    position: relative;
    top:-100px;
    left: 50px;
    border-radius: 50%;
    overflow: hidden;
    border: 5px solid white;
  }
  .album-img img {
    width: 100%;
  }
  /*歌单信息*/
  .album-info {
    color: black;
    width: 500px;
    font-size: 20px;
    font-weight: 500;
    margin-top: -100px;
    margin-left: 100px;
    padding: 30px 30px;
  }
  .album-info li {
    width: 100%;
    line-height: 40px;
  }
  /*歌单内容*/
  .album-content{
    margin-left: 300px;
    padding: 40px 100px;
  }
  /*歌单题目*/
  .album-title {
    font-size: 30px;
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
