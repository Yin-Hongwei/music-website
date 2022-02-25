<template>
  <div class='personal'>
    <div class='personal-info'>
      <div class='personal-img'>
        <img :src='userPic ? attachImageUrl(userPic) : userPic' alt='' @click="dialogTableVisible=true">
      </div>
      <div class="personal-msg">
        <div class="username">{{username}}</div>
        <div class='introduction'>{{introduction}}</div>
      </div>
      <el-button class="edit-info" round :icon="Edit" @click="goPage()">修改个人信息</el-button>
    </div>
    <div class='personal-body'>
      <song-list :songList='collectSongList'>
        <template v-slot:title>我的收藏</template>
      </song-list>
    </div>
    <el-dialog v-model="dialogTableVisible" title="修改头像">
      <upload></upload>
    </el-dialog>
  </div>
</template>

<script setup>
import { Edit } from '@element-plus/icons-vue'
import SongList from '@/components/SongList'
import Upload from './Upload'
</script>

<script>
import { mapGetters } from 'vuex'
import mixin from '@/mixins'
import { HttpManager } from '@/api'
import { PERSONAL_DATA } from '@/enums'

export default {
  mixins: [mixin],
  data () {
    return {
      username: '',
      userSex: '',
      birth: '',
      location: '',
      introduction: '',
      collectSongList: [], // 收藏的歌曲
      dialogTableVisible: false
    }
  },
  computed: {
    ...mapGetters([
      'userPic'
    ])
  },
  watch: {
    userPic() {
      this.dialogTableVisible = false
    }
  },
  created () {
    this.getUserInfo(this.userId)
    this.getCollection(this.userId)
  },
  methods: {
    goPage() {
      this.routerManager(PERSONAL_DATA, { path: PERSONAL_DATA})
    },
    getUserInfo (id) {
      HttpManager.getUserOfId(id)
        .then(res => {
          this.username = res[0].username
          this.userSex = res[0].sex
          this.birth = res[0].birth
          this.introduction = res[0].introduction
          this.location = res[0].location
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
          for (const item of collectIDList) {
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

<style lang='scss' scoped>
@import '@/assets/css/personal.scss';
</style>
