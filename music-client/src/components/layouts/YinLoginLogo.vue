<template>
  <div class='login-logo'>
    <yin-icon :icon="ICON.ERJI"></yin-icon>
  </div>
</template>

<script setup>
import YinIcon from './YinIcon'
import { ICON } from '@/enums'
</script>

<style lang='scss' scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.login-logo {
  background-color: $color-blue-light;
  height: 100vh;
  width: 50vw;
  min-width: 50vw;
  overflow: hidden;
  @include layout(center, center);
  .icon {
    @include icon(40rem, $color-blue-dark);
    transform: rotate(-30deg);
  }
}
</style>
