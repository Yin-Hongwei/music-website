<template>
  <div class="song-sheet-detail">
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
    <div class="song-list">
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
        <song-list :songList="listOfSongs">
          <template slot="title">歌单</template>
        </song-list>
        <comment :playId="songListId" :type="1"></comment>
      </div>
    </div>
  </div>
</template>

<script>
import mixin from '../../mixins'
import SongList from '../../components/SongList'
import Comment from '../../components/Comment'
import { mapGetters } from 'vuex'
import { HttpManager } from '../../api/index'

export default {
  name: 'song-sheet-detail',
  mixins: [mixin],
  components: {
    SongList,
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
      'songDetails', // 单个歌单信息
      'listOfSongs', // 存放的音乐
      'avator' // 用户头像
    ])
  },
  created () {
    this.songListId = this.songDetails.id // 给歌单ID赋值
    this.singers = this.songDetails
    this.getSongId() // 获取歌单里面的歌曲ID
    this.getRank(this.songListId) // 获取评分
  },
  methods: {
    // 收集歌单里面的歌曲
    getSongId () {
      HttpManager.getListSongOfSongId(this.songListId)
        .then(res => {
          // 获取歌单里的歌曲信息
          for (let item of res) {
            this.getSongList(item.songId)
          }
          this.$store.commit('setListOfSongs', this.songLists)
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 获取单里的歌曲
    getSongList (id) {
      HttpManager.getSongOfId(id)
        .then(res => {
          this.songLists.push(res[0])
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 获取评分
    getRank (id) {
      HttpManager.getRankOfSongListId(id)
        .then(res => {
          this.value5 = res / 2
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 提交评分
    pushValue () {
      if (this.loginIn) {
        let params = new URLSearchParams()
        params.append('songListId', this.songListId)
        params.append('consumerId', this.userId)
        params.append('score', this.value3 * 2)
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

<style lang="scss" scoped>
@import '../../assets/css/song-sheet-detail.scss';
</style>
