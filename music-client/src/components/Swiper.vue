<template>
  <div class="swiper">
    <div class="swiper-container">
      <el-carousel :interval="4000" type="card" height="380px">
        <el-carousel-item v-for="(item, index) in swiperList" :key="index">
          <img :src="item.picImg"/>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script>
import { swiperList } from '../assets/data/swiper'

export default {
  name: 'swiper',
  data () {
    return {
      swiperList: []
    }
  },
  created () {
    this.swiperList = swiperList
  }
}
</script>

<style scoped>
.swiper{
  width: 90%;
  margin: 40px auto;
}
</style>
