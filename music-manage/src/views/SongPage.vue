<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" @click="centerDialogVisible = true">添加歌曲</el-button>
            </div>
            <el-table :data="data" stripe border style="width: 100%" ref="multipleTable" height="500px" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="40"></el-table-column>
                <el-table-column label="歌手图片" width="110">
                    <template slot-scope="scope">
                        <img :src="getUrl(scope.row.pic)" alt="" style="width: 80px;"/>
                        <div class="play" @click="setSongUrl(scope.row.url)">
                            <div v-if="toggle !== scope.row.url">
                              <svg class="icon" aria-hidden="true">
                                <use xlink:href="#icon-bofanganniu"></use>
                              </svg>
                            </div>
                            <div v-if="toggle === scope.row.url">
                              <svg class="icon" aria-hidden="true">
                                <use xlink:href="#icon-zanting"></use>
                              </svg>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="歌名" prop="name" width="150"></el-table-column>
                <el-table-column label="专辑" prop="introduction" width="150"></el-table-column>
                <el-table-column label="歌词">
                    <template slot-scope="scope">
                        <ul style="height: 100px; overflow: scroll">
                            <li>
                            <li v-for="(item, index) in parseLyric(scope.row.lyric)" :key="index">
                                {{ item}}
                            </li>
                        </ul>
                    </template>
                </el-table-column>
                <el-table-column label="歌曲预览" width="100">
                    <template slot-scope="scope">
                        <el-upload
                            class="upload-demo"
                            :action="uploadUrl(scope.row.id)"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                            :before-upload="beforeAvatarUpload">
                            <el-button size="small" type="primary">更新图片</el-button>
                        </el-upload>
                      <br>
                        <el-upload
                            class="upload-demo change"
                            :action="uploadSongUrl(scope.row.id)"
                            :show-file-list="false"
                            :on-success="handleSongSuccess"
                            :before-upload="beforeSongUpload">
                            <el-button size="small" type="primary">更新歌曲</el-button>
                        </el-upload>
                    </template>
                </el-table-column>
                <el-table-column label="评论" width="80">
                    <template  slot-scope="scope">
                        <el-button size="mini" @click="getComment(data[scope.$index].id)">评论</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
          <div class="pagination">
            <el-pagination
              @current-change="handleCurrentChange"
              background
              layout="total, prev, pager, next"
              :current-page="currentPage"
              :page-size="pageSize"
              :total="tableData.length">
            </el-pagination>
          </div>
        </div>

        <!--添加歌曲-->
        <el-dialog title="添加歌曲" :visible.sync="centerDialogVisible" width="400px" center>
            <form action="" id="tf">
                <div style="display: flex; justify-content: space-around;">
                    <div style="width:48%">
                        <label>歌手</label>
                        <el-input type="text" name="singerName" id="singerName"></el-input>
                    </div>
                    <div style="line-height: 80px">-</div>
                    <div style="width:48%">
                        <label>歌曲名</label>
                        <el-input type="text" name="name"></el-input>
                    </div>
                </div>
                <div>
                    <label>歌曲介绍</label>
                    <el-input type="textarea" value="" name="introduction"></el-input>
                </div>
                <div>
                    <label>歌词</label>
                    <el-input type="textarea" value="" name="lyric"></el-input>
                </div>
                <div>
                    <label>歌曲上传</label>
                    <br>
                    <input type="file" name="file" id="upadte-file-input">
                </div>
            </form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="centerDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="getSingerName">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="歌手-歌曲">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="简介">
                    <el-input v-model="form.introduction"></el-input>
                </el-form-item>
                <el-form-item label="歌词">
                    <el-input  type="textarea" v-model="form.lyric"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'
import SongAudio from '../components/SongAudio'
import '@/assets/js/iconfont.js'

