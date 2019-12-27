<template>
  <div class="album">
    <div class="album-bg"></div>
    <div class="album-body">
      <div class="album-slide">
        <div class="album-img">
          <img :src=attachImageUrl(singers.pic) alt="">
        </div>
        <div class="album-info">
          <h2>简介：</h2>
          <span>
            {{singers.introduction}}
          </span>
        </div>
      </div>
      <div class="album-content">
        <div class="album-title">
          <p>{{singers.title}}</p>
        </div>
        <!--评分-->
        <div class="album-score">
          <div>
            <h3>歌单评分：</h3>
            <div>
              <el-rate v-model="value5" disabled></el-rate>
            </div>
          </div>
          <span>{{value5 * 2}}</span>
          <div>
            <h3>评价：</h3>
            <div @click="pushValue()">
              <el-rate v-model="value3" show-text allow-half></el-rate>
            </div>
          </div>
        </div>
        <!--歌曲-->
        <div class="songs-body">
          <album-content :songList="listOfSongs">
            <template slot="title">歌单</template>
          </album-content>
          <comment :id="songListId" :type="1"></comment>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import AlbumContent from '../components/AlbumContent'
import Comment from '../components/Comment'

export default {
  name: 'song-list-album-page',
  components: {
    AlbumContent,
    Comment
  },
  data () {
    return {
      songLists: [],
      singers: {},
      count: 0, // 点赞数
      songListId: '', // 歌单ID
      value3: 0,
      value5: 0
    }
  },
  computed: {
    ...mapGetters([
      'loginIn', // 登录标识
      'tempList', // 单个歌单信息
      'listOfSongs', // 存放的音乐
      'userId', // 用户ID
      'avator' // 用户头像
    ])
  },
  created () {
    this.songListId = this.tempList.id // 给歌单ID赋值
    this.singers = this.tempList
    this.getSongId() // 获取歌单里面的歌曲ID
    this.getRank(this.songListId) // 获取评分
  },
  mixins: [mixin],
  methods: {
    // 收集歌单里面的歌曲
    getSongId () {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/listSongOfSingers?songListId=${this.songListId}`)
        .then(function (response) {
          // 获取歌单里的歌曲信息
          for (let item of response.data) {
            _this.getSongList(item.songId)
          }
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
      axios.get(`${_this.$store.state.HOST}/listSongsOfSongs?id=${id}`)
        .then(res => {
          _this.songLists.push(res.data[0])
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 获取评分
    getRank (id) {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/api/getRank?songListId=${id}`)
        .then(res => {
          _this.value5 = res.data / 2
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
        params.append('songListId', _this.songListId)
        params.append('consumerId', _this.userId)
        params.append('score', _this.value3 * 2)
        axios.post(`${_this.$store.state.HOST}/api/pushRank`, params)
          .then(res => {
            if (res.data.code === 1) {
              _this.getRank(_this.songListId)
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
  font-size: 20px;
  font-weight: 500;
  margin-top: -80px;
  padding: 30px 50px;
}

.album-info > span {
  color: rgba(0, 0, 0, 0.5);
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
  display: flex;
  align-items: center;
  margin: 50px;
}

.album-score > div {
  margin-left: 100px;
}

.album-score > span{
  font-size: 60px;
}

.album-score h3 {
  margin: 10px 0;
}

/*歌曲列表*/
.songs-body {
  background-color: white;
  border-radius: 12px;
  padding: 0 40px 50px 40px;
  min-width: 700px;
}

</style>
