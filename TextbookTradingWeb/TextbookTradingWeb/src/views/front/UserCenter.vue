<template>
  <div class="container user-center-page">
    <el-row :gutter="20">
      <!-- 左侧菜单 -->
      <el-col :span="6">
        <el-card class="menu-card" :body-style="{ padding: '0px' }">
          <div class="user-brief">
            <el-avatar :size="64" :src="userStore.userInfo.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div class="nickname">{{ userStore.userInfo.nickname }}</div>
            <el-tag size="small" type="warning" v-if="userStore.userInfo.isVerified">已认证</el-tag>
          </div>
          <el-menu
            :default-active="activeMenu"
            class="user-menu"
            @select="handleMenuSelect"
          >
            <el-menu-item index="profile">
              <el-icon><User /></el-icon>
              <span>个人信息</span>
            </el-menu-item>
            <el-menu-item index="security">
              <el-icon><Lock /></el-icon>
              <span>账号安全</span>
            </el-menu-item>
            <el-menu-item index="verification">
              <el-icon><Checked /></el-icon>
              <span>学校认证</span>
            </el-menu-item>
            <el-menu-item index="my-posts">
              <el-icon><Document /></el-icon>
              <span>我的发布</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-col>

      <!-- 右侧内容 -->
      <el-col :span="18">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>{{ menuTitle }}</span>
            </div>
          </template>

          <!-- 个人信息表单 -->
          <div v-if="activeMenu === 'profile'">
            <el-form :model="userForm" label-width="100px" style="max-width: 500px">
              <el-form-item label="头像">
                <el-upload
                  class="avatar-uploader"
                  action="#"
                  :show-file-list="false"
                  :auto-upload="false"
                  :on-change="handleAvatarChange"
                  accept="image/*"
                >
                  <img :src="userForm.avatar || defaultAvatar" class="avatar" @error="handleAvatarError" />
                </el-upload>
              </el-form-item>
              <el-form-item label="用户名">
                <el-input v-model="userForm.username" disabled />
              </el-form-item>
              <el-form-item label="昵称">
                <el-input v-model="userForm.nickname" />
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="userForm.phone" />
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="userForm.email" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveProfile">保存修改</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 账号安全 -->
          <div v-if="activeMenu === 'security'">
            <el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="100px" style="max-width: 500px">
              <el-form-item label="当前密码" prop="oldPassword">
                <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入当前密码" />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="请输入新密码（至少6位）" />
              </el-form-item>
              <el-form-item label="确认新密码" prop="confirmPassword">
                <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="updatePassword">修改密码</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 学校认证 -->
          <div v-if="activeMenu === 'verification'">
            <el-alert
              v-if="userStore.userInfo.isVerified"
              title="您已完成学校认证"
              type="success"
              :closable="false"
              show-icon
              style="margin-bottom: 20px"
            />
            <el-form :model="verifyForm" label-width="100px" style="max-width: 500px">
              <el-form-item label="学校">
                <el-input v-model="verifyForm.school" :disabled="userStore.userInfo.isVerified" />
              </el-form-item>
              <el-form-item label="学院">
                <el-input v-model="verifyForm.college" :disabled="userStore.userInfo.isVerified" />
              </el-form-item>
              <el-form-item label="专业">
                <el-input v-model="verifyForm.major" :disabled="userStore.userInfo.isVerified" />
              </el-form-item>
              <el-form-item v-if="!userStore.userInfo.isVerified">
                <el-button type="primary" @click="submitVerification">提交认证</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 我的发布 -->
          <div v-if="activeMenu === 'my-posts'">
            <el-table :data="myBooks" style="width: 100%">
              <el-table-column label="教材" width="300">
                <template #default="scope">
                  <div class="book-cell">
                    <el-image :src="scope.row.cover" class="book-img" />
                    <span class="book-name">{{ scope.row.title }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="price" label="价格" width="100" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="getStatusType(scope.row)">
                    {{ getStatusText(scope.row) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="发布时间" />
              <el-table-column label="操作" width="150">
                <template #default="scope">
                  <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Checked, Document, Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { updateInfo, updatePassword as updatePwdApi } from '@/api/user'
import { getMyPublish } from '@/api/textbook'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()
const activeMenu = ref('profile')
const pwdFormRef = ref(null)

const menuTitle = computed(() => {
  const titles = {
    profile: '个人信息',
    security: '账号安全',
    verification: '学校认证',
    'my-posts': '我的发布'
  }
  return titles[activeMenu.value]
})

const userForm = reactive({
  username: userStore.userInfo.username,
  nickname: userStore.userInfo.nickname,
  avatar: userStore.userInfo.avatar,
  phone: userStore.userInfo.phone || '',
  email: userStore.userInfo.email || ''
})

const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== pwdForm.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const pwdRules = {
  oldPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'change' }
  ]
}

const verifyForm = reactive({
  school: userStore.userInfo.school || '',
  college: userStore.userInfo.college || '',
  major: userStore.userInfo.major || ''
})

const myBooks = ref([])

const handleMenuSelect = (index) => {
  activeMenu.value = index
  if (index === 'my-posts') {
    fetchMyBooks()
  }
}

// 当新密码改变时，重新验证确认密码字段
watch(() => pwdForm.newPassword, () => {
  if (pwdFormRef.value && pwdForm.confirmPassword) {
    pwdFormRef.value.validateField('confirmPassword')
  }
})

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const handleAvatarError = (e) => {
  e.target.src = defaultAvatar
}

const handleAvatarChange = async (file) => {
  // 先显示预览
  const reader = new FileReader()
  reader.onload = (e) => {
    userForm.avatar = e.target.result
  }
  reader.readAsDataURL(file.raw)
  
  // 上传到服务器
  try {
    const formData = new FormData()
    formData.append('file', file.raw)
    formData.append('type', 'avatar')
    
    const res = await request.post('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    
    // 上传成功后，使用服务器返回的路径
    if (res.data) {
      userForm.avatar = res.data
      ElMessage.success('头像上传成功')
    }
  } catch (error) {
    console.error('Avatar upload error:', error)
    ElMessage.error('头像上传失败: ' + (error.message || '未知错误'))
  }
}

const fetchMyBooks = async () => {
  try {
    const res = await getMyPublish({ pageNum: 1, pageSize: 100 })
    myBooks.value = res.data.records
  } catch (error) {}
}

const getStatusText = (row) => {
  // 先判断审核状态
  if (row.auditStatus === 0) return '审核中'
  if (row.auditStatus === 2) return '审核拒绝'
  // 审核通过后，显示上架状态
  const map = { 0: '已下架', 1: '在售中', 2: '已售出' }
  return map[row.status]
}

const getStatusType = (row) => {
  // 先判断审核状态
  if (row.auditStatus === 0) return 'warning'
  if (row.auditStatus === 2) return 'danger'
  // 审核通过后，显示上架状态
  const map = { 0: 'info', 1: 'success', 2: 'warning' }
  return map[row.status]
}

const saveProfile = async () => {
  try {
    await updateInfo(userForm)
    ElMessage.success('信息修改成功')
    userStore.setUserInfo({ ...userStore.userInfo, ...userForm })
  } catch (error) {}
}

const updatePassword = async () => {
  if (!pwdFormRef.value) return
  
  pwdFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await updatePwdApi({
          oldPassword: pwdForm.oldPassword,
          newPassword: pwdForm.newPassword,
          confirmPassword: pwdForm.confirmPassword
        })
        ElMessage.success('密码修改成功')
        pwdFormRef.value.resetFields()
      } catch (error) {
        ElMessage.error(error.message || '密码修改失败')
      }
    }
  })
}

