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

<style scoped>
.scroll-top{
  position:fixed;
  right:10px;
  bottom: 80px;
  height:30px;
  width: 50px;
  text-align:center;
  padding-top:20px;
  background-color: white;
  border-radius: 20%;
  overflow: hidden;
  box-shadow: 0 0 4px 3px rgba(0, 0, 0, 0.2);
}

.scroll-top:hover:before{
  top:50%
}

.scroll-top:hover .box-in{
  visibility: hidden;
}

.scroll-top:before{
  position: absolute;
  top: -50%;
  left: 50%;
  transform: translate(-50%,-50%);
  content:'回到顶部';
  width: 30px;
  font-weight:bold;

}

.box-in{
  visibility: visible;
  display:inline-block;
  height:15px;
  width: 15px;
  border: 1px solid black;
  border-color: black transparent transparent black;
  transform:rotate(45deg);
}
</style>
