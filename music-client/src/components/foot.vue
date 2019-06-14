<template>
  <div class="foot">
    <p>
      关于 | 帮助 | 条款 | 反馈
    </p>
    <p>
      Copyright &copy; Yin-Hongwei
    </p>
  </div>
</template>

<script>
export default {
  name: 'foot'
}
</script>

<style scoped>
.foot{
  /*width: 100%;*/
  background-color: white;
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
.foot p {
  height: 40px;
}
</style>
