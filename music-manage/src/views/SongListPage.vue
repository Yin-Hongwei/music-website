<template>
    <div class="table">
        <div class="container">
          <div class="handle-box">
                <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" @click="centerDialogVisible = true">添加歌单</el-button>
            </div>
          <el-table :data="data" border stripe style="width: 100%" height="500px" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="40"></el-table-column>
                <el-table-column label="歌单图片" width="100">
                    <template slot-scope="scope">
                        <img :src="getUrl(scope.row.pic)" alt="" style="width: 80px;"/>
                            <el-upload
                                class="upload-demo"
                                :action="uploadUrl(scope.row.id)"
                                :show-file-list="false"
                                :on-success="handleAvatarSuccess"
                                :before-upload="beforeAvatarUpload">
                                <el-button size="small" type="primary">更新图片</el-button>
                            </el-upload>
                    </template>
                </el-table-column>
                <el-table-column prop="title" label="标题" width="200"></el-table-column>
                <el-table-column label="简介">
                    <template slot-scope="scope">
                        <p style="height: 100px; overflow: scroll">{{ scope.row.introduction }}</p>
                    </template>
                </el-table-column>
                <el-table-column label="风格" width="100">
                    <template slot-scope="scope">
                        <div>{{ scope.row.style }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="内容" width="80">
                    <template  slot-scope="scope">
                        <el-button size="mini" @click="getContent(data[scope.$index].id)">内容</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="评论" width="80">
                    <template  slot-scope="scope">
                        <el-button size="mini" @click="getComment(data[scope.$index].id)">评论</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            @click="handleDelete(scope.row.id)">删除</el-button>
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

        <!--添加歌单-->
        <el-dialog title="添加歌单" :visible.sync="centerDialogVisible" width="400px" center>
            <el-form :model="registerForm" status-icon ref="registerForm" label-width="70px" class="demo-ruleForm">
                <el-form-item prop="title" label="歌单名">
                    <el-input v-model="registerForm.title" placeholder="歌单名"></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="歌单介绍">
                    <el-input v-model="registerForm.introduction" placeholder="歌单介绍"></el-input>
                </el-form-item>
                <el-form-item prop="style" label="风格">
                    <el-input v-model="registerForm.style" placeholder="风格"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="centerDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addsongList">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
            <el-form ref="form" :model="form" label-width="40px">
                <el-form-item label="标题">
                    <el-input v-model="form.title"></el-input>
                </el-form-item>
                <el-form-item label="简介">
                    <el-input  type="textarea" v-model="form.introduction"></el-input>
                </el-form-item>
                <el-form-item label="风格">
                    <el-input v-model="form.style"></el-input>
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

export default {
  name: 'song-list-page',
  data () {
    return {
      registerForm: {
        title: '',
        introduction: '',
        style: ''
      },
      tableData: [],
      tempDate: [],
      multipleSelection: [], // 记录要删除的歌单
      centerDialogVisible: false,
      editVisible: false,
      delVisible: false,
      select_word: '',
      form: {
        id: '',
        title: '',
        pic: '',
        introduction: '',
        style: ''
      },
      pageSize: 5, // 页数
      currentPage: 1, // 当前页
      idx: -1
    }
  },
  computed: {
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
          if (item.title.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },
  created () {
    this.getData()
  },
  mixins: [mixin],
  methods: {
    uploadUrl (id) {
      return `${this.$store.state.HOST}/api/updateSongListImg?id=${id}`
    },
    // 获取歌单信息
    getData () {
      let _this = this
      _this.tableData = []
      _this.tempDate = []
      _this.$axios.get(`${_this.$store.state.HOST}/listSongLists`).then((res) => {
        _this.tableData = res.data
        _this.tempDate = res.data
        _this.currentPage = 1
      })
    },
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    getContent (id) {
      this.$router.push({path: '/listSong', query: {id: id}})
    },
    getComment (id) {
      this.$router.push({path: '/Comment', query: {id: id, type: 1}})
    },
    handleEdit (row) {
      this.idx = row.id
      this.form = {
        id: row.id,
        title: row.title,
        pic: row.pic,
        introduction: row.introduction,
        style: row.style
      }
      this.editVisible = true
    },
    // 保存编辑
    saveEdit () {
      let _this = this
      var params = new URLSearchParams()
      params.append('id', _this.form.id)
      params.append('title', _this.form.title)
      params.append('pic', _this.form.pic)
      params.append('introduction', _this.form.introduction)
      params.append('style', _this.form.style)
      _this.$axios.post(`${_this.$store.state.HOST}/api/updateSongListMsgs`, params)
        .then(res => {
          if (res.data.code === 1) {
            _this.$notify({
              title: '编辑成功',
              type: 'success'
            })
            _this.getData()
          } else {
            _this.$notify({
              title: '编辑失败',
              type: 'error'
            })
          }
        })
        .catch(failResponse => {})
      _this.editVisible = false
    },
    // 添加歌单
    addsongList () {
      let _this = this
      let params = new URLSearchParams()
      params.append('title', _this.registerForm.title)
      params.append('pic', '/img/songListPic/123.jpg')
      params.append('introduction', _this.registerForm.introduction)
      params.append('style', _this.registerForm.style)
      _this.$axios.post(`${_this.$store.state.HOST}/api/addSongList`, params).then(res => {
        if (res.data.code === 1) {
          _this.getData()
          _this.registerForm = {}
          _this.$notify({
            title: '添加成功',
            type: 'success'
          })
        } else {
          _this.$notify({
            title: '添加失败',
            type: 'error'
          })
        }
      }).catch(failResponse => {})
      _this.centerDialogVisible = false
    },
    // 确定删除
    deleteRow () {
      let _this = this
      _this.$axios.get(`${_this.$store.state.HOST}/api/deleteSongLists?id=${_this.idx}`)
        .then(res => {
          if (res.data) {
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
      this.delVisible = false
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
.pagination {
    display: flex;
    justify-content: center;
}
</style>
