<template>
  <div class="body-section">
    <div class="song-head">全部歌手</div>
    <content-list :contentList="singersList"></content-list>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {axios} from 'axios'
import ContentList from '../components/ContentList'

export default {
  name: 'singer-page',
  computed: {
    ...mapGetters([
      'singersList'
    ])
  },
  components: {
    ContentList
  },
  mounted () {
    this.getSingerLists()
  },
  methods: {
    handleChangeView: function (item) {
      this.activeName = item.name
      this.albumDatas = []
      if (item.name === '全部') {
        this.getSingerAll()
      } else if (item.name === '男' || item.name === '女') {
        this.getSingerSex(item.type)
      }
    },
    // 获取所有歌手
    getSingerAll () {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/listSingers`)
        .then(function (res) {
          _this.albumDatas = res.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 通过性别对歌手分类
    getSingerSex (sex) {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/api/singer?sex=${sex}`)
        .then((res) => {
          _this.albumDatas = res.data
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  }
}
</script>

<style scoped>
*{
  box-sizing: border-box;
}

.body-section {
  margin: 30px 10%;
  padding-bottom: 50px;
  background-color: #ffffff;
}

.singer-header {
  width: 100%;
  padding: 0 40px;
}

li {
  display: inline-block;
  line-height: 40px;
  margin: 40px 20px 15px 20px;
  font-size: 20px;
  font-weight: 400;
  color: #67757f;
  border-bottom: none;
  cursor: pointer;
}

.active {
  color: black;
}
</style>
