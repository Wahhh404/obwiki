import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';
import {Tool} from "@/utils/tool";
import api from '@/api/index';

/*
* 使用axios拦截器打印请求和响应的数据
* */
api.interceptors.request.use(function (config:any){
    console.log('请求参数：',config);
    const token = store.state.user.token;
    if (Tool.isNotEmpty(token)) {
        config.headers.token = token;
        console.log("请求headers增加token:", token);
    }
    return config;
},error => {
    return Promise.reject(error);
});


const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

//全局使用图标
const icons:any = Icons;
for (const  i in icons){
    app.component(i,icons[i]);
}

console.log('环境',process.env.NODE_ENV);
console.log('服务端',process.env.VUE_APP_SERVER);
