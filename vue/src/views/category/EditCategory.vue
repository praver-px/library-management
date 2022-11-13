<template>
  <div style="width: 75%">
    <h2>编辑管理员</h2>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="500px">
      <el-form-item label="分类名" prop="name">
        <el-input v-model="form.name" placeholder="请输入分类名"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="password">
        <el-input v-model="form.remark" placeholder="请输入密码"></el-input>
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
  name: "EditCategory",
  data() {
    return {
      options: regionData,
      selectedOptions: [],
      form: {},
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
        ]
      },
    }
  },
  created() {
    const id = this.$route.query.id
    request.get("/category/" + id).then(res => {
      this.form = res.data;
    })
  },
  methods: {
    handleChange(value) {
      console.log(value)
    },
    save() {
      request.put("/category/update", this.form).then(res => {
        console.log(this.form)
        if (res.code === '200') {
          this.$notify.success("修改成功");
          this.$router.push("/categoryList");
        } else {
          this.$notify.error(res.msg);
        }
      })
    }
  }
}
</script>

<style scoped>

</style>