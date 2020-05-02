<template>
  <div class="scroll-top" @click="returnTop">
    <div class="box-in"></div>
  </div>
</template>

<script>
export default {
  name: 'scroll-top',
  methods: {
    returnTop () {
      let timer = null
      cancelAnimationFrame(timer)
      let startTime = new Date()
      let S = document.body.scrollTop || document.documentElement.scrollTop
      let T = 500
      timer = requestAnimationFrame(function func () {
        let diff = new Date() - startTime
        let t = T - Math.max(0, T - diff)
        document.documentElement.scrollTop = document.body.scrollTop = S - (t * S) / T
        timer = requestAnimationFrame(func)
        if (t === T) {
          cancelAnimationFrame(timer)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/scroll-top.scss';
</style>
