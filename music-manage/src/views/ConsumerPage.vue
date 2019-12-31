<template>
    <div class="table">
        <div class="container">
          <div class="handle-box">
                <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                <el-input v-model="select_word" placeholder="筛选相关用户" class="handle-input mr10"></el-input>
                <el-button type="primary" @click="centerDialogVisible = true">添加新用户</el-button>
            </div>
          <el-table :data="data" border stripe style="width: 100%" ref="multipleTable" height="500px" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="40"></el-table-column>
                <el-table-column label="歌手图片" width="100">
                    <template slot-scope="scope">
                        <img :src="getUrl(scope.row.avator)" alt="" style="width: 80px;"/>
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
                <el-table-column prop="username" label="用户名" width="80"></el-table-column>
                <el-table-column prop="password" label="密码" width="120"></el-table-column>
                <el-table-column label="性别" width="50">
                    <template slot-scope="scope">
                        <div>{{changeSex(scope.row.sex) }}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="phoneNum" label="手机号码" width="120"></el-table-column>
                <el-table-column prop="email" label="邮箱" width="120"></el-table-column>
                <el-table-column prop="birth" label="生日" width="80"></el-table-column>
                <el-table-column prop="introduction" label="签名"></el-table-column>
                <el-table-column prop="location" label="地区" width="80"></el-table-column>
                <el-table-column label="收藏" width="80">
                    <template  slot-scope="scope">
                        <el-button size="mini" @click="getCollect(data[scope.$index].id)">收藏</el-button>
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

        <!--添加新用户-->
        <el-dialog title="添加用户" :visible.sync="centerDialogVisible" width="400px" center>
            <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" label-width="70px" class="demo-ruleForm">
                <el-form-item prop="username" label="用户名">
                    <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password" label="密码">
                    <el-input type="password" placeholder="密码" v-model="registerForm.password"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="registerForm.sex">
                        <el-radio :label="0">女</el-radio>
                        <el-radio :label="1">男</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item prop="phoneNum" label="手机" >
                    <el-input  placeholder="手机" v-model="registerForm.phoneNum"></el-input>
                </el-form-item>
                <el-form-item prop="email" label="邮箱">
                    <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
                </el-form-item>
                <el-form-item prop="birth" label="生日">
                    <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%;"></el-date-picker>
                </el-form-item>
                <el-form-item prop="introduction" label="签名">
                    <el-input  type="textarea" placeholder="签名" v-model="registerForm.introduction" ></el-input>
                </el-form-item>
                <el-form-item prop="location" label="地区">
                    <el-select v-model="registerForm.location" placeholder="地区">
                        <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addPeople">确 定</el-button>
  </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
            <el-form ref="form" :model="form" label-width="60px">
                <el-form-item label="用户名">
                    <el-input v-model="form.username" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="form.password"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="form.sex">
                        <el-radio :label="0">女</el-radio>
                        <el-radio :label="1">男</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="手机">
                    <el-input v-model="form.phoneNum"></el-input>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="form.email"></el-input>
                </el-form-item>
                <el-form-item prop="birth" label="生日">
                    <el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width: 100%;"></el-date-picker>
                </el-form-item>
                <el-form-item label="签名">
                    <el-input v-model="form.introduction"></el-input>
                </el-form-item>
                <el-form-item label="地区">
                    <el-input v-model="form.location"></el-input>
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
  name: 'consumer-page',
  mixins: [mixin],
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
      cities: [{
        value: '北京',
        label: '北京'
      }, {
        value: '天津',
        label: '天津'
      }, {
        value: '河北',
        label: '河北'
      }, {
        value: '山西',
        label: '山西'
      }, {
        value: '内蒙古',
        label: '内蒙古'
      }, {
        value: '辽宁',
        label: '辽宁'
      }, {
        value: '吉林',
        label: '吉林'
      }, {
        value: '黑龙江',
        label: '黑龙江'
      }, {
        value: '上海',
        label: '上海'
      }, {
        value: '江苏',
        label: '江苏'
      }, {
        value: '浙江',
        label: '浙江'
      }, {
        value: '安徽',
        label: '安徽'
      }, {
        value: '福建',
        label: '福建'
      }, {
        value: '江西',
        label: '江西'
      }, {
        value: '山东',
        label: '山东'
      }, {
        value: '河南',
        label: '河南'
      }, {
        value: '湖北',
        label: '湖北'
      }, {
        value: '湖南',
        label: '湖南'
      }, {
        value: '广东',
        label: '广东'
      }, {
        value: '广西',
        label: '广西'
      }, {
        value: '海南',
        label: '海南'
      }, {
        value: '重庆',
        label: '重庆'
      }, {
        value: '四川',
        label: '四川'
      }, {
        value: '贵州',
        label: '贵州'
      }, {
        value: '云南',
        label: '云南'
      }, {
        value: '西藏',
        label: '西藏'
      }, {
        value: '陕西',
        label: '陕西'
      }, {
        value: '甘肃',
        label: '甘肃'
      }, {
        value: '青海',
        label: '青海'
      }, {
        value: '宁夏',
        label: '宁夏'
      }, {
        value: '新疆',
        label: '新疆'
      }, {
        value: '香港',
        label: '香港'
      }, {
        value: '澳门',
        label: '澳门'
      }, {
        value: '台湾',
        label: '台湾'
      }],
      rules: {
        username: [
          { required: true, trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        phoneNum: [
          { essage: '请选择日期', trigger: 'blur' }
        ],
        email: [
          { message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        birth: [
          { required: true, type: 'date', message: '请选择日期', trigger: 'change' }
        ],
        introduction: [
          { message: '请输入介绍', trigger: 'blur' }
        ],
        location: [
          { message: '请输入地区', trigger: 'change' }
        ]
      },
      tableData: [], // 记录用户信息，用于显示
      tempDate: [], // 记录用户信息，用于搜索时能临时记录一份用户信息
      is_search: false,
      multipleSelection: [], // 记录要删除的用户信息
      centerDialogVisible: false,
      editVisible: false, // 显示编辑框
      delVisible: false, // 显示删除框
      select_word: '', // 记录输入框输入的内容
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
      return `${this.$store.state.HOST}/api/updateUserImg?id=${id}`
    },
    // 获取用户信息
    getData () {
      let _this = this
      _this.tableData = []
      _this.tempDate = []
      _this.$axios.get(`${_this.$store.state.HOST}/AllUsers`).then((res) => {
        _this.tableData = res.data
        _this.tempDate = res.data
        _this.currentPage = 1
      })
    },
    getCollect (id) {
      this.$router.push({path: '/collect', query: { id }})
    },
    addPeople () {
      let _this = this
      let d = _this.registerForm.birth
      var datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      var params = new URLSearchParams()
      params.append('username', _this.registerForm.username)
      params.append('password', _this.registerForm.password)
      params.append('sex', _this.registerForm.sex)
      params.append('phone_num', _this.registerForm.phoneNum)
      params.append('email', _this.registerForm.email)
      params.append('birth', datetime)
      params.append('introduction', _this.registerForm.introduction)
      params.append('location', _this.registerForm.location)
      params.append('avator', '/img/user.jpg')
      _this.$axios.post(`${_this.$store.state.HOST}/api/signup`, params)
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
      let d = new Date(this.form.birth)
      var datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      var params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('username', this.form.username)
      params.append('password', this.form.password)
      params.append('sex', this.form.sex)
      params.append('phone_num', this.form.phoneNum)
      params.append('email', this.form.email)
      params.append('birth', datetime)
      params.append('introduction', this.form.introduction)
      params.append('location', this.form.location)
      this.$axios.post(`${this.$store.state.HOST}/api/updateUserMsgs`, params)
        .then(res => {
          if (res.data.code === 1) {
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
        })
        .catch(failResponse => {})
      this.editVisible = false
    },
    // 确定删除
    deleteRow () {
      var _this = this
      _this.$axios.get(`${_this.$store.state.HOST}/api/deleteUsers?id=${_this.idx}`)
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
