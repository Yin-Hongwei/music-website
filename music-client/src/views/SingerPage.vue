<template>
  <div class="body-section">
    <div class="song-head">全部歌手</div>
    <div class="song-content">
      <div class="content-item" v-for="(item, index) in singersList" :key="index">
        <div class="kuo" @click="goSingerAblum(item.id, index)">
          <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
        </div>
        <p class="item-name">{{item.name}}</p>
      </div>
    </div>
  </div>
</template>

<script>
import '../assets/css/mask.css'
import { mapGetters } from 'vuex'
import {mixin} from '../mixins'
export default {
  name: 'singer-page',
  computed: {
    ...mapGetters([
      'singersList'
    ])
  },
  mixins: [mixin],
  methods: {
    goSingerAblum (id, index) {
      this.$store.commit('setIndex', index)
      window.sessionStorage.setItem('index', JSON.stringify(index))
      this.$router.push({path: '/singer-album-page/' + id})
    }
  }
}
</script>

<style scoped>
  .body-section {
    margin: 30px 150px;
    padding-bottom: 50px;
    min-width: 800px;
    background-color: #ffffff;
  }
  .song-head {
    height: 100px;
    font-size: 28px;
    font-weight: 500;
    text-align: center;
    line-height: 100px;
    color: black;
  }
  .song-content {
    padding: 0 30px;
    display: flex;
    flex-wrap: wrap;
  }
  .content-item {
    width: 12vw;
    min-width: 170px;
  }
  .kuo {
    width: 12vw;
    height: 12vw;
    min-width: 170px;
    min-height: 170px;
    border-radius: 50%;
  }
  .item-name {
    text-align: center;
    margin: 10px 8px;
  }
</style>
