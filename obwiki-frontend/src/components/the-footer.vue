<template>
  <a-layout-footer style="text-align: center">
    海洋生物知识库系统 <span v-show="user.id">欢迎: {{ user.name }}</span>
  </a-layout-footer>
</template>
<script lang="ts" setup>
import { computed, onMounted } from 'vue';
import { Tool } from "@/utils/tool";
import store from '@/store';
import { notification } from "ant-design-vue";

const user = computed(() => store.state.user);

let websocket: any;//声明
let token: any;
onMounted(() => {
  // 判断当前浏览器是否支持 websocket(H5自带)
  if ('WebSocket' in window) {
    token = Tool.uuid(10);//生成当前客户端token
    // 连接地址：ws://127.0.0.1:8880/ws/xxx
    websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
    initWebSocket()//初始化

    // 关闭
    // websocket.close();
  } else {
    alert('当前浏览器 不支持')
  }
});

const onOpen = () => {
  console.log('WebSocket连接成功，状态码：', websocket.readyState)
};
const onMessage = (event: any) => {
  console.log('WebSocket收到消息：', event.data);
  notification['info']({
    message: '收到消息',
    description: event.data,
  });
};
const onError = () => {
  console.log('WebSocket连接错误，状态码：', websocket.readyState)
};
const onClose = () => {
  console.log('WebSocket连接关闭，状态码：', websocket.readyState)
};
const initWebSocket = () => {
  // 连接成功
  websocket.onopen = onOpen;
  // 收到消息的回调
  websocket.onmessage = onMessage;
  // 连接错误
  websocket.onerror = onError;
  // 连接关闭的回调
  websocket.onclose = onClose;
};
</script>