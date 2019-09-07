<template>
  <div id="song-list-page">
    <ul class="song-list-header">
      <li
        v-for="(item, index) in songStyle"
        :key="index"
        :class="{active: item.name === activeName}"
        @click="handleChangeView(item.type, item.name)">
        {{item.name}}
      </li>
    </ul>
    <component :is="currentView"></component>
  </div>
</template>

<script>
import PageOne from '../components/page/PageOne'
import PageTwo from '../components/page/PageTwo'
import PageThree from '../components/page/PageThree'
import PageFour from '../components/page/PageFour'
import PageFive from '../components/page/PageFive'
import PageSix from '../components/page/PageSix'
import PageSeven from '../components/page/PageSeven'
import PageEight from '../components/page/PageEight'
export default {
  name: 'song-list-page',
  data () {
    return {
      songStyle: [
        {
          name: '全部歌单',
          type: 'One'
        },
        {
          name: '华语',
          type: 'Two'
        },
        {
          name: '粤语',
          type: 'Three'
        },
        {
          name: '欧美',
          type: 'Four'
        },
        {
          name: '日韩',
          type: 'Five'
        },
        {
          name: '轻音乐',
          type: 'Six'
        },
        {
          name: 'BGM',
          type: 'Seven'
        },
        {
          name: '乐器',
          type: 'Eight'
        }
      ],
      currentView: 'pageOne',
      activeName: '全部歌单'
    }
  },
  components: {
    PageOne,
    PageTwo,
    PageThree,
    PageFour,
    PageFive,
    PageSix,
    PageSeven,
    PageEight
  },
  methods: {
    handleChangeView: function (value, name) {
      this.currentView = 'page' + value
      this.activeName = name
    }
  }
}
</script>

<style scoped>
  #song-list-page {
    margin: 30px 150px;
    padding-bottom: 50px;
    min-width: 800px;
    background-color: #ffffff;
  }
  .song-list-header {
    width: 100%;
    padding: 0 40px;
  }
  li {
    display: inline-block;
    line-height: 40px;
    margin: 40px 20px;
    font-size: 20px;
    font-weight: 400;
    color: #67757f;
    border-bottom: none;
    cursor: pointer;
  }
  .active {
    color: black;
    font-weight: 600;
    border-bottom: 4px solid black;
  }

</style>
