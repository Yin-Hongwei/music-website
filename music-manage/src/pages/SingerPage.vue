<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button class="handle-del mr10" type="primary" size="mini" @click="delAll">批量删除</el-button>
        <el-input v-model="select_word" class="handle-input mr10" size="mini" placeholder="筛选关键词"></el-input>
        <el-button type="primary" size="mini" @click="centerDialogVisible = true">添加歌手</el-button>
      </div>
      <el-table ref="multipleTable" size="mini" border style="width: 100%" height="550px" :data="data" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column label="歌手图片" width="110" align="center">
          <template slot-scope="scope">
            <div class="singer-img">
              <img :src="getUrl(scope.row.pic)" alt="" style="width: 100%;"/>
            </div>
            <el-upload
              class="upload-demo"
              :action="uploadUrl(scope.row.id)"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              >
              <el-button size="mini">更新图片</el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="歌手" width="120" align="center"></el-table-column>
        <el-table-column label="性别" width="50" align="center">
          <template slot-scope="scope">
            <div>{{changeSex(scope.row.sex) }}</div>
          </template>
        </el-table-column>
        <el-table-column label="出生" width="120" align="center">
          <template slot-scope="scope">
              <div>{{attachBirth(scope.row.birth) }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="地区" width="100" align="center"></el-table-column>
        <el-table-column label="简介">
          <template slot-scope="scope">
            <p style="height: 100px; overflow: scroll">{{ scope.row.introduction }}</p>
          </template>
        </el-table-column>
        <el-table-column label="歌曲管理" width="110" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="songEdit(scope.row.id, scope.row.name)">歌曲管理</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="currentPage"
          :page-size="pageSize"
          :total="tableData.length"
          @current-change="handleCurrentChange">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="添加歌手" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form
        class="demo-ruleForm"
        :model="registerForm"
        status-icon
        ref="registerForm"
        label-width="80px"
        >
        <el-form-item prop="name" label="歌手名" size="mini">
          <el-input v-model="registerForm.name" placeholder="歌手名"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">组合</el-radio>
            <el-radio :label="3">不明</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="location" label="故乡" size="mini">
          <el-input v-model="registerForm.location" placeholder="故乡"></el-input>
        </el-form-item>
        <el-form-item prop="birth" label="出生" size="mini">
          <el-date-picker
            type="date"
            placeholder="选择日期"
            v-model="registerForm.birth"
            style="width: 100%;"
          ></el-date-picker>
        </el-form-item>
        <el-form-item prop="introduction" label="歌手介绍" size="mini">
          <el-input v-model="registerForm.introduction" type="textarea" placeholder="歌手介绍"></el-input>
        </el-form-item>
      </el-form>
      <span class="dialog-footer" slot="footer">
        <el-button size="mini" @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="addsinger">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
      <el-form ref="form" :model="form" label-width="60px">
        <el-form-item label="歌手" size="mini">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <el-radio-group v-model="form.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">组合</el-radio>
            <el-radio :label="3">不明</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生日" size="mini">
          <el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="地区" size="mini">
          <el-input v-model="form.location"></el-input>
        </el-form-item>
        <el-form-item label="简介" size="mini">
          <el-input type="textarea" v-model="form.introduction"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini"  @click="editVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="saveEdit">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 删除提示框 -->
    <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
      <div class="del-dialog-cnt" align="center">删除不可恢复，是否确定删除？</div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="delVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="deleteRow">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { setSinger, getAllSinger, updateSingerMsg, deleteSinger } from '../api/index'

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
      return `${this.$store.state.HOST}/singer/avatar/update?id=${id}`
    },
    // 添加歌手
    addsinger () {
      let d = this.registerForm.birth
      let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      let params = new URLSearchParams()
      params.append('name', this.registerForm.name)
      params.append('sex', this.registerForm.sex)
      params.append('pic', '/img/singerPic/hhh.jpg')
      params.append('birth', datetime)
      params.append('location', this.registerForm.location)
      params.append('introduction', this.registerForm.introduction)
      setSinger(params)
        .then(res => {
          if (res.code === 1) {
            this.getData()
            this.registerForm = {}
            this.notify('添加成功', 'success')
          } else {
            this.notify('添加失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.centerDialogVisible = false
    },
    // 获取歌手
    getData () {
      this.tableData = []
      this.tempDate = []
      getAllSinger().then(res => {
        this.tableData = res
        this.tempDate = res
        this.currentPage = 1
      })
    },
    // 编辑
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
      let d = new Date(this.form.birth)
      let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      let params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('name', this.form.name)
      params.append('sex', this.form.sex)
      params.append('pic', this.form.pic)
      params.append('birth', datetime)
      params.append('location', this.form.location)
      params.append('introduction', this.form.introduction)
      updateSingerMsg(params)
        .then(res => {
          if (res.code === 1) {
            this.getData()
            this.notify('编辑成功', 'success')
          } else {
            this.notify('编辑失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.editVisible = false
    },
    // 确定删除
    deleteRow () {
      deleteSinger(this.idx)
        .then(res => {
          if (res) {
            this.getData()
            this.notify('删除成功', 'success')
          } else {
            this.notify('删除失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.delVisible = false
    },
    songEdit (id, name) {
      this.$router.push({path: `/song`, query: {id: id, name: name}})
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

.singer-img {
  width: 100%;
  height: 80px;
  border-radius: 5px;
  margin-bottom: 5px;
  overflow: hidden;
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>
