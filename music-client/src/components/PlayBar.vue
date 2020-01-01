<template>
  <div class="play-bar" :class="{show:!toggle}">
    <div @click="toggle=!toggle" class="item-up" :class="{turn:toggle}">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icon-jiantou-xia-cuxiantiao"></use>
      </svg>
    </div>
    <div class="kongjian" >
      <!--上一首-->
      <div class="item" @click="prev">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-ziyuanldpi"></use>
        </svg>
      </div>
      <!--播放-->
      <div class="item" @click="togglePlay">
        <svg class="icon" aria-hidden="true">
          <use :xlink:href="playButtonUrl"></use>
        </svg>
      </div>
      <!--下一首-->
      <div class="item" @click="next">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-ziyuanldpi1"></use>
        </svg>
      </div>
      <!--歌曲图片-->
      <div class="item-img" @click="goPlayerPage()">
         <img :src=picUrl alt="">
      </div>
      <!--播放进度-->
      <div class="playing-speed">
        <!--播放开始时间-->
        <div class="current-time">{{ nowTime }}</div>
        <div class="progress-box">
          <div class="item-song-title">
            <div>{{this.title}}</div>
            <div>{{this.artist}}</div>
          </div>
          <div ref="progress" class="progress" @mousemove="mousemove">
            <!--进度条-->
            <div ref="bg" class="bg" @click="updatemove">
              <div ref="curProgress" class="cur-progress" :style="{width: curLength+'%'}"></div>
            </div>
              <!--进度条 end -->
            <!--拖动的点点-->
            <div ref="idot" class="idot" :style="{left: curLength+'%'}" @mousedown="mousedown" @mouseup="mouseup"></div>
            <!--拖动的点点 end -->
          </div>
        </div>
        <!--播放结束时间-->
        <div class="left-time">{{ songTime }}</div>
      </div>
      <!--添加-->
      <div class="item" @click="collection()">
        <el-button plain style="border: 0;">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-xihuan-shi"></use>
          </svg>
        </el-button>
      </div>
      <!--下载-->
      <div class="item" @click="down">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-xiazai"></use>
        </svg>
      </div>
      <!--歌曲列表-->
      <div class="item" @click="changeAside">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-liebiao"></use>
        </svg>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { mapGetters } from 'vuex'
import { mixin } from '../mixins'

