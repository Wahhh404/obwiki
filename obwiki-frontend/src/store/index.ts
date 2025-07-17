import { createStore } from 'vuex'

//声明对象
declare let SessionStorage :any;
const USER = 'USER';//声明key
 const store = createStore({
  state: {
    //创建用户对象
    user:SessionStorage.get(USER) || {}
  },
  getters: {
  },
  mutations: {
    //提供外部调用方法存储用户信息
    setUser(state,user){
      state.user = user;
      SessionStorage.set(USER,user);
    }
  },
  actions: {
  },
  modules: {
  }
}) //对外暴露
export default store;