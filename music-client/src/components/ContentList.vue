<template>
  <div class="content-list">
    <ul class="section-content">
      <li class="content-item" v-for="(item, index) in contentList" :key="index">
        <div class="kuo" @click="goAblum(item)">
          <img class="item-img" :src="attachImageUrl(item.pic)" alt="">
          <div class="mask"  @click="goAblum(item)">
            <svg class="icon" aria-hidden="true">
              <use :xlink:href="BOFANG"></use>
            </svg>
          </div>
        </div>
        <p class="item-name">{{item.name || item.title}}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import mixin from '../mixins'
import { ICON } from '../assets/icon/index'

export default {
  name: 'content-list',
  mixins: [mixin],
  props: {
    contentList: Array,
    path: String
  },
  data () {
    return {
      BOFANG: ICON.BOFANG
    }
  },
  methods: {
    goAblum (item) {
      this.$store.commit('setTempList', item)
      this.$router.push({path: `/${this.path}/${item.id}`})
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/content-list.scss';
</style>
