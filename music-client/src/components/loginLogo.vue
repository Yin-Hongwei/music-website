<template>
  <div class="bg-left">
    <div class="item-logo">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icon-erji"></use>
      </svg>
    </div>
  </div>
</template>

<script>
export default {
  name: 'loginLogo'
}
</script>

<style scoped>
  .item-logo {
    transform: rotate(-30deg);
  }
  .icon {
    width: 550px;
    height: 680px;
    fill: currentColor;
    color: #2796dd;
  }
  .bg-left {
    background-color: #2aa3ef;
    height: 100vh;
    width: 600px;
    overflow: hidden;
  }
</style>
