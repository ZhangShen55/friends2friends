import { createApp } from 'vue'
import App from './App.vue'
import 'vant/lib/index.css'
import Vant from 'vant' // 全局引入
import router from  "./config/router"
// import {Button, NavBar} from "vant"; 按需引入
// import * as VueRouter from 'vue-router'


const app = createApp(App);
app.use(Vant);
app.use(router);
app.mount('#app');

