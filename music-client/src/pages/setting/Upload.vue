<template>
  <div class='upload'>
    <p class='title'>修改头像</p>
    <hr/>
    <div class='section'>
      <el-upload
        drag
        :action='uploadUrl()'
        :show-file-list='false'
        :on-success='handleAvatarSuccess'
        :before-upload='beforeAvatarUpload'>
        <i class='el-icon-upload'></i>
        <div class='el-upload__text'>将文件拖到此处，或<em>修改头像</em></div>
        <div class='el-upload__tip' slot='tip'>只能上传jpg/png文件，且不超过10M</div>
      </el-upload>
    </div>
  </div>
</template>

<script>
import mixin from '../../mixins'

export default {
  name: 'Upload',
  mixins: [mixin],
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
      const upTypes = ['image/jpg', 'image/jpeg', 'image/png', 'image/pjpeg', 'image/gif', 'image/bmp', 'image/x-png']
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error('图片大小不可以超过 10MB 喔!')
      }
      if (!upTypes.includes(file.type)) {
        this.$message.error('图片只能是 JPG或PNG 格式喔!')
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

<style lang='scss' scoped>
@import '@/assets/css/upload.scss';
</style>
