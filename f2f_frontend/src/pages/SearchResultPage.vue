<template>
  <van-card v-for="user in userList"
            :title="`${user.username}（${user.planetCode}）`"
            :desc=user.profile
            :thumb=user.avatarUrl
  >
    <template #tags>
      <van-tag plain type="primary" v-for="tag in user.tags" style="margin-right: 5px;margin-top: 5px">{{
          tag
        }}
      </van-tag>
    </template>
    <template #footer>
      <van-button size="mini" type="primary">联系我</van-button>
    </template>
  </van-card>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import myAxios from "../plugins/myAxios"
import {Toast} from "vant";

const route = useRoute();
const {tags} = route.query;
onMounted(() => {
      myAxios.get(
          '/user/search/tags',
          {
            params: {
              tagNameList: tags,
            }
          })
          .then(function(response){
            console.log('/user/search/tags succeed',response);
            Toast.success('')
      })
          .catch(function (error){
        console.error('/user/search/tags error',error);
        Toast.
      }).then(function (){
        //总是执行
        console.log('成功失败我都执行...')
      })
      ;})



console.log(route.query)

const mockUser = {
  id: 12345,
  username: '张深',
  userAccount: 'sylvan',
  avatarUrl: 'https://avatars.githubusercontent.com/u/79956480?v=4',
  gender: 0,
  profile: '发生的发生的发生的发生大发手打刚发个短发说法个等级高发季发哈工卡合法估计',
  phone: 17605231331,
  email: 'mainuser@88.com',
  planetCode: '001',
  createTime: new Date(),
  tags: ['Java', 'Python', '打工仔', '数码爱好者'],
}

const userList = ref([mockUser])

</script>

<style scoped>

</style>