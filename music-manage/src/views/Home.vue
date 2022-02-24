<template>
  <div>
    <yin-header></yin-header>
    <yin-aside></yin-aside>
    <div class="content-box" :class="{ 'content-collapse': collapse }">
      <div class="content">
        <router-view></router-view>
      </div>
    </div>
    <yin-audio></yin-audio>
  </div>
</template>

<script setup>
import { ref } from "vue";
import YinHeader from "../components/layouts/YinHeader";
import YinAudio from "../components/layouts/YinAudio";
import YinAside from "../components/layouts/YinAside";
import emitter from "../utils/emitter";

const collapse = ref(false);
emitter.on("collapse", (msg) => {
  collapse.value = msg;
});
</script>
