<template>
  <div>
    <!--  搜索栏-->
    <div style="margin:20px 0 20px 10px;">
      <el-button type="danger" @click="rmvList" :disabled="this.multipleSelection.length === 0" circle><i
          class="el-icon-delete"></i></el-button>
      <el-input style="width: 240px;margin-left: 20px; " placeholder="请输入用户名" v-model="params.username"
                clearable></el-input>
      <el-input style="width: 240px;margin-left: 5px; " placeholder="请输入电话号码" v-model="params.phone"
                clearable></el-input>
      <el-input style="width: 240px;margin-left: 5px" placeholder="请输入联系方式" v-model="params.email"
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
        <el-table-column prop="avatar" label="头像">
          <template v-slot="scope">
            <el-image
                style="width: 50px; height: 50px"
                :src="scope.row.avatar"
                :preview-src-list="[scope.row.avatar]">

            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="password" label="密码"></el-table-column>
        <el-table-column prop="phone" label="号码"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column label="状态">
          <template v-slot="scope">
            <el-switch
                v-model="scope.row.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                @change="changeStatus(scope.row)">
            </el-switch>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template v-slot="scope">
            <!--          scope.row就是当前行的数据-->
            <el-button type="primary" icon="el-icon-edit" circle
                       @click="$router.push('/editAdmin?id='+scope.row.id)"></el-button>
            <el-popconfirm title="您确定要删除这行信息嘛？" style="margin-left:5px;" @confirm="del(scope.row.id)">
              <el-button type="danger" circle slot="reference"><i class="el-icon-delete"></i></el-button>
            </el-popconfirm>
            <el-button type="warning" icon="el-icon-refresh-right" style="margin-left:5px;" circle
                       @click="resetPass(scope.row)"></el-button>
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
    </div>
  </div>
</template>

<script>

import request from "@/util/request";
import Cookies from "js-cookie";

export default {
  name: "admin",
  data() {
    return {
      ids: '',
      tableData: [],
      multipleSelection: [],
      loginInfo:JSON.parse(Cookies.get('admin')),
      total: 0,
      params: {
        pageNum: 1,
        pageSize: 8,
        username: '',
        email: '',
        phone: '',
      },
    }
  },
  created() {
    this.load()
  },
  methods: {
    changeStatus(row) {
      if (this.loginInfo.id === row.id && !row.status) {
        row.status = true
        this.$notify.warning("不允许修改自己的状态");
        return
      }
      request.put("/admin/update", row).then(res => {
        console.log('传入的数据为：' + row)
        if (res.code === '200') {
          this.$notify.success("状态修改成功");
          this.load();
        } else {
          this.$notify.error(res.msg);
        }
      })
    },
    resetPass(row) {
      row.password = '';
      request.put("/admin/update", row).then(res => {
        console.log(this.form)
        if (res.code === '200') {
          this.$notify.success("重置了" + row.username + '的密码');
          if (row.id === this.loginInfo.id) {
            Cookies.remove('admin');
            this.$router.push('/login')
          }
          this.load();
        } else {
          this.$notify.error(res.msg);
        }
      })
    },
    rmvList() {
      let ids = this.multipleSelection.map(value => value.id);
      this.$confirm("确定要删除这" + ids.length + "行数据吧？", "确认信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: 'warning'
      }).then(() => {
        // console.log(ids);
        request.delete("/admin/deleteList/" + (ids + '')).then(res => {
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

      request.get("/admin/page", {params: this.params}).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
    del(id) {
      request.delete('/admin/delete/' + id).then(res => {
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
    }
  }
}
</script>

<style scoped>

</style>