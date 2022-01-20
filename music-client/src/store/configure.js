export default {
  state: {
    BASE_URL: process.env.NODE_HOST,
    token: false, // 用户是否登录
    showAside: false, // 是否显示侧边栏
    searchWord: '', // 搜索关键词
    activeNavName: '' // 导航栏名称
  },
  getters: {
    BASE_URL: state => state.BASE_URL,
    token: state => state.token,
    activeNavName: state => state.activeNavName,
    showAside: state => state.showAside,
    searchWord: state => state.searchWord
  },
  mutations: {
    setToken: (state, token) => { state.token = token },
    setActiveNavName: (state, activeNavName) => { state.activeNavName = activeNavName },
    setShowAside: (state, showAside) => { state.showAside = showAside },
    setSearchWord: (state, searchWord) => { state.searchWord = searchWord }
  }
}
