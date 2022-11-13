<template>
  <div style="width: 75%">
    <h2>编辑用户</h2>
    <el-form :inline="true" :model="form"   label-width="500px">
      <el-form-item label="姓名">
        <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="form.username" placeholder="请输入用户名" disabled></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="form.age" placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="form.sex" size="medium">
          <el-radio border label="男"></el-radio>
          <el-radio border label="女"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" placeholder="请输入地址"></el-input>

        <!--        <el-cascader-->
<!--            size="large"-->
<!--            :options="options"-->
<!--            v-model="form.address">-->
<!--        </el-cascader>-->
      </el-form-item>
      <el-form-item label="邮箱">
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
import { regionData } from 'element-china-area-data';
import request from "@/util/request";

export default {
  name: "EditUser",
  data(){
    return{
      options: regionData,
      selectedOptions: [],
      form:{},
    }
  },
  created() {
    const  id = this.$route.query.id
    request.get("/user/"+id).then(res =>{
      this.form = res.data;
    })
  },
  methods:{
    handleChange (value) {
      console.log(value)
    },
    save(){
      request.put("/user/update",this.form).then(res =>{
        console.log(this.form)
        if (res.code === '200'){
          this.$notify.success("修改成功");
          this.$router.push("/userList");
        }else {
          this.$notify.error(res.msg);
        }
      })
    }
  }
}
</script>

<style scoped>

</style>