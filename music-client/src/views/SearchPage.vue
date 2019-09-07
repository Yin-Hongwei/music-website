<template>
  <div id="search-page">
    <div class="my-bg"></div>
    <div class="my-body">
      <div class="content">
        <nav class="searchList-nav" ref="change">
          <span @click="handleChangeView('Songs', 0)">歌曲</span>
          <span @click="handleChangeView('SongLists', 1)">歌单</span>
        </nav>
        <component :is="currentView"></component>
      </div>
    </div>
  </div>
</template>

<script>
import searchSongs from '../components/search/SearchSongs'
import searchSongLists from '../components/search/SearchSongLists'
import { mapGetters } from 'vuex'
import {mixin} from '../mixins'
export default {
  name: 'search-page',
  data () {
    return {
      currentView: 'searchSongs'
    }
  },
  components: {
    searchSongs,
    searchSongLists
  },
  computed: {
    ...mapGetters([
      'searchword'
    ])
  },
  mounted () {
    this.changeIndex(0)
  },
  watch: {
    searchword: function () {
      this.getSong()
    }
  },
  mixins: [mixin],
  methods: {
    // 切换组件
    handleChangeView: function (component, index) {
      this.currentView = 'search' + component
      this.changeIndex(index)
    },
    // 标题加样式
    changeIndex (index) {
      for (let i = 0; i < 2; i++) {
        this.$refs.change.children[i].style.color = '#67757f'
        this.$refs.change.children[i].style.fontWeight = '300'
        this.$refs.change.children[i].style.borderBottom = 'none'
      }
      this.$refs.change.children[index].style.color = 'black'
      this.$refs.change.children[index].style.fontWeight = '400'
      this.$refs.change.children[index].style.borderBottom = '5px solid black'
    }
  }
}
</script>

<style scoped>
  .my-bg {
    width: 100%;
    height: 100px;
    background-color: #93d2f8;
  }
  .my-body{
    position: relative;
    padding: 0 100px;
  }
  .content {
    background-color: white;
    margin: 50px 0;
    border-radius: 12px;
    padding: 20px 40px;
    min-width: 900px;
  }
  .searchList-nav {
    padding-left: 20px;
    font-size: 20px;
    font-weight: 600;
    color: black;
  }
  .searchList-nav span {
    display: inline-block;
    margin: 10px 20px 20px -10px;
  }
</style>
