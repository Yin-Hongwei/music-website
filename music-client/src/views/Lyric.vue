<template>
  <div class='song-lyric'>
    <dir class="lyric-container">
      <transition-group name='lyric-fade'>
        <!--有歌词-->
        <ul :style='{ top: lrcTop }' class='has-lyric' v-if='lyricArr.length' key='has-lyric'>
          <li v-for='(item, index) in lyricArr' :key='index'>
            {{ item[1] }}
          </li>
        </ul>
        <!--没歌词-->
        <div v-else class='no-lyric' key='no-lyric'>
          <span>暂无歌词</span>
        </div>
      </transition-group>
    </dir>
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
              // this.lrcTop = -i * 30 + 10 + 'px'
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
@import "@/assets/css/var.scss";
.lyric-container {
  margin: 0;
  padding: 0;
  // height: 60vh;
  // overflow: auto;
  // position: relative;
}
.song-lyric {
  margin: auto;
  padding-top: $header-height;
  width: 700px;
  background-color: $color-white;
  border-radius: 12px;
  padding: 80px 20px 50px 20px;
  font-family: $font-family;

  .has-lyric {
    // position: absolute;
    font-size: 18px;
    padding: 30px 0;
    width: 100%;
    min-height: 170px;
    text-align: center;

    li {
      width: 100%;
      height: 40px;
      line-height: 40px;
    }
  }

  .no-lyric {
    margin: 100px 0;
    width: 100%;
    text-align: center;

    span {
      font-size: 18px;
      text-align: center;
    }
  }
}

.lyric-fade-enter,
.lyric-fade-leave-to {
  transform: translateX(30px);
  opacity: 0;
}

.lyric-fade-enter-active,
.lyric-fade-leave-active {
  transition: all 0.3s ease;
}

</style>
