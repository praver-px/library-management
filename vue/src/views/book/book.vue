<template>
  <div>
    <!--  搜索栏-->
    <div style="margin:20px 0 20px 10px;">
      <el-button type="danger" @click="rmvList" :disabled="this.multipleSelection.length === 0" circle><i
          class="el-icon-delete"></i></el-button>
      <el-input style="width: 240px;margin-left: 20px; " placeholder="请输入书名" v-model="params.name"
                clearable></el-input>
      <el-input style="width: 240px;margin-left: 20px; " placeholder="请输入图书标准码" v-model="params.bookNo"
                clearable></el-input>
      <el-button style="margin-left: 5px;" type="primary" @click="load"><i class="el-icon-search"></i><span>搜索</span>
      </el-button>
      <el-button style="margin-left: 5px;" type="warning" @click="reset"><i
          class="el-icon-refresh"></i><span>重置</span></el-button>
    </div>


    <!--  数据栏-->
    <div style="margin: 0 0 0 10px">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" sortable></el-table-column>
        <el-table-column prop="avatar" label="封面">
          <template v-slot="scope">
            <el-image
                style="width: 60px; height: 90px"
                :src="scope.row.avatar"
                :preview-src-list="[scope.row.avatar]">

            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="书名"></el-table-column>
        <el-table-column prop="bookNo" width="200" label="图书标准码"></el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="publishDate" label="出版日期"></el-table-column>
        <el-table-column prop="author" label="作者"></el-table-column>
        <el-table-column prop="publisher" label="出版社"></el-table-column>
        <el-table-column prop="category" label="分类"></el-table-column>
        <el-table-column prop="score" label="积分"></el-table-column>
        <el-table-column prop="nums" label="数量"></el-table-column>

        <el-table-column label="操作">
          <template v-slot="scope">
            <!--          scope.row就是当前行的数据-->
            <!--            <el-button type="success" v-if="!scope.row.pid" circle @click="handleAdd(scope.row)"><i class="el-icon-plus"></i> </el-button>-->
            <el-button type="primary" icon="el-icon-edit" circle
                       @click="$router.push('/editBook?id='+scope.row.id)"></el-button>
            <el-popconfirm title="您确定要删除这行信息嘛？" style="margin-left:5px;" @confirm="del(scope.row.id)">
              <el-button type="danger" circle slot="reference"><i class="el-icon-delete"></i></el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <!--      分页-->
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

export default {
  name: "Book",
  data() {
    return {
      ids: '',
      tableData: [],
      dialogFormVisible: false,
      multipleSelection: [],
      total: 0,
      pid: null,
      form: {},
      params: {
        pageNum: 1,
        pageSize: 5,
        name: '',
        bookNo: '',
      },
    }
  },
  created() {
    this.load()
  },
  methods: {
    rmvList() {
      let ids = this.multipleSelection.map(value => value.id);
      this.$confirm("确定要删除这" + ids.length + "行数据吧？", "确认信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: 'warning'
      }).then(() => {
        // console.log(ids);
        request.delete("/book/deleteList/" + (ids + '')).then(res => {
          if (res.code === '200') {
            this.$message.success("批量删除成功!");
            this.load();
          }else {
            this.$notify.error(res.msg);
          }
        })
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

      request.get("/book/page", {params: this.params}).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list;
          console.log(this.tableData);
          console.log(res.data.list)
          this.total = res.data.total;
        }
      })
    },
    del(id) {
      request.delete('/book/delete/' + id).then(res => {
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
  }
}
</script>

<style scoped>

</style>