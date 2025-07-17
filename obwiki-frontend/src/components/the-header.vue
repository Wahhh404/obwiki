<template>
  <a-layout-header class="header">
    <div class="logo">海洋生物知识库 </div>
    <a-menu theme="dark" mode="horizontal" :style="{ lineHeight: '64px', display: 'block' }">
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook">
        <router-link to="/admin/ebook">海洋生物种类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category">
        <router-link to="/admin/category">海洋生物分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>



      <a-menu-item key="user" :style="{ float: 'right' }">
        <span v-show="user.id">您好：{{ user.name }}</span>
      </a-menu-item>

      <a-menu-item key="login" :style="{ float: 'right' }">
        <a v-show="!user.id" @click="showLoginModal">登录</a>
      </a-menu-item>

      <a-menu-item :style="{ float: 'right' }">
        <a-popconfirm title="确认退出登录?" ok-text="是" cancel-text="否" @confirm="logout()">
          <a v-show="user.id">退出登录</a>
        </a-popconfirm>
      </a-menu-item>



    </a-menu>

    <a-modal title="登录" v-model:visible="loginModalVisible" :confirm-loading="loginModalLoading" @ok="login">
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts" setup>
import { message } from "ant-design-vue";
import api from '@/api/index';
import { computed, defineComponent, ref } from 'vue';
import store from '@/store';

declare let hexMd5: any;
declare let KEY: any;

//登录之后保存的用户
const user = computed(() => store.state.user);
// 用来登录

const loginUser = ref({
  loginName: "",
  password: ""
});


const loginModalVisible = ref(false);
const loginModalLoading = ref(false);
const showLoginModal = () => {
  loginModalVisible.value = true;
};

// 退出登录
const logout = () => {
  console.log("退出登录开始");
  api.get('/user/logout/' + user.value.token).then((response) => {
    const data = response.data;
    if (data.success) {
      message.success("退出登录成功！");
      store.commit("setUser", {});
    } else {
      message.error(data.message);
    }
  });
};


// 登录
const login = () => {
  console.log("开始登录");
  loginModalLoading.value = true;
  loginUser.value.password = hexMd5(loginUser.value.password + KEY);
  api.post('/user/login', loginUser.value).then((response) => {
    loginModalLoading.value = false;
    const data = response.data;
    if (data.success) {
      loginModalVisible.value = false;
      console.log("登录成功返回数据:", data);
      //vuex进行数据保存   存储数据库返回数据
      store.commit("setUser", data.content);
      message.success("登录成功！");
    } else {
      message.error(data.message);
      loginModalLoading.value = false;
    }
  });
};

</script>
<style scoped>
.logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
</style>

<style scoped>
.logo {
  width: 150px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}
</style>
