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
    document.documentElement.scrollTop = document.body.scrollTop =
      S - (t * S) / T;
    timer = requestAnimationFrame(func);
    if (t === T) {
      cancelAnimationFrame(timer);
    }
  });
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.scroll-top {
  position: fixed;
  width: 40px;
  height: 20px;
  right: 10px;
  bottom: 80px;
  padding-top: 20px;
  text-align: center;
  background-color: $color-white;
  border-radius: 20%;
  overflow: hidden;
  cursor: pointer;
  @include box-shadow(0 0 4px 3px rgba(0, 0, 0, 0.2));
  &:hover:before {
    top: 50%;
  }
  &:hover .box-in {
    visibility: hidden;
  }
  &:before {
    content: "回到顶部";
    position: absolute;
    font-weight: bold;
    font-size: small;
    width: 30px;
    top: -50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
}

.box-in {
  visibility: visible;
  display: inline-block;
  height: 10px;
  width: 10px;
  border: 1px solid $color-black;
  border-color: $color-black transparent transparent $color-black;
  // 按钮中间的小三角翻转
  transform: rotate(45deg) translate(-5px, -5px);
}
</style>
