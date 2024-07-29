<template>
  <template v-if="user">
    <van-cell title="头像" is-link to="/user/edit" :value="user.avatarUrl">
      <img style="height: 45px" :src="user.avatarUrl"/>
    </van-cell>
    <van-cell title="昵称" is-link @click="toEdit('username','昵称',user.username)" :value="user.username"/>
    <van-cell title="账号" :value="user.userAccount"/>
    <van-cell title="性别" is-link @click="toEdit('gender','性别',user.gender)" :value="user.gender"/>
    <van-cell title="电话" is-link @click="toEdit('phone','电话',user.phone)" :value="user.phone"/>
    <van-cell title="邮箱" is-link @click="toEdit('email','邮箱',user.email)" :value="user.email"/>
    <van-cell title="星球号" :value="user.planetCode"/>
    <van-cell title="注册时间" :value="user.createTime"/>
  </template>

</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {getCurrentUser} from "../service/user.ts";
// import myAxios from "../plugins/myAxios";
// import {showFailToast, showSuccessToast} from "vant";

const router = useRouter();
const user = ref({});


// 创建一个钩子
onMounted(async () => {
  const res = await getCurrentUser()
  if (res) {
    //res存在
    // console.log(res) //打印response看看
    user.value = res;
    // showSuccessToast('获取用户信息成功')
  } else {
    // showFailToast('获取用户信息失败')
  }

})

const toEdit = (editKey: string, editName: string, currentValue: string) => {
  router.push({
    path: "/user/edit/",
    query: {
      editKey,
      editName,
      currentValue,
    },
  })
};


// const user =
//     {
//       id: 1,
//       username: 'sylvan',
//       userAccount: 'zhangshen',
//       avatarUrl: 'https://avatars.githubusercontent.com/u/79956480?v=4&size=64',
//       gender:'男',
//       phone: '1231312321',
//       email: '1231312321@qq.com',
//       planetCode: '110',
//       createTime: new Date(),
//     }


</script>


<style scoped>

</style>