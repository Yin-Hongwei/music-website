<template>
  <div class='song-lyric'>
    <h1 class='lyric-title'>歌词</h1>
    <transition-group name='lyric-fade'>
      <!--有歌词-->
      <ul :style='{top:lrcTop}' class='has-lyric' v-if='lyricArr.length' key='has-lyric'>
        <li v-for='(item, index) in lyricArr' :key='index'>
          {{ item[1] }}
        </li>
      </ul>
      <!--没歌词-->
      <div v-else class='no-lyric' key='no-lyric'>
        <span>暂无歌词</span>
      </div>
    </transition-group>
    <comment :playId='songId' :type='0'></comment>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '../mixins'
import Comment from '../components/Comment'
import { parseLyric } from '../utils'

export default {
  name: 'Lyric',
  mixins: [mixin],
  components: {
    Comment
  },
  data () {
    return {
      lrcTop: '200px', // 歌词滑动
      showLrc: false, // 切换唱片和歌词
      lyricArr: [] // 当前歌曲的歌词
    }
  },
  computed: {
    ...mapGetters([
      'curTime',
      'songId', // 歌曲ID
      'lyric', // 歌词
      'currentPlayList', // 存放的音乐
      'currentPlayIndex' // 当前歌曲在歌曲列表的位置
    ])
  },
  watch: {
    songId () {
      this.lyricArr = parseLyric(this.currentPlayList[this.currentPlayIndex].lyric)
    },
    // 播放时间的开始和结束
    curTime () {
      // 处理歌词位置及颜色
      if (this.lyricArr.length !== 0) {
        for (let i = 0; i < this.lyricArr.length; i++) {
          if (this.curTime >= this.lyricArr[i][0]) {
            for (let j = 0; j < this.lyricArr.length; j++) {
              document.querySelectorAll('.has-lyric li')[j].style.color = '#000'
              document.querySelectorAll('.has-lyric li')[j].style.fontSize = '15px'
            }
            if (i >= 0) {
              document.querySelectorAll('.has-lyric li')[i].style.color = '#95d2f6'
              document.querySelectorAll('.has-lyric li')[i].style.fontSize = '25px'
            }
          }
        }
      }
    }
  },
  created () {
    this.lyricArr = this.lyric ? parseLyric(this.lyric) : []
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/lyric.scss';
</style>
