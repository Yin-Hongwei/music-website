<template>
  <div class="search">
    <nav class="searchList-nav" ref="change">
      <span :class="{isActive: toggle === 'Song'}" @click="handleChangeView('Song', 0)">歌曲</span>
      <span :class="{isActive: toggle === 'SongSheet'}" @click="handleChangeView('SongSheet', 1)">歌单</span>
    </nav>
    <component :is="currentView"></component>
  </div>
</template>

<script>
import mixin from '../mixins'
import SearchSong from '../components/search/SearchSong'
import searchSongList from '../components/search/SearchSongList'
import { mapGetters } from 'vuex'

export default {
  name: 'Search',
  mixins: [mixin],
  components: {
    SearchSong,
    searchSongList
  },
  data () {
    return {
      toggle: 'Song',
      currentView: 'SearchSong'
    }
  },
  computed: {
    ...mapGetters([
      'searchword'
    ])
  },
  watch: {
    searchword () {
      this.getSong()
    }
  },
  methods: {
    // 切换组件
    handleChangeView (component) {
      this.currentView = 'search' + component
      this.toggle = component
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/search.scss';
</style>
