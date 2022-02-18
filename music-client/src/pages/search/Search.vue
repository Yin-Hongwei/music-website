<template>
  <div class='search'>
    <nav class='searchList-nav'>
      <span
        v-for='(item, index) in searchNavList'
        :key='index'
        :class='{"is-active": currentView === item.value}'
        @click='handleChangeView(item.value)'>
        {{item.name}}
      </span>
    </nav>
    <component :is='currentView'></component>
  </div>
</template>

<script>
import mixin from '../../mixins'
import SearchSong from './SearchSong'
import SearchSongList from './SearchSongList'

export default {
  name: 'Search',
  mixins: [mixin],
  components: {
    SearchSong,
    SearchSongList
  },
  data () {
    return {
      searchNavList: [
        {
          name: '歌曲',
          value: 'SearchSong'
        },
        {
          name: '歌单',
          value: 'SearchSongList'
        }
      ],
      currentView: 'SearchSong'
    }
  },
  methods: {
    // 切换组件
    handleChangeView (component) {
      this.currentView = component
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/search.scss';
</style>
