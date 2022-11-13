<template>
  <div>
    <!--    头部区域-->
    <div class="el-header">
      <div style="width: 300px;">
        <img class="logo-img" src="@/assets/logo.png" alt="LOGO">
        <span style="margin-left: 20px;font-size: 28px; color: #263580;">图书管理系统</span>
      </div>
      <div style=" flex: 1"></div>
      <div style="width: 80px;">
        <el-dropdown>
          <el-avatar
              :size="50"
              :src="this.form.avatar"
              icon="el-icon-user-solid"
              @error="'true'"
              style="margin-top: 20px; cursor: pointer;">
          </el-avatar>
          <el-dropdown-menu slot="dropdown" style="margin-top: -20px">
            <el-dropdown-item disabled><span>{{ admin.username }}</span></el-dropdown-item>
            <el-dropdown-item divided><span @click="handleChangePass">修改密码</span></el-dropdown-item>
            <el-dropdown-item><span @click="logout">退出登录</span></el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>

    </div>
    <!--    侧边栏和主体-->
    <div style="display: flex">
      <!--      侧边栏导航栏-->
      <div style="width: 200px;height: calc(100vh - 82px)">
        <el-menu :default-active="$route.path" router class="el-menu-demo">
          <el-menu-item index="/">
            <i class="el-icon-s-home"></i>
            <span>首页</span>
          </el-menu-item>

          <el-submenu index="admin">
            <template slot="title">
              <i class="el-icon-s-custom"></i>
              <span>管理员</span>
            </template>
            <el-menu-item index="/addAdmin">添加管理员</el-menu-item>
            <el-menu-item index="/adminList">管理员列表</el-menu-item>
          </el-submenu>

          <el-submenu index="user">
            <template slot="title">
              <i class="el-icon-s-custom"></i>
              <span>会员管理</span>
            </template>
            <el-menu-item index="/AddUser">添加会员</el-menu-item>
            <el-menu-item index="/userList">会员列表</el-menu-item>
          </el-submenu>

          <el-submenu index="category">
            <template slot="title">
              <i class="el-icon-s-unfold"></i>
              <span>分类管理</span>
            </template>
            <el-menu-item index="/AddCategory">添加分类</el-menu-item>
            <el-menu-item index="/CategoryList">分类列表</el-menu-item>
          </el-submenu>

          <el-submenu index="book">
            <template slot="title">
              <i class="el-icon-s-unfold"></i>
              <span>图书管理</span>
            </template>
            <el-menu-item index="/AddBook">添加图书</el-menu-item>
            <el-menu-item index="/bookList">图书列表</el-menu-item>
          </el-submenu>

          <el-submenu index="borrow">
            <template slot="title">
              <i class="el-icon-document-copy"></i>
              <span>借书管理</span>
            </template>
            <el-menu-item index="/addBorrow">借书添加</el-menu-item>
            <el-menu-item index="/borrowList">借书列表</el-menu-item>
          </el-submenu>

          <el-submenu index="retur">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>还书管理</span>
            </template>
            <el-menu-item index="/returList">还书列表</el-menu-item>
          </el-submenu>

          <el-menu-item index="/about">
            <i class="el-icon-chat-dot-round"></i>
            <span>关于</span>
          </el-menu-item>
        </el-menu>
      </div>
      <!--      主体数据-->
      <div style="flex: 1; width: 0; margin-right: 5px;background-color: white;">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import request from "@/util/request";

export default {
  name: "Layout",
  data() {
    return {
      form:{},
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
    }
  },
  created() {
    request.get("/admin/"+this.admin.id).then(res =>{
      this.form = res.data;
    })
  },
  methods: {
    handleChangePass() {
      this.$prompt('请输入修改后的密码', '修改密码', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[a-zA-Z0-9]{3,9999}$/,
        inputErrorMessage: '密码应在3字符以上'
      }).then(({value}) => {
        this.admin.password = value;
        request.put('/admin/update', this.admin).then(res => {
          console.log(this.form)
          if (res.code === '200') {
            this.$notify.success("修改成功");
            Cookies.remove('admin');
            this.$router.push("/login");

          } else {
            this.$notify.error(res.msg);
          }
        })
      })
    },
    logout() {
      Cookies.remove('admin')
      this.$router.push('/login');
      // 清除浏览器的用户数据

    },
  }
}
</script>

<style scoped>
.el-header {
  height: 80px;
  line-height: 80px;
  background-color: white;
  margin-bottom: 2px;
  display: flex;
}

.logo-img {
  height: 60px;
  width: 60px;
  position: relative;
  top: 10px;
  left: 20px;
}
</style>