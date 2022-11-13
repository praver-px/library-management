import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../views/Layout.vue'
import Cookies from "js-cookie";


Vue.use(VueRouter)

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login/login'),
    },
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: '/home',
        children: [
            {
                path: 'home',
                name: 'Home',
                component: () => import('@/views/HomeView')
            },
            //  user
            {
                path: 'userList',
                name: 'user',
                component: () => import('@/views/user/user')
            },
            {
                path: 'addUser',
                name: 'AddUser',
                component: () => import('@/views/user/AddUser')
            },
            {
                path: 'editUser',
                name: 'EditUser',
                component: () => import('@/views/user/EditUser')
            },
            //  admin
            {
                path: 'adminList',
                name: 'admin',
                component: () => import('@/views/admin/admin')
            },
            {
                path: 'addAdmin',
                name: 'AddAdmin',
                component: () => import('@/views/admin/AddAdmin')
            },
            {
                path: 'editAdmin',
                name: 'EditAdmin',
                component: () => import('@/views/admin/EditAdmin')
            },
            // category
            {
                path: 'categoryList',
                name: 'category',
                component: () => import('@/views/category/category')
            },
            {
                path: 'addCategory',
                name: 'AddCategory',
                component: () => import('@/views/category/AddCategory')
            },
            {
                path: 'editCategory',
                name: 'EditCategory',
                component: () => import('@/views/category/EditCategory')
            },
            // book
            {
                path: 'bookList',
                name: 'book',
                component: () => import('@/views/book/book')
            },
            {
                path: 'addBook',
                name: 'AddBook',
                component: () => import('@/views/book/AddBook')
            },
            {
                path: 'editBook',
                name: 'EditBook',
                component: () => import('@/views/book/EditBook')
            },
            {
                path: 'about',
                name: 'about',
                component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
            },
            //  ====  Borrow  ====
            {
                path: 'borrowList',
                name: 'BorrowList',
                component: () => import('@/views/borrow/List.vue')
            },
            {
                path: 'addBorrow',
                name: 'AddBorrow',
                component: () => import('@/views/borrow/Add.vue')
            },
            {
                path: 'editBorrow',
                name: 'EditBorrow',
                component: () => import('@/views/borrow/Edit.vue')
            },
            //  ====  Return  ====
            {
                path: 'returList',
                name: 'returList',
                component: () => import('@/views/retur/List.vue')
            },
        ]
    },

    {
        path: "*",
        component: () => import("@/views/404")
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

//路由守卫，登录验证
router.beforeEach((to, from, next) => {
    if (to.path === '/login') next()
    const admin = Cookies.get("admin")
    if (!admin && to.path !== '/login') return next("/login")
    next()
})

export default router
