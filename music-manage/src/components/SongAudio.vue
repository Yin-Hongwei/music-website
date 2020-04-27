<template>
  <div class="song-audio">
    <audio
      id="player"
      :src="url"
      controls="controls"
      preload="true"
      @canplay="startPlay"
      @ended="ended"
    ></audio>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'song-audio',
  computed: {
    ...mapGetters([
      'id', // 音乐id
      'url', // 音乐链接
      'isPlay' // 播放状态
    ])
  },
  watch: {
    // 监听播放还是暂停
    isPlay: function () {
      this.togglePlay()
    }
  },
  methods: {
    // 开始/暂停
    togglePlay () {
      let player = document.querySelector('#player')
      if (this.isPlay) {
        player.play()
      } else {
        player.pause()
      }
    },
    // 获取歌曲链接后准备播放
    startPlay () {
      let player = document.querySelector('#player')
      //  开始播放
      player.play()
    },
    // 音乐播放结束时触发
    ended () {
      this.isPlay = false
    }
  }
}
</script>

<style>
#player {
  display: none;
}
</style>
