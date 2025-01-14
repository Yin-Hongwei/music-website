<template>
  <el-container id="appSend">
    <el-header>
      <h1>修改密码</h1>
    </el-header>
    <el-main>
      <el-form @submit.prevent="handleSubmit">
        <el-form-item label="邮箱：" prop="email">
          <el-input id="email" v-model="email" type="email" required />
          <el-button @click="sendVerificationCode" :disabled="countdown > 0">
            {{ countdown > 0 ? `重新发送(${countdown}s)` : '发送验证码' }}
          </el-button>
        </el-form-item>
        <el-form-item label="验证码：" prop="code">
          <el-input id="code" v-model="code" type="text" required />
        </el-form-item>
        <el-form-item label="新密码：" prop="password">
          <el-input id="password" v-model="password" type="password" required />
        </el-form-item>
        <el-form-item label="确认密码：" prop="confirmPassword">
          <el-input id="confirmPassword" v-model="confirmPassword" type="password" required />
        </el-form-item>
        <el-form-item>
          
          <el-button @click="handleSubmit" type="submit">提交</el-button>
          
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>


<style>
#appSend {
  max-width: 400px;
  margin: 0 auto;
}
</style>

<script>
import axios from 'axios';
export default {
  

  data() {
  
    return {
      email: "",
      code: "",
      password: "",
      confirmPassword: "",
      countdown: 0,
    };
  },
  methods: {
    async sendVerificationCode() {
      try {
        const email = document.getElementById('email').value;
        const { data: { message, success } } = await axios.get('http://localhost:8888/user/sendVerificationCode', { params: { email } });

        console.log(message, success);

        this.$message({
          message,
          type: success ? 'success' : 'error'
        });

        if (success) {
          // 设置倒计时
          this.countdown = 58;
          const timer = setInterval(() => {
            if (this.countdown > 0) {
              this.countdown--;
            } else {
              clearInterval(timer);
            }
          }, 1000);
        }
      } catch (error) {
        console.error('发送验证码异常', error);
        this.$message({
          message: '发送验证码异常',
          type: 'error'
        });
      }
    },
    async handleSubmit() {
      try {
        const email =document.getElementById('email').value;
        const code=document.getElementById('code').value
        const password=document.getElementById('password').value
        const confirmPassword=document.getElementById('confirmPassword').value
        const data = {
          email: email,
          code: code,
          password: password,
          confirmPassword: confirmPassword
        };
        const { data: { message, success } } = await axios.post('http://localhost:8888/user/resetPassword', data);
        console.log(message, success);
        this.$message({
          message,
          type: success ? 'success' : 'error'
        });
        if (success){
          this.$router.back();
        }
      } catch (error) {
        console.error('提交异常', error);
        this.$message({
          message: '提交异常',
          type: 'error'
        });
      }
    }
  },
};
</script>
