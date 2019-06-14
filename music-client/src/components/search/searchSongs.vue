<template>
  <div class="song-content">
    <div class="song-item">
      <div class="item-kong"></div>
      <div class="item-n">歌曲名</div>
      <div class="item-s">艺人</div>
      <div class="item-z">专辑</div>
    </div>
    <hr>
    <ul>
      <li v-for="(item, index) in listOfSongs" :key="index">
        <div class="song-item" @click="toplay(item.id, item.url, item.pic, index, item.name, item.lyric)">
          <div class="item-i">{{index + 1}}</div>
          <div class="item-n">{{replaceFName(item.name)}}</div>
          <div class="item-s">{{replaceLName(item.name)}}</div>
          <div class="item-z">{{item.introduction}}</div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {mixin} from '../../mixins'
export default {
  name: 'searchSongLists',
  computed: {
    ...mapGetters([
      'listOfSongs' // 存放的音乐
    ])
  },
  mounted: function () {
    this.getSong()
  },
  mixins: [mixin]
}
</script>

<style scoped>
  .song-content{
    min-height: 300px;
  }
  ul {
    width: 100%;
    padding-bottom: 50px;
  }
  li {
    border-bottom: solid 1px rgba(0, 0, 0, 0.2);
    display: block;
    height: 40px;
    line-height: 40px;
    text-indent: 20px;
    cursor: pointer;
  }
  .song-item {
    display: flex;
    flex-wrap: nowrap;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
  }
  .item-kong{
    width: 8%
  }
  .item-i {
    width: 5%;
  }
  .item-n {
    width: 30%;
  }
  .item-s {
    width: 20%;
  }
  .item-z {
    width: 45%;
  }
</style>
