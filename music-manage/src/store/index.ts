import { createStore } from 'vuex'

export default createStore({
  state: {
    userPic: "/img/avatorImages/user.jpg",
    isPlay: false,
    url: '',
    id: '',
    breadcrumbList: []
  },
  getters: {
    userPic: state => state.userPic,
    isPlay: state => state.isPlay,
    url: state => state.url,
    id: state => state.id,
    breadcrumbList: state => state.breadcrumbList
  },
  mutations: {
    setUserPic: (state, userPic) => { state.userPic = userPic },
    setIsPlay: (state, isPlay) => { state.isPlay = isPlay },
    setUrl: (state, url) => { state.url = url },
    setId: (state, id) => { state.id = id },
    setBreadcrumbList: (state, breadcrumbList) => { state.breadcrumbList = breadcrumbList }
  }
})