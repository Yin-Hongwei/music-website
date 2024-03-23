<template>
  <div class="container">
    <el-form ref="form" :model="form" label-width="80px" @submit.prevent="submitForm">
      <el-form-item label="Email" prop="email">
        <el-input v-model="form.email" type="email" required></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" native-type="submit">Submit</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
</style>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      form: {
        email: ''
      }
    };
  },
  methods: {
   async submitForm() {
 try {
   const response = await axios.post('http://localhost:8888/user/forget', { email: this.form.email });
   console.log(response.data);
   this.$message({
     message: response.data,
     type: 'success'
   });
 } catch (error) {
   console.error('Error submitting email:');
   this.$message({
     message: 'response.data',
     type: 'error'
   });
 } 
 }
},
};
</script>
