<template>
  <div class="upload-page">
    <div class="section">
      <el-upload
        :action="uploadUrl()"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload">
        <el-button size="small" type="primary">点击上传</el-button>
      </el-upload>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'upload-page',
  data () {
    return {
      imageUrl: ''
    }
  },
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  methods: {
    uploadUrl () {
      let url = `${this.$store.state.HOST}/api/updateUserImg?id=${this.userId}` // 生产环境和开发环境的判断
      return url
    },
    handleAvatarSuccess (res, file) {
      let _this = this
      if (res.code === 1) {
        _this.imageUrl = URL.createObjectURL(file.raw)
        _this.$store.commit('setAvator', res.avator)
        window.localStorage.setItem('avator', JSON.stringify(res.avator))
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
    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }
  }
}
</script>

<style scoped>
.upload-page {
  width: 100%;
}

.section {
  height: 600px;
  background-color: #e6ecf0;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
