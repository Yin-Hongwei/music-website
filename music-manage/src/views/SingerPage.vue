<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
        <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
        <el-button type="primary" @click="centerDialogVisible = true">添加歌手</el-button>
      </div>
      <el-table ref="multipleTable" stripe border style="width: 100%" height="500px" :data="data" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column label="歌手图片" width="110">
          <template slot-scope="scope">
            <img :src="getUrl(scope.row.pic)" alt style="width: 80px;" />
            <el-upload
              class="upload-demo"
              :action="uploadUrl(scope.row.id)"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <el-button size="small" type="primary">更新图片</el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="歌手" width="120"></el-table-column>
        <el-table-column label="性别" width="60">
          <template slot-scope="scope">
            <div>{{changeSex(scope.row.sex) }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="birth" label="出生" width="100"></el-table-column>
        <el-table-column prop="location" label="地区" width="100"></el-table-column>
        <el-table-column label="简介">
          <template slot-scope="scope">
            <p style="height: 100px; overflow: scroll">{{ scope.row.introduction }}</p>
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

    <el-dialog title="添加歌手" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form
        :model="registerForm"
        status-icon
        ref="registerForm"
        label-width="80px"
        class="demo-ruleForm"
      >
        <el-form-item prop="name" label="歌手名">
          <el-input v-model="registerForm.name" placeholder="歌手名"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="location" label="故乡">
          <el-input v-model="registerForm.location" placeholder="故乡"></el-input>
        </el-form-item>
        <el-form-item prop="birth" label="出生">
          <el-date-picker
            type="date"
            placeholder="选择日期"
            v-model="registerForm.birth"
            style="width: 100%;"
          ></el-date-picker>
        </el-form-item>
        <el-form-item prop="introduction" label="歌手介绍">
          <el-input v-model="registerForm.introduction" placeholder="歌手介绍"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addsinger">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
      <el-form ref="form" :model="form" label-width="60px">
        <el-form-item label="歌手">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="地区">
          <el-input v-model="form.location"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input type="textarea" v-model="form.introduction"></el-input>
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
import { mixin } from '../mixins'

export default {
  name: 'singer-page',
  mixins: [mixin],
  data () {
    return {
      registerForm: {
        // 添加框信息
        name: '',
        sex: '',
        birth: '',
        location: '',
        introduction: ''
      },
      tableData: [],
      tempDate: [],
      multipleSelection: [],
      centerDialogVisible: false,
      editVisible: false,
      delVisible: false,
      select_word: '',
      form: {
        // 编辑框信息
        id: '',
        name: '',
        sex: '',
        pic: '',
        birth: '',
        location: '',
        introduction: ''
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
  methods: {
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    uploadUrl (id) {
      return `${this.$store.state.HOST}/api/updateSingerImg?id=${id}`
    },
    addsinger () {
      let _this = this
      let d = _this.registerForm.birth
      var datetime =
        d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      var params = new URLSearchParams()
      params.append('name', this.registerForm.name)
      params.append('sex', this.registerForm.sex)
      params.append('pic', '/img/singerPic/hhh.jpg')
      params.append('birth', datetime)
      params.append('location', this.registerForm.location)
      params.append('introduction', this.registerForm.introduction)
      _this.$axios.post(`${_this.$store.state.HOST}/api/addSinger`, params)
        .then(res => {
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
        })
        .catch(failResponse => {})
      _this.centerDialogVisible = false
    },
    getData () {
      var _this = this
      _this.tableData = []
      _this.tempDate = []
      _this.$axios.get(`${_this.$store.state.HOST}/listSingers`).then(res => {
        _this.tableData = res.data
        _this.tempDate = res.data
        _this.currentPage = 1
      })
    },
    handleEdit (row) {
      this.editVisible = true
      this.idx = row.id
      let datetime = row.birth
      this.form = {
        id: row.id,
        name: row.name,
        sex: row.sex,
        pic: row.pic,
        birth: datetime,
        location: row.location,
        introduction: row.introduction
      }
    },
    // 保存编辑
    saveEdit () {
      let _this = this
      let d = new Date(_this.form.birth)
      let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      let params = new URLSearchParams()
      params.append('id', _this.form.id)
      params.append('name', _this.form.name)
      params.append('sex', _this.form.sex)
      params.append('pic', _this.form.pic)
      params.append('birth', datetime)
      params.append('location', _this.form.location)
      params.append('introduction', _this.form.introduction)
      _this.$axios.post(`${_this.$store.state.HOST}/api/updateSingerMsgs`, params)
        .then(res => {
          if (res.data.code === 1) {
            _this.getData()
            _this.$notify({
              title: '编辑成功',
              type: 'success'
            })
          } else {
            _this.$notify({
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
      _this.$axios.get(`${_this.$store.state.HOST}/api/deleteSingers?id=${_this.idx}`)
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
      _this.delVisible = false
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
