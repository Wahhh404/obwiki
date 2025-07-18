<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h3 v-if="level1.length === 0">
        对不起，找不到相关文档!
      </h3>
      <a-row>
        <a-col :span="6">
          <a-tree
            v-if="level1.length > 0"
            :tree-data="level1"
            :replace-fields="{ title: 'name', key: 'id', value: 'id' }"
            :default-expand-all="true"
            :default-selected-keys="defaultSelectedKeys"
            @select="onSelect"
          />
        </a-col>
        <a-col :span="18">
          <div>
            <h2>{{ doc.name }}</h2>
            <div>
              <span>阅读数：{{ doc.viewCount }}</span> &nbsp; &nbsp;
              <span>点赞数：{{ doc.voteCount }}</span>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc" />
          </div>
          <div
            class="wangeditor"
            :innerHTML="html"
          />
          <div class="vote-div">
            <a-button
              type="primary"
              shape="round"
              :size="'large'"
              style="height: 55px"
              @click="vote"
            >
              <template #icon>
                <LikeOutlined /> &nbsp;点赞：{{ doc.voteCount }}
              </template>
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>
<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import api from '@/api/index';
import { message } from 'ant-design-vue';
import { Tool } from "@/utils/tool";
import { useRoute } from "vue-router";


onMounted(() => {
  handleQuery();
});


const route = useRoute();
const docs = ref();
const html = ref();
const defaultSelectedKeys = ref();
defaultSelectedKeys.value = [];

//当前选中的文档信息
const doc = ref();
doc.value = {};

const level1 = ref(); // 一级文档树，children属性就是二级文档
level1.value = [];

/**
 * 数据查询
 **/
const handleQuery = () => {
  api.get("/doc/all/" + route.query.ebookId).then((response) => {
    const data = response.data;
    if (data.success) {
      docs.value = data.content;

      level1.value = [];
      level1.value = Tool.array2Tree(docs.value, 0);

      console.log(Tool.isNotEmpty(level1))
      // if (Tool.isNotEmpty(level1)) {
      //   defaultSelectedKeys.value = [level1.value[0].id];
      //   handleQueryContent(level1.value[0].id);


      //   //初始线上文档信息
      //   doc.value = level1.value[0];
      // }

    } else {
      message.error(data.message);
    }
  });
};

/**
 * 内容查询
 **/
const handleQueryContent = (id: number) => {
  api.get("/content/findContent/" + id).then((response) => {
    const data = response.data;
    if (data.success) {
      html.value = data.content;
    } else {
      message.error(data.message);
    }
  });
};

const onSelect = (selectedKeys: any, info: any) => {
  console.log('selected', selectedKeys, info);
  if (Tool.isNotEmpty(selectedKeys)) {
    //选中某一个节点时，加载该节点的文档信息
    doc.value = info.selectedNodes[0];
    // 加载内容
    handleQueryContent(selectedKeys[0]);
  }
};

// 点赞
const vote = () => {
  api.get('/doc/vote/' + doc.value.id).then((response) => {
    const data = response.data;
    if (data.success) {
      doc.value.voteCount++;
    } else {
      message.error(data.message);
    }
  });
};

</script>

<style>
/* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
/* table 样式 */
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}

.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}

.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  display: inline;
  zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}

.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditor ul,
ol {
  margin: 10px 0 10px 20px;
}

/* 和antdv p冲突，覆盖掉 */
.wangeditor blockquote p {
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}

.vote-div {
  padding: 15px;
  text-align: center;
}

/* 图片自适应 */
.wangeditor img {
  max-width: 100%;
  height: auto;
}

/* 视频自适应 */
.wangeditor iframe {
  width: 100%;
  height: 400px;
}
</style>
