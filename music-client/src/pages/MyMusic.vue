<template>
  <div class="my-music">
    <div class="album-slide">
      <div class="album-img">
        <img :src=attachImageUrl(avator) alt="">
      </div>
      <ul class="album-info">
        <li>昵称： {{username}}</li>
        <li>性别： {{userSex}}</li>
        <li>生日： {{birth}}</li>
        <li>故乡： {{location}}</li>
      </ul>
    </div>
    <div class="song-list">
      <div class="album-title">个性签名: {{introduction}}</div>
      <div class="songs-body">
        <song-list :songList="collectList">
          <template slot="title">我的收藏</template>
        </song-list>
      </div>
    </div>
  </div>
</template>

<script>
import mixin from '../mixins'
import SongList from '../components/SongList'
import { HttpManager } from '../api/index'

export default {
  name: 'MyMusic',
  mixins: [mixin],
  components: {
    SongList
  },
  data () {
    return {
      avator: '',
      username: '',
      userSex: '',
      birth: '',
      location: '',
      introduction: '',
      collection: [], // 存放收藏的歌曲ID
      collectList: [] // 收藏的歌曲
    }
  },
  mounted () {
    this.getMsg(this.userId)
    this.getCollection(this.userId)
  },
  methods: {
    getMsg (id) {
      HttpManager.getUserOfId(id)
        .then(res => {
          this.username = res[0].username
          this.getuserSex(res[0].sex)
          this.birth = this.attachBirth(res[0].birth)
          this.introduction = res[0].introduction
          this.location = res[0].location
          this.avator = res[0].avator
        })
        .catch(err => {
          console.error(err)
        })
    },
    getuserSex (sex) {
      if (sex === 0) {
        this.userSex = '女'
      } else if (sex === 1) {
        this.userSex = '男'
      }
    },
    // 收藏的歌曲ID
    getCollection (userId) {
      HttpManager.getCollectionOfUser(userId)
        .then(res => {
          this.collection = res
          // 通过歌曲ID获取歌曲信息
          for (let item of this.collection) {
            this.getCollectSongs(item.songId)
          }
          this.$store.commit('setListOfSongs', this.collectList)
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 获取收藏的歌曲
    getCollectSongs (id) {
      HttpManager.getSongOfId(id)
        .then(res => {
          this.collectList.push(res[0])
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/my-music.scss';
</style>
