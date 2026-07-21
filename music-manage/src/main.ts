import { createApp } from "vue";
import type { Plugin } from "vue";
import { createPinia } from "pinia";
import naive from "naive-ui";
import App from "./App.vue";
import router from "./router";
import "./assets/css/main.css";

const pinia = createPinia();

// Pinia 与项目里 `vue` 若被解析成两套类型，会出现 TS2345；运行时仍是一套 Vue，此处仅收窄类型。
createApp(App)
  .use(pinia as unknown as Plugin)
  .use(router)
  .use(naive)
  .mount("#app");
