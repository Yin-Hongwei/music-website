<template>
  <div class="singer-album-page">
    <div class="album">
      <div class="slide">
        <div class="singer-img">
          <img :src=attachImageUrl(singer.pic) alt="">
        </div>
        <ul class="info">
          <li>性别：{{attachSex(singer.sex)}}</li>
          <li>生日：{{singer.birth}}</li>
          <li>地址：{{singer.location}}</li>
        </ul>
      </div>
      <div class="section">
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
  </div>
</template>

<script>
import axios from 'axios'
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import AlbumContent from '../components/AlbumContent'

export default {
  name: 'singer-album-page',
  components: {
    AlbumContent
  },
  mixins: [mixin],
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
      let _this = this
      axios.get(`${_this.$store.state.HOST}/listSongs?singerId=${_this.singerId}`)
        .then(function (res) {
          _this.$store.commit('setListOfSongs', res.data)
          window.sessionStorage.setItem('listOfSongs', JSON.stringify(res.data))
        })
        .catch(function (error) {
          console.log(error)
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

<style scoped>
.singer-album-page{
  background-color: #93d2f8;
}

.album {
  margin-top: 200px;
  background-color: #e6ecf0;
}

/*左*/
.slide {
  float: left;
  width: 400px;
}

.singer-img {
  height: 300px;
  width: 300px;
  display: inline-block;
  position: relative;
  top: -100px;
  left: 50px;
  border-radius: 10%;
  overflow: hidden;
  border: 5px solid white;
}

.singer-img img {
  width: 100%;
}

.info {
  color: black;
  font-size: 20px;
  font-weight: 500;
  padding-left: 100px;
}

.info li {
  width: 100%;
  height: 40px;
}

/*右*/
.section{
  margin-left: 300px;
  padding: 30px 100px;
}

.intro {
  font-size: 20px;
}

.intro > span {
  color: rgba(0, 0, 0, 0.5);
}

.content {
  margin-top: 50px;
}

</style>
