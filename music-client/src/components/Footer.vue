<template>
  <div class="footer">
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
  name: 'footer'
}
</script>

<style scoped>
.footer{
  width: 100%;
  background-color: white;
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
#footer p {
  height: 30px;
}
</style>
