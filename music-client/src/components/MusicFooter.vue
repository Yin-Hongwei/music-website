<template>
  <div class="music-footer">
    <p v-for="(item, index) in list" :key="index">
      {{item}}
    </p>
  </div>
</template>

<script>
export default {
  name: 'music-footer',
  data () {
    return {
      list: [
        `关于 | 帮助 | 条款 | 反馈`,
        `Copyright © 2019`,
        `Designed by Yin-Hongwei`
      ]
    }
  }
}
</script>

<style scoped>
.music-footer{
  width: 100%;
  background-color: white;
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
.music-footer p {
  height: 30px;
}
</style>
