<template>
  <div class="play-bar" :class="{show:!toggle}">
    <div @click="toggle=!toggle" class="item-up" :class="{turn: toggle}">
      <svg class="icon" aria-hidden="true">
        <use :xlink:href="ZHEDIE"></use>
      </svg>
    </div>
    <div class="kongjian" >
      <!--上一首-->
      <div class="item" @click="prev">
        <svg class="icon" aria-hidden="true">
          <use :xlink:href="SHANGYISHOU"></use>
        </svg>
      </div>
      <!--播放-->
      <div class="item" @click="togglePlay">
        <svg class="icon" aria-hidden="true">
          <use :xlink:href="playBtnIcon"></use>
        </svg>
      </div>
      <!--下一首-->
      <div class="item" @click="next">
        <svg class="icon" aria-hidden="true">
          <use :xlink:href="XIAYISHOU"></use>
        </svg>
      </div>
      <!--歌曲图片-->
      <div class="item-img" @click="goPlayerPage">
         <img :src="songPic" alt="">
      </div>
      <!--播放进度-->
      <div class="playing-speed">
        <!--播放开始时间-->
        <div class="current-time">{{ nowTime }}</div>
        <div class="progress-box">
          <div class="item-song-title">
            <div>{{this.songTitle}}</div>
            <div>{{this.singerName}}</div>
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
      <!--音量-->
      <div class="item icon-volume" >
        <svg v-if="volume !== 0" class="icon" aria-hidden="true">
          <use :xlink:href="YINLIANG"></use>
        </svg>
        <svg v-else class="icon" aria-hidden="true">
          <use :xlink:href="JINGYIN"></use>
        </svg>
        <el-slider class="volume" v-model="volume" :vertical="true"></el-slider>
      </div>
      <!--收藏-->
      <div class="item" @click="collection">
        <svg :class="{ active: isCollection }" class="icon" aria-hidden="true">
          <use :xlink:href="XIHUAN"></use>
        </svg>
      </div>
      <!--下载-->
      <div class="item" @click="downloadMusic">
        <svg class="icon" aria-hidden="true">
          <use :xlink:href="XIAZAI"></use>
        </svg>
      </div>
      <!--歌曲列表-->
      <div class="item" @click="changeAside">
        <svg class="icon" aria-hidden="true">
          <use :xlink:href="LIEBIAO"></use>
        </svg>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '../../mixins'
import { HttpManager } from '../../api'
import { formatSeconds } from '../../utils'
import { ICON, LYRIC } from '../../enums'

