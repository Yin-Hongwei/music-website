<template>
  <div class="content-list">
    <ul class="section-content">
      <li class="content-item" v-for="(item, index) in contentList" :key="index">
        <div class="kuo" @click="goAblum(item, item.name)">
          <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
          <div class="mask"  @click="goAblum(item, item.name)">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-bofang"></use>
            </svg>
          </div>
        </div>
        <p class="item-name">{{item.name || item.title}}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import { mixin } from '../mixins'

export default {
  name: 'content-list',
  mixins: [mixin],
  props: [
    'contentList'
  ],
  methods: {
    goAblum (item, type) {
      this.$store.commit('setTempList', item)
      window.sessionStorage.setItem('tempList', JSON.stringify(item))
      if (type) {
        this.$router.push({path: `/singer-album-page/${item.id}`})
      } else {
        this.$router.push({path: `/song-list-album-page/${item.id}`})
      }
    }
  }
}
</script>

<style scoped>
  div, ul, li, p{
    box-sizing: border-box;
  }
  .content-list {
    min-height: 500px;
    padding: 0 20px;
  }
  .section-content {
    display: flex;
    flex-wrap: wrap;
    align-content: flex-start;
  }
  .content-item {
    flex-flow: column wrap;
    width: 18%;
    margin: 20px 1%;
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
    width: 100%;
    padding-bottom: 100%;
    height: 0;
    overflow: hidden;
  }
  .mask{
    position: absolute;
    top:0;
    background-color: rgba(52,47,41,.4);
    transition:all .3s ease-in-out;
    opacity: 0;
    display: flex;
    justify-content: center;
  }
  .mask > .icon {
    position: absolute;
    top: 40%
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
    width: 2.5em;
    height: 2.5em;
    fill: currentColor;
    color: rgba(240,240,240,1);
    font-size: 1.6em;
  }

</style>
