<template>
  <van-card v-for="user in props.userList"
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
import {UserType} from '../models/user'

interface UserCardListProp {
  userList : UserType[]
}

const props = withDefaults(defineProps<UserCardListProp>(),{
  // @ts-ignore
  userList : [] as UserType[],
});
</script>

<style scoped>

</style>