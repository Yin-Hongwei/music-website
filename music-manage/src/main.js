import Vue from 'vue'
import Vuex from 'vuex'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/main.css'
import 'babel-polyfill'
import VCharts from 'v-charts'
import App from './App'
import router from './router'
import store from './store/index'
import '@/assets/js/iconfont.js'

Vue.use(Vuex)
Vue.use(ElementUI)
Vue.use(VCharts)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router: router,
  store: new Vuex.Store(store),
  render: h => h(App)
})
