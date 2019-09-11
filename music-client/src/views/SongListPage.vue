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
      <content-list :contentList="albumDatas" :type="1" :styleName="'songList'"></content-list>
    </div>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import ContentList from '../components/ContentList'

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
      activeName: '全部歌单',
      albumDatas: []
    }
  },
  computed: {
    ...mapGetters([
      'songsList'
    ])
  },
  components: {
    ContentList
  },
  mounted () {
    this.getSongLists()
    this.handleChangeView('全部歌单')
  },
  mixins: [mixin],
  methods: {
    handleChangeView: function (name) {
      let pattern = new RegExp(name)
      this.activeName = name
      this.albumDatas = []
      if (name === '全部歌单') {
        this.albumDatas = this.songsList
      } else {
        this.getAlbumDatas(pattern)
      }
    },
    getAlbumDatas (pattern) {
      for (let i = 0; i < this.songsList.length; i++) {
        if (pattern.test(this.songsList[i].style)) {
          var item = this.songsList[i]
          item.list = i
          this.albumDatas.push(item)
        }
      }
    },
    goSongAlbum (id, index) {
      this.$store.commit('setIndex', index)
      window.sessionStorage.setItem('index', JSON.stringify(index))
      this.$router.push({path: '/song-list-album-page/' + id})
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
