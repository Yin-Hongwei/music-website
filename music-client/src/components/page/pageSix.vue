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
import '../../assets/css/mask.css'
import '../../assets/css/page.css'
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
</style>