const submitVerification = () => {
  ElMessage.success('认证申请已提交，请等待审核')
  // 模拟通过
  userStore.setUserInfo({ ...userStore.userInfo, ...verifyForm, isVerified: true })
}

const handleEdit = (row) => {
  // 跳转到发布页面，并传递教材ID用于编辑
  router.push({ path: '/publish', query: { id: row.id } })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条发布吗？', '提示', { type: 'warning' })
    // 调用删除API
    await request.delete(`/textbook/${row.id}`)
    ElMessage.success('删除成功')
    // 刷新列表
    fetchMyBooks()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  if (userStore.userInfo.isVerified) {
    verifyForm.school = userStore.userInfo.school
    verifyForm.college = userStore.userInfo.college
    verifyForm.major = userStore.userInfo.major
  }
})
</script>

<style scoped lang="scss">
.user-center-page {
  padding: 20px 0;

  :deep(.el-card) {
    border-radius: var(--radius-md);
    border: 1px solid var(--border-light);
    box-shadow: var(--shadow-sm);
  }
}

.menu-card {
  .user-brief {
    padding: 32px 20px;
    text-align: center;
    border-bottom: 1px solid var(--border-light);
    background: linear-gradient(180deg, var(--primary-lighter), transparent);

    .el-avatar {
      border: 3px solid var(--bg-card);
      box-shadow: var(--shadow-md);
    }

    .nickname {
      margin: 15px 0 5px;
      font-weight: 700;
      font-size: 18px;
      color: var(--text-primary);
    }
  }

  .user-menu {
    border-right: none;

    :deep(.el-menu-item) {
      border-radius: var(--radius-sm);
      margin: 2px 8px;
      height: 44px;
      line-height: 44px;
      transition: all 0.2s;

      &.is-active {
        background: var(--primary-lighter);
        color: var(--primary-color);
        font-weight: 500;
      }

      &:hover:not(.is-active) {
        background: var(--bg-color);
      }
    }
  }
}

.content-card {
  min-height: 500px;

  :deep(.el-card__header) {
    font-weight: 600;
    color: var(--text-primary);
    border-bottom: 1px solid var(--border-light);
  }
}

.avatar-uploader {
  :deep(.el-upload) {
    border: 2px dashed var(--border-color);
    border-radius: var(--radius-md);
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: all 0.2s;
    &:hover {
      border-color: var(--primary-color);
      background: var(--primary-lighter);
    }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: var(--text-placeholder);
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
}

.book-cell {
  display: flex;
  align-items: center;
  .book-img {
    width: 40px;
    height: 50px;
    margin-right: 10px;
    border-radius: 4px;
  }
  .book-name {
    font-size: 14px;
    color: var(--text-primary);
  }
}
</style>


