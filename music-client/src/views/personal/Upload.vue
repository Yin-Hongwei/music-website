<template>
  <div class='upload'>
    <el-upload
      drag
      :action='uploadUrl()'
      :show-file-list='false'
      :on-success='handleAvatarSuccess'
      :before-upload='beforeAvatarUpload'>
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">将文件拖到此处或点击上传</div>
      <template #tip>
        <p class="el-upload__tip">只能上传 {{this.upTypes.join('、')}} 文件，且不超过10M</p>
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
      imageUrl: '',
      upTypes: ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'x-png', 'icon']
    }
  },
  methods: {
    uploadUrl () {
      return `${this.BASE_URL}/user/avatar/update?id=${this.userId}`
    },
    beforeAvatarUpload (file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error('图片大小不可以超过 10MB!')
      } else if (!this.upTypes.includes(file.type.replace(/image\//, ""))) {
        this.$message.error(`图片只支持 ${this.upTypes.join('、')} 格式!`)
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
