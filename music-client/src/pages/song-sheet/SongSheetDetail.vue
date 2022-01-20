<template>
  <div class="song-sheet-detail">
    <div class="album-slide">
      <div class="album-img">
        <img :src="attachImageUrl(songDetails.pic)" alt="">
      </div>
      <div class="album-info">
        <h2>简介：</h2>
        <span>
          {{songDetails.introduction}}
        </span>
      </div>
    </div>
    <div class="song-list">
      <div class="album-title">
        <p>{{songDetails.title}}</p>
      </div>
      <!--评分-->
      <div class="album-score">
        <div>
          <h3>歌单评分：</h3>
          <div>
            <el-rate v-model="rank" disabled></el-rate>
          </div>
        </div>
        <span>{{rank * 2}}</span>
        <div>
          <h3>评价：</h3>
          <div @click="pushValue()">
            <el-rate v-model="score" show-text allow-half></el-rate>
          </div>
        </div>
      </div>
      <!--歌曲-->
      <div class="songs-body">
        <song-list :songList="currentSongList">
          <template slot="title">歌单</template>
        </song-list>
        <comment :playId="songListId" :type="1"></comment>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '../../mixins'
import SongList from '../../components/SongList'
import Comment from '../../components/Comment'
import { HttpManager } from '../../api'

export default {
  name: 'song-sheet-detail',
  mixins: [mixin],
  components: {
    SongList,
    Comment
  },
  data () {
    return {
      currentSongList: [], // 存放的音乐
      count: 0, // 点赞数
      songListId: '', // 歌单 ID
      score: 0,
      rank: 0
    }
  },
  computed: {
    ...mapGetters([
      'token', // 登录标识
      'songDetails', // 单个歌单信息
      'userPic' // 用户头像
    ])
  },
  created () {
    this.songListId = this.songDetails.id // 给歌单ID赋值
    this.getRank(this.songListId) // 获取评分
    this.getSongId(this.songListId) // 获取歌单里面的歌曲ID
  },
  methods: {
    // 收集歌单里面的歌曲
    getSongId (id) {
      HttpManager.getListSongOfSongId(id)
        .then(res => {
          // 获取歌单里的歌曲信息
          for (let item of res) {
            this.getSongList(item.songId)
          }
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 获取单里的歌曲
    getSongList (id) {
      HttpManager.getSongOfId(id)
        .then(res => {
          this.currentSongList.push(res[0])
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 获取评分
    getRank (id) {
      HttpManager.getRankOfSongListId(id)
        .then(res => {
          this.rank = res / 2
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 提交评分
    pushValue () {
      if (!this.token) {
        this.score = 0
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
        return
      }

      let params = new URLSearchParams()
      params.append('songListId', this.songListId)
      params.append('consumerId', this.userId)
      params.append('score', this.score * 2)
      HttpManager.setRank(params)
        .then(res => {
          if (res.code === 1) {
            this.getRank(this.songListId)
            this.$notify({
              title: '评分成功',
              type: 'success'
            })
          } else {
            this.$notify({
              title: '评分失败',
              type: 'error'
            })
          }
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/css/song-sheet-detail.scss';
</style>
