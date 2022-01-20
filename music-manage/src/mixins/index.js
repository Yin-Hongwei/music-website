import { BASE_URL, HOME, INFO, SONG, SINGER, SONG_LIST, LIST_SONG, COMMENT, CONSUMER, COLLECT, SIGN_IN, ERROR, SIGN_OUT } from '../enums'

export const mixin = {
  methods: {
    getUrl (url) {
      return `${BASE_URL}/${url}`
    },
    // 获取要删除列表的id
    handleDelete (id) {
      this.idx = id
      this.delVisible = true
    },
    // 获取批量要删除的列表
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    // 批量删除
    delAll () {
      for (let item of this.multipleSelection) {
        this.handleDelete(item.id)
        this.deleteRow(item.id)
      }
      this.multipleSelection = []
    },
    getBirth (val) {
      let birth = String(val).match(/[0-9-]+(?=\s)/)
      return Array.isArray(birth) ? birth[0] : birth
    },
    changeSex (value) {
      if (value === 0) {
        return '女'
      } else if (value === 1) {
        return '男'
      } else if (value === 2) {
        return '组合'
      } else if (value === 3) {
        return '不明'
      } else if (value === '男' || value === '女') {
        return value
      }
    },
    toggleSex (value) {
      if (value === '女') {
        return 0
      } else if (value === '男') {
        return 1
      }
    },
    // 更新图片
    handleAvatarSuccess (res, file) {
      if (res.code === 1) {
        this.imageUrl = URL.createObjectURL(file.raw)
        this.getData()
        this.$notify({
          title: '上传成功',
          type: 'success'
        })
      } else {
        this.$notify({
          title: '上传失败',
          type: 'error'
        })
      }
    },
    beforeAvatarUpload (file) {
      const isJPG = (file.type === 'image/jpeg') || (file.type === 'image/png')
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    // 路由管理
    routerManager (routerName, { path, query }) {
      switch (routerName) {
        case HOME:
        case SIGN_OUT:
          this.$router.push({ path })
          break
        case INFO:
          this.$router.push({ path })
          break
        case SONG:
          this.$router.push({ path, query })
          break
        case SINGER:
          this.$router.push({ path })
          break
        case SONG_LIST:
          this.$router.push({ path })
          break
        case LIST_SONG:
          this.$router.push({ path, query })
          break
        case COMMENT:
          this.$router.push({ path, query })
          break
        case SIGN_IN:
          this.$router.push({ path })
          break
        case CONSUMER:
          this.$router.push({ path, query })
          break
        case COLLECT:
          this.$router.push({ path, query })
          break
        case ERROR:
          this.$router.push({ path })
          break
        default:
          this.$router.push({ path })
          break
      }
    },
    goBack (step = -1) {
      this.$router.go(step)
    }
  }
}