export default {
  name: 'yin-play-bar',
  mixins: [mixin],
  data () {
    return {
      tag: false,
      nowTime: '00:00',
      songTime: '00:00',
      curLength: 0, // 进度条的位置
      progressLength: 0, // 进度条长度
      mouseStartX: 0, // 拖拽开始位置
      toggle: true,
      volume: 50,
      XIAZAI: ICON.XIAZAI,
      ZHEDIE: ICON.ZHEDIE,
      SHANGYISHOU: ICON.SHANGYISHOU,
      XIAYISHOU: ICON.XIAYISHOU,
      YINLIANG: ICON.YINLIANG1,
      JINGYIN: ICON.JINGYIN,
      LIEBIAO: ICON.LIEBIAO,
      XIHUAN: ICON.XIHUAN
    }
  },
  computed: {
    ...mapGetters([
      'token', // 用户登录状态
      'isPlay', // 播放状态
      'playBtnIcon', // 播放状态的图标
      'songId', // 音乐id
      'songUrl', // 音乐地址
      'songTitle', // 歌名
      'singerName', // 歌手名
      'songPic', // 歌曲图片
      'curTime', // 当前音乐的播放位置
      'duration', // 音乐时长
      'currentPlayList',
      'currentPlayIndex', // 当前歌曲在歌曲列表的位置
      'showAside', // 是否显示侧边栏
      'autoNext', // 用于触发自动播放下一首
      'isCollection' // 是否收藏
    ])
  },
  watch: {
    // 切换播放状态的图标
    isPlay (val) {
      if (val) {
        this.$store.commit('setPlayBtnIcon', ICON.ZANTING)
      } else {
        this.$store.commit('setPlayBtnIcon', ICON.BOFANG)
      }
    },
    volume () {
      this.$store.commit('setVolume', this.volume / 100)
    },
    // 播放时间的开始和结束
    curTime () {
      this.nowTime = formatSeconds(this.curTime)
      this.songTime = formatSeconds(this.duration)
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
    document.querySelector('.icon-volume').addEventListener('click', (e) => {
      document.querySelector('.volume').classList.add('show-volume')
      e.stopPropagation()
    }, false)
    document.querySelector('.volume').addEventListener('click', (e) => {
      e.stopPropagation()
    }, false)
    document.addEventListener('click', () => {
      document.querySelector('.volume').classList.remove('show-volume')
    }, false)
  },
  methods: {
    // 下载
    downloadMusic () {
      HttpManager.downloadMusic(this.songUrl)
        .then(res => {
          let content = res.data
          let eleLink = document.createElement('a')
          eleLink.download = `${this.singerName}-${this.songTitle}.mp3`
          eleLink.style.display = 'none'
          // 字符内容转变成blob地址
          let blob = new Blob([content])
          eleLink.href = URL.createObjectURL(blob)
          // 触发点击
          document.body.appendChild(eleLink)
          eleLink.click()
          // 然后移除
          document.body.removeChild(eleLink)
        })
        .catch(err => {
          console.error(err)
        })
    },
    changeAside () {
      this.$store.commit('setShowAside', !this.showAside)
    },
    // 控制音乐播放 / 暂停
    togglePlay () {
      if (this.isPlay) {
        this.$store.commit('setIsPlay', false)
      } else {
        this.$store.commit('setIsPlay', true)
      }
    },
    // 拖拽开始
    mousedown (e) {
      this.mouseStartX = e.clientX
      this.tag = true
    },
    // 拖拽结束
    mouseup () {
      this.tag = false
    },
    // 拖拽中
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
      if (this.currentPlayIndex !== -1 && this.currentPlayList.length > 1) {
        if (this.currentPlayIndex > 0) {
          this.$store.commit('setCurrentPlayIndex', this.currentPlayIndex - 1)
          this.toPlay(this.currentPlayList[this.currentPlayIndex].url)
        } else {
          this.$store.commit('setCurrentPlayIndex', this.currentPlayList.length - 1)
          this.toPlay(this.currentPlayList[this.currentPlayIndex].url)
        }
      }
    },
    // 下一首
    next () {
      if (this.currentPlayIndex !== -1 && this.currentPlayList.length > 1) {
        if (this.currentPlayIndex < this.currentPlayList.length - 1) {
          this.$store.commit('setCurrentPlayIndex', this.currentPlayIndex + 1)
          this.toPlay(this.currentPlayList[this.currentPlayIndex].url)
        } else {
          this.$store.commit('setCurrentPlayIndex', 0)
          this.toPlay(this.currentPlayList[0].url)
        }
      }
    },
    // 选中播放
    toPlay (url) {
      if (url && url !== this.songUrl) {
        const song = this.currentPlayList[this.currentPlayIndex]
        const id = song.id
        const pic = song.pic
        const name = song.name
        const index = this.currentPlayIndex
        const lyric = song.lyric
        this.playMusic({id, url, pic, index, name, lyric, currentSongList: this.currentPlayList})
      }
    },
    goPlayerPage () {
      this.routerManager(LYRIC, { path: `${LYRIC}/${this.songId}` })
    },
    collection () {
      if (this.token) {
        var params = new URLSearchParams()
        params.append('userId', this.userId)
        params.append('type', 0) // 0 代表歌曲， 1 代表歌单
        params.append('songId', this.songId)
        HttpManager.setCollection(params)
          .then(res => {
            if (res.code === 1) {
              this.$store.commit('setIsCollection', true)
              this.$notify({
                title: '收藏成功',
                type: 'success'
              })
            } else if (res.code === 2) {
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
          .catch(err => {
            console.error(err)
          })
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

<style lang="scss" scoped>
@import '@/assets/css/yin-play-bar.scss';
</style>
