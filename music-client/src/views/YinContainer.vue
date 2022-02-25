<template>
  <yin-header></yin-header>
  <router-view />
  <yin-current-play></yin-current-play>
  <yin-play-bar></yin-play-bar>
  <yin-scroll-top></yin-scroll-top>
  <yin-footer></yin-footer>
  <yin-audio></yin-audio>
</template>

<script setup>
import { getCurrentInstance } from "vue";
import YinHeader from "@/components/layouts/YinHeader";
import YinCurrentPlay from "@/components/layouts/YinCurrentPlay";
import YinPlayBar from "@/components/layouts/YinPlayBar";
import YinScrollTop from "@/components/layouts/YinScrollTop";
import YinFooter from "@/components/layouts/YinFooter";
import YinAudio from "@/components/layouts/YinAudio";

const { proxy } = getCurrentInstance();
if (sessionStorage.getItem("dataStore")) {
  proxy.$store.replaceState(
    Object.assign(
      {},
      proxy.$store.state,
      JSON.parse(sessionStorage.getItem("dataStore"))
    )
  );
}

// 页面刷新时将 vuex 里的信息保存到 sessionStorage
window.addEventListener("beforeunload", () => {
  sessionStorage.setItem("dataStore", JSON.stringify(proxy.$store.state));
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

#app {
  @include layout(flex-start, stretch, column);
}
</style>
