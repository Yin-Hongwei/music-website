<template>
  <div class="search">
    <nav class="searchList-nav" ref="change">
      <span :class="{isActive: toggle === 'Songs'}" @click="handleChangeView('Songs', 0)">歌曲</span>
      <span :class="{isActive: toggle === 'SongLists'}" @click="handleChangeView('SongLists', 1)">歌单</span>
    </nav>
    <component :is="currentView"></component>
  </div>
</template>

<script>
import searchSongs from '../components/search/SearchSongs'
import searchSongLists from '../components/search/SearchSongLists'
import { mapGetters } from 'vuex'
import mixin from '../mixins'

export default {
  name: 'search',
  mixins: [mixin],
  components: {
    searchSongs,
    searchSongLists
  },
  data () {
    return {
      toggle: 'Songs',
      currentView: 'searchSongs'
    }
  },
  computed: {
    ...mapGetters([
      'searchword'
    ])
  },
  watch: {
    searchword: function () {
      this.getSong()
    }
  },
  methods: {
    // 切换组件
    handleChangeView: function (component) {
      this.currentView = 'search' + component
      this.toggle = component
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/search.scss';
</style>
