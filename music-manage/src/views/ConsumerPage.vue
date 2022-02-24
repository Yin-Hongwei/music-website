<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="small" class="handle-del mr10" @click="delAll">批量删除</el-button>
        <el-input v-model="select_word" size="small" placeholder="筛选相关用户" class="handle-input mr10"></el-input>
        <el-button type="primary" size="small" @click="centerDialogVisible = true">添加新用户</el-button>
      </div>
      <el-table :data="data" border size="small" style="width: 100%" ref="multipleTable" height="550px" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40" align="center"></el-table-column>
        <el-table-column label="歌手图片" width="102" align="center">
          <template v-slot="scope">
            <img :src="getUrl(scope.row.avator)" alt="" style="width: 80px;"/>
            <el-upload
              class="upload-demo"
              :action="uploadUrl(scope.row.id)"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <el-button size="small">更新图片</el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column label="用户名" prop="username" width="80" align="center"></el-table-column>
        <el-table-column label="密码" prop="password" width="80" align="center"></el-table-column>
        <el-table-column label="性别" width="50" align="center">
            <template v-slot="scope">
                <div>{{changeSex(scope.row.sex) }}</div>
            </template>
        </el-table-column>
        <el-table-column label="手机号码" prop="phoneNum" width="120" align="center"></el-table-column>
        <el-table-column label="邮箱" prop="email" width="120" align="center"></el-table-column>
        <el-table-column label="生日" width="120" align="center">
            <template v-slot="scope">
                <div>{{getBirth(scope.row.birth)}}</div>
            </template>
        </el-table-column>
        <el-table-column prop="introduction" label="签名" align="center"></el-table-column>
        <el-table-column prop="location" label="地区" width="80" align="center"></el-table-column>
        <el-table-column label="收藏" width="80" align="center">
            <template  v-slot="scope">
                <el-button size="small" @click="getCollect(data[scope.$index].id)">收藏</el-button>
            </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
            <template v-slot="scope">
                <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
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

    <!--添加新用户-->
    <el-dialog title="添加用户" v-model="centerDialogVisible" width="400px" center>
      <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" label-width="70px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="username" size="small">
          <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" size="small">
          <el-input type="password" placeholder="密码" v-model="registerForm.password"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="small">
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">保密</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机" prop="phoneNum" size="small">
          <el-input  placeholder="手机" v-model="registerForm.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" size="small">
          <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="生日" prop="birth" size="small">
            <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="签名" prop="introduction" size="small">
          <el-input  type="textarea" placeholder="签名" v-model="registerForm.introduction" ></el-input>
        </el-form-item>
        <el-form-item label="地区" prop="location" size="small">
          <el-select v-model="registerForm.location" placeholder="地区">
            <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button size="small" @click="centerDialogVisible = false">取 消</el-button>
          <el-button type="primary" size="small" @click="addPeople">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" v-model="editVisible" width="400px">
      <el-form ref="form" :model="form" label-width="60px">
        <el-form-item label="用户名" size="small">
          <el-input v-model="form.username" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="密码" size="small">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="small">
          <el-radio-group v-model="form.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">保密</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机" size="small">
          <el-input v-model="form.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" size="small">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="生日" prop="birth" size="small">
          <el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="签名" size="small">
          <el-input v-model="form.introduction"></el-input>
        </el-form-item>
        <el-form-item label="地区" size="small">
          <el-input v-model="form.location"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button size="small" @click="editVisible = false">取 消</el-button>
          <el-button type="primary" size="small" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 删除提示框 -->
    <yin-del-dialog :delVisible="delVisible" @deleteRow="deleteRow" @cancelRow="delVisible = $event"></yin-del-dialog>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { HttpManager } from '../api/index'
import { RULES, AREA, COLLECT } from '../enums'
import { getDateTime } from '../utils'
import YinDelDialog from '@/components/dialog/YinDelDialog'

