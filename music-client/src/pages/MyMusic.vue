<template>
  <div class="my-music">
    <div class="album-slide">
      <div class="album-img">
        <img :src="attachImageUrl(userPic)" alt="">
      </div>
      <ul class="album-info">
        <li>昵称： {{username}}</li>
        <li>性别： {{getUserSex(userSex)}}</li>
        <li>生日： {{getBirth(birth)}}</li>
        <li>故乡： {{location}}</li>
      </ul>
    </div>
    <div class="song-list">
      <div class="album-title">个性签名: {{introduction}}</div>
      <div class="songs-body">
        <song-list :songList="collectSongList">
          <template slot="title">我的收藏</template>
        </song-list>
      </div>
    </div>
  </div>
</template>

<script>
import mixin from '../mixins'
import SongList from '../components/SongList'
import { HttpManager } from '../api'

export default {
  name: 'MyMusic',
  mixins: [mixin],
  components: {
    SongList
  },
  data () {
    return {
      userPic: '',
      username: '',
      userSex: '',
      birth: '',
      location: '',
      introduction: '',
      collectSongList: [] // 收藏的歌曲
    }
  },
  mounted () {
    this.getUserInfo(this.userId)
    this.getCollection(this.userId)
  },
  methods: {
    getUserInfo (id) {
      HttpManager.getUserOfId(id)
        .then(res => {
          this.username = res[0].username
          this.userSex = res[0].sex
          this.birth = res[0].birth
          this.introduction = res[0].introduction
          this.location = res[0].location
          this.userPic = res[0].avator
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 收藏的歌曲ID
    getCollection (userId) {
      HttpManager.getCollectionOfUser(userId)
        .then(res => {
          const collectIDList = res || [] // 存放收藏的歌曲ID
          // 通过歌曲ID获取歌曲信息
          for (let item of collectIDList) {
            this.getCollectSongList(item.songId)
          }
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 获取收藏的歌曲
    getCollectSongList (id) {
      HttpManager.getSongOfId(id)
        .then(res => {
          this.collectSongList.push(res[0])
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/css/my-music.scss';
</style>
