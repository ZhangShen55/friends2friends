<template>
  <user-card-list :user-list="userList"/>
  <van-empty v-if="!userList || userList.length < 1" description="搜索不到相关用户" />

</template>

<script setup >
import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import myAxios from "../plugins/myAxios"
import {showFailToast, showSuccessToast, Toast} from "vant";
import qs from 'qs';
import UserCardList from "../components/UserCardList.vue";

const route = useRoute();
const {tags} = route.query;
const userList = ref([])

onMounted(async () => {
  const userListData = await myAxios.get(
      '/user/search/tags',
      {
        params: {
          tagNameList: tags,
        },
        paramsSerializer: params => {
          return qs.stringify(params, {indices: false})
        }
      })
      .then(function (response) {
        console.log('/user/search/tags succeed', response);
        showSuccessToast('请求成功');
        return response?.data; //？表示可选（可为空）

      })
      .catch(function (error) {
        console.error(error);
        showFailToast(error);
      })
  if (userListData) {
    userListData.forEach(user=>{
      if(user.tags){
        user.tags = JSON.parse(user.tags); //将json形式的tags转化成数组
      }
    })
    userList.value = userListData;
  }
})

</script>

<style scoped>

</style>