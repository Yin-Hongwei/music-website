import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    HOST: 'http://localhost:8888',
    isPlay: false,
    url: '',
    id: ''
  },
  getters: {
    isPlay: state => state.isPlay,
    url: state => state.url,
    id: state => state.id
  },
  mutations: {
    setIsPlay: (state, isPlay) => { state.isPlay = isPlay },
    setUrl: (state, url) => { state.url = url },
    setId: (state, id) => { state.id = id }
  }
})

export default store
