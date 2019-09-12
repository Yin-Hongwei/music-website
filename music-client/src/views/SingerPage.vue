<template>
  <div class="body-section">
    <div class="song-head">全部歌手</div>
    <content-list :contentList="singersList" :type="1"></content-list>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {mixin} from '../mixins'
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
  mixins: [mixin]
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
  .song-head {
    height: 100px;
    font-size: 28px;
    font-weight: 500;
    text-align: center;
    line-height: 100px;
    color: black;
  }
</style>
