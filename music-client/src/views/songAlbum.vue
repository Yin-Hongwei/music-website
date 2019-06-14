<template>
  <div class="album">
    <div class="album-bg"></div>
    <div class="album-body">
      <div class="album-slide">
        <div class="album-img">
          <img :src=attachImageUrl(singers.pic) alt="">
        </div>
        <div class="album-info">
          简介：{{singers.introduction}}
        </div>
      </div>
      <div class="album-content">
        <div class="album-title">
          <p>{{singers.title}}</p>
        </div>
        <!--评分-->
        <div class="album-score">
          <p>歌单评分</p>
          <el-rate v-model="value5" disabled text-color="#ff9900" score-template="{value}"></el-rate>
          <span>{{value5}}</span>
        </div>
        <div class="push-score">
          <span @click="pushValue()">
          评价：<el-rate v-model="value3" show-text ></el-rate>
          </span>
        </div>
        <!--歌曲-->
        <div class="songs-body">
          <p class="songs-title">歌单</p>
          <div class="song-item songs-head">
            <div class="item-i"></div>
            <div class="item-n">歌曲名</div>
            <div class="item-s">艺人</div>
            <div class="item-z">专辑</div>
          </div>
          <hr>
          <ul class="listOfSongs">
            <li v-for="(item, index) in listOfSongs" :key="index">
              <div class="song-item" @click="toplay(item.id, item.url, item.pic, index, item.name, item.lyric)">
                <div class="item-i">{{index + 1}}</div>
                <div class="item-n">{{replaceFName(item.name)}}</div>
                <div class="item-s">{{replaceLName(item.name)}}</div>
                <div class="item-z">{{item.introduction}}</div>
              </div>
            </li>
          </ul>
          <div class="comment">
            <p>评论</p>
            <div class="comment-hd">
              <img :src=attachImageUrl(avator) alt="">
            </div>
            <el-input
              class="comment-input"
              type="textarea"
              :rows="2"
              placeholder="请输入内容"
              v-model="textarea">
            </el-input>
            <el-button type="primary" class="sub-btn" @click="postComment()">评论</el-button>
          </div>
          <p>精彩评论 共{{songsComment.length}}条评论</p>
          <hr>
          <ul class="popular" v-for="(item, index) in songsComment" :key="index">
            <li>
              <div class="popular-img">
                <img :src=attachImageUrl(userPic[index]) alt="">
              </div>
              <div class="popular-body">
                <div class="popular-content">
                  {{userName[index]}} : {{item.content}}
                </div>
                <div class="popular-msg">
                  <div>{{item.createTime}}</div>
                  <div ref="up" @click="postUp(item.id, item.up, index)">
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-zan"></use>
                    </svg>
                    {{item.up}}
                  </div>
                </div>
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
  name: 'song-album',
  data () {
    return {
      listSongOfSinger: [],
      songLists: [],
      singers: {},
      songsComment: [],
      textarea: '',
      count: 0,
      userPic: [], // 保存评论用户头像
      userName: [], // 保存评论用户名字
      value3: null,
      value5: 0
    }
  },
  computed: {
    ...mapGetters([
      'loginIn',
      'songsList', // 歌单列表
      'index', // 列表中的序号
      'listOfSongs', // 存放的音乐
      'index',
      'userId',
      'avator',
      'songslistComment'
    ])
  },
  mounted: function () {
    // 获取当前歌单信息
    this.singers = this.songsList[this.index]
    // 获取歌单里面的歌曲ID
    this.getSongId()
    this.getRank(this.singers.id)
    this.getAllComment()
    // 获取歌单评论
    // this.getComment()
    this.count = this.songsComment.length
  },
  mixins: [mixin],
  methods: {
    // 收集歌单里面的歌曲
    getSongId () {
      let _this = this
      axios.get(_this.$store.state.HOST + '/listSongOfSingers', {
        params: {
          songListId: _this.singers.id
        }})
        .then(function (response) {
          console.log('------歌单中歌曲的ID------')
          console.log(response.data)
          _this.listSongOfSinger = response.data
          // 获取歌单里的歌曲信息
          for (let i = 0; i < _this.listSongOfSinger.length; i++) {
            _this.getSongList(_this.listSongOfSinger[i].songId)
          }
          console.log('------歌曲------')
          console.log(_this.songLists)
          _this.$store.commit('setListOfSongs', _this.songLists)
          window.sessionStorage.setItem('listOfSongs', JSON.stringify(_this.songLists))
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 获取单里的歌曲
    getSongList (id) {
      let _this = this
      axios.get(_this.$store.state.HOST + '/listSongsOfSongs?id=' + id)
        .then(function (response) {
          _this.songLists.push(response.data[0])
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 获取评分
    getRank (id) {
      let _this = this
      axios.get(_this.$store.state.HOST + '/api/getRank?songListId=' + id)
        .then(function (res) {
          _this.value5 = res.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 提交评分
    pushValue () {
      if (this.loginIn) {
        let _this = this
        var params = new URLSearchParams()
        params.append('songListId', _this.index + 1)
        params.append('consumerId', _this.userId)
        params.append('score', _this.value3 * 2)
        axios.post(_this.$store.state.HOST + '/api/pushRank', params)
          .then(response => {
            if (response.data.code === 1) {
              _this.getRank(_this.singers.id)
              _this.$notify({
                title: '评分成功',
                type: 'success'
              })
            } else {
              _this.$notify({
                title: '评分失败',
                type: 'error'
              })
            }
          })
          .catch(failResponse => {
            _this.$notify({
              title: '您已评价过啦',
              type: 'warning'
            })
          })
      } else {
        this.value3 = null
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      }
    },
    postUp (id, up, index) {
      if (this.loginIn) {
        let _this = this
        var params = new URLSearchParams()
        // // console.log(_this.$refs.up[index].className)
        params.append('id', id)
        params.append('up', up + 1)
        axios.post(_this.$store.state.HOST + '/api/postUp', params)
          .then(response => {
            if (response.data.code === 1) {
              _this.$refs.up[index].children[0].style.color = '#2796dd'
              _this.getAllComment()
            }
          })
          .catch(failResponse => {})
      } else {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      }
    },
    // 提交评论
    postComment () {
      if (this.loginIn) {
        // 0 代表歌曲， 1 代表歌单
        let _this = this
        var params = new URLSearchParams()
        params.append('userId', _this.userId)
        params.append('type', 1)
        params.append('songListId', _this.index)
        params.append('comtent', _this.textarea)
        axios.post(_this.$store.state.HOST + '/api/commentList', params)
          .then(response => {
            if (response.data.code === 1) {
              _this.textarea = ''
              _this.getAllComment()
              _this.count += 1
              _this.$notify({
                title: '评论成功',
                type: 'success'
              })
            } else {
              _this.$notify({
                title: '评论失败',
                type: 'error'
              })
            }
          })
          .catch(failResponse => {})
      } else {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      }
    },
    // 获取所有评论
    getAllComment () {
      let _this = this
      axios.get(_this.$store.state.HOST + '/songsListComment')
        .then(function (response) {
          console.log(response.data)
          _this.$store.commit('setSongslistComment', response.data)
          window.sessionStorage.setItem('songslistComment', JSON.stringify(response.data))
          // 获取用户评论
          _this.getComment()
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 获取用户评论信息
    getComment () {
      this.songsComment = []
      for (let i = 0, len = this.songslistComment.length; i < len; i++) {
        if (this.songslistComment[i].songListId === this.index && this.songslistComment[i].type === 1) {
          this.getUsers(this.songslistComment[i].userId)
          this.songsComment.push(this.songslistComment[i])
        }
      }
    },
    // 获取评论用户的昵称和头像
    getUsers (id) {
      let _this = this
      axios.get(_this.$store.state.HOST + '/commentOfConsumer?id=' + id)
        .then(function (response) {
          _this.userPic.push(response.data[0].avator)
          _this.userName.push(response.data[0].username)
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
  .album-body {
    width: 100%;
    display: inline-block;
  }
  /*歌单左侧*/
  .album-slide {
    float: left;
    width: 400px;
  }
  /*歌单图像*/
  .album-img {
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
  .album-img img {
    width: 100%;
  }
  /*歌单信息*/
  .album-info {
    color: black;
    font-size: 20px;
    font-weight: 500;
    margin-top: -80px;
    padding: 30px 50px;
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
  /*歌单打分*/
  .album-score {
    margin-left: 400px;
  }
  .album-score p {
    margin: 30px 0;
  }
  .album-score span {
    position: relative;
    top: -75px;
    left: 150px;
    font-size: 60px;
    color: black;
  }
  .push-score {
    position: relative;
    top: -100px;
  }
  /*歌曲列表*/
  .songs-body {
    background-color: white;
    border-radius: 12px;
    padding: 20px 40px;
    min-width: 700px;
  }
  .songs-title {
    height: 80px;
    line-height: 60px;
    font-size: 20px;
    font-weight: 600;
    color: black;
    text-align: center;
  }
  .listOfSongs {
    width: 100%;
    padding-bottom: 50px;
  }
  .listOfSongs li {
    border-bottom: solid 1px rgba(0, 0, 0, 0.2);
    display: block;
    height: 40px;
    line-height: 40px;
    text-indent: 20px;
    cursor: pointer;
  }
  .songs-head {
    padding-left: 30px;
    font-size: 18px;
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
  .item-n {
    width: 30%;
  }
  .item-s {
    width: 20%;
  }
  .item-z {
    width: 45%;
  }
  /*评论*/
  .comment {
    position: relative;
    width: 100%;
  }
  .comment-hd {
    width: 65px;
    position: absolute;
  }
  .comment-hd img {
    width: 100%;
  }
  .comment-input {
    margin-left: 80px;
    position: relative;
    width: 90%;
  }
  .sub-btn{
    margin-top: 10px;
    margin-left: 90%;
    right: 10px;
  }
  /*热门评论*/
  .popular {
    width: 100%;
  }
  .popular li{
    border-bottom: solid 1px rgba(0, 0, 0, 0.2);
    padding: 15px;
    display: flex;
  }
  .popular-img {
    width: 50px;
  }
  .popular-img img {
    width: 100%;
  }
  .popular-body {
    flex: 1;
  }
  .popular-content {
    padding: 0 20px 5px 20px;
  }
  .popular-msg {
    margin-top: 10px;
    padding: 0 20px 0 20px;
    display: flex;
    justify-content: space-between;
  }
  .icon {
    width: 30px;
    height: 30px;
    fill: currentColor;
  }

</style>
