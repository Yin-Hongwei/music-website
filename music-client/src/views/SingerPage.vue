<template>
  <div class="singer-page">
    <div class="singer-header">
      <ul>
        <li
          v-for="(item, index) in singerStyle"
          :key="index"
          :class="{active: item.name === activeName}"
          @click="handleChangeView(item)">
          {{item.name}}
        </li>
      </ul>
    </div>
    <content-list :contentList="albumDatas"></content-list>
  </div>
</template>

<script>
import axios from 'axios'
import ContentList from '../components/ContentList'

export default {
  name: 'singer-page',
  components: {
    ContentList
  },
  data () {
    return {
      singerStyle: [
        {
          name: '全部',
          type: '3'
        },
        {
          name: '男',
          type: '1'
        },
        {
          name: '女',
          type: '0'
        }
      ],
      activeName: '全部',
      albumDatas: []
    }
  },
  created () {
    this.getSingerAll()
  },
  methods: {
    handleChangeView: function (item) {
      this.activeName = item.name
      this.albumDatas = []
      if (item.name === '全部') {
        this.getSingerAll()
      } else if (item.name === '男' || item.name === '女') {
        this.getSingerSex(item.type)
      }
    },
    // 获取所有歌手
    getSingerAll () {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/listSingers`)
        .then(function (res) {
          _this.albumDatas = res.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 通过性别对歌手分类
    getSingerSex (sex) {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/api/singer?sex=${sex}`)
        .then((res) => {
          _this.albumDatas = res.data
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  }
}
</script>

<style scoped>
div, ul, li{
  box-sizing: border-box;
}

.singer-page {
  margin: 30px 10%;
  padding-bottom: 50px;
  background-color: #ffffff;
}

.singer-header {
  width: 100%;
  padding: 0 40px;
}

li {
  display: inline-block;
  line-height: 40px;
  margin: 40px 20px 15px 20px;
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
