<template>
  <div>
    <!--  搜索栏-->
    <div style="margin:20px 0 20px 10px;">
      <el-button type="danger" @click="rmvList" :disabled="this.multipleSelection.length === 0" circle><i
          class="el-icon-delete"></i></el-button>
      <el-input style="width: 240px;margin-left: 20px; " placeholder="请输入分类名称" v-model="params.name"
                clearable></el-input>
      <el-button style="margin-left: 5px;" type="primary" @click="load"><i class="el-icon-search"></i><span>搜索</span>
      </el-button>
      <el-button style="margin-left: 5px;" type="warning" @click="reset"><i
          class="el-icon-refresh"></i><span>重置</span></el-button>
    </div>


    <!--  数据栏-->
    <div style="margin: 0 0 0 10px">
      <el-table :data="tableData" stripe row-key="id" default-expand-all @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" sortable></el-table-column>
        <el-table-column prop="name" label="分类名"></el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>

        <el-table-column label="操作">
          <template v-slot="scope">
            <!--          scope.row就是当前行的数据-->
            <el-button type="success" v-if="!scope.row.pid" circle @click="handleAdd(scope.row)"><i class="el-icon-plus"></i> </el-button>
            <el-button type="primary" icon="el-icon-edit" circle
                       @click="$router.push('/editCategory?id='+scope.row.id)"></el-button>
            <el-popconfirm title="您确定要删除这行信息嘛？" style="margin-left:5px;" @confirm="del(scope.row.id)">
              <el-button type="danger" circle slot="reference"><i class="el-icon-delete"></i></el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 20px;">
        <el-pagination
            :current-page="params.pageNum"
            :page-size="params.pageSize"
            layout="total,prev,pager,next"
            @current-change="handleCurrentChange"
            :hide-on-single-page="true"
            :total="total">
        </el-pagination>
      </div>


      <el-dialog title="添加二级分类" :visible.sync="dialogFormVisible" width="30%">
        <el-form :model="form" label-width="100px" ref="ruleForm" :rules="rules" style="width: 85%">
          <el-form-item label="分类名称" prop="name">
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="分类备注" prop="remark">
            <el-input v-model="form.remark" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>

import request from "@/util/request";
import Cookies from "js-cookie";

export default {
  name: "category",
  data() {
    return {
      ids: '',
      tableData: [],
      dialogFormVisible: false,
      multipleSelection: [],
      total: 0,
      pid:null,
      form: {},
      params: {
        pageNum: 1,
        pageSize: 8,
        name: '',
      },
      rules: {
        name: [
          {required: true, message: '请输入分类名称', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    handleAdd(row) {
      // 将当前行的id作为二级分类的pid
      this.pid = row.id
      this.dialogFormVisible = true
    },
    rmvList() {
      let ids = this.multipleSelection.map(value => value.id);
      this.$confirm("确定要删除这" + ids.length + "行数据吧？", "确认信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: 'warning'
      }).then(() => {
        // console.log(ids);
        request.delete("/category/deleteList/" + (ids + '')).then(res => {
          if (res.code === '200') {
            this.$message.success("批量删除成功!");
            this.load();
          }
        })
      }).catch(() => {

      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    load() {
      // fetch('http://localhost:8090/user/list').then(res => res.json()).then(res =>{
      //   console.log(res);
      //   this.tableData =res.data;
      // })

      // console.log(this.params)

      request.get("/category/page", {params: this.params}).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
    del(id) {
      request.delete('/category/delete/' + id).then(res => {
        if (res.code === '200') {
          this.$notify.success("删除成功");
          this.load();
        } else {
          this.$notify.error(res.msg);
        }
      })
    },
    reset() {
      this.params = {
        pageNum: 1,
        pageSize: 8,
        username: '',
        phone: '',
        email: '',
      }
      this.load();
    },
    handleCurrentChange(pageNum) {
      //  点击分页按钮实现翻页
      this.params.pageNum = pageNum;
      this.load();
    },
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          // 给二级分类赋值 pid
          this.form.pid = this.pid
          request.post('/category/save', this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success('新增二级分类成功')
              this.$refs['ruleForm'].resetFields()
              this.dialogFormVisible = false
              this.load()
            } else {
              this.$notify.error(res.msg)
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>

</style>