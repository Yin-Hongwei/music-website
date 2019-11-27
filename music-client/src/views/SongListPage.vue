<template>
  <div class="song-list-page">
    <div class="song-list-header">
      <ul>
        <li
          v-for="(item, index) in songStyle"
          :key="index"
          :class="{active: item.name === activeName}"
          @click="handleChangeView(item.name)">
          {{item.name}}
        </li>
      </ul>
    </div>
    <div class="song-content">
      <content-list :contentList="albumDatas"></content-list>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { mapGetters } from 'vuex'
import ContentList from '../components/ContentList'

export default {
  name: 'song-list-page',
  components: {
    ContentList
  },
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
      activeName: '全部歌单',
      cur_page: 0, // 当前页
      total: 1, // 总页数
      albumDatas: [] // 歌单
    }
  },
  computed: {
    ...mapGetters([
      'songsList'
    ])
  },
  mounted () {
    this.handleChangeView('全部歌单')
  },
  methods: {
    // 获取歌单（目前只支持通过分页数获取，不支持查询分页）
    getSongList (page) {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/api/songListPage?page=${page}&&size=15`).then((res) => {
        _this.total = res.data.page.totalPages * 10
        _this.albumDatas = res.data._embedded.songLists
      })
    },
    // 换页
    handleCurrentChange (val) {
      this.cur_page = val - 1
      this.getSongList(this.cur_page)
    },
    // 获取歌单
    handleChangeView: function (name) {
      this.activeName = name
      this.albumDatas = []
      if (name === '全部歌单') {
        this.getSongList(this.cur_page)
      } else {
        this.getSongListOfStyle(name)
      }
    },
    // 通过类别获取歌单
    getSongListOfStyle (style) {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/api/songList/likeStyle?style=${style}`)
        .then(res => {
          _this.albumDatas = res.data
        })
    }
  }
}
</script>

<style scoped>
.song-list-page {
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

.pagination {
  display: flex;
  justify-content: center;
}
</style>
