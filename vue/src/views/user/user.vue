<template>
  <div>
    <!--  搜索栏-->
    <div style="margin:20px 0 20px 10px;">
      <el-button type="danger" @click="rmvUserList" :disabled="this.multipleSelection.length === 0" circle><i
          class="el-icon-delete"></i></el-button>
      <el-input style="width: 240px;margin-left: 20px; " placeholder="请输入名称" v-model="params.name"
                clearable></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入联系方式" v-model="params.phone"
                clearable></el-input>
      <el-button style="margin-left: 5px;" type="primary" @click="load"><i class="el-icon-search"></i><span>搜索</span>
      </el-button>
      <el-button style="margin-left: 5px;" type="warning" @click="reset"><i
          class="el-icon-refresh"></i><span>重置</span></el-button>
    </div>


    <!--  数据栏-->
    <div style="margin: 0 0 0 10px">
      <el-table :data="tableData" stripe ref="multipleTable" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" sortable></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="age" label="年龄" sortable></el-table-column>
        <el-table-column prop="sex" label="性别"></el-table-column>
        <el-table-column prop="phone" label="号码"></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="account" label="账号积分"></el-table-column>

        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button type="warning" icon="el-icon-plus" circle @click="handleAccount(scope.row)"></el-button>
            <!--          scope.row就是当前行的数据-->
            <el-button type="primary" icon="el-icon-edit" circle
                       @click="$router.push('/editUser?id='+scope.row.id)"></el-button>
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

      <el-dialog title="添加积分" :visible.sync=" dialogFormVisible" width="30%">
        <el-form :model="form" label-width="100px" ref="ruleForm" :rules="rules" style="width: 80%">
          <el-form-item label="当前积分:">
            <el-input disabled v-model="form.account" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="积分">
            <!--          <el-input v-model="form.score" autocomplete="off"></el-input>-->
            <el-input-number v-model="form.score" :step="5" :min="5" :max="300"></el-input-number>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="addAccount">确认</el-button>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>

import request from "@/util/request";

export default {
  name: "user",
  data() {
    const checkNum = (rule, value, callback) => {
      value = parseInt(value)
      if (value < 0 || value >= 300) {
        callback(new Error('请输入一个正确的整数'));
      }
      callback()
    };
    return {
      ids: '',
      tableData: [],
      multipleSelection: [],
      total: 0,
      params: {
        pageNum: 1,
        pageSize: 8,
        name: '',
        phone: '',
      },
      dialogFormVisible: false,
      form: {},
      rules: {
        score: [
          {require: true, message: '请输入需要充值的积分', trigger: 'blur'},
          {validator: checkNum, trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    rmvUserList() {
      let ids = this.multipleSelection.map(value => value.id);
      this.$confirm("确定要删除这" + ids.length + "行数据吧？", "确认信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: 'warning'
      }).then(() => {
        // console.log(ids);
        request.delete("/user/deleteList/" + (ids + '')).then(res => {
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
      request.get("/user/page", {params: this.params}).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }

      })
    },
    del(id) {
      request.delete('/user/delete/' + id).then(res => {
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
        name: '',
        phone: '',
      }
      this.load();
    },
    handleCurrentChange(pageNum) {
      //  点击分页按钮实现翻页
      this.params.pageNum = pageNum;
      this.load();
    },
    handleAccount(row) {
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true
    },
    addAccount() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/user/account', this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success("充值成功");
              this.dialogFormVisible=false;
              this.load();
            } else {
              this.$notify.error(res.msg);
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