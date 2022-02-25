<template>
  <div class="the-footer">
    <p v-for="(item, index) in footerList" :key="index">
      {{ item }}
    </p>
  </div>
</template>

<script setup>
import { ref } from "vue";

const footerList = ref([
  "关于 | 帮助 | 条款 | 反馈",
  "Copyright © 2019 Yin-Hongwei",
]);
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.the-footer {
  width: 100%;
  height: $footer-height;
  background-color: $theme-footer-color;
  @include layout(center, center, column);
}

p {
  height: 30px;
}
</style>
