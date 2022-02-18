<template>
  <div class='play-list'>
    <div class='section-title' v-if='title'>{{title}}</div>
    <ul class='section-content'>
      <li class='content-item' v-for='(item, index) in playList' :key='index'>
        <div class='kuo' @click='goAblum(item)'>
          <img class='item-img' :src='attachImageUrl(item.pic)' alt=''>
          <div class='mask'  @click='goAblum(item)'>
            <svg class='icon' aria-hidden='true'>
              <use :xlink:href='BOFANG'></use>
            </svg>
          </div>
        </div>
        <p class='item-name'>{{item.name || item.title}}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import mixin from '../mixins'
import { ICON } from '../enums'

export default {
  name: 'PlayList',
  mixins: [mixin],
  props: {
    title: String,
    playList: Array,
    path: String
  },
  data () {
    return {
      BOFANG: ICON.BOFANG
    }
  },
  methods: {
    goAblum (item) {
      this.$store.commit('setSongDetails', item)
      this.routerManager(this.path, { path: `/${this.path}/${item.id}` })
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/play-list.scss';
</style>