export default {
  name: 'play-bar',
  mixins: [mixin],
  data () {
    return {
      tag: false,
      nowTime: '00:00',
      songTime: '00:00',
      curLength: 0, // 进度条的位置
      progressLength: 0, // 进度条长度
      mouseStartX: 0, // 拖拽开始位置
      toggle: true
    }
  },
  computed: {
    ...mapGetters([
      'loginIn',
      'userId',
      'isPlay', // 播放状态
      'playButtonUrl', // 播放状态的图标
      'id', // 音乐id
      'url',
      'title', // 歌名
      'artist', // 歌手名
      'picUrl', // 歌曲图片
      'curTime', // 当前音乐的播放位置
      'duration', // 音乐时长
      'listOfSongs', // 当前歌单列表
      'listIndex', // 当前歌曲在歌曲列表的位置
      'showAside', // 是否显示侧边栏
      'autoNext' // 用于触发自动播放下一首
    ])
  },
  watch: {
    // 切换播放状态的图标
    isPlay (val) {
      if (val) {
        this.$store.commit('setPlayButtonUrl', '#icon-zanting')
        window.sessionStorage.setItem('playButtonUrl', JSON.stringify('#icon-zanting'))
      } else {
        this.$store.commit('setPlayButtonUrl', '#icon-bofang')
        window.sessionStorage.setItem('playButtonUrl', JSON.stringify('#icon-bofang'))
      }
    },
    // 播放时间的开始和结束
    curTime () {
      this.nowTime = this.formatSeconds(this.curTime)
      this.songTime = this.formatSeconds(this.duration)
      // 移动进度条
      this.curLength = (this.curTime / this.duration) * 100
      // 处理歌词位置及颜色
    },
    // 自动播放下一首
    autoNext () {
      this.next()
    }
  },
  mounted () {
    this.progressLength = this.$refs.progress.getBoundingClientRect().width
  },
  methods: {
    // 下载
    down () {
      let _this = this
      axios({
        method: 'get',
        url: _this.url,
        responseType: 'blob'
      })
        .then(res => {
          let content = res.data
          var eleLink = document.createElement('a')
          eleLink.download = `${_this.artist}-${_this.title}.mp3`
          eleLink.style.display = 'none'
          // 字符内容转变成blob地址
          var blob = new Blob([content])
          eleLink.href = URL.createObjectURL(blob)
          // 触发点击
          document.body.appendChild(eleLink)
          eleLink.click()
          // 然后移除
          document.body.removeChild(eleLink)
        })
        .catch(failResponse => {})
    },
    changeAside () {
      let temp = !this.showAside
      this.$store.commit('setShowAside', temp)
      window.sessionStorage.setItem('showAside', JSON.stringify(temp))
    },
    // 控制音乐播放/暂停
    togglePlay () {
      if (this.isPlay) {
        this.$store.commit('setIsPlay', false)
        window.sessionStorage.setItem('isPlay', JSON.stringify(false))
      } else {
        this.$store.commit('setIsPlay', true)
        window.sessionStorage.setItem('isPlay', JSON.stringify(true))
      }
    },
    //  解析播放时间
    formatSeconds (value) {
      let theTime = parseInt(value)
      let theTime1 = 0
      let theTime2 = 0
      if (theTime > 60) {
        theTime1 = parseInt(theTime / 60) // 分
        theTime = parseInt(theTime % 60) // 秒
        // 是否超过一个小时
        if (theTime1 > 60) {
          theTime2 = parseInt(theTime1 / 60) // 小时
          theTime1 = 60 // 分
        }
      }
      // 多少秒
      if (parseInt(theTime) < 10) {
        var result = '0:0' + parseInt(theTime)
      } else {
        result = '0:' + parseInt(theTime)
      }
      // 多少分钟时
      if (theTime1 > 0) {
        if (parseInt(theTime) < 10) {
          result = '0' + parseInt(theTime)
        } else {
          result = parseInt(theTime)
        }
        result = parseInt(theTime1) + ':' + result
      }
      // 多少小时时
      if (theTime2 > 0) {
        if (parseInt(theTime) < 10) {
          result = '0' + parseInt(theTime)
        } else {
          result = parseInt(theTime)
        }
        result = parseInt(theTime2) + ':' + parseInt(theTime1) + ':' + result
      }
      return result
    },
    //  拖拽开始
    mousedown (e) {
      this.mouseStartX = e.clientX
      this.tag = true
    },
    mouseup () {
      this.tag = false
    },
    //  拖拽ing
    mousemove (e) {
      if (!this.duration) {
        return false
      }
      if (this.tag) {
        let movementX = e.clientX - this.mouseStartX
        let curLength = this.$refs.curProgress.getBoundingClientRect().width
        //  计算出百分比
        this.progressLength = this.$refs.progress.getBoundingClientRect().width
        let newPercent = ((curLength + movementX) / this.progressLength) * 100
        if (newPercent > 100) {
          newPercent = 100
        }
        this.curLength = newPercent
        this.mouseStartX = e.clientX
        //  根据百分比推出对应的播放时间
        this.changeTime(newPercent)
      }
    },
    // 更改歌曲进度
    changeTime (percent) {
      let newCurTime = this.duration * (percent * 0.01)
      this.$store.commit('setChangeTime', newCurTime)
      window.sessionStorage.setItem('changeTime', JSON.stringify(newCurTime))
    },
    updatemove (e) {
      if (!this.tag) {
        let curLength = this.$refs.bg.offsetLeft
        this.progressLength = this.$refs.progress.getBoundingClientRect().width
        let newPercent = ((e.clientX - curLength) / this.progressLength) * 100
        if (newPercent < 0) {
          newPercent = 0
        } else if (newPercent > 100) {
          newPercent = 100
        }
        this.curLength = newPercent
        this.changeTime(newPercent)
      }
    },
    // 上一首
    prev () {
      if (this.listIndex !== -1 && this.listOfSongs.length > 1) {
        if (this.listIndex > 0) {
          this.$store.commit('setListIndex', this.listIndex - 1)
          window.sessionStorage.setItem('listIndex', JSON.stringify(this.listIndex - 1))
          this.toPlay(this.listOfSongs[this.listIndex].url)
        } else {
          this.$store.commit('setListIndex', this.listOfSongs.length - 1)
          window.sessionStorage.setItem('listIndex', JSON.stringify(this.listOfSongs.length - 1))
          this.toPlay(this.listOfSongs[this.listIndex].url)
        }
      }
    },
    // 下一首
    next () {
      if (this.listIndex !== -1 && this.listOfSongs.length > 1) {
        if (this.listIndex < this.listOfSongs.length - 1) {
          this.$store.commit('setListIndex', this.listIndex + 1)
          window.sessionStorage.setItem('listIndex', JSON.stringify(this.listIndex - 1))
          this.toPlay(this.listOfSongs[this.listIndex].url)
        } else {
          this.$store.commit('setListIndex', 0)
          window.sessionStorage.setItem('listIndex', JSON.stringify(0))
          this.toPlay(this.listOfSongs[0].url)
        }
      }
    },
    updateMsg () {
      this.$store.commit('setId', this.listOfSongs[this.listIndex].id)
      window.sessionStorage.setItem('id', JSON.stringify(this.listOfSongs[this.listIndex].id))
      this.$store.commit('setpicUrl', this.$store.state.HOST + this.listOfSongs[this.listIndex].pic)
      window.sessionStorage.setItem('picUrl', JSON.stringify(this.$store.state.HOST + this.listOfSongs[this.listIndex].pic))
      this.$store.commit('setTitle', this.replaceFName(this.listOfSongs[this.listIndex].name))
      window.sessionStorage.setItem('title', JSON.stringify(this.replaceFName(this.listOfSongs[this.listIndex].name)))
      this.$store.commit('setArtist', this.replaceLName(this.listOfSongs[this.listIndex].name))
      window.sessionStorage.setItem('artist', JSON.stringify(this.replaceLName(this.listOfSongs[this.listIndex].name)))
      this.$store.commit('setLyric', this.parseLyric(this.listOfSongs[this.listIndex].lyric))
      window.sessionStorage.setItem('lyric', JSON.stringify(this.parseLyric(this.listOfSongs[this.listIndex].lyric)))
    },
    // 选中播放
    toPlay (url) {
      if (url && url !== this.url) {
        this.updateMsg()
        this.$store.commit('setUrl', this.$store.state.HOST + url)
        window.sessionStorage.setItem('url', JSON.stringify(this.$store.state.HOST + url))
      }
    },
    goPlayerPage () {
      this.$router.push({path: `/player-page/${this.id}`})
    },
    collection () {
      if (this.loginIn) {
        // 0 代表歌曲， 1 代表歌单
        let _this = this
        var params = new URLSearchParams()
        params.append('userId', _this.userId)
        params.append('type', 0)
        params.append('songId', _this.id || '')
        axios.post(`${_this.$store.state.HOST}/api/collectionList`, params)
          .then(res => {
            if (res.data.code === 1) {
              this.$notify({
                title: '收藏成功',
                type: 'success'
              })
            } else if (res.data.code === 2) {
              this.$notify({
                title: '已收藏',
                type: 'warning'
              })
            } else {
              this.$notify.error({
                title: '收藏失败',
                showClose: false
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
    }
  }
}
</script>

<style scoped>
.play-bar{
  position: fixed;
  bottom: 0;
  width: 100%;
  transition: all .5s;
  box-shadow: 0 0 10px black;
}

.item-up {
  position: absolute;
  bottom: 70px;
  left: 20px;
  cursor: pointer;
}

.kongjian {
  bottom: 0;
  height: 60px;
  width: 100%;
  background-color: #fefefe;
  display: flex;
  flex-wrap: nowrap;
  justify-content: center;
  align-items: center;
  min-width: 1000px;
}

.show {
  bottom: -60px;
}

.turn{
  transform: rotate(180deg);
}

.item{
  width: 80px;
  height: 50px;
  line-height: 60px;
  text-align: center;
  cursor: pointer;
}

.item-img {
  width: 50px;
  height: 50px;
}

.item-img img {
  width: 100%;
}

.item-song-title {
  display: flex;
  justify-content: space-between;
  height: 20px;
  line-height: 10px;
}

.playing-speed {
  height: 50px;
  width: 600px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.playing-speed .current-time,
.playing-speed .left-time {
  width: 70px;
  text-align: center;
  font-size: 13px;
  color:black;
  font-weight: 500;
  top: -10px;
}

.playing-speed .progress-box {
  flex: 1;
}

.playing-speed .progress-box .progress {
  width: 100%;
  background: #a9dbf9;
  height: 6px;
}

.bg {
  height: 100%;
}

.cur-progress {
  height: 100%;
  background: #30a4fc;
}

.playing-speed>.progress-box .idot {
  width: 16px;
  height: 16px;
  position: relative;
  border-radius: 50%;
  background-color: black;
  top: -11px;
  vertical-align: middle;
}

.icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  color: black;
  font-size: 1.5em;
}
</style>
