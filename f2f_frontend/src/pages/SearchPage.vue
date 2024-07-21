<template>
  <form action="/">
    <van-search
        v-model="searchText"
        show-action
        placeholder="请输入需要搜索的标签"
        @search="onSearch"
        @cancel="onCancel"
        @keyup.delete="deleteJudgeSearchTextOfNull"
    />
  </form>
  <van-divider>已选标签</van-divider>
  <div v-if="activeIds.length===0">请选择标签</div>


  <van-row gutter="5">
    <van-col span="6" v-for="tag in activeIds">
      <van-tag closeable type="primary" @close="doClose(tag)">
        {{ tag }}
      </van-tag>
    </van-col>
  </van-row>

  <van-divider>选择标签</van-divider>

  <!--  多选分类-->
  <van-tree-select
      v-model:active-id="activeIds"
      v-model:main-active-index="activeIndex"
      :items="tagList"

  />
  <div style="padding: 1px 50px">
    <van-button block type="primary" @click="doSearchResult" >搜索</van-button>

  </div>



</template>

<script setup lang="ts">
import {ref} from 'vue';
import {useRouter} from "vue-router";


const router = useRouter();


const searchText = ref(''); //输入的值
/**
 * 搜索标签方法
 */
const onSearch = () => {
  tagList.value = originalList.map(parentTag => {
    const tempChildren = [...parentTag.children];
    const tempParentTag = {...parentTag};
    tempParentTag.children = tempChildren.filter(item => item.text.includes(searchText.value));
    //这里应该有个定位到父级别的功能 后期补充
    return tempParentTag;
  })
};




/**
 * delete按键判空
 * 判定为空 originalList对tagList重新赋值
 */
const deleteJudgeSearchTextOfNull = () => {
  if (searchText.value.length === 0) {
    tagList.value = originalList;
  }

}

const show = ref(true);
const close = () => {
  show.value = false;
};

const activeIds = ref([]);  //默认显示
const activeIndex = ref(0);
const originalList = [
  {
    text: '性别',
    children: [
      {text: '男', id: '男'},
      {text: '女', id: '女'},
    ],
  },
  {
    text: '身份',
    children: [
      {text: '大一', id: '大一'},
    ],
  },
  {
    text: '方向',
    children: [
      {text: 'Java', id: 'Java'},
    ],
  },
  {
    text: '目标',
    children: [
      {text: '考研', id: '考研'},

    ],

  },

  {
    text: '段位',
    children: [
      {text: '初级', id: '初级'},
    ],

  },

];

let tagList = ref(originalList);



/**
 * 取消键触发清空效果
 */
const onCancel = () => {
  //取消清空
  searchText.value = '';
  //这里也不是点击cancel 再使得分类列表变为originalList 应该searchText为空
  tagList.value = originalList;
}

/**
 * 点击选中后标签的× 取消选择标签
 * @param tag
 */
const doClose = (tag) => {
  activeIds.value = activeIds.value.filter(item => {
    // tag与activeIds.value内容不等时 返回true 将这个tag保留 反之则过滤掉（删除）
    return item !== tag;
  })
};


/**
 * 点击搜索按钮 显示搜索结果
 */
const doSearchResult = () => {
  router.push({
    path: '/user/list',
    query: {
      tags: activeIds.value
    },
  })
}

</script>

<style scoped>

</style>