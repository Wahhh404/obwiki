<template>
    <div>
        <a-layout>
            <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
                <p>
                    <a-form layout="inline" :model="param">
                        <a-form-item>
                            <a-input v-model:value="param.name" placeholder="名称">
                            </a-input>
                        </a-form-item>
                        <a-form-item>
                            <a-button type="primary" @click="handleQuery({ page: 1, size: pagination.pageSize })">
                                查询
                            </a-button>
                        </a-form-item>
                        <a-form-item>
                            <a-button type="primary" @click="add()">
                                注册
                            </a-button>
                        </a-form-item>
                    </a-form>
                </p>


                <a-table :columns="columns" :row-key="record => record.id" :data-source="users" :pagination="pagination"
                    :loading="loading" @change="handleTableChange">
                    <template #bodyCell="{ column, record }">

                        <template v-if="column.dataIndex === 'action'">
                            <a-space size="small">
                                <a-button type="primary" @click="resetPassword(record)">
                                    重置密码
                                </a-button>
                                <a-button type="primary" @click="edit(record)">
                                    编辑
                                </a-button>
                                <a-popconfirm title="删除后不可以恢复，确认删除?" ok-text="是" cancel-text="否"
                                    @confirm="handleDelete(record.id)">
                                    <a-button type="danger">
                                        删除
                                    </a-button>
                                </a-popconfirm>
                            </a-space>
                        </template>
                    </template>
                </a-table>
            </a-layout-content>
        </a-layout>

        <a-modal title="重置密码" v-model:visible="resetModalVisible" :confirm-loading="resetModalLoading"
            @ok="handleResetModalOk">
            <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
                <a-form-item label="新密码">
                    <a-input v-model:value="user.password" type="password" />
                </a-form-item>
            </a-form>
        </a-modal>
        <a-modal title="用户表单" v-model:open="modalVisible" :confirm-loading="modalLoading" @ok="handleModalOk">
            <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
                <a-form-item label="登录名">
                    <a-input v-model:value="user.loginName" :disabled="!!user.id" />
                </a-form-item>
                <a-form-item label="昵称">
                    <a-input v-model:value="user.name" />
                </a-form-item>
                <a-form-item label="密码" v-show="!user.id">
                    <a-input v-model:value="user.password" />
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import api from '@/api/index';
import { Tool } from "@/utils/tool";
import { message } from "ant-design-vue";
const users = ref();//定义查询用户返回集合
const param = ref();
param.value = {};

//定义分页参数
const pagination = ref({
    current: 1,
    pageSize: 10,
    total: 0
});
const loading = ref(false);
const columns = [
    {
        title: '登录名',
        key: 'cover',
        dataIndex: 'loginName'
    },
    {
        title: '名称',
        key: 'name',
        dataIndex: 'name'
    },
    {
        title: '密码',
        key: 'password',
        dataIndex: 'password'
    },
    {
        title: 'Action',
        key: 'action',
        dataIndex: 'action'

    }
];



// -------- 重置密码 ---------
const resetModalVisible = ref(false);//是否打开对话框
const resetModalLoading = ref(false);//是否等待
//打开重置密码对话框
const resetPassword = (record: any) => {
    resetModalLoading.value = false;//
    resetModalVisible.value = true;//打开对话框
    user.value = Tool.copy(record);//获取当前行数据
    user.value.password = null;//设置密码为空不显示
};
//执行修改
const handleResetModalOk = () => {
    resetModalLoading.value = true;
    const reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$/;
    //判断输入密码是否满足
    if (reg.test(user.value.password)) {
        user.value.password = hexMd5(user.value.password + KEY);

        api.post("/user/resetPassword", user.value).then((response) => {
            resetModalLoading.value = false;
            const data = response.data; // data = commonResp
            if (data.success) {
                resetModalVisible.value = false;

                // 重新加载列表
                handleQuery({
                    page: pagination.value.current,
                    size: pagination.value.pageSize,
                });
            } else {
                message.error(data.message);
            }
        });
    } else {
        message.error("【密码】至少包含 数字和英文，长度6-32");
        resetModalLoading.value = false;
    }
};

onMounted(() => {
    handleQuery({
        page: 1,
        size: pagination.value.pageSize
    })
})
/*
* 数据查询
* */
const handleQuery = (params: any) => {
    loading.value = true;
    users.value = [];
    api.get("/user/list", {
        params: {
            page: params.page,
            size: params.size,
            name: param.value.name
        }
    }).then((resp) => {
        loading.value = false;
        const data = resp.data;
        if (data.success) {
            users.value = data.content.list;

            //重置分页按钮
            pagination.value.current = params.page;
            pagination.value.total = data.content.total;
        }
    });
};
/*
* 表格点击页码时触发
* */
const handleTableChange = (pagination: any) => {
    console.log("看看自带分页的参数都有些啥：" + pagination);
    handleQuery({
        page: pagination.current,
        size: pagination.pageSize
    });
};

// 编辑相关功能
const user = ref();
const modalVisible = ref(false);
const modalLoading = ref(false);
//编辑
const edit = (record: any) => {
    modalVisible.value = true;
    user.value = Tool.copy(record);
}
//新增
const add = () => {
    modalVisible.value = true;
    user.value = {};

}


//删除
const handleDelete = (id: bigint) => {
    console.log("id:", id)
    api.delete('/user/delete/' + id).then((resp) => {
        if (resp.data.success) {
            //重新加载列表
            handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize
            });
        }
    })
}
declare let hexMd5: any;
declare let KEY: any;

const handleModalOk = () => {
    modalLoading.value = true;

    console.log(user.value);
    user.value.password = hexMd5(user.value.password + KEY);

    api.post("/user/save", user.value).then((resp) => {
        const data = resp.data;
        if (data.success) {
            modalVisible.value = false;
            modalLoading.value = false;
            //重新加载列表
            handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize
            })
        } else {
            message.error(data.message);
            modalLoading.value = false;
        }
    })
};

</script>
<style scoped></style>