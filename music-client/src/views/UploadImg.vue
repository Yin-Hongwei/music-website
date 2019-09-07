<template>
  <div id="upload-img">
    <div class="my-bg"></div>
    <div class="kuang">
      <el-upload
        class="upload-demo change"
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
  name: 'upload-img',
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
      var url = 'http://localhost:8080/api/updateUserImg?id=' + this.userId // 生产环境和开发环境的判断
      return url
    },
    handleAvatarSuccess (res, file) {
      let _this = this
      console.log(res)
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
  #upload-img {
    width: 100%;
  }
  .kuang {
    margin: 200px auto;
  }
  .my-bg {
    width: 100%;
    height: 100px;
    background-color: #93d2f8;
  }
  .change {
    position: relative;
    left: 47%;
    top: 20px;
  }
</style>
