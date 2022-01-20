<template>
  <div class="singer-detail">
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
    <div class="song-list">
      <div class="intro">
        <h2>{{singer.name}}</h2>
        <span>{{singer.introduction}}</span>
      </div>
      <div class="content">
        <song-list :songList="listOfSongs">
          <template slot="title">歌单</template>
        </song-list>
      </div>
    </div>
  </div>
</template>

<script>
import mixin from '../../mixins'
import SongList from '../../components/SongList'
import { mapGetters } from 'vuex'
import { HttpManager } from '../../api/index'

export default {
  name: 'SingerDetail',
  mixins: [mixin],
  components: {
    SongList
  },
  data () {
    return {
      singerId: '',
      singer: {}
    }
  },
  computed: {
    ...mapGetters([
      'songDetails',
      'listOfSongs'
    ])
  },
  mounted () {
    this.singerId = this.$route.params.id // 给歌单ID赋值
    this.singer = this.songDetails
    this.getSongList()
  },
  methods: {
    getSongList () {
      HttpManager.getSongOfSingerId(this.singerId)
        .then(res => {
          this.$store.commit('setListOfSongs', res)
        })
        .catch(err => {
          console.error(err)
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
@import '../../assets/css/singer-detail.scss';
</style>
