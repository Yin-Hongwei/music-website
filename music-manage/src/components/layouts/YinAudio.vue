<template>
  <div class="song-audio">
    <audio
      ref="player"
      controls="controls"
      preload="true"
      :src="url"
      @canplay="startPlay"
      @ended="ended"
    ></audio>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'YinAudio',
  computed: {
    ...mapGetters([
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
      console.log(this.$refs.player)
      const player = this.$refs.player
      this.isPlay ? player.play() : player.pause()
    },
    // 获取歌曲链接后准备播放
    startPlay () {
      const player = this.$refs.player
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
audio {
  display: none;
}
</style>
