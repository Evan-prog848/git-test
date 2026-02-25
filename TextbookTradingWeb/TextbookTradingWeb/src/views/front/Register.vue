<template>
  <div class="register-container">
    <div class="register-box">
      <div class="register-header">
        <div class="logo">
          <el-icon :size="40" color="#2b9a6e"><Reading /></el-icon>
          <span class="title">二手教材交易系统</span>
        </div>
        <p class="subtitle">即刻加入，开启二手教材之旅</p>
      </div>

      <el-card class="register-card" shadow="hover">
        <h2 class="form-title">欢迎注册</h2>
        <el-form :model="registerForm" :rules="rules" ref="registerFormRef" size="large" class="register-form">
          <el-form-item prop="username">
            <el-input 
              v-model="registerForm.username" 
              placeholder="请输入用户名" 
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              v-model="registerForm.password" 
              type="password" 
              placeholder="设置密码" 
              :prefix-icon="Lock" 
              show-password
            />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input 
              v-model="registerForm.confirmPassword" 
              type="password" 
              placeholder="确认密码" 
              :prefix-icon="Lock" 
              show-password
            />
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input 
              v-model="registerForm.nickname" 
              placeholder="请输入昵称" 
              :prefix-icon="Edit"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="submit-btn" :loading="loading" @click="handleRegister">
              立即注册
            </el-button>
          </el-form-item>
          <div class="form-footer">
            已有账号？ 
            <el-button link type="primary" @click="$router.push('/login')">立即登录</el-button>
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
import { useRouter } from 'vue-router'
import { User, Lock, Edit, Reading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { register } from '@/api/user'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await register({
          username: registerForm.username,
          password: registerForm.password,
          confirmPassword: registerForm.confirmPassword,
          nickname: registerForm.nickname
        })
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped lang="scss">
.register-container {
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

.register-box {
  width: 100%;
  max-width: 440px;
  padding: 20px;
  position: relative;
  z-index: 1;
}

.register-header {
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

.register-card {
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

  .register-form {
    padding: 0 10px;
  }

  :deep(.el-input__wrapper) {
    border-radius: var(--radius-sm);
    padding: 4px 12px;
  }
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


