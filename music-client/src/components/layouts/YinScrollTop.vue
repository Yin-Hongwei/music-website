<template>
  <div class="scroll-top" @click="returnTop">
    <div class="box-in"></div>
  </div>
</template>

<script setup>
function returnTop() {
  let timer = null;
  cancelAnimationFrame(timer);
  const startTime = new Date();
  const S = document.body.scrollTop || document.documentElement.scrollTop;
  const T = 500;
  timer = requestAnimationFrame(function func() {
    const diff = new Date() - startTime;
    const t = T - Math.max(0, T - diff);
    document.documentElement.scrollTop = document.body.scrollTop = S - (t * S) / T;
    timer = requestAnimationFrame(func);
    if (t === T) {
      cancelAnimationFrame(timer);
    }
  });
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/yin-scroll-top.scss";
</style>
