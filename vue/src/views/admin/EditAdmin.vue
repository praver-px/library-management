<template>
  <div style="width: 75%">
    <h2>编辑管理员</h2>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="500px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" show-password placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>

      <el-form-item label="头像" prop="avatar">
        <el-upload
            class="avatar-uploader"
            :action="'http://localhost:8090/api/admin/file/upload?token=' + this.admin.token"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="form.avatar" :src="form.avatar" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
    </el-form>

    <div style="text-align: center;margin-top: 30px">
      <el-button type="primary" @click="save">提交</el-button>
      <!--      <el-button type="danger">取消</el-button>-->
    </div>

  </div>
</template>

<script>
import {regionData} from 'element-china-area-data';
import request from "@/util/request";
import Cookies from "js-cookie";

export default {
  name: "EditUser",
  data() {
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      options: regionData,
      selectedOptions: [],
      form: {},
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
          {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码不能为空'},
        ],
        phone: [
          {required: true, message: '请输入号码', trigger: 'blur'},
          {min: 11, max: 11, message: '请输入正确的号码', trigger: 'blur'}
        ],
        email: []
      },
    }
  },
  created() {
    const id = this.$route.query.id
    request.get("/admin/" + id).then(res => {
      this.form = res.data;
    })
  },
  methods: {
    handleChange(value) {
      console.log(value)
    },
    save() {
      request.put("/admin/update", this.form).then(res => {
        console.log(this.form)
        if (res.code === '200') {
          this.$notify.success("修改成功");
          this.$router.push("/adminList");
        } else {
          this.$notify.error(res.msg);
        }
      })
    },
    handleAvatarSuccess(res) {
      if (res.code === '200') {
        console.log(res.data)
        // this.$set(this.form, 'cover', res.data)
        this.form.avatar = res.data
      }
    },
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>