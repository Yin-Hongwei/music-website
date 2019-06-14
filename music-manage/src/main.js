import Vue from 'vue';
import App from './App';
import router from './router';
import axios from 'axios';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
import "babel-polyfill";

Vue.use(ElementUI, { size: 'small' });
Vue.prototype.$axios = axios;

new Vue({
    router,
    render: h => h(App)
}).$mount('#app');
