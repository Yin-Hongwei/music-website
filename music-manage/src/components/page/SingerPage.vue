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
                <el-button type="primary" @click="centerDialogVisible = true">添加歌手</el-button>
            </div>
            <el-table :data="tableData" stripe border style="width: 100%"  ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="歌手图片" width="100">
                    <template slot-scope="scope">
                        <img :src="getUrl(scope.row.pic)" alt="" style="width: 80px;"/>
                        <el-upload
                            class="upload-demo"
                            :action="uploadUrl(scope.row.id)"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                            :before-upload="beforeAvatarUpload">
                            <el-button size="small" type="primary">更新图片</el-button>
                        </el-upload>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="歌手" width="120"></el-table-column>
                <el-table-column label="性别" width="60">
                    <template slot-scope="scope">
                        <div>{{changeSex(scope.row.sex) }}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="birth" label="出生" width="100"></el-table-column>
                <el-table-column prop="location" label="地区" width="100"></el-table-column>
                <el-table-column label="简介">
                    <template slot-scope="scope">
                        <p style="height: 100px; overflow: scroll">{{ scope.row.introduction }}</p>
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

        <el-dialog title="添加歌手" :visible.sync="centerDialogVisible" width="400px" center>
            <el-form :model="registerForm" status-icon ref="registerForm" label-width="0px" class="demo-ruleForm">
                <el-form-item prop="name" label="歌手名">
                    <el-input v-model="registerForm.name" placeholder="歌手名"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="registerForm.sex">
                        <el-radio :label="0">女</el-radio>
                        <el-radio :label="1">男</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item prop="location" label="故乡">
                    <el-input v-model="registerForm.location" placeholder="故乡"></el-input>
                </el-form-item>
                <el-form-item prop="birth" label="出生">
                    <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%;"></el-date-picker>
                </el-form-item>
                <el-form-item prop="introduction" label="歌手介绍">
                    <el-input v-model="registerForm.introduction" placeholder="歌手介绍"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="centerDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addsinger">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="歌手">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="form.sex">
                        <el-radio :label="0">女</el-radio>
                        <el-radio :label="1">男</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="生日">
                    <el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width: 100%;"></el-date-picker>
                </el-form-item>
                <el-form-item label="地区">
                    <el-input v-model="form.location"></el-input>
                </el-form-item>
                <el-form-item label="简介">
                    <el-input  type="textarea" v-model="form.introduction"></el-input>
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
    name: 'singer-page',
    data() {
        return {
            registerForm: { // 添加框信息
                name: '',
                sex: '',
                birth: '',
                location: '',
                introduction: ''
            },
            tableData: [],
            tempDate: [],
            multipleSelection: [],
            centerDialogVisible: false,
            editVisible: false,
            delVisible: false,
            select_word:'',
            form: { // 编辑框信息
                id: '',
                name: '',
                sex: '',
                pic: '',
                birth: '',
                location: '',
                introduction: ''
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
    mounted () {
        console.log(this.tableData)
    },
    mixins: [mixin],
    methods: {
        uploadUrl (id) {
            var url = 'http://localhost:8080/api/updateSingerImg?id=' + id // 生产环境和开发环境的判断
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
        addsinger () {
            let _this = this
            let d = _this.registerForm.birth
            var datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
            var params = new URLSearchParams()
            params.append('name', this.registerForm.name)
            params.append('sex', this.registerForm.sex)
            params.append('pic', '/img/singerPic/hhh.jpg')
            params.append('birth', datetime)
            params.append('location', this.registerForm.location)
            params.append('introduction', this.registerForm.introduction)
            _this.$axios.post('http://localhost:8080/api/addSinger', params)
                .then(res => {
                    console.log(res)
                    if (res.data.code === 1) {
                        _this.getData()
                        _this.registerForm = []
                        _this.$notify({
                            title: '添加成功',
                            type: 'success'
                        });
                    } else {
                        _this.$notify({
                            title: '添加失败',
                            type: 'error'
                        });
                    }
                })
                .catch(failResponse => {})
            _this.centerDialogVisible = false
        },
        getData() {
            var _this = this
            _this.tableData = []
            _this.$axios.get('http://localhost:8080/listSingers').then((res) => {
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
        handleEdit(index, row) {
            this.editVisible = true;
            this.idx = index;
            const item = this.tableData[index];
            let datetime = item.birth
            this.form = {
                id: item.id,
                name: item.name,
                sex: item.sex,
                pic: item.pic,
                birth: datetime,
                location: item.location,
                introduction: item.introduction,
            }
        },
        // 保存编辑
        saveEdit() {
            let _this = this
            let d = _this.form.birth
            let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
            let params = new URLSearchParams()
            params.append('id', _this.form.id)
            params.append('name', _this.form.name)
            params.append('sex', _this.form.sex)
            params.append('pic', _this.form.pic)
            params.append('birth', datetime)
            params.append('location', _this.form.location)
            params.append('introduction', _this.form.introduction)
            _this.$axios.post('http://localhost:8080/api/updateSingerMsgs', params)
                .then(response => {
                    console.log(response.data)
                    if (response.data.code === 1) {
                        _this.getData()
                        // _this.$set(this.tableData, _this.idx, _this.form);
                        _this.$notify({
                            title: '编辑成功',
                            type: 'success'
                        });
                    } else {
                        _this.$notify({
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
            _this.$axios.get('http://localhost:8080/api/deleteSingers?id=' + _this.tableData[_this.idx].id)
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
