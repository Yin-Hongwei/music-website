<template>
  <div class="song-container">
    <div class="song-pic">
      <img :src="songPic" />
    </div>
    <ul class="song-info">
      <li>歌手：{{ singerName }}</li>
      <li>歌曲：{{ songTitle }}</li>
    </ul>
  </div>
  <div class="container">
    <div class="lyric-container">
      <div class="song-lyric">
        <transition-group name="lyric-fade">
          <!--有歌词-->
          <ul
            :style="{ top: lrcTop }"
            class="has-lyric"
            v-if="lyricArr.length"
            key="has-lyric"
          >
            <li v-for="(item, index) in lyricArr" :key="index">
              {{ item[1] }}
            </li>
          </ul>
          <!--没歌词-->
          <div v-else class="no-lyric" key="no-lyric">
            <span>暂无歌词</span>
          </div>
        </transition-group>
      </div>
      <comment :playId="songId" :type="0"></comment>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mixin from "../mixins";
import Comment from "../components/Comment";
import { parseLyric } from "../utils";

export default {
  name: "Lyric",
  mixins: [mixin],
  components: {
    Comment,
  },
  data() {
    return {
      lrcTop: "80px", // 歌词滑动
      lyricArr: [], // 当前歌曲的歌词
    };
  },
  computed: {
    ...mapGetters([
      "curTime",
      "songTitle", // 歌名
      "singerName", // 歌手名
      "songPic", // 歌曲图片
      "songId", // 歌曲ID
      "lyric", // 歌词
      "currentPlayList", // 存放的音乐
      "currentPlayIndex", // 当前歌曲在歌曲列表的位置
    ]),
  },
  watch: {
    songId() {
      this.lyricArr = parseLyric(
        this.currentPlayList[this.currentPlayIndex].lyric
      );
    },
    // 播放时间的开始和结束
    curTime() {
      // 处理歌词位置及颜色
      if (this.lyricArr.length !== 0) {
        for (let i = 0; i < this.lyricArr.length; i++) {
          if (this.curTime >= this.lyricArr[i][0]) {
            for (let j = 0; j < this.lyricArr.length; j++) {
              document.querySelectorAll(".has-lyric li")[j].style.color =
                "#000";
              document.querySelectorAll(".has-lyric li")[j].style.fontSize =
                "14px";
            }
            if (i >= 0) {
              this.lrcTop = -i * 30 + 50 + "px";
              document.querySelectorAll(".has-lyric li")[i].style.color =
                "#95d2f6";
              document.querySelectorAll(".has-lyric li")[i].style.fontSize =
                "18px";
            }
          }
        }
      }
    },
  },
  created() {
    this.lyricArr = this.lyric ? parseLyric(this.lyric) : [];
  },
};
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.container {
  padding-top: $header-height + 30px;
}

.song-container {
  position: fixed;
  top: 120px;
  left: 50px;
  .song-pic {
    height: 300px;
    overflow: hidden;
    border: 4px solid white;
    border-radius: 12px;
    img {
      height: 100%;
    }
  }
  .song-info {
    width: 100%;
    li {
      width: 100%;
      line-height: 40px;
      font-size: 18px;
      text-align: center;
    }
  }
}

.lyric-container {
  margin: 0 150px 0px 400px;
  border-radius: 12px;
  padding: 80px 20px 30px 20px;
  font-family: $font-family;
  background-color: $color-white;
  .song-lyric {
    position: relative;
    min-height: 300px;
    padding: 30px 0;
    overflow: auto;
    .has-lyric {
      position: absolute;
      transition: all 1s;
      li {
        width: 100%;
        height: 40px;
        text-align: center;
        font-size: 14px;
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
