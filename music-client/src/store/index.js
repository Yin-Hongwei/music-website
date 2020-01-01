import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

const song = {
  state: {
    isPlay: false, // 播放状态
    playButtonUrl: '#icon-bofang', // 播放状态的图标
    id: null, // 音乐ID
    url: '', // 歌曲URL
    duration: 0, //  音乐时长
    curTime: 0, // 当前音乐的播放位置
    changeTime: 0, //  指定播放时刻
    title: '', //  歌名
    artist: '', //  歌手名
    picUrl: '', // 歌曲图片
    autoNext: true, // 用于触发自动播放下一首
    lyric: [], // 未处理的歌词数据
    listOfSongs: [], // 当前歌单列表
    tempList: {}, // 单个歌单信息
    listIndex: null // 当前歌曲在歌曲列表的位置
  },
  getters: {
    isPlay: state => state.isPlay,
    playButtonUrl: state => {
      let playButtonUrl = state.playButtonUrl
      if (!playButtonUrl) {
        playButtonUrl = JSON.parse(window.sessionStorage.getItem('playButtonUrl') || null)
      }
      return playButtonUrl
    },
    id: state => {
      let id = state.id
      if (!id) {
        id = JSON.parse(window.sessionStorage.getItem('id') || null)
      }
      return id
    },
    url: state => {
      let url = state.url
      if (!url) {
        url = JSON.parse(window.sessionStorage.getItem('url') || null)
      }
      return url
    },
    duration: state => {
      let duration = state.duration
      if (!duration) {
        duration = JSON.parse(window.sessionStorage.getItem('duration') || null)
      }
      return duration
    },
    curTime: state => {
      let curTime = state.curTime
      if (!curTime) {
        curTime = JSON.parse(window.sessionStorage.getItem('curTime') || null)
      }
      return curTime
    },
    changeTime: state => {
      let changeTime = state.changeTime
      if (!changeTime) {
        changeTime = JSON.parse(window.sessionStorage.getItem('changeTime') || null)
      }
      return changeTime
    },
    title: state => {
      let title = state.title
      if (!title) {
        title = JSON.parse(window.sessionStorage.getItem('title') || null)
      }
      return title
    },
    artist: state => {
      let artist = state.artist
      if (!artist) {
        artist = JSON.parse(window.sessionStorage.getItem('artist') || null)
      }
      return artist
    },
    picUrl: state => {
      let picUrl = state.picUrl
      if (!picUrl) {
        picUrl = JSON.parse(window.sessionStorage.getItem('picUrl')) || 'http://localhost:8888/img/tubiao.jpg'
      }
      return picUrl
    },
    autoNext: state => {
      let autoNext = state.autoNext
      if (!autoNext) {
        autoNext = JSON.parse(window.sessionStorage.getItem('autoNext') || null)
      }
      return autoNext
    },
    lyric: state => {
      let lyric = state.lyric
      if (lyric.length === 0) {
        lyric = JSON.parse(window.sessionStorage.getItem('lyric') || null)
      }
      return lyric
    },
    tempList: state => {
      let tempList = state.tempList
      if (JSON.stringify(tempList) === '{}') {
        tempList = JSON.parse(window.sessionStorage.getItem('tempList') || null)
      }
      return tempList
    },
    listOfSongs: state => {
      let listOfSongs = state.listOfSongs
      if (!listOfSongs.length) {
        listOfSongs = JSON.parse(window.sessionStorage.getItem('listOfSongs') || null)
      }
      return listOfSongs
    },
    listIndex: state => {
      let listIndex = state.listIndex
      if (!listIndex) {
        listIndex = JSON.parse(window.sessionStorage.getItem('listIndex') || null)
      }
      return listIndex
    }
  },
  mutations: {
    setIsPlay: (state, isPlay) => { state.isPlay = isPlay },
    setPlayButtonUrl: (state, playButtonUrl) => { state.playButtonUrl = playButtonUrl },
    setId: (state, id) => { state.id = id },
    setUrl: (state, url) => { state.url = url },
    setDuration: (state, duration) => { state.duration = duration },
    setCurTime: (state, curTime) => { state.curTime = curTime },
    setChangeTime: (state, changeTime) => { state.changeTime = changeTime },
    setTitle: (state, title) => { state.title = title },
    setArtist: (state, artist) => { state.artist = artist },
    setpicUrl: (state, picUrl) => { state.picUrl = picUrl },
    setAutoNext: (state, autoNext) => { state.autoNext = autoNext },
    setLyric: (state, lyric) => { state.lyric = lyric },
    setTempList: (state, tempList) => { state.tempList = tempList },
    setListOfSongs: (state, listOfSongs) => { state.listOfSongs = listOfSongs },
    setListIndex: (state, listIndex) => { state.listIndex = listIndex }
  }
}

const user = {
  state: {
    userId: '',
    username: '',
    avator: ''
  },
  getters: {
    userId: state => {
      let userId = state.userId
      if (!userId) {
        userId = JSON.parse(window.localStorage.getItem('userId') || null)
      }
      return userId
    },
    username: state => {
      let username = state.username
      if (!username) {
        username = JSON.parse(window.localStorage.getItem('username') || null)
      }
      return username
    },
    avator: state => {
      let avator = state.avator
      if (!avator) {
        avator = JSON.parse(window.localStorage.getItem('avator') || null)
      }
      return avator
    }
  },
  mutations: {
    setUserId: (state, userId) => { state.userId = userId },
    setUsername: (state, username) => { state.username = username },
    setAvator: (state, avator) => { state.avator = avator }
  }
}

const store = new Vuex.Store({
  modules: {
    user,
    song
  },
  state: {
    HOST: 'http://localhost:8888',
    loginIn: false, // 是否登录
    activeName: '', // 歌单类型名
    searchword: '', // 搜索关键词
    showAside: false, // 是否显示侧边栏
    index: 0 // 列表中的序号
  },
  getters: {
    loginIn: state => {
      let loginIn = state.loginIn
      if (!loginIn) {
        loginIn = JSON.parse(window.sessionStorage.getItem('loginIn') || null)
      }
      return loginIn
    },
    activeName: state => {
      let activeName = state.activeName
      if (!activeName) {
        activeName = JSON.parse(window.sessionStorage.getItem('activeName') || null)
      }
      return activeName
    },
    showAside: state => {
      let showAside = state.showAside
      if (!showAside) {
        showAside = JSON.parse(window.sessionStorage.getItem('showAside') || null)
      }
      return showAside
    },
    index: state => {
      let index = state.index
      if (!index) {
        index = JSON.parse(window.sessionStorage.getItem('index') || null)
      }
      return index
    },
    searchword: state => state.searchword
  },
  mutations: {
    setLoginIn: (state, loginIn) => { state.loginIn = loginIn },
    setActiveName: (state, activeName) => { state.activeName = activeName },
    setShowAside: (state, showAside) => { state.showAside = showAside },
    setIndex: (state, index) => { state.index = index },
    setSearchword: (state, searchword) => { state.searchword = searchword }
  },
  actions: {}
})

export default store
