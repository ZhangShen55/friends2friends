<template >
  <user-card-list :user-list="userList"/>
  <van-empty v-if="!userList || userList.length < 1" description="搜索为空" />
</template>

<script setup >
import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import myAxios from "../plugins/myAxios"
import {showFailToast, showSuccessToast, Toast} from "vant";
import UserCardList from "../components/UserCardList.vue";
import qs from 'qs';

const route = useRoute();
const {tags} = route.query;
const userList = ref([])

onMounted(async () => {
  const userListData = await myAxios.get(
      '/user/recommend',
      {
        params: {
          pageNum:1,
          pageSize:20,
        },
      })
      .then(function (response) {
        console.log('/user/recommend succeed', response);
        showSuccessToast('请求成功');
        return response?.data?.records; //？表示可选（可为空）

      })
      .catch(function (error) {
        console.log('/user/recommend fail', error);
        showFailToast('请求失败');
      })

  if (userListData) {
    userListData.forEach(user=>{
      if(user.tags){
        user.tags = JSON.parse(user.tags); //将json形式的字符串tags转化成数组
      }
    })
    userList.value = userListData;
  }
})

</script>

<style scoped>

</style>