import { mapGetters } from 'vuex'
import { HOME, SONG_SHEET, SINGER, PERSONAL, SETTING, SIGN_IN, SIGN_UP, SIGN_OUT, SEARCH, LYRIC, SONG_SHEET_DETAIL, SINGER_DETAIL, ERROR, PERSONAL_DATA } from '../enums'

const mixin = {
  computed: {
    ...mapGetters([
      'userId', // 用户 ID
      'token', // 用户登录状态
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
    checkStatus () {
      if (!this.token) {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
        return false
      }
      return true
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
        case SEARCH:
          this.$router.push({ path, query })
          break
        case HOME:
        case SIGN_OUT:
        case SONG_SHEET:
        case SONG_SHEET_DETAIL:
        case SINGER:
        case SINGER_DETAIL:
        case PERSONAL:
        case SETTING:
        case PERSONAL_DATA:
        case SIGN_IN:
        case SIGN_UP:
        case LYRIC:
        case ERROR:
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
