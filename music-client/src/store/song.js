import { ICON, BASE_URL } from '../enums'

export default {
  state: {
    /** 音乐信息 */
    songId: '', // 音乐 ID
    songTitle: '', // 歌名
    songUrl: '', // 音乐 URL
    songPic: `${BASE_URL}/img/songPic/tubiao.jpg`, // 歌曲图片
    singerName: '', //  歌手名
    isCollection: false, // 是否收藏
    lyric: [], // 处理后的歌词数据

    /** 音乐播放信息 */
    isPlay: false, // 播放状态
    playBtnIcon: ICON.BOFANG, // 播放状态的图标
    volume: 0, // 音量
    duration: 0, // 音乐时长
    curTime: 0, // 当前音乐的播放位置
    changeTime: 0, // 指定播放时刻
    autoNext: true, // 用于触发自动播放下一首

    /** 音乐列表信息 */
    currentPlayList: [], // 当前播放列表
    songDetails: null, // 单个歌单信息
    currentPlayIndex: -1 // 当前歌曲在歌曲列表的位置
  },
  getters: {
    songId: state => state.songId,
    songTitle: state => state.songTitle,
    songUrl: state => state.songUrl,
    songPic: state => state.songPic,
    singerName: state => state.singerName,
    isCollection: state => state.isCollection,
    lyric: state => state.lyric,

    isPlay: state => state.isPlay,
    playBtnIcon: state => state.playBtnIcon,
    volume: state => state.volume,
    duration: state => state.duration,
    curTime: state => state.curTime,
    changeTime: state => state.changeTime,
    autoNext: state => state.autoNext,

    currentPlayList: state => state.currentPlayList,
    songDetails: state => state.songDetails,
    currentPlayIndex: state => state.currentPlayIndex
  },
  mutations: {
    setSongId: (state, songId) => { state.songId = songId },
    setSongTitle: (state, songTitle) => { state.songTitle = songTitle },
    setSongUrl: (state, songUrl) => { state.songUrl = songUrl },
    setSongPic: (state, songPic) => { state.songPic = songPic },
    setSingerName: (state, singerName) => { state.singerName = singerName },
    setIsCollection: (state, isCollection) => { state.isCollection = isCollection },
    setAutoNext: (state, autoNext) => { state.autoNext = autoNext },
    setLyric: (state, lyric) => { state.lyric = lyric },

    setIsPlay: (state, isPlay) => { state.isPlay = isPlay },
    setPlayBtnIcon: (state, playBtnIcon) => { state.playBtnIcon = playBtnIcon },
    setVolume: (state, volume) => { state.volume = volume },
    setDuration: (state, duration) => { state.duration = duration },
    setCurTime: (state, curTime) => { state.curTime = curTime },
    setChangeTime: (state, changeTime) => { state.changeTime = changeTime },

    setCurrentPlayList: (state, currentPlayList) => { state.currentPlayList = currentPlayList },
    setSongDetails: (state, songDetails) => { state.songDetails = songDetails },
    setCurrentPlayIndex: (state, currentPlayIndex) => { state.currentPlayIndex = currentPlayIndex }
  },
  actions: {
    playMusic: ({ commit }, { id, url, pic, index, songTitle, singerName, lyric, currentSongList }) => {
      commit('setSongId', id)
      commit('setSongUrl', BASE_URL + url)
      commit('setSongPic', BASE_URL + pic)
      commit('setCurrentPlayIndex', index)
      commit('setSongTitle', songTitle)
      commit('setSingerName', singerName)
      commit('setLyric', lyric)
      commit('setCurrentPlayList', currentSongList)
    }
  }
}
