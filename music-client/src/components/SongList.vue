<template>
  <div class='content'>
    <h1 class='title'>
      <slot name='title'></slot>
    </h1>
    <hr>
    <ul>
      <li class='list-title'>
        <div class='song-item'>
          <span class='item-index'></span>
          <span class='item-title'>歌曲名</span>
          <span class='item-name'>艺人</span>
          <span class='item-intro'>专辑</span>
        </div>
      </li>
      <li class='list-content' v-for='(item, index) in songList' :key='index'>
        <div
          class='song-item'
          :class='{"is-play": songId === item.id}'
          @click='playMusic({
            id: item.id,
            url: item.url,
            pic: item.pic,
            index: index,
            name: item.name,
            lyric: item.lyric,
            currentSongList: songList
          })'
        >
          <span class='item-index'>
            <span v-if='songId !== item.id'>{{index + 1}}</span>
            <svg v-if='songId === item.id' class='icon' aria-hidden='true'>
              <use :xlink:href='YINLIANG'></use>
            </svg>
          </span>
          <span class='item-title'>{{getSongTitle(item.name)}}</span>
          <span class='item-name'>{{getSingerName(item.name)}}</span>
          <span class='item-intro'>{{item.introduction}}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '../mixins'
import { ICON } from '../enums'

export default {
  name: 'SongList',
  mixins: [mixin],
  props: {
    songList: Array
  },
  data () {
    return {
      YINLIANG: ICON.YINLIANG
    }
  },
  computed: {
    ...mapGetters([
      'songId' // 音乐 ID
    ])
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/song-list.scss';
</style>
