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
import PlayList from '@/components/PlayList'
import { songStyle } from '@/enums'
import { HttpManager } from '@/api'

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
      try {
        this.getSongList()
      } catch (error) {
        console.error(error)
      }
  },
  methods: {
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    // 获取歌单
   async handleChangeView (item) {
      this.activeName = item.name
      this.allPlayList = []
      try {
        if (item.name === '全部歌单') {
          await this.getSongList()
        } else {
          await this.getSongListOfStyle(item.name)
        }
      } catch (error) {
        console.error(error)
      }
    },
    // 获取全部歌单
    async getSongList () {
      this.allPlayList = await HttpManager.getSongList()
      this.currentPage = 1
    },
    // 通过类别获取歌单
    async getSongListOfStyle (style) {
      this.allPlayList = await HttpManager.getSongListOfStyle(style)
      this.currentPage = 1
    }
  }
}
</script>

<style lang='scss' scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.song-sheet {
  margin: 30px 8%;
  margin-top: 0;
  padding-top: $header-height;
  padding-bottom: 50px;
  background-color: $color-white;
}

.song-sheet-header {
  width: 100%;
  li {
    display: inline-block;
    line-height: 3rem;
    margin: 1rem 3rem 1rem 3rem;
    font-size: 1.2rem;
    font-weight: 400;
    color: $color-grey;
    border-bottom: none;
    cursor: pointer;
  }
  li.active {
    color: $color-black;
    font-weight: 600;
    border-bottom: 4px solid $color-black;
  }
}

.pagination {
  @include layout(center);
  transform: translateY(15px);
}

</style>
