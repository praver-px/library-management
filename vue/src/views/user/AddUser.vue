<template>
  <div style="width: 75%">
    <h2>新增用户</h2>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" :label-position="'right'" label-width="500px"
             size="medium">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="form.age" placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="form.sex" size="medium">
          <el-radio border label="男"></el-radio>
          <el-radio border label="女"></el-radio>
        </el-radio-group>

      </el-form-item>
      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="form.address" placeholder="请输入地址"></el-input>
        <!--        <el-cascader-->
        <!--            size="large"-->
        <!--            :options="options"-->
        <!--            v-model="selectedOptions">-->
        <!--        </el-cascader>-->
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
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

export default {
  name: "AddUser",
  data() {
    return {
      options: regionData,
      selectedOptions: [],
      form: {},
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
          {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '年龄不能为空'},
          {type: 'number', message: '年龄必须为数字值'}
        ],
        sex: [
          {required: true, message: '性别不能为空'},
        ],
        phone:[
          {required: true, message: '请输入号码', trigger: 'blur'},
          {min: 11, max: 11, message: '请输入正确的号码', trigger: 'blur'}
        ],
        address:[
          {required: true, message: '地址不能为空'},
        ],
        email: [
          {required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
        ]
      },
    }
  },
  methods: {

    save() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid){
          request.post("/user/save", this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success("新增成功");
              this.form = {};
            } else {
              this.$notify.error(res.msg);
            }
          })
        }
      })

    }
  }
}
</script>

<style scoped>

</style>