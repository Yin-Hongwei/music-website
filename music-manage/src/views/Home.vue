<template>
  <yin-header></yin-header>
  <yin-aside></yin-aside>
  <div class="content-box" :class="{ 'content-collapse': collapse }">
    <router-view></router-view>
  </div>
  <yin-audio></yin-audio>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import YinHeader from "@/components/layouts/YinHeader.vue";
import YinAudio from "@/components/layouts/YinAudio.vue";
import YinAside from "@/components/layouts/YinAside.vue";
import emitter from "@/utils/emitter";

const collapse = ref(false);
emitter.on("collapse", (msg) => {
  collapse.value = msg as boolean;
});
</script>

<style scoped>
.content-box {
  position: absolute;
  left: 150px;
  right: 0;
  top: 60px;
  bottom: 0;
  overflow-y: scroll;
  transition: left 0.3s ease-in-out;
  padding: 20px;
}

.content-collapse {
  left: 65px;
}
</style>
