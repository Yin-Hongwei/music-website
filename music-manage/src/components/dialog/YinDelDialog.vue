<template>
  <div>
    <!-- 删除提示框 -->
    <el-dialog title="提示" v-model="centerDialogVisible" width="300px" center>
      <div class="del-dialog-cnt" align="center">删除不可恢复，是否确定删除？</div>
      <template #footer>
        <span  class="dialog-footer">
          <el-button size="small" @click="cancelRow">取 消</el-button>
          <el-button type="primary" size="small" @click="deleteRow">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    delVisible: Boolean
  },
  data() {
    return {
      centerDialogVisible: this.delVisible
    }
  },
  watch: {
    delVisible (value) {
      this.centerDialogVisible = value
    }
  },
  methods: {
    cancelRow () {
      this.$emit('cancelRow', false)
    },
    deleteRow (e) {
      this.$emit('deleteRow', e)
    }
  }
}
</script>
