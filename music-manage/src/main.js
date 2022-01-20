import Vue from 'vue'
import Vuex from 'vuex'
import VCharts from 'v-charts'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import './assets/css/main.css'
import App from './App'
import router from './router'
import store from './store/index'
import './assets/icons/iconfont.js'

Vue.use(Vuex)
Vue.use(VCharts)
Vue.use(ElementUI)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router: router,
  store: new Vuex.Store(store),
  render: h => h(App)
})
