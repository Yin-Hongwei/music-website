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
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'

export default {
  name: 'comment',
  mixins: [mixin],
  props: [
    'playId', // 歌曲ID或歌单ID
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
      'id',
      'userId', // 用户ID
      'index', // 列表中的序号
      'loginIn', // 用户是否登录
      'avator' // 用户头像
    ])
  },
  watch: {
    id () {
      this.getComment()
    }
  },
  mounted () {
    this.getComment()
  },
  methods: {
    // 获取所有评论
    getComment () {
      this.$api.commentAPI.getAllComment(this.type, this.playId)
        .then(res => {
          this.commentList = res.data
          for (let item of res.data) {
            this.getUsers(item.userId)
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    // 获取评论用户的昵称和头像
    getUsers (id) {
      this.$api.userAPI.getUserOfId(id)
        .then(res => {
          this.userPic.push(res.data[0].avator)
          this.userName.push(res.data[0].username)
        })
        .catch(err => {
          console.log(err)
        })
    },
    // 提交评论
    postComment () {
      if (this.loginIn) {
        this.$api.commentAPI.setComment(this.type, this.playId, this.userId, this.textarea)
          .then(res => {
            if (res.data.code === 1) {
              this.textarea = ''
              this.getComment()
              this.notify('评论成功', 'success')
            } else {
              this.notify('评论失败', 'error')
            }
          })
          .catch(err => {
            console.log(err)
          })
      } else {
        this.notify('请先登录', 'warning')
      }
    },
    // 点赞
    postUp (id, up, index) {
      if (this.loginIn) {
        this.$api.commentAPI.setLike(id, up)
          .then(res => {
            if (res.data.code === 1) {
              this.$refs.up[index].children[0].style.color = '#2796dd'
              this.getComment()
            }
          })
          .catch(err => {
            console.log(err)
          })
      } else {
        this.notify('请先登录', 'warning')
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
