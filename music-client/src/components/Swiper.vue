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
export default {
  name: 'swiper',
  data () {
    return {
      swiperList: [{
        picImg: require('@/assets/img/swiper/blur-1851426_640.jpg')
      }, {
        picImg: require('@/assets/img/swiper/concert-768722_640.jpg')
      }, {
        picImg: require('@/assets/img/swiper/boy-984293_640.jpg')
      }, {
        picImg: require('@/assets/img/swiper/ipad-605439_640.jpg')
      }, {
        picImg: require('@/assets/img/swiper/microphone-1209816_640.jpg')
      }, {
        picImg: require('@/assets/img/swiper/music-notes-3221097_640.jpg')
      }, {
        picImg: require('@/assets/img/swiper/piano-1655558_640.jpg')
      }, {
        picImg: require('@/assets/img/swiper/turntable-1337986_640.jpg')
      }]
    }
  }
}
</script>

<style scoped>
.swiper{
  width: 90%;
  margin: 40px auto;
}
</style>
