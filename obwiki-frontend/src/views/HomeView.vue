<template>

  <div>
    <a-layout>
      <a-layout-sider width="200" style="background: #fff">
        <a-menu mode="inline" style="height: 100%" @click="handleClick">
          <a-menu-item key="welcome">
            <!-- <MailOutlined /> -->
            <span>欢迎</span>
          </a-menu-item>
          <a-sub-menu v-for="item in level1" :key="item.id">
            <template #title>
              <span>{{ item.name }}</span>
            </template>
            <a-menu-item v-for="child in item.children" :key="child.id">
              <span>{{ child.name }}</span>
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <div class="welcome" v-show="isShowWelcome">
        <the-welcome></the-welcome>
      </div>

        <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }"
          :data-source="ebooks">

          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <template #actions>
                <span>
                  <component :is="'FileOutlined'" style="margin-right: 8px" />
                  {{ item.docCount }}
                </span>
                <span>
                  <component :is="'UserOutlined'" style="margin-right: 8px" />
                  {{ item.viewCount }}
                </span>
                <span>
                  <component :is="'LikeOutlined'" style="margin-right: 8px" />
                  {{ item.voteCount }}
                </span>
              </template>


              <a-list-item-meta :description="item.description">
                <template #title>
                  <router-link :to="'/doc?ebookId=' + item.id">
                    {{ item.name }}
                  </router-link>
                </template>
                <template #avatar><a-avatar :src="item.cover" /></template>



              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </a-layout-content>
    </a-layout>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import api from '@/api/index'
import { Tool } from '@/utils/tool';
import { message } from 'ant-design-vue';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
import TheWelcome from '@/components/the-welcome.vue';


//定义响应式数据
const ebooks = ref([]);
const openKeys = ref([]);
const selectedKeys2 = ref([]);

//修改查询方法 携带分类id参数
const handleQueryEbook = () => {
  api.get("/ebook/list", {
    params: {
      page: 1,
      size: 1000,
      categoryId2: categoryId2
    }
  }).then((resp) => {
    ebooks.value = resp.data.content.list;
  })
}
const isShowWelcome = ref(true);
let categoryId2 = 0;
//点击分类导航栏 获取选中id,发送查询请求
const handleClick = (value: any) => {
  if (value.key === 'welcome') {
    isShowWelcome.value = true;
  } else {
    categoryId2 = value.key;
    isShowWelcome.value = false;
    handleQueryEbook();
  }
}

const actions: Record<string, any>[] = [
  { icon: StarOutlined, text: '156' },
  { icon: LikeOutlined, text: '156' },
  { icon: MessageOutlined, text: '2' },
];

/*
  * 分类相关
  * */
const level1 = ref();
const handleQueryCategory = () => {
  api.get("/category/all").then((resp) => {
    const data = resp.data;

    if (data.success) {

      level1.value = [];
      level1.value = Tool.array2Tree(data.content, 0);

    } else {
      message.error(data.message);
    }
  });
};
//完成渲染后执行
onMounted(() => {
  handleQueryCategory();
})
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
  margin-right: 10px;
}
</style>
