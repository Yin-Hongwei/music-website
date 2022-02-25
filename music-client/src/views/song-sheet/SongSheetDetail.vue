<template>
  <div class="song-sheet-detail">
    <div class="album-slide">
      <div class="album-img">
        <img :src="attachImageUrl(songDetails.pic)" alt="">
      </div>
      <div class="album-info">
        <h2>简介：</h2>
        <span> {{songDetails.introduction}} </span>
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
        <song-list :songList="currentSongList"></song-list>
        <comment :playId="songListId" :type="1"></comment>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '@/mixins'
import SongList from '@/components/SongList'
import Comment from '@/components/Comment'
import { HttpManager } from '@/api'

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
      songListId: '', // 歌单 ID
      score: 0,
      rank: 0
    }
  },
  computed: {
    ...mapGetters([
      'songDetails', // 单个歌单信息
      'userPic' // 用户头像
    ])
  },
  created () {
    try {
      this.songListId = this.songDetails.id // 给歌单ID赋值
      this.getRank(this.songListId) // 获取评分
      this.getSongId(this.songListId) // 获取歌单里面的歌曲ID
    } catch (error) {
      console.error(error)
    }
  },
  methods: {
    // 收集歌单里面的歌曲
    async getSongId (id) {
      const result = await HttpManager.getListSongOfSongId(id)
      // 获取歌单里的歌曲信息
      for (const item of result) {
        // 获取单里的歌曲
        const resultSong = await HttpManager.getSongOfId(item.songId)
        this.currentSongList.push(resultSong[0])
      }
    },
    // 获取评分
    async getRank (id) {
      const result = await HttpManager.getRankOfSongListId(id)
      this.rank = result / 2
    },
    // 提交评分
    async pushValue () {
      if (!this.checkStatus()) return

      const params = new URLSearchParams()
      params.append('songListId', this.songListId)
      params.append('consumerId', this.userId)
      params.append('score', this.score * 2)

      try {
        const result = await HttpManager.setRank(params)
        if (result.code === 1) {
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
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/css/song-sheet-detail.scss';
</style>
