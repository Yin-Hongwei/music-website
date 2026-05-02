<template>
  <el-footer class="yin-footer">
    <p v-for="(item, index) in footerList" :key="index">
      {{ item }}
    </p>
  </el-footer>
</template>

<script lang="ts" setup>
const footerList = ["Copyright © 2018 Yin-Hongwei"];
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.yin-footer {
  width: 100%;
  height: $footer-height;
  margin-bottom: 0;
  box-sizing: border-box;
  font-size: 14px;
  @include layout(center, center, column);
}

p {
  height: 30px;
}
</style>
