<template>
  <div class="content-list">
    <div v-if="type === 0" class="section" v-for="(list, index) in contentList" :key="index">
      <div class="section-head">{{list.name}}</div>
      <ul class="section-content">
        <li class="content-item" v-for="(item, index) in list.list" :key="index">
          <div class="kuo">
            <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
            <div class="mask"  @click="goRouter(item.id, index)">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-bofang"></use>
              </svg>
            </div>
          </div>
          <p class="item-name">{{item.name || item.title}}</p>
        </li>
      </ul>
    </div>
    <div v-if="type === 1 && styleName === 'singer'">
      <ul class="singer-content">
        <li class="singer-item" v-for="(item, index) in contentList" :key="index">
          <div @click="goAblum(item.id, item.list || index)">
            <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
          </div>
          <p class="item-name">{{item.name || item.title}}</p>
        </li>
      </ul>
    </div>
    <div v-if="type === 1 && styleName === 'songList'">
      <ul class="section-content">
        <li class="content-item" v-for="(item, index) in contentList" :key="index">
          <div class="kuo" @click="goAblum(item.id, item.list || index)">
            <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
          </div>
          <p class="item-name">{{item.name || item.title}}</p>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import {mixin} from '../mixins'

export default {
  name: 'content-list',
  props: [
    'contentList',
    'type',
    'styleName'
  ],
  mixins: [mixin],
  methods: {
    goSongAlbum (id, index) {
      this.$store.commit('setIndex', index)
      window.sessionStorage.setItem('index', JSON.stringify(index))
      this.$router.push({path: '/song-list-album-page/' + id})
    },
    goAblum (id, index) {
      this.$store.commit('setIndex', index)
      window.sessionStorage.setItem('index', JSON.stringify(index))
      if (this.styleName === 'singer') {
        this.$router.push({path: '/singer-album-page/' + id})
      } else if (this.styleName === 'songList') {
        this.$router.push({path: '/song-list-album-page/' + id})
      }
    }
  }
}
</script>

<style scoped>
  *{
    box-sizing: border-box;
  }
  .content-list {
    padding: 0 20px;
  }
  .section {
    width: 100%;
    margin-bottom: 80px;
    padding: 0 120px 50px 120px;
    background-color: #ffffff;
  }
  .section-head {
    height: 100px;
    font-size: 28px;
    font-weight: 500;
    text-align: center;
    line-height: 100px;
    color: black;
  }
  .singer-content{
    -moz-column-count:5; /* Firefox */
    -webkit-column-count:5; /* Safari 和 Chrome */
    column-count:5;
    -moz-column-gap: 2em;
    -webkit-column-gap: 2em;
    column-gap: 2em;
    width: 100%;
    margin:2em auto;
  }
  .singer-item{
    margin-bottom: 2em;
    -moz-page-break-inside: avoid;
    -webkit-column-break-inside: avoid;
    break-inside: avoid;
    border-radius: 6px;
    overflow: hidden;
    -webkit-box-shadow: 0 0 5px 1px rgba(0, 0, 0, 0.1);
    -moz-box-shadow: 0 0 5px 1px rgba(0, 0, 0, 0.1);
    box-shadow: 0 0 5px 1px rgba(0, 0, 0, 0.1);
  }
  .section-content {
    display: flex;
    flex-wrap: wrap;
  }
  .content-item {
    flex-flow: column wrap;
    width: 13vw;
    margin: 5px 1vw;
    overflow: hidden;
    border-radius: 6px;
    -webkit-box-shadow: 0 0 5px 1px rgba(0, 0, 0, 0.1);
    -moz-box-shadow: 0 0 5px 1px rgba(0, 0, 0, 0.1);
    box-shadow: 0 0 5px 1px rgba(0, 0, 0, 0.1);
    position: relative;
  }
  li:hover{
    -webkit-box-shadow: 0 0 10px 4px rgba(0, 0, 0, 0.4);
    -moz-box-shadow: 0 0 10px 4px rgba(0, 0, 0, 0.4);
    box-shadow: 0 0 10px 4px rgba(0, 0, 0, 0.4);
  }
  li:hover .item-img{
    transform: scale(1.1);
  }
  .item-img {
    width: 100%;
    transition: all 0.4s ease;
  }
  .kuo, .mask {
    width: 13vw;
    height: 13vw;
    overflow: hidden;
  }
  .mask{
    position: absolute;
    top:0;
    background-color: rgba(52,47,41,.4);
    transition:all .3s ease-in-out;
    opacity: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .mask:hover{
    opacity: 1;
    cursor: pointer;
  }
  .item-name {
    overflow:hidden;
    text-overflow:ellipsis;
    display:-webkit-box;
    -webkit-box-orient:vertical;
    -webkit-line-clamp:2;
    margin: 10px 8px;
  }
  .icon {
    width: 2em;
    height: 2em;
    fill: currentColor;
    color: rgba(240,240,240,1);
    font-size: 1.6em;
  }

</style>
