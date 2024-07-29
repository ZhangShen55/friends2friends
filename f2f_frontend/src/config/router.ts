import {createMemoryHistory, createRouter, createWebHistory} from 'vue-router'
import Index from "../pages/Index.vue"
import TeamPage from "../pages/TeamPage.vue"
import UserPage from "../pages/UserPage.vue"
import UserEditPage from "../pages/UserEditPage.vue"
import SearchPage from "../pages/SearchPage.vue"
import SearchResultPage from "../pages/SearchResultPage.vue";
import UserLoginPage from "../pages/UserLoginPage.vue";

const routes = [
    {path: '/', component: Index},
    {path: '/team', component: TeamPage},
    {path: '/user', component: UserPage},
    {path: '/search', component: SearchPage},
    {path: '/user/list', component: SearchResultPage},
    {path: '/user/edit', component: UserEditPage},
    {path: '/user/login', component: UserLoginPage},

]

const router = createRouter({
    // history: createMemoryHistory(), //隐藏路由地址
    history: createWebHistory(), //地址栏全显示地址
    routes,
})

export default router
