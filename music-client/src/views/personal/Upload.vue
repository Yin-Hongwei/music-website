<template>
  <div class='upload'>
    <el-upload
      drag
      :action='uploadUrl()'
      :show-file-list='false'
      :on-success='handleAvatarSuccess'
      :before-upload='beforeAvatarUpload'>
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">将文件拖到此处，或<em>修改头像</em></div>
      <template #tip>
        <div class="el-upload__tip">只能上传jpg/png文件，且不超过10M</div>
      </template>
    </el-upload>
  </div>
</template>

<script>
import { UploadFilled } from '@element-plus/icons-vue'
import mixin from '@/mixins'

export default {
  name: 'Upload',
  mixins: [mixin],
  components: {
    uploadFilled: UploadFilled
  },
  data () {
    return {
      imageUrl: ''
    }
  },
  methods: {
    uploadUrl () {
      return `${this.BASE_URL}/user/avatar/update?id=${this.userId}`
    },
    beforeAvatarUpload (file) {
      const upTypes = ['image/jpg', 'image/jpeg', 'image/png', 'image/pjpeg', 'image/gif', 'image/bmp', 'image/x-png', 'image/icon']
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error('图片大小不可以超过 10MB!')
      } else if (!upTypes.includes(file.type)) {
        this.$message.error(`图片只支持 ${upTypes.join('、').replace('image/', '')} 格式!`)
      }
    },
    handleAvatarSuccess (res, file) {
      if (res.code === 1) {
        this.imageUrl = URL.createObjectURL(file.raw)
        this.$store.commit('setUserPic', res.avator)
        this.$message({
          message: '修改成功',
          type: 'success'
        })
      } else {
        this.$notify({
          title: '修改失败',
          type: 'error'
        })
      }
    }
  }
}
</script>

<style scoped>
.upload {
  width: 100%;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
