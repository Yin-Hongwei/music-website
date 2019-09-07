<template>
  <div class="content">
    <div class="song-lyric">
      <h2>歌词</h2>
      <ul v-show="lyr.length" v-bind:style="{top:lrcTop}"  class="lrc">
        <li class="lyric" v-for="(item, index) in lyr" :key="index">
          {{ item[1] }}
        </li>
      </ul>
      <!--没歌词的情况-->
      <ul v-show="!lyr.length" class="no-lyric">
        <span class="no-lrc">暂无歌词</span>
      </ul>
      <h2>评论</h2>
      <div class="comment">
        <div class="comment-hd">
          <img src="../assets/img/user.jpg" alt="">
        </div>
        <el-input class="comment-input" type="textarea" :rows="2" placeholder="请输入内容" v-model="textarea"></el-input>
        <el-button type="primary" class="sub-btn" @click="postComment()">评论</el-button>
      </div>
      <p>精彩评论 共{{count}}条评论</p>
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
                ({{item.up}})
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'
export default {
  name: 'player-page',
  data () {
    return {
      lrcTop: 200 + 'px', // 歌词滑动
      showLrc: false, // 切换唱片和歌词
      lyr: [], // 当前歌曲的歌词
      textarea: '', // 输入评论内容
      count: 0, // 当前歌曲评论总数
      userPic: [], // 保存评论用户头像
      userName: [], // 保存评论用户名字
      songsComment: [] // 当前歌曲的评论
    }
  },
  computed: {
    ...mapGetters([
      'loginIn',
      'curTime',
      'lyric', // 歌词
      'listOfSongs', // 存放的音乐
      'id', // 音乐id
      'listIndex', // 当前歌曲在歌曲列表的位置
      'userId', // 用户ID
      'songslistComment' // 评论列表
    ])
  },
  watch: {
    id: function () {
      this.lyr = this.parseLyric(this.listOfSongs[this.listIndex].lyric)
      this.getSongComment()
      this.count = this.songsComment.length
    },
    // 播放时间的开始和结束
    curTime: function () {
      // 处理歌词位置及颜色
      if (this.lyr.length !== 0) {
        for (var i = 0; i < this.lyr.length; i++) {
          if (this.curTime >= this.lyr[i][0]) {
            for (var j = 0; j < this.lyr.length; j++) {
              document.querySelectorAll('.lrc li')[j].style.color = '#000'
              document.querySelectorAll('.lrc li')[j].style.fontSize = '15px'
            }
            if (i >= 0) {
              // this.lrcTop = -i * 30 + 180 + 'px'
              document.querySelectorAll('.lrc li')[i].style.color = '#95d2f6'
              document.querySelectorAll('.lrc li')[i].style.fontSize = '25px'
            }
          }
        }
      }
    }
  },
  mounted () {
    this.lyr = this.lyric
    this.getAllComment()
    this.count = this.songsComment.length
  },
  mixins: [mixin],
  methods: {
    postComment () {
      if (this.loginIn) {
        // 0 代表歌曲， 1 代表歌单
        let _this = this
        var params = new URLSearchParams()
        params.append('userId', _this.userId)
        params.append('type', 0)
        params.append('songId', _this.id)
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
          }).catch(failResponse => {})
      } else {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      }
    },
    // 点赞
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
    // 获取所有评论
    getAllComment () {
      let _this = this
      axios.get(_this.$store.state.HOST + '/songsListComment')
        .then(function (response) {
          _this.$store.commit('setSongslistComment', response.data)
          window.sessionStorage.setItem('songslistComment', JSON.stringify(response.data))
          _this.getSongComment()
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    getSongComment () {
      this.songsComment = []
      console.log(this.songslistComment)
      for (let i = 0; i < this.songslistComment.length; i++) {
        if (this.songslistComment[i].songId === this.id && this.songslistComment[i].type === 0) {
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
          // console.log('------------评论的用户信息-----------------')
          // console.log(response.data[0])
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
.content {
  padding: 50px 0;
}
.song-lyric {
  margin: auto;
  width: 700px;
  background-color: #ffffff;
  border-radius: 12px;
  padding: 0 20px 50px 20px;
}

.lrc {
  font-size: 18px;
  padding: 30px 0;
  width: 100%;
  text-align: center;
}
.lyric {
  width: 100%;
  height: 40px;
  line-height: 40px;
}
.no-lyric {
  margin: 150px 0;
  width: 100%;
  text-align: center;
}
.no-lrc {
  font-size: 30px;
  text-align: center;
}
  h2 {
    text-align: center;
    height: 50px;
    line-height: 50px;
    border-bottom: 2px solid black;
  }
/*评论*/
.comment {
  position: relative;
  padding: 30px;
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
