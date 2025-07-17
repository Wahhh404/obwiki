<template>
  <div>
    <a-layout>
      <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
        <a-row>
          <a-col :span="8">
            <p>
              <a-form
                layout="inline"
                :model="param"
              >
                <a-form-item>
                  <a-button
                    type="primary"
                    @click="handleAdd()"
                  >
                    新增
                  </a-button>
                </a-form-item>
              </a-form>
            </p>


            <a-table
              v-if="level1.length > 0"
              :columns="columns"
              :row-key="(record: Doc) => record.id"
              :data-source="level1"
              :pagination="false"
              :loading="loading"
              :default-expand-all-rows="true"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                  <a-space size="small">
                    <a-button
                      type="primary"
                      @click="handleEdit(record)"
                    >
                      编辑
                    </a-button>
                    <a-popconfirm
                      title="删除后不可以恢复，确认删除?"
                      ok-text="是"
                      cancel-text="否"
                      @confirm="handleDelete(record.id)"
                    >
                      <a-button danger>
                        删除
                      </a-button>
                    </a-popconfirm>
                  </a-space>
                </template>
              </template>
            </a-table>
          </a-col>

          <a-col :span="16">
            <p>
              <a-form layout="inline">
                <a-form-item>
                  <a-button
                    type="primary"
                    @click="handleSave()"
                  >
                    保存
                  </a-button>
                </a-form-item>
              </a-form>
            </p>

            <a-form
              :model="doc"
              :label-col="{ span: 6 }"
              :wrapper-col="{ span: 18 }"
            >
              <a-form-item>
                <a-input
                  v-model:value="doc.name"
                  placeholder="名称"
                />
              </a-form-item>
              <a-form-item>
                <a-tree-select
                  v-model:value="doc.parent"
                  show-search
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  placeholder="请选择父文档"
                  allow-clear
                  tree-default-expand-all
                  :tree-data="treeSelectData"
                  tree-node-filter-prop="label"
                  :replace-fields="{ children: 'children', label: 'name', key: 'id', value: 'id' }"
                />
              </a-form-item>
              <a-form-item>
                <a-select
                  ref="select"
                  v-model:value="doc.parent"
                  style="width: 120px"
                >
                  <a-select-option value="0">
                    无
                  </a-select-option>
                  <a-select-option
                    v-for="c in level1"
                    :key="c.id"
                    :value="c.id"
                    :disabled="doc.id === c.id"
                  >
                    {{ c.name }}
                  </a-select-option>
                </a-select>
              </a-form-item>


              <a-form-item>
                <a-input
                  v-model:value="doc.sort"
                  placeholder="顺序"
                />
              </a-form-item>


              <a-form-item>
                <div style="border: 1px solid #ccc">
                  <Toolbar
                    :editor="editorRef"
                    :default-config="toolbarConfig"
                    mode="default"
                  />
                  <Editor
                    v-model="doc.content"
                    :default-config="editorConfig"
                    mode="default"
                    style="height: 500px; overflow-y: hidden;"
                    @on-created="handleCreated"
                  />
                </div>
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      </a-layout-content>
    </a-layout>
  </div>
</template>

<script lang="ts" setup>
import api from '@/api/index';
import { ref, onMounted, shallowRef } from 'vue';
import { Tool } from "@/utils/tool";
import { message } from 'ant-design-vue';
import { useRoute } from 'vue-router';
const route = useRoute();
const ebookId = route.query.ebookId || '0'; // 默认值为 '0'
import '@wangeditor/editor/dist/css/style.css' // 样式
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'


const editorRef = ref()
const valueHtml = ref('<p>请输入内容</p>')
const toolbarConfig = {}
const editorConfig = { placeholder: '请输入内容...', MENU_CONF: {} }
const handleCreated = (editor: any) => {
  editorRef.value = editor
}

const docs = ref();//定义查询电子书返回集合
const param = ref();
param.value = {};
//定义分页参数
const pagination = ref({
  current: 1,
  pageSize: 2,
  total: 0
});
const loading = ref(false);
const columns = [
  {
    title: '名称',
    dataIndex: 'name'
  },
  {
    title: '操作',
    key: 'action',
    dataIndex: 'action'
  }
];
const modalVisible = ref(false);
const modalLoading = ref(false);
const level1 = ref<{ id: number; name: string; children?: any[] }[]>([]);// 编辑相关功能
const doc = ref({
  id: 0,
  name: "",
  parent: 0,
  ebookId: 0,
  sort: 0,
  content: '',
});
//定义显示文档树 响应式数据,因为树选择组件属性转台会随当前编辑节点而变化  初始值为空
const treeSelectData = ref<{ id: number; name: string; children?: any[]; disabled?: boolean }[]>([]);
interface Doc {
  id?: bigint;
  ebookId?: bigint;
  parent?: bigint;
  name?: string;
  sort?: number;
  viewCount?: number;
  voteCount?: number;
  content?: string;
}

