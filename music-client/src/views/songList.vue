<template>
  <div class="body-section">
    <nav class="songsList-nav" ref="change">
      <span class="num-one" @click="handleChangeView('One')">全部歌单</span>
      <span @click="handleChangeView('Two', 1)">华语</span>
      <span @click="handleChangeView('Three', 2)">粤语</span>
      <span @click="handleChangeView('Four', 3)">欧美</span>
      <span @click="handleChangeView('Five', 4)">日韩</span>
      <span @click="handleChangeView('Six', 5)">轻音乐</span>
      <span @click="handleChangeView('Seven', 6)">BGM</span>
      <span @click="handleChangeView('Eight', 7)">乐器</span>
    </nav>
    <component :is="currentView"></component>
  </div>
</template>

<script>
import pageOne from '../components/page/pageOne'
import pageTwo from '../components/page/pageTwo'
import pageThree from '../components/page/pageThree'
import pageFour from '../components/page/pageFour'
import pageFive from '../components/page/pageFive'
import pageSix from '../components/page/pageSix'
import pageSeven from '../components/page/pageSeven'
import pageEight from '../components/page/pageEight'
export default {
  name: 'song-list',
  data () {
    return {
      currentView: 'pageOne'
    }
  },
  components: {
    pageOne,
    pageTwo,
    pageThree,
    pageFour,
    pageFive,
    pageSix,
    pageSeven,
    pageEight
  },
  methods: {
    handleChangeView: function (component, index) {
      this.currentView = 'page' + component
      this.changeIndex(index)
    },
    changeIndex (index) {
      for (let i = 1; i < 8; i++) {
        this.$refs.change.children[i].style.color = '#67757f'
        this.$refs.change.children[i].style.fontWeight = '300'
        this.$refs.change.children[i].style.borderBottom = 'none'
      }
      this.$refs.change.children[index].style.color = 'black'
      this.$refs.change.children[index].style.fontWeight = '400'
      this.$refs.change.children[index].style.borderBottom = '3px solid black'
    }
  }
}
</script>

<style scoped>
  .body-section {
    margin: 30px 150px;
    padding-bottom: 50px;
    min-width: 800px;
    background-color: #ffffff;
  }
  .songsList-nav:first-child {
    font-size: 28px;
    font-weight: 500;
    color: black;
    margin-left: 40px;
  }
  span {
    display: inline-block;
    line-height: 50px;
    margin: 40px 10px;
    cursor: pointer;
  }
  span:not(.num-one) {
    font-size: 20px;
    font-weight: 300;
    color: black;
  }

</style>
