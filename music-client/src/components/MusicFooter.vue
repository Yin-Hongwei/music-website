<template>
  <div class="music-footer">
    <p>
      关于 | 帮助 | 条款 | 反馈
    </p>
    <p>
      Copyright &copy; 2019
    </p>
    <p>
      Designed by Yin-Hongwei
    </p>
  </div>
</template>

<script>
export default {
  name: 'music-footer'
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
