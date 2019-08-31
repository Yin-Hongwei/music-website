<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-tickets"></i> 数据</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" @click="centerDialogVisible = true">添加歌曲</el-button>
            </div>
            <el-table :data="tableData" stripe border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="50"></el-table-column>
                <el-table-column label="歌曲预览">
                    <template slot-scope="scope">
                    <aplayer autoplay :music="{
                     narrow: true,
                     title: scope.row.name,
                     artist: scope.row.introduction,
                     pic: 'http://localhost:8080' + scope.row.pic,
                     src: 'http://localhost:8080' + scope.row.url,
                     lrc: 'http://localhost:8080' + scope.row.lyric
                     }">
                    </aplayer>
                    </template>
                </el-table-column>
                <el-table-column label="歌词" width="300">
                    <template slot-scope="scope">
                        <ul style="height: 100px; overflow: scroll">
                            <li v-for="(item, index) in parseLyric(scope.row.lyric)" :key="index">
                                {{ item[1] }}
                            </li>
                        </ul>
                    </template>
                </el-table-column>
                <el-table-column label="歌曲预览" width="100">
                    <template slot-scope="scope">
                        <el-upload
                            class="upload-demo"
                            :action="uploadUrl(scope.row.id)"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                            :before-upload="beforeAvatarUpload">
                            <el-button size="small" type="primary">更新图片</el-button>
                        </el-upload>
                        <el-upload
                            class="upload-demo change"
                            :action="uploadSongUrl(scope.row.id)"
                            :show-file-list="false"
                            :on-success="handleSongSuccess"
                            :before-upload="beforeSongUpload">
                            <el-button size="small" type="primary">更新歌曲</el-button>
                        </el-upload>
                    </template>
                </el-table-column>
                <el-table-column label="评论" width="80">
                    <template  slot-scope="scope">
                        <el-button size="mini" @click="getComment(tableData[scope.$index].id)">评论</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!--添加歌曲-->
        <el-dialog title="添加歌曲" :visible.sync="centerDialogVisible" width="400px" center>
            <form action="" id="tf">
                <div>
                    <label>歌曲名</label>
                    <el-input type="text" name="name"></el-input>
                </div>
                <div>
                    <label>歌手</label>
                    <el-input type="text" name="singerName" id="singerName"></el-input>
                </div>
                <div>
                    <label>歌曲介绍</label>
                    <el-input type="textarea" value="" name="introduction"></el-input>
                </div>
                <div>
                    <label>歌词</label>
                    <el-input type="textarea" value="" name="lyric"></el-input>
                </div>
                <div>
                    <label>歌曲上传</label>
                    <el-input type="file" name="file" id="upadte-file-input"></el-input>
                </div>
            </form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="centerDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="getSingerName">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="歌手-歌曲">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="简介">
                    <el-input v-model="form.introduction"></el-input>
                </el-form-item>
                <el-form-item label="歌词">
                    <el-input  type="textarea" v-model="form.lyric"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import {mixin} from '../../mixins'
