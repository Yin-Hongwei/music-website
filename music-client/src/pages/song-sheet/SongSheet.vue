<template>
  <div class='song-sheet'>
    <ul class='song-sheet-header'>
      <li
        v-for='(item, index) in songStyle'
        :key='index'
        :class='{active: item.name === activeName}'
        @click='handleChangeView(item)'>
        {{item.name}}
      </li>
    </ul>
    <play-list :playList='data' path='song-sheet-detail'></play-list>
    <div class='pagination'>
      <el-pagination
        @current-change='handleCurrentChange'
        background
        layout='total, prev, pager, next'
        :current-page='currentPage'
        :page-size='pageSize'
        :total='allPlayList.length'>
      </el-pagination>
    </div>
  </div>
</template>

<script>
import PlayList from '../../components/PlayList'
import { songStyle } from '../../enums'
import { HttpManager } from '../../api'

export default {
  name: 'SongSheet',
  components: {
    PlayList
  },
  data () {
    return {
      songStyle: songStyle, // 歌单导航栏类别
      activeName: '全部歌单',
      pageSize: 15, // 页数
      currentPage: 1, // 当前页
      allPlayList: [] // 歌单
    }
  },
  computed: {
    // 计算当前表格中的数据
    data () {
      return this.allPlayList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  created () {
    this.getSongList()
  },
  methods: {
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    // 获取歌单
    handleChangeView (item) {
      this.activeName = item.name
      this.allPlayList = []
      if (item.name === '全部歌单') {
        this.getSongList()
      } else {
        this.getSongListOfStyle(item.name)
      }
    },
    // 获取全部歌单
    getSongList () {
      HttpManager.getSongList()
        .then(res => {
          this.currentPage = 1
          this.allPlayList = res
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 通过类别获取歌单
    getSongListOfStyle (style) {
      HttpManager.getSongListOfStyle(style)
        .then(res => {
          this.currentPage = 1
          this.allPlayList = res
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/song-sheet.scss';
</style>
