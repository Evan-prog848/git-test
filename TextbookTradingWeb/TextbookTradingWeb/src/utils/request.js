import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from '@/utils/auth'

const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 30000 // 增加到30秒，因为ISBN查询需要调用外部API
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = token.startsWith('Bearer ') ? token : `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      // 如果请求配置了silent选项，则不显示错误提示
      if (!response.config.silent) {
        ElMessage.error(res.message || 'Error')
      }
      if (res.code === 401) {
        // 未登录或token过期
        removeToken()
        location.href = '/login'
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    // 如果请求配置了silent选项，则不显示错误提示
    if (!error.config?.silent) {
      ElMessage.error(error.message)
    }
    return Promise.reject(error)
  }
)

export default service