/*
* 数据查询
* */
const handleQuery = () => {
  loading.value = true;
  api.get("/doc/all").then((resp) => {
    loading.value = false;
    const data = resp.data;
    if (data.success) {

      level1.value = [];//每次查询清空分类数组
      //通过工具类 递归父分类及子分类  参数1 分类所有数据 参数2 父分类id为0
      level1.value = Tool.array2Tree(data.content, 0);
    }
  });
};

//编辑
const handleEdit = (record: any) => {
  doc.value = Tool.copy(record);
  modalVisible.value = true;

  handleQueryContent();//查询文档内容

  //不能选择当前节点及其子节点，作为父节点，会使得树断开
  treeSelectData.value = Tool.copy(level1.value);
  setDisable(treeSelectData.value, record.id);

  //为树选择添加一个“无”的选项
  treeSelectData.value.unshift({ id: 0, name: '无' });
}

//内容查询
const handleQueryContent = () => {
  api.get("/content/findContent/" + doc.value.id).then((response) => {
    const data = response.data;
    if (data.success) {
      editorRef.value.setHtml(data.content || '')
    } else {
      message.error(data.message);
    }
  });
};
/**
 * 将某节点及其子孙节点全部置为disabled
 */
const setDisable = (treeSelectData: any, id: any) => {
  // console.log(treeSelectData, id);
  // 遍历数组，即遍历某一层节点
  for (let i = 0; i < treeSelectData.length; i++) {
    const node = treeSelectData[i];
    if (node.id === id) {
      // 将目标节点设置为disabled
      node.disabled = true;

      // 遍历所有子节点，将所有子节点全部都加上disabled
      const children = node.children;
      if (Tool.isNotEmpty(children)) {
        for (let j = 0; j < children.length; j++) {
          setDisable(children, children[j].id)
        }
      }
    } else {
      // 如果当前节点不是目标节点，则到其子节点再找找看。
      const children = node.children;
      if (Tool.isNotEmpty(children)) {
        setDisable(children, id);
      }
    }
  }
};
//新增
const handleAdd = () => {
  modalVisible.value = true;
  console.log("我要新增节点")
  doc.value = {
    id: 0,
    ebookId: parseInt(route.query.ebookId as string || '0'),
    parent: 0, // 初始化为 null 或符合树结构的对象
    name: '',
    sort: 0,
    content: ''
  };
  //不能选择当前节点及其子节点，作为父节点，会使得树断开
  treeSelectData.value = Tool.copy(level1.value);
  //为树选择添加一个“无”的选项
  treeSelectData.value.unshift({ id: 0, name: '无' });
}
/**
     * 查找整根树枝
     */
const ids: Array<string> = [];
const deleteNames: Array<string> = [];
const getDeleteIds = (treeSelectData: any, id: any) => {
  // console.log(treeSelectData, id);
  // 遍历数组，即遍历某一层节点
  for (let i = 0; i < treeSelectData.length; i++) {
    const node = treeSelectData[i];
    if (node.id === id) {
      // 如果当前节点就是目标节点
      console.log("delete", node);
      // 将目标ID放入结果集ids
      // node.disabled = true;
      ids.push(id);
      deleteNames.push(node.name);

      // 遍历所有子节点
      const children = node.children;
      if (Tool.isNotEmpty(children)) {
        for (let j = 0; j < children.length; j++) {
          getDeleteIds(children, children[j].id)
        }
      }
    } else {
      // 如果当前节点不是目标节点，则到其子节点再找找看。
      const children = node.children;
      if (Tool.isNotEmpty(children)) {
        getDeleteIds(children, id);
      }
    }
  }
};

//删除
const handleDelete = (id: number) => {
  // 清空数组，否则多次删除时，数组会一直增加
  ids.length = 0;
  deleteNames.length = 0;
  getDeleteIds(level1.value, id);//获取要删除的节点及子节点id

  api.get('/doc/remove?ids=' + ids.join(",")).then((response) => {
    const data = response.data;
    if (data.success) {
      // 重新加载列表
      handleQuery();
    } else {
      message.error(data.message);
    }
  })
}

// 保存
const handleSave = () => {
  modalLoading.value = true;

  doc.value.content = editorRef.value.getHtml();
  api.post("/doc/save", doc.value).then((resp) => {
    const data = resp.data;
    if (data.success) {
      modalLoading.value = false;

      message.success("保存成功！");

      //重新加载列表
      handleQuery();
    }
  })
};

onMounted(() => {
  handleQuery();
})
</script>
<style scoped></style>
