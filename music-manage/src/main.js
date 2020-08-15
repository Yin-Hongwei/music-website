import Vue from 'vue'
import Router from 'vue-router'
import Vuex from 'vuex'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/main.css'
import 'babel-polyfill'
import VCharts from 'v-charts'
import App from './App'
import router from './router'
import store from './store/index'

Vue.use(Router)
Vue.use(Vuex)
Vue.use(ElementUI)
Vue.use(VCharts)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router: new Router(router),
  store: new Vuex.Store(store),
  render: h => h(App)
})
