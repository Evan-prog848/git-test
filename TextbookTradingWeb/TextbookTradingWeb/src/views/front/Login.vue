<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <div class="logo">
          <el-icon :size="40" color="#2b9a6e"><Reading /></el-icon>
          <span class="title">二手教材交易系统</span>
        </div>
        <p class="subtitle">让知识传递，让书籍循环</p>
      </div>
      
      <el-card class="login-card" shadow="hover">
        <h2 class="form-title">欢迎登录</h2>
        <el-form :model="loginForm" :rules="rules" ref="loginFormRef" size="large" class="login-form">
          <el-form-item prop="username">
            <el-input 
              v-model="loginForm.username" 
              placeholder="请输入用户名" 
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="请输入密码" 
              :prefix-icon="Lock" 
              show-password
            />
          </el-form-item>
          <div class="form-options">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <el-button link type="primary">忘记密码？</el-button>
          </div>
          <el-form-item>
            <el-button type="primary" class="submit-btn" :loading="loading" @click="handleLogin">
              立即登录
            </el-button>
          </el-form-item>
          <div class="form-footer">
            还没有账号？ 
            <el-button link type="primary" @click="$router.push('/register')">立即注册</el-button>
          </div>
        </el-form>
      </el-card>

      <div class="page-footer">
        <p>© 2025 Textbook Trading System</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { User, Lock, Reading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { login, getInfo } from '@/api/user'
import axios from 'axios'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const loginFormRef = ref(null)
const loading = ref(false)
const rememberMe = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      const baseURL = import.meta.env.VITE_APP_BASE_API
      
      try {
        // 先静默尝试管理员登录（使用原生axios绕过全局拦截器）
        const adminRes = await axios.post(`${baseURL}/admin/login`, loginForm)
        
        if (adminRes.data && adminRes.data.code === 200 && adminRes.data.data?.token) {
          localStorage.setItem('adminToken', adminRes.data.data.token)
          localStorage.setItem('token', adminRes.data.data.token)
          
          const adminInfo = await axios.get(`${baseURL}/admin/info`, {
            headers: { 'Authorization': `Bearer ${adminRes.data.data.token}` }
          })
          localStorage.setItem('adminInfo', JSON.stringify(adminInfo.data.data))
          
          ElMessage.success('管理员登录成功')
          router.push('/admin/dashboard')
          return
        }
      } catch (adminError) {
        // 管理员登录失败，静默处理，继续尝试普通用户登录
      }
      
      try {
        // 尝试普通用户登录
        const res = await login(loginForm)
        userStore.setToken(res.data.token)

        const infoRes = await getInfo()
        userStore.setUserInfo(infoRes.data)

        ElMessage.success('登录成功')
        const redirect = route.query.redirect || '/'
        router.push(redirect)
      } catch (error) {
        // 用户登录也失败，不需要额外处理，全局拦截器已显示错误
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped lang="scss">
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("https://images.unsplash.com/photo-1507842217343-583bb7270b66?q=80&w=2690&auto=format&fit=crop");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(26, 29, 35, 0.55), rgba(43, 154, 110, 0.3));
    z-index: 0;
  }
}

.login-box {
  width: 100%;
  max-width: 420px;
  padding: 20px;
  position: relative;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;

  .logo {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;

    :deep(.el-icon) {
      background: rgba(255, 255, 255, 0.15);
      border-radius: 12px;
      padding: 8px;
      backdrop-filter: blur(8px);
    }

    .title {
      font-size: 30px;
      font-weight: 800;
      color: #ffffff;
      text-shadow: 0 2px 8px rgba(0,0,0,0.3);
      letter-spacing: -0.5px;
    }
  }

  .subtitle {
    font-size: 15px;
    color: rgba(255, 255, 255, 0.85);
    margin: 0;
    text-shadow: 0 1px 4px rgba(0,0,0,0.3);
    font-weight: 400;
  }
}

.login-card {
  border-radius: var(--radius-lg);
  border: none;
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.12);
  background: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(20px);
  
  .form-title {
    text-align: center;
    margin: 0 0 28px;
    font-size: 22px;
    color: var(--text-primary);
    font-weight: 700;
  }

  .login-form {
    padding: 0 10px;
  }

  :deep(.el-input__wrapper) {
    border-radius: var(--radius-sm);
    padding: 4px 12px;
  }
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.submit-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 2px;
  border-radius: var(--radius-md);
  background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
  border: none;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(43, 154, 110, 0.35);
  }

  &:active {
    transform: translateY(0);
  }
}

.form-footer {
  text-align: center;
  margin-top: 12px;
  font-size: 14px;
  color: var(--text-regular);
}

.page-footer {
  text-align: center;
  margin-top: 36px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
}
</style>


