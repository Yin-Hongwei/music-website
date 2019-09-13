import Vue from 'vue';
import App from './App';
import router from './router';
import store from './store/index'
import axios from 'axios';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
import "babel-polyfill";
import '@/assets/js/iconfont.js'

Vue.use(ElementUI);
Vue.prototype.$axios = axios;

new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
})
