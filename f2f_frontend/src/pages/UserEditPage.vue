
<template>

  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="editUser.currentValue"
          :name="editUser.editKey"
          :label="editUser.editName"
          :placeholder="'请输入' + editUser.editName"
      />

    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from 'vue';
import myAxios from "../plugins/myAxios.js";
import {showFailToast, showSuccessToast} from "vant";
import {getCurrentUser} from "../service/user.ts";


const  route = useRoute()
const  router = useRouter()

// 拿到query中的值
const editUser = ref(
    {
      editKey : route.query.editKey,
      editName : route.query.editName,
      currentValue : route.query.currentValue,
  });



const onSubmit = async () => {
  const currentUser = await getCurrentUser();
  //判断当前用户是否存在
  if(!currentUser){
    showFailToast('获取当前用户失败')
    return;
  }

  console.log(currentUser,'当前用户')
  try {
    const res = await myAxios.post('/user/update',
        {
          'id': currentUser.id,
          [editUser.value.editKey as string] : editUser.value.currentValue
        });

    // console.log(res,'输出res看看')
    if(res.code === 0 && res.data >　0){
      showSuccessToast('修改成功')
      //重定向到上一页
      router.back();
    }else{
      showFailToast('修改失败')
    }
  }catch (error){
    // 处理网络请求或其他错误
    console.error('网络请求失败：', error);
    showFailToast('更新失败');
  }

};

</script>
<style scoped>

</style>