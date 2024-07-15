import { createApp } from 'vue'
// import './style.css'
import App from './App.vue'
import 'vant/lib/index.css'
import Vant from 'vant' // 全局引入
// import {Button, NavBar} from "vant"; 按需引入

const app = createApp(App);
app.use(Vant);
app.mount('#app');


