// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/index'
import Swiper from 'swiper'
import 'swiper/dist/css/swiper.min.css'
import MintUI from 'mint-ui'
import 'mint-ui/lib/style.css'
import './assets/css/style.css'
// import './assets/css/headNav.css'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import MuseUI from 'muse-ui'
import 'muse-ui/dist/muse-ui.css'

import '@/assets/js/iconfont.js'
import '@/assets/js/iconfont1.js'
Vue.use(MintUI)
Vue.use(ElementUI)
Vue.use(MuseUI)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  Swiper,
  components: { App },
  template: '<App/>'
})