export default {
  name: 'ConsumerPage',
  mixins: [mixin],
  components: {
    YinDelDialog
  },
  data () {
    return {
      registerForm: { // 注册
        username: '',
        password: '',
        sex: '',
        phoneNum: '',
        email: '',
        birth: '',
        introduction: '',
        location: ''
      },
      form: { // 记录编辑的信息
        id: '',
        username: '',
        password: '',
        sex: '',
        phoneNum: '',
        email: '',
        birth: '',
        introduction: '',
        location: '',
        createTime: '',
        updateTime: ''
      },
      cities: AREA,
      rules: RULES,
      userPic: '/images/user.jpg',
      tableData: [], // 记录用户信息，用于显示
      tempDate: [], // 记录用户信息，用于搜索时能临时记录一份用户信息
      is_search: false,
      multipleSelection: [], // 记录要删除的用户信息
      centerDialogVisible: false,
      editVisible: false, // 显示编辑框
      delVisible: false, // 显示删除框
      select_word: '', // 记录输入框输入的内容
      pageSize: 5, // 页数
      currentPage: 1, // 当前页
      idx: -1 // 记录当前点中的行
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
          if (item.username.includes(this.select_word)) {
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
      return `${this.$store.state.HOST}/user/avatar/update?id=${id}`
    },
    // 获取用户信息
    getData () {
      this.tableData = []
      this.tempDate = []
      HttpManager.getAllUser().then((res) => {
        this.tableData = res
        this.tempDate = res
        this.currentPage = 1
      })
    },
    getCollect (id) {
      this.routerManager(COLLECT, {path: COLLECT, query: { id }})
    },
    // 添加用户
    addPeople () {
      let datetime = getDateTime(this.registerForm.birth)
      let params = new URLSearchParams()
      params.append('username', this.registerForm.username)
      params.append('password', this.registerForm.password)
      params.append('sex', this.registerForm.sex)
      params.append('phone_num', this.registerForm.phoneNum)
      params.append('email', this.registerForm.email)
      params.append('birth', datetime)
      params.append('introduction', this.registerForm.introduction)
      params.append('location', this.registerForm.location)
      params.append('avator', this.userPic)
      HttpManager.setUser(params)
        .then(res => {
          if (res.code === 1) {
            this.getData()
            this.registerForm = {}
            this.$notify({
              title: '添加成功',
              type: 'success'
            })
          } else {
            this.$notify({
              title: '添加失败',
              type: 'error'
            })
          }
        })
        .catch(err => {
          console.error(err)
        })
      this.centerDialogVisible = false
    },
    // 编辑
    handleEdit (row) {
      this.idx = row.id
      this.form = {
        id: row.id,
        username: row.username,
        password: row.password,
        sex: row.sex,
        phoneNum: row.phoneNum,
        email: row.email,
        birth: row.birth,
        introduction: row.introduction,
        location: row.location,
        avator: row.avator
      }
      this.editVisible = true
    },
    // 保存编辑
    saveEdit () {
      let datetime = getDateTime(new Date(this.form.birth))
      let params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('username', this.form.username)
      params.append('password', this.form.password)
      params.append('sex', this.form.sex)
      params.append('phone_num', this.form.phoneNum)
      params.append('email', this.form.email)
      params.append('birth', datetime)
      params.append('introduction', this.form.introduction)
      params.append('location', this.form.location)
      HttpManager.updateUserMsg(params).then(res => {
        if (res.code === 1) {
          this.getData()
          this.$notify({
            title: '修改成功',
            type: 'success'
          })
        } else {
          this.$notify({
            title: '修改失败',
            type: 'error'
          })
        }
      }).catch(err => {
        console.error(err)
      })
      this.editVisible = false
    },
    // 确定删除
    deleteRow () {
      HttpManager.deleteUser(this.idx)
        .then(res => {
          if (res) {
            this.getData()
            this.$notify({
              title: '删除成功',
              type: 'success'
            })
          } else {
            this.$notify({
              title: '删除失败',
              type: 'error'
            })
          }
        })
        .catch(error => {
          console.error(error)
        })
      this.delVisible = false
    }
  }
}

</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
  font-size: 12px;
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
