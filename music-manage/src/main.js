import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import App from './App.vue'
import router from './router'
import store from './store'
import 'element-plus/dist/index.css'
import './assets/css/main.css'
import './assets/icons/iconfont.js'

const app = createApp(App)
app.use(router)
app.use(store)
app.use(ElementPlus)
app.mount('#app')