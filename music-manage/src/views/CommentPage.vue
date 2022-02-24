<template>
  <div class="table">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-tickets"></i> 评论信息
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="small" class="handle-del mr10" @click="delAll">批量删除</el-button>
        <el-input v-model="select_word" size="small" placeholder="筛选关键词" class="handle-input mr10"></el-input>
      </div>
      <el-table
        :data="tableData"
        size="small"
        border
        style="width: 100%"
        ref="multipleTable"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column prop="name" label="用户"></el-table-column>
        <el-table-column prop="content" label="评论内容"></el-table-column>
        <el-table-column label="操作" width="150">
          <template v-slot="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" v-model="editVisible" width="400px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="评论内容" size="small">
          <el-input v-model="form.content"></el-input>
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
import YinDelDialog from '@/components/dialog/YinDelDialog'

export default {
  name: 'CommentPage',
  mixins: [mixin],
  components: {
    YinDelDialog
  },
  data () {
    return {
      tableData: [],
      tempDate: [],
      multipleSelection: [],
      editVisible: false,
      delVisible: false,
      select_word: '',
      form: {
        id: '',
        userId: '',
        songId: '',
        songListId: '',
        content: '',
        type: '',
        up: ''
      },
      idx: -1
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
    // 获取评论
    getData () {
      this.tableData = []
      this.tempDate = []
      let promise = null
      if (this.$route.query.type == 0) {
        promise = HttpManager.getCommentOfSongId(this.$route.query.id)
      } else if (this.$route.query.type == 1) {
        promise = HttpManager.getCommentOfSongListId(this.$route.query.id)
      }

      promise.then(res => {
        for (let item of res) {
          this.getUsers(item.userId, item)
        }
      })
    },
    getUsers (id, item) {
      HttpManager.getUserOfId(id)
        .then(res => {
          let o = item
          o.name = res[0].username
          this.tableData.push(o)
          this.tempDate.push(o)
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 编辑
    handleEdit (row) {
      this.idx = row.id
      this.form = {
        id: row.id,
        userId: row.userId,
        songId: row.songId,
        songListId: row.songListId,
        content: row.content,
        type: row.type,
        up: row.up
      }
      this.editVisible = true
    },
    // 保存编辑
    saveEdit () {
      let params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('userId', this.form.userId)
      params.append('songId', this.form.songId === null ? '' : this.form.songId)
      params.append('songListId', this.form.songId === null ? '' : this.form.songListId)
      params.append('content', this.form.content)
      params.append('type', this.form.type)
      params.append('up', this.form.up)
      HttpManager.updateCommentMsg(params)
        .then(res => {
          if (res.code === 1) {
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
        .catch(err => {
          console.error(err)
        })
      this.editVisible = false
    },
    // 确定删除
    deleteRow () {
      HttpManager.deleteComment(this.idx)
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
        .catch(err => {
          console.error(err)
        })
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
</style>
