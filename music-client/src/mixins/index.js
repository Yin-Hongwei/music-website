import axios from 'axios'
export const mixin = {
  methods: {
    // 获取图片信息
    attachImageUrl (srcUrl) {
      return this.$store.state.HOST + srcUrl || '../assets/img/user.jpg'
    },
    // 得到名字后部分
    replaceFName (str) {
      let arr = str.split('-')
      return arr[1]
    },
    // 得到名字前部分
    replaceLName (str) {
      let arr = str.split('-')
      return arr[0]
    },
    // 播放
    toplay: function (id, url, pic, index, name, lyric) {
      // console.log(lyric)
      this.$store.commit('setId', id)
      window.sessionStorage.setItem('id', JSON.stringify(id))
      this.$store.commit('setListIndex', index)
      window.sessionStorage.setItem('listIndex', JSON.stringify(index))
      this.$store.commit('setUrl', this.$store.state.HOST + url)
      window.sessionStorage.setItem('url', JSON.stringify(this.$store.state.HOST + url))
      this.$store.commit('setpicUrl', this.$store.state.HOST + pic)
      window.sessionStorage.setItem('picUrl', JSON.stringify(this.$store.state.HOST + pic))
      this.$store.commit('setTitle', this.replaceFName(name))
      window.sessionStorage.setItem('title', JSON.stringify(this.replaceFName(name)))
      this.$store.commit('setArtist', this.replaceLName(name))
      window.sessionStorage.setItem('artist', JSON.stringify(this.replaceLName(name)))
      this.$store.commit('setLyric', this.parseLyric(lyric))
      window.sessionStorage.setItem('lyric', JSON.stringify(this.parseLyric(lyric)))
      // console.log(this.$store.state.playing['songsList'])
    },
    // 解析歌词
    parseLyric (text) {
      let lines = text.split('\n')
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g
      let result = []

      while (!pattern.test(lines[0])) {
        lines = lines.slice(1)
      }

      lines[lines.length - 1].length === 0 && lines.pop()
      for (let item of lines) {
        let time = item.match(pattern) // 存前面的时间段
        let value = item.replace(pattern, '') // 存歌词
        // console.log(time) // 时间
        // console.log(value) // 歌词数据
        for (let item1 of time) {
          var t = item1.slice(1, -1).split(':')
          if (value !== '') {
            result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value])
          }
        }
      }
      result.sort(function (a, b) {
        return a[0] - b[0]
      })
      return result
    },
    // 搜索音乐
    getSong () {
      if (!this.$route.query.keywords) {
        this.$store.commit('setListOfSongs', [])
        this.$notify({
          title: '您输入内容为空',
          type: 'warning'
        })
      } else {
        let _this = this
        axios.get(`${_this.$store.state.HOST}/listSongsOfSearch?name=${_this.$route.query.keywords}`)
          .then(function (res) {
            if (!res.data.length) {
              _this.$store.commit('setListOfSongs', [])
              _this.$notify({
                title: '系统暂无该歌曲',
                type: 'warning'
              })
            } else {
              _this.$store.commit('setListOfSongs', res.data)
              window.sessionStorage.setItem('listOfSongs', JSON.stringify(res.data))
            }
          })
          .catch(function (error) {
            console.log(error)
          })
      }
    }
  }
}
