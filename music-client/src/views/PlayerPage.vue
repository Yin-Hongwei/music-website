<template>
  <div class="content">
    <div class="song-lyric">
      <h2>歌词</h2>
      <transition-group name="lyr-fade">
        <!--有个词-->
        <div v-show="lyr.length" key="has-lyr">
          <ul :style="{top:lrcTop}"  class="lrc">
            <li class="lyric" v-for="(item, index) in lyr" v-bind:key="index">
              {{ item[1] }}
            </li>
          </ul>
        </div>
        <!--没歌词的情况-->
        <div v-show="!lyr.length" class="no-lyric" key="no-lyr">
          <span class="no-lrc">暂无歌词</span>
        </div>
      </transition-group>
      <comment :id="id" :type="0"></comment>
    </div>
  </div>
</template>

<script>
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'
import Comment from '../components/Comment'

export default {
  name: 'player-page',
  components: {
    Comment
  },
  mixins: [mixin],
  data () {
    return {
      lrcTop: '200px', // 歌词滑动
      showLrc: false, // 切换唱片和歌词
      lyr: [] // 当前歌曲的歌词
    }
  },
  computed: {
    ...mapGetters([
      'curTime',
      'id', // 歌曲ID
      'lyric', // 歌词
      'listOfSongs', // 存放的音乐
      'listIndex' // 当前歌曲在歌曲列表的位置
    ])
  },
  watch: {
    id: function () {
      this.lyr = this.parseLyric(this.listOfSongs[this.listIndex].lyric)
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
              document.querySelectorAll('.lrc li')[i].style.color = '#95d2f6'
              document.querySelectorAll('.lrc li')[i].style.fontSize = '25px'
            }
          }
        }
      }
    }
  },
  created () {
    this.lyr = this.lyric
  }
}
</script>

<style scoped>
.content {
  padding: 50px 0;
  flex: 1;
}
.song-lyric {
  margin: auto;
  width: 700px;
  background-color: #ffffff;
  border-radius: 12px;
  padding: 0 20px 50px 20px;
}
.lyr-fade-enter,
.lyr-fade-leave-to {
  transform: translateX(30px);
  opacity: 0;
}
.lyc-fade-enter-active,
.lyc-fade-leave-active {
  transition: all .3s ease;
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
  /* margin: 200px 0;
  width: 100%;
  text-align: center; */
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

</style>
