<template>
  <div>
    <el-row :gutter="20" class="mgb20">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{padding: '0px'}">
          <div class="grid-content grid-con-1">
            <div class="grid-cont-right">
              <div class="grid-num">{{userCount}}</div>
              <div>用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{padding: '0px'}">
          <div class="grid-content grid-con-2">
            <div class="grid-cont-right">
              <div class="grid-num">{{songCount}}</div>
              <div>歌曲总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{padding: '0px'}">
          <div class="grid-content grid-con-3">
            <div class="grid-cont-right">
              <div class="grid-num">{{singerCount}}</div>
              <div>歌手数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{padding: '0px'}">
          <div class="grid-content grid-con-4">
            <div class="grid-cont-right">
              <div class="grid-num">{{songListCount}}</div>
              <div>歌单数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <h3 style="margin-bottom: 20px">用户性别比例</h3>
    <el-row style="background-color: white">
      <el-col :span="24">
        <ve-pie :data="userSex"></ve-pie>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data () {
    return {
      user: [],
      userSex: {
        columns: ['性别', '总数'],
        rows: [
          { '性别': '男', '总数': 0 },
          { '性别': '女', '总数': 0 }
        ]
      },
      userCount: 0,
      songCount: 0,
      singerCount: 0,
      songListCount: 0
    }
  },
  mounted () {
    this.getUser()
    this.getSinger()
    this.getSong()
    this.getSongList()
  },
  methods: {
    getUser () {
      let _this = this
      _this.$axios.get(`${_this.$store.state.HOST}/AllUsers`).then((res) => {
        _this.user = res.data
        _this.userCount = res.data.length
        _this.userSex.rows[0]['总数'] = _this.setSex(1)
        _this.userSex.rows[1]['总数'] = _this.setSex(0)
      })
    },
    setSex (sex) {
      let count = 0
      for (let item of this.user) {
        if (sex === item.sex) {
          count++
        }
      }
      return count
    },
    getSinger () {
      let _this = this
      _this.$axios.get(`${_this.$store.state.HOST}/listSingers`).then((res) => {
        _this.singerCount = res.data.length
      })
    },
    getSong () {
      let _this = this
      _this.$axios.get(`${_this.$store.state.HOST}/AllSongs`).then((res) => {
        _this.songCount = res.data.length
      })
    },
    getSongList () {
      let _this = this
      _this.$axios.get(`${_this.$store.state.HOST}/listSongLists`).then((res) => {
        _this.songListCount = res.data.length
      })
    }
  }
}
</script>

<style>
  .grid-content {
    display: flex;
    align-items: center;
    height: 100px;
  }

  .grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
  }

  .grid-num {
    font-size: 30px;
    font-weight: bold;
  }
</style>
