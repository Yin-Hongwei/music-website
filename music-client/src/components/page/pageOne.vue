<template>
  <div class="song-content">
    <div class="content-item" v-for="(item, index) in songsList" :key="index">
      <div class="kuo">
        <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
        <div class="mask" @click="goSongAlbum(item.id, index)">
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
  name: 'pageOne',
  computed: {
    ...mapGetters([
      'songsList'
    ])
  },
  mounted: function () {
    console.log('------全部歌单------')
    console.log(this.$store.state.songsList)
  },
  mixins: [mixin],
  methods: {
    getAlbumDatas (pattern) {
      for (let i = 0; i < this.songsList.length; i++) {
        if (pattern.test(this.songsList[i].style)) {
          var item = this.songsList[i]
          item.list = i
          this.albumDatas.push(item)
          console.log(item)
        }
      }
    },
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
