import { defineStore } from 'pinia'
import { getToken, setToken as saveToken, getUser, setUser as saveUser, clearAll } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken() || '',
    userInfo: getUser() || {}
  }),
  getters: {
    isLogin: (state) => !!state.token
  },
  actions: {
    setToken(token) {
      this.token = token
      saveToken(token)
    },
    setUserInfo(userInfo) {
      this.userInfo = userInfo
      saveUser(userInfo)
    },
    logout() {
      this.token = ''
      this.userInfo = {}
      clearAll()
    }
  }
})


