import { createStore } from 'vuex'
import { BASE_URL } from '../enums'

export default createStore({
  state: {
    HOST: BASE_URL,
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
