import Vuex from 'vuex'
import Vue from 'vue'
import {getToken, setToken, removeToken} from '@/request/token'
import {login, getUserInfo,deleteAccount, logout, register} from '@/api/login'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    id: '',
    account: '',
    name: '',
    avatar: '',
     token: getToken(),//从浏览器缓存中获取token
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_ACCOUNT: (state, account) => {
      state.account = account
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ID: (state, id) => {
      state.id = id
    }
  },
  actions: {
    //登录
    login({commit}, user) {
      return new Promise((resolve, reject) => {
        //发起登录请求
        login(user.account, user.password).then(data => {
          if(data.success){
            //调用 mutations中的  SET_TOKEN方法
            commit('SET_TOKEN', data.data)
            //将token存在浏览器本地
            setToken(data.data)
            resolve()
          }else{
            reject(data.msg)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    getUserInfo({commit, state}) {
      let that = this
      return new Promise((resolve, reject) => {
        //发起获取用户信息请求
        getUserInfo(state.token).then(data => {
          if (data.success) {
            commit('SET_ACCOUNT', data.data.account)
            commit('SET_NAME', data.data.nickname)
            commit('SET_AVATAR', data.data.avatar)
            commit('SET_ID', data.data.id)
            resolve(data)
          } else {
            commit('SET_ACCOUNT', '')
            commit('SET_NAME', '')
            commit('SET_AVATAR', '')
            commit('SET_ID', '')
            //从浏览器中移除token
            removeToken()
            resolve(data)
          }

        }).catch(error => {
          commit('SET_ACCOUNT', '')
          commit('SET_NAME', '')
          commit('SET_AVATAR', '')
          commit('SET_ID', '')
          //从浏览器中移除token
          removeToken()
          reject(error)
        })
      })
    },
    // 退出
    logout({commit, state}) {
      return new Promise((resolve, reject) => {
        //发起登出请求
        logout(state.token).then(data => {
          if(data.success){
            commit('SET_TOKEN', '')
            commit('SET_ACCOUNT', '')
            commit('SET_NAME', '')
            commit('SET_AVATAR', '')
            commit('SET_ID', '')
            //从浏览器中移除token
            removeToken()
            resolve()
          }

        }).catch(error => {
          reject(error)
        })
      })
    },
    //注销账号
    deleteAccount({commit, state}) {
      return new Promise((resolve, reject) => {
        //发起注销请求
        deleteAccount(state.token).then(data => {
          if(data.success){
            commit('SET_TOKEN', '')
            commit('SET_ACCOUNT', '')
            commit('SET_NAME', '')
            commit('SET_AVATAR', '')
            commit('SET_ID', '')
            //从浏览器中移除token
            removeToken()
            resolve()
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    //前端 登出
    fedLogOut({commit}) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        commit('SET_ACCOUNT', '')
        commit('SET_NAME', '')
        commit('SET_AVATAR', '')
        commit('SET_ID', '')
        removeToken()
        resolve()
      }).catch(error => {
        reject(error)
      })
    },
    //注册
    register({commit}, user) {
      return new Promise((resolve, reject) => {
        //发起注册请求
        register(user.account, user.nickname, user.password).then((data) => {
          if(data.success){
            commit('SET_TOKEN', data.data)
            //将token存储在本地浏览器
            setToken(data.data)
            resolve()
          }else{
            reject(data.msg)
          }
        }).catch((error) => {
          reject(error)
        })
      })
    }
  }
})
