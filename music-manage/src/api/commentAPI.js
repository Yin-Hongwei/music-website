import axios from 'axios'

const host = 'http://localhost:8888'

export default {
  // 获得指定歌曲ID的评论列表
  getCommentOfSongId (songId) {
    return axios.get(`${host}/comment/song/detail?songId=${songId}`)
  },
  // 获得指定歌单ID的评论列表
  getCommentOfSongListId (songListId) {
    return axios.get(`${host}/comment/songList/detail?songListId=${songListId}`)
  },
  // 更新评论
  updateCommentMsg (id, userId, songId, songListId, content, type, up) {
    var params = new URLSearchParams()
    params.append('id', id)
    params.append('userId', userId)
    params.append('songId', songId === null ? '' : songId)
    params.append('songListId', songListId === null ? '' : songListId)
    params.append('content', content)
    params.append('type', type)
    params.append('up', up)
    return axios.post(`${host}/comment/update`, params)
  },
  // 删除评论
  deleteComment (id) {
    return axios.get(`${host}/comment/delete?id=${id}`)
  }
}
