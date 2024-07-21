
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
import {useRoute} from "vue-router";
import { ref } from 'vue';

let col : editName
const  route = useRoute()

// 拿到query中的值
const editUser = ref(
    {
  editKey : route.query.editKey,
  currentValue : route.query.currentValue,
  editName : route.query.editName,
}
);


const onSubmit = (values) => {
  console.log('submit', values);
};

</script>
<style scoped>

</style>