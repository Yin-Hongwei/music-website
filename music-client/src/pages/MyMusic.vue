<template>
  <div class="my-music">
    <div class="album-bg"></div>
    <div class="album-slide">
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
    </div>
    <div class="album-content">
      <div class="album-title">个性签名: {{introduction}}</div>
      <div class="songs-body">
        <album-content :songList="collectList">
          <template slot="title">我的收藏</template>
        </album-content>
      </div>
    </div>
  </div>
</template>

<script>
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'
import AlbumContent from '../components/AlbumContent'

export default {
  name: 'my-music',
  components: {
    AlbumContent
  },
  mixins: [mixin],
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
  computed: {
    ...mapGetters([
      'userId',
      'id',
      'listOfSongs' // 存放的音乐
    ])
  },
  mounted () {
    this.getMsg(this.userId)
    this.getCollection(this.userId)
  },
  methods: {
    getMsg (id) {
      this.$api.userAPI.getUserOfId(id)
        .then(res => {
          this.username = res.data[0].username
          this.getuserSex(res.data[0].sex)
          this.birth = this.attachBirth(res.data[0].birth)
          this.introduction = res.data[0].introduction
          this.location = res.data[0].location
          this.avator = res.data[0].avator
        })
        .catch(err => {
          console.log(err)
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
      this.$api.collectionAPI.getCollectionOfUser(userId)
        .then(res => {
          this.collection = res.data
          // 通过歌曲ID获取歌曲信息
          for (let item of this.collection) {
            this.getCollectSongs(item.songId)
          }
          this.$store.commit('setListOfSongs', this.collectList)
        })
        .catch(err => {
          console.log(err)
        })
    },
    // 获取收藏的歌曲
    getCollectSongs (id) {
      this.$api.songAPI.getSongOfId(id)
        .then(res => {
          this.collectList.push(res.data[0])
        })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>

<style scoped>
/*歌单背景*/
.album-bg {
  width: 100%;
  height: 200px;
  background-color: #93d2f8;
}

/*歌单左侧*/
.album-slide {
  float: left;
  width: 400px;
}

/*歌单图像*/
.album-img {
  height: 200px;
  width: 200px;
  display: inline-block;
  position: relative;
  top:-100px;
  left: 50px;
  border-radius: 50%;
  overflow: hidden;
  border: 5px solid white;
}

.album-img img {
  width: 100%;
}

/*歌单信息*/
.album-info {
  color: black;
  width: 500px;
  font-size: 20px;
  font-weight: 500;
  margin-top: -100px;
  margin-left: 100px;
  padding: 30px 30px;
}

.album-info li {
  width: 100%;
  line-height: 40px;
}

/*歌单内容*/
.album-content{
  margin-left: 300px;
  padding: 40px 100px;
}

/*歌单题目*/
.album-title {
  font-size: 30px;
  font-weight: 600;
}

.songs-body {
  margin-top: 50px;
}
</style>
