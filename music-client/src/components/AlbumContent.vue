<template>
  <div class="content">
    <h1 class="title">
      <slot name="title"></slot>
    </h1>
    <hr>
    <ul>
      <li class="list-title">
        <div class="song-item">
          <span class="item-index"></span>
          <span class="item-title">歌曲名</span>
          <span class="item-name">艺人</span>
          <span class="item-intro">专辑</span>
        </div>
      </li>
      <li class="list-content" v-for="(item, index) in songList" :key="index">
        <div class="song-item" :class="{'is-play': id === item.id}"  @click="toplay(item.id, item.url, item.pic, index, item.name, item.lyric)">
          <span class="item-index">
            <span v-if="id !== item.id">{{index + 1}}</span>
            <svg v-if="id === item.id" class="icon" aria-hidden="true">
              <use xlink:href="#icon-yinliang"></use>
            </svg>
          </span>
          <span class="item-title">{{replaceFName(item.name)}}</span>
          <span class="item-name">{{replaceLName(item.name)}}</span>
          <span class="item-intro">{{item.introduction}}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'

export default {
  name: 'album-content',
  mixins: [mixin],
  props: [
    'songList'
  ],
  computed: {
    ...mapGetters([
      'id' // 音乐ID
    ])
  }
}
</script>

<style scoped>
  *{
    box-sizing: border-box;
  }
  .content {
    background-color: white;
    border-radius: 12px;
    padding: 20px 40px;
    min-width: 700px;
  }
  .content .title {
    text-align: center;
  }
  .content > ul {
    width: 100%;
    padding-bottom: 50px;
  }
  .content > ul > li {
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    display: block;
    height: 50px;
    line-height: 50px;
    text-indent: 20px;
  }
  .list-content {
    cursor: pointer;
  }
  .song-item {
    display: flex;
    flex-wrap: nowrap;
    overflow: hidden;
    align-items: center;
    text-overflow:ellipsis;
    white-space: nowrap;
  }
  .item-index {
    width: 5%;
  }
  .item-title {
    width: 30%;
  }
  .item-name {
    width: 20%;
  }
  .item-intro {
    width: 45%;
  }
  .is-play{
    color: #30a4fc;
    font-weight: bold;
   }
  .icon {
    width: 1.5em;
    height: 1.5em;
    vertical-align: -0.3em;
    position: relative;
    right: 5px;
    fill: currentColor;
    overflow: hidden;
  }
</style>
