import {createMemoryHistory, createRouter} from 'vue-router'
import Index from "../pages/Index.vue"
import Team from "../pages/TeamPage.vue"
import User from "../pages/UserPage.vue"
import UserEditPage from "../pages/UserEditPage.vue"
import SearchPage from "../pages/SearchPage.vue"
import SearchResultPage from "../pages/SearchResultPage.vue";

const routes = [
    {path: '/', component: Index},
    {path: '/team', component: Team},
    {path: '/user', component: User},
    {path: '/search', component: SearchPage},
    {path: '/user/list', component: SearchResultPage},
    {path: '/user/edit', component: UserEditPage},

]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router
