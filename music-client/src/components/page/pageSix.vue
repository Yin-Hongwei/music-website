<template>
  <div class="song-content">
    <div class="content-item" v-for="(item, index) in albumDatas" :key="index">
      <div class="kuo">
        <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
        <div class="mask" @click="goSongAlbum(item.id, item.list)">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-bofang"></use>
          </svg>
        </div>
      </div>
      <p class="item-name">{{item.title}}</p>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {mixin} from '../../mixins'
export default {
  name: 'pageSix',
  data () {
    return {
      albumDatas: []
    }
  },
  computed: {
    ...mapGetters([
      'songsList'
    ])
  },
  mounted: function () {
    let pattern = /轻音乐/
    this.getAlbumDatas(pattern)
    console.log(this.albumDatas)
  },
  mixins: [mixin],
  methods: {
    goSongAlbum (id, index) {
      this.$store.commit('setIndex', index)
      window.sessionStorage.setItem('index', JSON.stringify(index))
      this.$router.push({path: '/songAlbum/' + id})
    }
  }
}
</script>

<style scoped>
  .song-content {
    display: flex;
    flex-wrap: wrap;
    padding: 0 30px;
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
    border-radius: 6px;
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
  .mask{
    position: absolute;
    width: 12vw;
    height: 12vw;
    min-width: 170px;
    min-height: 170px;
    border-radius: 6px;
    top:0;
    background-color: rgba(52,47,41,.4);
    transition:all .3s ease-in-out;
    opacity: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .mask:hover{
    opacity: 1;
    cursor: pointer;
  }
  .icon {
    width: 2em;
    height: 2em;
    fill: currentColor;
    color: rgba(240,240,240,1);
    font-size: 1.6em;
  }
</style>
