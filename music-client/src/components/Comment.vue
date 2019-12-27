<template>
  <div>
    <div class="comment">
      <h2>评论</h2>
      <div class="comment-msg">
        <div class="comment-img">
          <img :src=attachImageUrl(avator) alt="">
        </div>
        <el-input
          class="comment-input"
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="textarea">
        </el-input>
      </div>
      <el-button type="primary" class="sub-btn" @click="postComment()">评论</el-button>
    </div>
    <p>精彩评论: 共 {{commentList.length}} 条评论</p>
    <ul class="popular" v-for="(item, index) in commentList" :key="index">
      <li>
        <div class="popular-img">
          <img :src=attachImageUrl(userPic[index]) alt="">
        </div>
        <div class="popular-msg">
          <ul>
            <li class="name">{{userName[index]}}</li>
            <li class="time">{{item.createTime}}</li>
            <li class="content">{{item.content}}</li>
          </ul>
        </div>
        <div class="up" ref="up" @click="postUp(item.id, item.up, index)">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-zan"></use>
          </svg>
          {{item.up}}
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios'
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'

export default {
  name: 'comment',
  mixins: [mixin],
  props: [
    'id', // 歌曲ID或歌单ID
    'type' // 歌单（1）/歌曲（0）
  ],
  data () {
    return {
      commentList: [], // 存放评论内容
      userPic: [], // 保存评论用户头像
      userName: [], // 保存评论用户名字
      textarea: '' // 存放输入内容
    }
  },
  computed: {
    ...mapGetters([
      'userId', // 用户ID
      'index', // 列表中的序号
      'loginIn', // 用户是否登录
      'avator' // 用户头像
    ])
  },
  mounted () {
    this.getComment()
  },
  methods: {
    // 获取所有评论
    getComment () {
      let _this = this
      let url = ''
      if (this.type === 1) {
        url = '/songListComments?songListId='
      } else if (this.type === 0) {
        url = '/songComments?songId='
      }
      axios.get(_this.$store.state.HOST + url + _this.id)
        .then(function (res) {
          _this.commentList = res.data
          for (let item of res.data) {
            _this.getUsers(item.userId)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 获取评论用户的昵称和头像
    getUsers (id) {
      let _this = this
      axios.get(`${_this.$store.state.HOST}/commentOfConsumer?id=${id}`)
        .then(function (res) {
          _this.userPic.push(res.data[0].avator)
          _this.userName.push(res.data[0].username)
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 提交评论
    postComment () {
      if (this.loginIn) {
        // 0 代表歌曲， 1 代表歌单
        let _this = this
        var params = new URLSearchParams()
        if (this.type === 1) {
          params.append('songListId', _this.id)
        } else if (this.type === 0) {
          params.append('songId', _this.id)
        }
        params.append('userId', _this.userId)
        params.append('type', _this.type)
        params.append('comtent', _this.textarea)
        axios.post(`${_this.$store.state.HOST}/api/commentList`, params)
          .then(res => {
            console.log(res.data)
            if (res.data.code === 1) {
              _this.textarea = ''
              _this.getComment()
              _this.$notify({
                title: '评论成功',
                type: 'success'
              })
            } else {
              _this.$notify({
                title: '评论失败',
                type: 'error'
              })
            }
          })
          .catch(failResponse => {})
      } else {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      }
    },
    // 点赞
    postUp (id, up, index) {
      if (this.loginIn) {
        let _this = this
        var params = new URLSearchParams()
        params.append('id', id)
        params.append('up', up + 1)
        axios.post(`${_this.$store.state.HOST}/api/postUp`, params)
          .then(res => {
            if (res.data.code === 1) {
              _this.$refs.up[index].children[0].style.color = '#2796dd'
              _this.getComment()
            }
          })
          .catch(failResponse => {})
      } else {
        this.$notify({
          title: '请先登录',
          type: 'warning'
        })
      }
    }
  }
}
</script>

<style scoped>
  /*评论*/
  .comment h2 {
    margin-bottom: 20px;
    text-align: center;
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid black;
  }
  .comment-msg {
    display: flex;
    flex-direction: row;
  }
  .comment-img {
    width: 70px;
  }
  .comment-img img {
    width: 100%;
  }
  .comment-input {
    margin-left: 10px;
    flex: 1;
  }
  .sub-btn{
    margin-top: 10px;
    margin-left: 90%;
  }
  /*热门评论*/
  .popular {
    width: 100%;
  }
  .popular > li{
    border-bottom: solid 1px rgba(0, 0, 0, 0.1);
    padding: 15px 0;
    display: flex;
    flex-direction: row;
  }
  .popular-img {
    width: 50px;
  }
  .popular-img img {
    width: 100%;
  }
  .popular-msg {
    padding: 0 20px;
    flex: 1;
  }
  .popular-msg li {
    width: 100%;
  }
  .popular-msg .time {
    font-size: .6rem;
    color: rgba(0, 0, 0, 0.5);
  }
  .popular-msg .content {
    font-size: 1rem;
  }
  .up {
    width: 50px;
    line-height: 60px;
  }
  .icon {
    width: 1em;
    height: 1em;
    fill: currentColor;
    color: black;
    font-size: 1.5em;
  }
</style>
