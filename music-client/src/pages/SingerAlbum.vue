<template>
  <div class="singer-album">
    <div class="album-slide">
      <div class="singer-img">
        <img :src="attachImageUrl(singer.pic)" alt="">
      </div>
      <ul class="info">
        <li v-if="singer.sex !== 2">性别：{{attachSex(singer.sex)}}</li>
        <li>生日：{{attachBirth(singer.birth)}}</li>
        <li>故乡：{{singer.location}}</li>
      </ul>
    </div>
    <div class="album-content">
      <div class="intro">
        <h2>{{singer.name}}</h2>
        <span>{{singer.introduction}}</span>
      </div>
      <div class="content">
        <album-content :songList="listOfSongs">
          <template slot="title">歌单</template>
        </album-content>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '../mixins'
import AlbumContent from '../components/AlbumContent'
import { getSongOfSingerId } from '../api/index'

export default {
  name: 'singer-album',
  mixins: [mixin],
  components: {
    AlbumContent
  },
  data () {
    return {
      singerId: '',
      singer: {}
    }
  },
  computed: {
    ...mapGetters([
      'tempList',
      'listOfSongs'
    ])
  },
  mounted () {
    this.singerId = this.$route.params.id // 给歌单ID赋值
    this.singer = this.tempList
    this.getSongList()
  },
  methods: {
    getSongList () {
      getSongOfSingerId(this.singerId)
        .then(res => {
          this.$store.commit('setListOfSongs', res)
        })
        .catch(err => {
          console.log(err)
        })
    },
    attachSex (value) {
      if (value === 0) {
        return '女'
      } else if (value === 1) {
        return '男'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/singer-album.scss';
</style>
