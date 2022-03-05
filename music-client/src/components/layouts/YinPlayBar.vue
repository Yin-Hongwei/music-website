<template>
  <div class="play-bar" :class="{ show: !toggle }">
    <div class="fold" :class="{ turn: toggle }">
      <yin-icon :icon="iconList.ZHEDIE" @click="toggle=!toggle"></yin-icon>
    </div>
    <!--播放进度-->
    <el-slider class="progress" v-model="nowTime" @change="changeTime" size="small"></el-slider>
    <div class="control-box">
      <div class="info-box">
        <!--歌曲图片-->
        <div class="song-bar-img" @click="goPlayerPage">
          <img :src="songPic" alt="">
        </div>
        <!--播放开始结束时间-->
        <div v-if="songId">
          <div class="song-info">{{ this.songTitle }} - {{ this.singerName }}</div>
          <div class="time-info">{{ startTime }} / {{ endTime }}</div>
        </div>
      </div>
      <div class="song-ctr">
        <yin-icon :icon="playStateList[playStateIndex]" @click="changePlayState"></yin-icon>
        <!--上一首-->
        <yin-icon :icon="iconList.SHANGYISHOU" @click="prev"></yin-icon>
        <!--播放-->
        <yin-icon :icon="playBtnIcon" @click="togglePlay"></yin-icon>
        <!--下一首-->
        <yin-icon :icon="iconList.XIAYISHOU" @click="next"></yin-icon>
        <!--音量-->
        <el-dropdown trigger="click">
          <yin-icon v-if="volume !== 0" :icon="iconList.YINLIANG"></yin-icon>
          <yin-icon v-else :icon="iconList.JINGYIN"></yin-icon>
          <template #dropdown>
            <el-dropdown-menu>
              <el-slider style="height: 150px; margin: 10px 0;" v-model="volume" :vertical="true"></el-slider>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div class="song-ctr song-edit">
        <!--收藏-->
        <yin-icon :class="{ active: isCollection }" :icon="iconList.XIHUAN" @click="collection"></yin-icon>
        <!--下载-->
        <yin-icon :icon="iconList.XIAZAI" @click="downloadMusic"></yin-icon>
        <!--歌曲列表-->
        <yin-icon :icon="iconList.LIEBIAO" @click="changeAside"></yin-icon>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue"
import { mapGetters } from "vuex"
import mixin from "@/mixins/mixin"
import YinIcon from "./YinIcon.vue"
import { HttpManager } from "@/api"
import { formatSeconds } from "@/utils"
import { ICON, LYRIC } from "@/enums"

