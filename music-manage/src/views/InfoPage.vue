<template>
  <div class="table">
      <div class="crumbs">
          <el-breadcrumb separator="/">
              <el-breadcrumb-item><i class="el-icon-tickets"></i> 系统首页 </el-breadcrumb-item>
          </el-breadcrumb>
      </div>
      <div class="container">
        <h3>用户性别比例</h3>
        <ve-pie :data="userSex"></ve-pie>
      </div>
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
      }
    }
  },
  mounted () {
    this.getUser()
  },
  methods: {
    getUser () {
      let _this = this
      _this.$axios.get(`${_this.$store.state.HOST}/AllUsers`).then((res) => {
        _this.user = res.data
        console.log(res.data)
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
    }
  }
}
</script>

<style>

</style>
