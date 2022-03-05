<template>
  <div class="container">
    <div class="personal-data">
      <h2 class="title">编辑个人资料</h2>
      <div class="personal">
        <el-form :model="registerForm" label-width="80px">
          <el-form-item prop="username" label="用户名">
            <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input type="password" placeholder="密码" v-model="registerForm.password"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="registerForm.sex">
              <el-radio :label="0">女</el-radio>
              <el-radio :label="1">男</el-radio>
              <el-radio :label="2">保密</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="phoneNum" label="手机">
            <el-input  placeholder="手机" v-model="registerForm.phoneNum" ></el-input>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
          </el-form-item>
          <el-form-item prop="birth" label="生日">
            <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%;"></el-date-picker>
          </el-form-item>
          <el-form-item prop="introduction" label="签名">
            <el-input  type="textarea" placeholder="签名" v-model="registerForm.introduction" ></el-input>
          </el-form-item>
          <el-form-item prop="location" label="地区">
            <el-select v-model="registerForm.location" placeholder="地区" style="width:100%">
              <el-option
                v-for="item in area"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div class="btn">
          <el-button type="primary" @click="saveMsg()">保存</el-button>
          <el-button @click="goBack(-1)">取消</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  readonly,
  computed,
  onMounted,
  getCurrentInstance,
  reactive,
} from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import { AREA } from "@/enums";
import { HttpManager } from "@/api";
import { getDateTime } from "@/utils";

export default defineComponent({
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { goBack } = mixin();

    // 注册
    const area = readonly(AREA);
    const registerForm = reactive({
      username: "",
      password: "",
      sex: "",
      phoneNum: "",
      email: "",
      birth: new Date(),
      introduction: "",
      location: "",
      userPic: "",
    });
    const userId = computed(() => store.getters.userId);

    async function getUserInfo(id) {
      const result = await HttpManager.getUserOfId(id);
      registerForm.username = result[0].username;
      registerForm.password = result[0].password;
      registerForm.sex = result[0].sex;
      registerForm.phoneNum = result[0].phoneNum;
      registerForm.email = result[0].email;
      registerForm.birth = result[0].birth;
      registerForm.introduction = result[0].introduction;
      registerForm.location = result[0].location;
      registerForm.userPic = result[0].avator;
    }

    async function saveMsg() {
      const params = new URLSearchParams();
      params.append("id", userId.value);
      params.append("username", registerForm.username);
      params.append("password", registerForm.password);
      params.append("sex", registerForm.sex);
      params.append("phone_num", registerForm.phoneNum);
      params.append("email", registerForm.email);
      params.append("birth", getDateTime(registerForm.birth));
      params.append("introduction", registerForm.introduction);
      params.append("location", registerForm.location);

      const result = await HttpManager.updateUserMsg(params) as { code: number };
      if (result.code === 1) {
        proxy.$store.commit("setUsername", registerForm.username);
        (proxy as any).$notify.success({
          title: "编辑成功",
          showClose: true,
        });
        goBack(-1);
      } else {
        (proxy as any).$notify.error({
          title: "编辑失败",
          showClose: true,
        });
      }
    }

    onMounted(() => {
      getUserInfo(userId.value);
    });

    return {
      area,
      registerForm,
      saveMsg,
      goBack,
    };
  },
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.container {
  padding-top: $header-height + 30px;
}

.personal-data {
  background-color: white;
  margin: 0 20%;
  overflow: hidden;
  border-radius: 12px;

  .title {
    padding-top: 30px;
    margin-left: 50px;
    display: inline-block;
  }

  .personal {
    background-color: white;
    padding: 40px 70px 20px 40px;
  }

  .btn {
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>
