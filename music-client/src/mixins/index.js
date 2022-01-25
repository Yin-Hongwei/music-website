import { mapGetters } from 'vuex'
import { HOME, SONG_SHEET, SINGER, MY_MUSIC, SETTING, SIGN_IN, SIGN_UP, SIGN_OUT, SEARCH, LYRIC, SONG_SHEET_DETAIL, SINGER_DETAIL, ERROR } from '../enums'

const mixin = {
  computed: {
    ...mapGetters([
      'userId', // 用户 ID
      'token',
      'BASE_URL'
    ])
  },
  methods: {
    // 获取图片信息
    attachImageUrl (srcUrl) {
      return this.BASE_URL + srcUrl || '../assets/images/user.jpg'
    },
    getBirth (val) {
      let birth = String(val).match(/[0-9-]+(?=\s)/)
      return Array.isArray(birth) ? birth[0] : birth
    },
    getUserSex (sex) {
      if (sex === 0) {
        return '女'
      } else if (sex === 1) {
        return '男'
      }
    },
    // 获取歌曲名
    getSongTitle (str) {
      return str.split('-')[1]
    },
    // 获取歌手名
    getSingerName (str) {
      return str.split('-')[0]
    },
    changeIndex (value) {
      this.$store.commit('setActiveNavName', value)
    },
    // 播放
    playMusic ({ id, url, pic, index, name, lyric, currentSongList }) {
      const songTitle = this.getSongTitle(name)
      const singerName = this.getSingerName(name)
      this.$store.dispatch('playMusic', {id, url, pic, index, songTitle, singerName, lyric, currentSongList})
    },
    // 路由管理
    routerManager (routerName, { path, query }) {
      switch (routerName) {
        case HOME:
        case SIGN_OUT:
          this.$router.push({ path })
          break
        case SONG_SHEET:
          this.$router.push({ path })
          break
        case SONG_SHEET_DETAIL:
          this.$router.push({ path })
          break
        case SINGER:
          this.$router.push({ path })
          break
        case SINGER_DETAIL:
          this.$router.push({ path })
          break
        case MY_MUSIC:
          this.$router.push({ path })
          break
        case SETTING:
          this.$router.push({ path })
          break
        case SIGN_IN:
          this.$router.push({ path })
          break
        case SIGN_UP:
          this.$router.push({ path })
          break
        case SEARCH:
          this.$router.push({ path, query })
          break
        case LYRIC:
          this.$router.push({ path })
          break
        case ERROR:
          this.$router.push({ path })
          break
        default:
          this.$router.push({ path })
          break
      }
    },
    goBack (step = -1) {
      this.$router.go(step)
    }
  }
}

export default mixin