export default {
  name: 'song-page',
  components: {
    SongAudio
  },
  mixins: [mixin],
  data () {
    return {
      toggle: false,
      registerForm: {
        name: '',
        singerName: '',
        introduction: '',
        lyric: ''
      },
      tableData: [],
      tempDate: [],
      is_search: false,
      multipleSelection: [], // 记录要删除的歌曲
      centerDialogVisible: false,
      editVisible: false,
      delVisible: false,
      select_word: '',
      form: {
        id: '',
        singerId: '',
        name: '',
        introduction: '',
        createTime: '',
        updateTime: '',
        pic: '',
        lyric: '',
        url: ''
      },
      pageSize: 5, // 页数
      currentPage: 1, // 当前页
      idx: -1
    }
  },
  computed: {
    ...mapGetters([
      'isPlay' // 播放状态
    ]),
    // 计算当前表格中的数据
    data () {
      return this.tableData.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  watch: {
    select_word: function () {
      if (this.select_word === '') {
        this.tableData = this.tempDate
      } else {
        this.tableData = []
        for (let item of this.tempDate) {
          if (item.name.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },
  created () {
    this.getData()
  },
  destroyed () {
    this.$store.commit('setIsPlay', false)
  },
  methods: {
    // 拉取数据
    getData () {
      var _this = this
      _this.tableData = []
      _this.tempDate = []
      _this.$axios.get(`${_this.$store.state.HOST}/AllSongs`).then((res) => {
        console.log('歌曲信息===========>', res.data)
        _this.tableData = res.data
        _this.tempDate = res.data
        _this.currentPage = 1
      })
    },
    setSongUrl (url) {
      this.$store.commit('setUrl', this.$store.state.HOST + url)
      if (this.isPlay) {
        this.$store.commit('setIsPlay', false)
        this.toggle = ''
      } else {
        this.$store.commit('setIsPlay', true)
        this.toggle = url
      }
    },
    // 更新歌曲图片
    uploadUrl (id) {
      return `${this.$store.state.HOST}/api/updateSongPic?id=${id}`
    },
    // 更新歌曲url
    uploadSongUrl (id) {
      return `${this.$store.state.HOST}/api/updateSongUrl?id=${id}`
    },
    beforeSongUpload (file) {
      var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      const extension = testmsg === 'mp3'
      if (!extension) {
        this.$message({
          message: '上传文件只能是mp3格式！',
          type: 'error'
        })
      }
      return extension
    },
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    // 添加音乐
    getSingerName () {
      let _this = this
      let value = document.getElementById('singerName').value
      _this.$axios.get(`${_this.$store.state.HOST}/searachSingers?name=${value}`).then(function (res) {
        if (!res.data.length) {
          _this.$notify({
            title: '系统暂无该该歌手',
            type: 'warning'
          })
        } else {
          _this.addSong(res.data[0].id)
        }
      }).catch(function (error) {
        console.log(error)
      })
    },
    handleSongSuccess (res, file) {
      let _this = this
      if (res.code === 1) {
        _this.getData()
        _this.$notify({
          title: '上传成功',
          type: 'success'
        })
      } else {
        _this.$notify({
          title: '上传失败',
          type: 'error'
        })
      }
    },
    addSong (id) {
      let _this = this
      var form = new FormData(document.getElementById('tf'))
      form.append('singerId', id)
      var req = new XMLHttpRequest()
      req.onreadystatechange = function () {
        if (req.readyState === 4 && req.status === 200) {
          let res = JSON.parse(req.response)
          if (res.code) {
            _this.getData()
            _this.registerForm = {}
            _this.$notify({
              title: res.msg,
              type: 'success'
            })
          } else if (!res.code) {
            _this.$notify({
              title: '上传失败',
              type: 'error'
            })
          }
        }
      }
      req.open('post', `${_this.$store.state.HOST}/api/addSong`, false)
      req.send(form)
      _this.centerDialogVisible = false
    },
    handleEdit (row) {
      this.idx = row.id
      this.form = {
        id: row.id,
        singerId: row.singerId,
        name: row.name,
        introduction: row.introduction,
        createTime: row.createTime,
        updateTime: row.updateTime,
        pic: row.pic,
        lyric: row.lyric,
        url: row.url
      }
      this.editVisible = true
    },
    getComment (id) {
      this.$router.push({path: '/Comment', query: {id: id, type: 0}})
    },
    // 保存编辑
    saveEdit () {
      var params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('singerId', this.form.singerId)
      params.append('name', this.form.name)
      params.append('introduction', this.form.introduction)
      params.append('lyric', this.form.lyric)
      this.$axios.post(`${this.$store.state.HOST}/api/updateSongMsgs`, params)
        .then(response => {
          if (response.data) {
            this.getData()
            this.$notify({
              title: '编辑成功',
              type: 'success'
            })
          } else {
            this.$notify({
              title: '编辑失败',
              type: 'error'
            })
          }
        })
        .catch(failResponse => {})
      this.editVisible = false
    },
    // 确定删除
    deleteRow () {
      var _this = this
      _this.$axios.get(`${_this.$store.state.HOST}/api/deleteSongs?id=${_this.idx}`)
        .then(response => {
          if (response.data) {
            _this.getData()
            _this.$notify({
              title: '删除成功',
              type: 'success'
            })
          } else {
            _this.$notify({
              title: '删除失败',
              type: 'error'
            })
          }
        })
        .catch(failResponse => {})
      _this.delVisible = false
    },
    parseLyric (text) {
      let lines = text.split('\n')
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g
      let result = []
      for (let item of lines) {
        if (pattern.test(item)) {
          let value = item.replace(pattern, '') // 存歌词
          result.push(value)
        }
      }
      return result
    }
  }
}

</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}
.handle-input {
    width: 300px;
    display: inline-block;
}
.el-input__inner{
  background-color: aqua
}
.play {
    position: absolute;
    z-index: 100;
    width: 80px;
    height: 80px;
    top: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
}
.icon {
  width: 2em;
  height: 2em;
  color: white;
  fill: currentColor;
  overflow: hidden;
}
.pagination {
    display: flex;
    justify-content: center;
}
</style>
