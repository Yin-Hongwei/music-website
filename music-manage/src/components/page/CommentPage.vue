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
            </div>
            <el-table :data="tableData" stripe border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="50"></el-table-column>
                <el-table-column prop="name" label="用户"></el-table-column>
                <el-table-column prop="content" label="评论内容"></el-table-column>
                <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="评论内容">
                    <el-input v-model="form.content"></el-input>
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
export default {
    name: 'comment-page',
    data() {
        return {
            tableData: [],
            tempDate: [],
            multipleSelection: [],
            editVisible: false,
            delVisible: false,
            select_word:'',
            form: {
                id: '',
                userId: '',
                songId: '',
                songListId: '',
                content: '',
                type: '',
                up: ''
            },
            index: 0,
            idx: -1
        }
    },
    created() {
        this.getData();
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
    mixins: [mixin],
    methods: {
        getData() {
            var _this = this
            _this.tableData = []
            _this.tempDate = []
            if (_this.$route.query.type === 0) {
                _this.$axios.get('http://localhost:8080/songComments?songId=' + _this.$route.query.id).then((res) => {
                    for(var i = 0; i < res.data.length; i++){
                        _this.getUsers(res.data[i].userId, res.data[i])
                    }
                    _this.index = 0
                })
            } else if (_this.$route.query.type === 1){
                _this.$axios.get('http://localhost:8080/songListComments?songListId=' + _this.$route.query.id).then((res) => {
                    for(var i = 0; i < res.data.length; i++){
                        _this.getUsers(res.data[i].userId, res.data[i])
                    }
                    _this.index = 0
                })
            }
        },
        getUsers (id, item) {
            let _this = this
            _this.$axios.get('http://localhost:8080/commentOfConsumer?id=' + id).then(function (res) {
                    let o = {}
                    o = item
                    o.index = _this.index++
                    o.name = res.data[0].username
                    _this.tableData.push(o)
                    _this.tempDate.push(o)
                }).catch(function (error) {
                    console.log(error)
                })
        },

        handleEdit(index, row) {
            this.idx = index;
            const item = this.tableData[index];
            this.form = {
                id: item.id,
                userId:item.userId,
                songId: item.songId,
                songListId: item.songListId,
                content: item.content,
                type: item.type,
                up: item.up
            }
            this.editVisible = true;
        },
        // 保存编辑
        saveEdit() {
            var params = new URLSearchParams()
            params.append('id', this.form.id)
            params.append('userId', this.form.userId)
            if (this.form.songId === null){
                params.append('songId', "")
            }else {
                params.append('songId', this.form.songId)
            }
            if (this.form.songListId === null){
                params.append('songListId', "")
            }else {
                params.append('songListId', this.form.songListId)
            }
            params.append('content', this.form.content)
            params.append('type', this.form.type)
            params.append('up', this.form.up)
            this.$axios.post('http://localhost:8080/api/updateCommentMsgs', params)
                .then(response => {
                    if (response.data.code === 1) {
                        this.getData()
                        // this.$set(this.tableData, this.idx, this.form);
                        this.$notify({
                            title: '修改成功',
                            type: 'success'
                        });
                    } else {
                        this.$notify({
                            title: '修改失败',
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
            _this.$axios.get('http://localhost:8080/api/deleteComments?id=' + _this.tableData[_this.idx].id)
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