import Aplayer from 'vue-aplayer'
export default {
    name: 'song-page',
    data() {
        return {
            aaa: 'http://localhost:8080/api/addSongaaa',
            registerForm: {
                name: '',
                singerName: '',
                introduction: '',
                lyric: ''
            },
            tableData: [],
            tempDate: [],
            is_search: false,
            multipleSelection: [], // 记录要删除的歌曲
            centerDialogVisible: false,
            editVisible: false,
            delVisible: false,
            select_word:'',
            form: {
                id: '',
                singerId: '',
                name: '',
                introduction: '',
                createTime: '',
                updateTime: '',
                pic: '',
                lyric: '',
                url: ''
            },
            index: 0,
            idx: -1
        }
    },
    components: {
        Aplayer
    },
    watch: {
        select_word: function () {
            if (this.select_word === "") {
                this.tableData = this.tempDate
            } else {
                this.tableData=[]
                this.tempDate.filter((d) => {
                    if (d.name.indexOf(this.select_word) !== -1) {
                        this.tableData.push(d);
                    }
                })
            }
        }
    },
    created() {
        this.getData();
    },
    mixins: [mixin],
    methods: {
        // 更新歌曲图片
        uploadUrl (id) {
            let url = 'http://localhost:8080/api/updateSongPic?id=' + id
            return url
        },
        handleAvatarSuccess (res, file) {
            let _this = this
            console.log(res)
            if (res.code === 1) {
                _this.imageUrl = URL.createObjectURL(file.raw)
                _this.getData()
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
        },

        // 更新歌曲url
        uploadSongUrl (id) {
            var url = 'http://localhost:8080/api/updateSongUrl?id=' + id
            return url
        },
        beforeSongUpload (file) {
            var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
            const extension = testmsg === 'mp3'
            if (!extension) {
              this.$message({
                message: '上传文件只能是mp3格式！',
                type: 'error'
              })
            }
            return extension
        },
        handleSongSuccess (res, file) {
            let _this = this
            console.log(res)
            if (res.code === 1) {
                _this.getData()
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

        //拉取数据
        getData() {
            var _this = this
            _this.tableData = []
            _this.$axios.get('http://localhost:8080/AllSongs').then((res) => {
                for(var i = 0; i < res.data.length; i++){
                    var o = {}
                    o = res.data[i]
                    o.index = _this.index++
                    _this.tableData.push(o)
                    _this.tempDate.push(o)
                }
                _this.index = 0
            })
        },

        // 添加音乐
        getSingerName () {
            let _this = this
            let value = document.getElementById('singerName').value
            _this.$axios.get('http://localhost:8080/searachSingers?name=' + value).then(function (res) {
                if (!res.data.length) {
                    _this.$notify({
                        title: '系统暂无该该歌手',
                        type: 'warning'
                    })
                } else {
                    _this.addSong(res.data[0].id)
                }
            }).catch(function (error) {
                console.log(error)
            })
        },
        addSong(id){
            let _this = this
            var form = new FormData(document.getElementById("tf"))
            form.append("singerId", id)
            var req = new XMLHttpRequest()
            req.onreadystatechange=function(){
                if(req.readyState===4 && req.status===200){
                    _this.getData()
                    _this.registerForm = []
                    _this.$notify({
                        title: '添加成功',
                        type: 'success'
                    });
                }else {
                    _this.$notify({
                        title: '添加失败',
                        type: 'error'
                    });
                }
            };
            req.open("post", "http://localhost:8080/api/addSong", false)
            req.send(form);
            _this.centerDialogVisible = false
        },
        handleEdit(index, row) {
            this.idx = index;
            const item = this.tableData[index];
            this.form = {
                id: item.id,
                singerId: item.singerId,
                name: item.name,
                introduction: item.introduction,
                createTime: item.createTime,
                updateTime: item.updateTime,
                pic: item.pic,
                lyric: item.lyric,
                url: item.url
            }
            this.editVisible = true;
        },
        getComment (id) {
            this.$router.push({path: "/Comment", query: { id: id, type :0}})
        },
        // 保存编辑
        saveEdit() {
            var params = new URLSearchParams()
            params.append('id', this.form.id)
            params.append('singerId', this.form.singerId)
            params.append('name', this.form.name)
            params.append('introduction', this.form.introduction)
            params.append('lyric', this.form.lyric)
            this.$axios.post('http://localhost:8080/api/updateSongMsgs', params)
                .then(response => {
                    if (response.data) {
                        this.$set(this.tableData, this.idx, this.form);
                        this.$notify({
                            title: '编辑成功',
                            type: 'success'
                        });
                    } else {
                        this.$notify({
                            title: '编辑失败',
                            type: 'error'
                        });
                    }
                })
                .catch(failResponse => {})
            this.editVisible = false;
        },
        // 确定删除
        deleteRow(){
            var _this = this
            _this.$axios.get('http://localhost:8080/api/deleteSongs?id=' + _this.tableData[_this.idx].id)
                .then(response => {
                    console.log(response)
                    if (response.data) {
                        _this.tableData.splice(_this.idx, 1);
                        _this.$notify({
                            title: '删除成功',
                            type: 'success'
                        });
                    } else {
                        _this.$notify({
                            title: '删除失败',
                            type: 'error'
                        });
                    }
                })
                .catch(failResponse => {})
            _this.delVisible = false;
        },
        parseLyric (text) {
            // console.log(typeof text)
            var lines = text.split('\n'),
                pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g,
                result = []
            // console.log(lines)
            while (!pattern.test(lines[0])) {
                lines = lines.slice(1)
            };
            // console.log(lines.length)
            lines[lines.length - 1].length === 0 && lines.pop()
            lines.forEach(function (item) {
                let time = item.match(pattern) // 存前面的时间段
                let value = item.replace(pattern, '') // 存歌词
                // console.log(time) // 时间
                // console.log(value) // 歌词数据
                time.forEach(function (item1) {
                    var t = item1.slice(1, -1).split(':')
                    if (value !== '') {
                        result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value])
                    }
                })
            })
            result.sort(function (a, b) {
                return a[0] - b[0]
            })
            // console.log(result)
            return result
        }
    },
}

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

</style>
