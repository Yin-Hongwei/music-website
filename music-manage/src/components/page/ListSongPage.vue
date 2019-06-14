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
            <el-table :data="tableData" border stripe style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="50"></el-table-column>
                <el-table-column prop="l" label="歌手"></el-table-column>
                <el-table-column prop="f" label="歌曲"></el-table-column>
                <el-table-column label="操作" width="80">
                    <template slot-scope="scope">
                        <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!--添加歌曲-->
        <el-dialog title="添加歌曲" :visible.sync="centerDialogVisible" width="400px" center>
            <el-form :model="registerForm" status-icon ref="registerForm" label-width="0px" class="demo-ruleForm">
                <el-form-item prop="singerName" label="歌手名字">
                    <el-input v-model="registerForm.singerName" placeholder="歌手名字"></el-input>
                </el-form-item>
                <el-form-item prop="songName" label="歌曲名字">
                    <el-input v-model="registerForm.songName" placeholder="歌曲名字"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="centerDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="getSongId">确 定</el-button>
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
    export default {
        name: 'list-song-page',
        data() {
            return {
                registerForm: {
                    singerName: '',
                    songName: ''
                },
                tableData: [],
                tempDate: [],
                multipleSelection: [],
                centerDialogVisible: false,
                editVisible: false,
                delVisible: false,
                select_word:'',
                form: {
                    id: '',
                    songId: '',
                    songListId: ''
                },
                index: 0,
                idx: -1
            }
        },
        watch: {
            select_word: function () {
                if (this.select_word === "") {
                    this.tableData = this.tempDate
                } else {
                    this.tableData=[]
                    this.tempDate.filter((d) => {
                        if (d.l.indexOf(this.select_word) !== -1) {
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
            getData () {
                var _this = this
                _this.$axios.get('http://localhost:8080/listSongOfSingers?songListId=' + _this.$route.query.id).then((res) => {
                    _this.tableData = []
                    for (var i = 0; i< res.data.length; i++) {
                        _this.getSong(res.data[i].songId)
                    }
                })
            },
            getSong (id) {
                var _this = this
                _this.$axios.get('http://localhost:8080/listSongsOfSongs?id=' + id)
                    .then(function (res) {
                        console.log(res.data)
                        var o = {}
                        o.f = _this.replaceFName(res.data[0].name)
                        o.l = _this.replaceLName(res.data[0].name)
                        o.index = _this.index++
                        _this.tableData.push(o)
                        _this.tempDate.push(o)
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            },
            getSongId () {
                var id = this.registerForm.singerName + '-' + this.registerForm.songName
                var _this = this
                _this.$axios.get('http://localhost:8080/listSongsOfSearch?name=' + id).then((res) => {
                    _this.addsong(res.data[0].id)
                })
            },
            addsong (id) {
                var _this = this
                var params = new URLSearchParams()
                params.append('songId', id)
                params.append('songListId', _this.$route.query.id)
                console.log('id='+id)
                console.log('_this.$route.query.id='+_this.$route.query.id)
                _this.$axios.post('http://localhost:8080/api/addListSong', params).then((res) => {
                    if (res.data.code === 1) {
                        this.$set(this.tableData, this.idx, this.form);
                        this.$notify({
                            title: '添加成功',
                            type: 'success'
                        });
                    } else {
                        this.$notify({
                            title: '添加失败',
                            type: 'error'
                        });
                    }
                })
                this.centerDialogVisible = false;
            },
            handleEdit(index, row) {
                this.idx = index;
                const item = this.tableData[index];
                this.form = {
                    id: item.id,
                    songId: item.songId,
                    songListId: item.songListId,
                }
                this.editVisible = true;
            },
            // 保存编辑
            saveEdit() {
                var params = new URLSearchParams()
                params.append('id', this.form.id)
                params.append('songId', this.form.songId)
                params.append('songListId', this.form.songListId)
                this.$axios.post('http://localhost:8080/api/updateListSongMsgs', params)
                    .then(response => {
                        if (response.data.code === 1) {
                            this.$set(this.tableData, this.idx, this.form);
                            this.$notify({
                                title: '删除成功',
                                type: 'success'
                            });
                        } else {
                            this.$notify({
                                title: '删除失败',
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
                _this.$axios.get('http://localhost:8080/api/deleteListSongs?id=' + _this.tableData[_this.idx].id)
                    .then(response => {
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
            }
        }
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