export default defineComponent({
  setup() {
    const { routerManager, playMusic, checkStatus } = mixin()
    return { playMusic, routerManager, checkStatus }
  },
  // mixins: [mixin],
  data () {
    return {
      startTime: "00:00",
      endTime: "00:00",
      nowTime: 0, // 进度条的位置
      toggle: true,
      volume: 50,
      playState: ICON.XUNHUAN,
      playStateList: [ICON.XUNHUAN, ICON.LUANXU],
      playStateIndex: 0,
      iconList: {
        XIAZAI: ICON.XIAZAI,
        ZHEDIE: ICON.ZHEDIE,
        SHANGYISHOU: ICON.SHANGYISHOU,
        XIAYISHOU: ICON.XIAYISHOU,
        YINLIANG: ICON.YINLIANG1,
        JINGYIN: ICON.JINGYIN,
        LIEBIAO: ICON.LIEBIAO,
        XIHUAN: ICON.XIHUAN
      },
    }
  },
  components: {
    YinIcon
  },
  computed: {
    ...mapGetters([
      "userId",
      "isPlay", // 播放状态
      "playBtnIcon", // 播放状态的图标
      "songId", // 音乐id
      "songUrl", // 音乐地址
      "songTitle", // 歌名
      "singerName", // 歌手名
      "songPic", // 歌曲图片
      "curTime", // 当前音乐的播放位置
      "duration", // 音乐时长
      "currentPlayList",
      "currentPlayIndex", // 当前歌曲在歌曲列表的位置
      "showAside", // 是否显示侧边栏
      "autoNext", // 用于触发自动播放下一首
      "isCollection" // 是否收藏
    ])
  },
  watch: {
    // 切换播放状态的图标
    isPlay (value) {
      this.$store.commit("setPlayBtnIcon", value ? ICON.ZANTING : ICON.BOFANG)
    },
    volume () {
      this.$store.commit("setVolume", this.volume / 100)
    },
    // 播放时间的开始和结束
    curTime () {
      this.startTime = formatSeconds(this.curTime)
      this.endTime = formatSeconds(this.duration)
      // 移动进度条
      this.nowTime = (this.curTime / this.duration) * 100
    },
    // 自动播放下一首
    autoNext () {
      this.next()
    }
  },
  methods: {
    // 下载
    async downloadMusic () {
      const result = await HttpManager.downloadMusic(this.songUrl) as { data: any }
      const eleLink = document.createElement("a")
      eleLink.download = `${this.singerName}-${this.songTitle}.mp3`
      eleLink.style.display = "none"
      // 字符内容转变成blob地址
      const blob = new Blob([result.data])
      eleLink.href = URL.createObjectURL(blob)
      document.body.appendChild(eleLink) // 触发点击
      eleLink.click()
      document.body.removeChild(eleLink) // 移除
    },
    changeAside () {
      this.$store.commit("setShowAside", !this.showAside)
    },
    // 控制音乐播放 / 暂停
    togglePlay () {
      this.$store.commit("setIsPlay", this.isPlay ? false : true)
    },
    changeTime () {
      this.$store.commit("setChangeTime", this.duration * (this.nowTime * 0.01))
    },
    changePlayState() {
      this.playStateIndex = this.playStateIndex >= this.playStateList.length - 1 ? 0 : ++this.playStateIndex
      this.playState = this.playStateList[this.playStateIndex]
    },
    // 上一首
    prev () {
      if (this.playState === ICON.LUANXU) {
          let playIndex = Math.floor(Math.random() * this.currentPlayList.length)
          playIndex = playIndex === this.currentPlayIndex ? playIndex + 1 : playIndex
          this.$store.commit("setCurrentPlayIndex", playIndex)
          this.toPlay(this.currentPlayList[playIndex].url)
      } else if (this.currentPlayIndex !== -1 && this.currentPlayList.length > 1) {
        if (this.currentPlayIndex > 0) {
          this.$store.commit("setCurrentPlayIndex", this.currentPlayIndex - 1)
          this.toPlay(this.currentPlayList[this.currentPlayIndex].url)
        } else {
          this.$store.commit("setCurrentPlayIndex", this.currentPlayList.length - 1)
          this.toPlay(this.currentPlayList[this.currentPlayIndex].url)
        }
      }
    },
    // 下一首
    next () {
      if (this.playState === ICON.LUANXU) {
        let playIndex = Math.floor(Math.random() * this.currentPlayList.length)
        playIndex = playIndex === this.currentPlayIndex ? playIndex + 1 : playIndex
        this.$store.commit("setCurrentPlayIndex", playIndex)
        this.toPlay(this.currentPlayList[playIndex].url)
      } else if (this.currentPlayIndex !== -1 && this.currentPlayList.length > 1) {
        if (this.currentPlayIndex < this.currentPlayList.length - 1) {
          this.$store.commit("setCurrentPlayIndex", this.currentPlayIndex + 1)
          this.toPlay(this.currentPlayList[this.currentPlayIndex].url)
        } else {
          this.$store.commit("setCurrentPlayIndex", 0)
          this.toPlay(this.currentPlayList[0].url)
        }
      }
    },
    // 选中播放
    toPlay (url) {
      if (url && url !== this.songUrl) {
        const song = this.currentPlayList[this.currentPlayIndex]
        this.playMusic({ 
          id: song.id, 
          url, 
          pic: song.pic, 
          index: this.currentPlayIndex,
          name: song.name, 
          lyric: song.lyric, 
          currentSongList: this.currentPlayList 
        })
      }
    },
    goPlayerPage () {
      this.routerManager(LYRIC, { path: `${LYRIC}/${this.songId}` })
    },
    async collection () {
      if (!this.checkStatus()) return
      
      const params = new URLSearchParams()
      params.append("userId", this.userId)
      params.append("type", "0") // 0 代表歌曲， 1 代表歌单
      params.append("songId", this.songId)

      const result = await HttpManager.setCollection(params) as { code: number };
      if (result.code === 1) {
        (this as any).$store.commit("setIsCollection", true);
        (this as any).$notify({
          title: "收藏成功",
          type: "success",
        })
      } else if (result.code === 2) {
        (this as any).$notify({
          title: "已收藏",
          type: "warning"
        })
      } else {
        (this as any).$notify.error({
          title: "收藏失败",
          showClose: false,
        })
      }
    }
  }
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/yin-play-bar.scss";
</style>
