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
import { mapGetters } from 'vuex'
import {mixin} from '../mixins'
export default {
  name: 'singer',
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
      this.$router.push({path: '/singerAlbum/' + id})
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
    /*justify-content: center;*/
  }
  .content-item {
    padding: 0 20px;
    width: 12vw;
    min-width: 170px;
    position: relative;
  }
  .kuo {
    width: 12vw;
    height: 12vw;
    min-width: 170px;
    min-height: 170px;
    border-radius: 50%;
    overflow: hidden;
    -webkit-box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.6);
    -moz-box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.6);
    box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.6);
  }
  .item-img {
    width: 100%;
  }
  .item-name {
    overflow:hidden;
    text-overflow:ellipsis;
    display:-webkit-box;
    -webkit-box-orient:vertical;
    -webkit-line-clamp:2;
    margin: 10px 8px;
  }
  .item-img:hover {
    cursor: pointer;
  }
  .item-name {
    text-align: center;
    margin: 10px 8px;
  }
</style>
