<template>
  <van-nav-bar
      title="标题"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
  >
    <template  #right >
      <van-icon  name="search" size="18"/>
    </template>
  </van-nav-bar>

  <div id="content">
    <!--路由显示内容-->
    <router-view/>
  </div>

  <van-tabbar route  @change="onChange" v-model="active">
    <van-tabbar-item to="/" icon="wap-home" name="index">主页</van-tabbar-item>
    <van-tabbar-item to="/team" icon="friends" name="team">队伍</van-tabbar-item>
    <van-tabbar-item to="/user" icon="manager" name="user">个人</van-tabbar-item>
    <van-tabbar-item to="/user/login" icon="wap-home" name="login">登录</van-tabbar-item>
  </van-tabbar>

</template>

<script setup>
import {ref} from 'vue'
import {showToast} from 'vant'
import {useRouter} from "vue-router";

const router = useRouter()
const onClickLeft = () => {
  router.back()
}
const onClickRight = () => {
  router.push('/search')
}

const active = ref('index'); //高亮 主页
const onChange = (value) => showToast(`标签 ${value}`); // 点击时触发一个轻量的提示

</script>
<style scoped>
#content{
  padding-bottom: 50px;
}
</style>