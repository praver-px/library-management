<template>



  <div style="height: 100vh; overflow: hidden; position: relative" >
    <el-card class="cover" v-if="loginAdmin.id" >
      <slide-verify :l="42"
                    :r="10"
                    :w="310"
                    :h="155"
                    slider-text="向右滑动"
                    @success="onSuccess"
                    @fail="onFail"
                    @refresh="onRefresh"
      ></slide-verify>
    </el-card>

    <div
        style="width: 500px; height: 400px; background-color: white; border-radius: 10px;margin: 150px auto; padding:50px">
      <div style="margin: 30px; text-align: center;font-size: 30px;font-weight: bold;color: dodgerblue;">
        <span>登录</span></div>
      <el-form :model="admin" ref="loginForm" :rules="rules" :label-position="'left'">
        <el-form-item prop="username">
          <el-input v-model="admin.username" prefix-icon="el-icon-user" clearable></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="admin.password" prefix-icon="el-icon-lock" clearable show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="login"> 登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/util/request";
import Cookies from 'js-cookie';

export default {
  name: "login",
  data() {
    return {
      admin: {},
      loginAdmin: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名！', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在3-10之间', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码！', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在3-10之间', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['loginForm'].validate((valid) => {
        if (valid) {
          request.post("/admin/login", this.admin).then(res => {
            if (res.code === '200') {
              this.loginAdmin = res.data;
            } else {
              this.$notify.error(res.msg);
            }
          })
        }
      })
    },
    onSuccess() {
      Cookies.set('admin', JSON.stringify(this.loginAdmin));
      this.$notify.success("登录成功！");
      this.$router.push("/");
    },
    onFail() {

    },
    onRefresh() {
      console.log('refresh')
    }
  }
}
</script>

<style>
.cover {
  width: fit-content;
  background-color: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}
</style>