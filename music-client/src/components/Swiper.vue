<template>
  <div class="swiper">
    <div class="swiper-container">
      <el-carousel :interval="4000" type="card" height="280px">
        <el-carousel-item v-for="(item, index) in swiperList" :key="index">
          <img :src="item.picImg"/>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script>
export default {
  name: 'swiper',
  props: {
    swiperList: Array
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/swiper.scss';
</style>
