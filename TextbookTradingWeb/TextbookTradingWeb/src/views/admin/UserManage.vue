<template>
  <div class="user-manage">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchQuery">
        <el-form-item label="用户名">
          <el-input v-model="searchQuery.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="searchQuery.school" placeholder="请输入学校" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchQuery.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span>用户列表</span>
          <el-button type="primary" @click="handleAdd">新增用户</el-button>
        </div>
      </template>
      <el-table :data="userList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="头像" width="80">
          <template #default="scope">
            <el-avatar :size="40" :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="school" label="学校" width="180" />
        <el-table-column label="认证状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isVerified ? 'success' : 'info'">
              {{ scope.row.isVerified ? '已认证' : '未认证' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="账号状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="信用积分" width="100">
          <template #default="scope">
            <span :class="getCreditClass(scope.row.creditScore)">
              {{ scope.row.creditScore ?? 100 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" link @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="small" type="danger" link @click="handleResetPwd(scope.row)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="searchQuery.pageNum"
          v-model:page-size="searchQuery.pageSize"
          :total="total"
          layout="total, prev, pager, next, jumper"
          @current-change="fetchUsers"
        />
      </div>
    </el-card>

    <!-- 新增用户弹窗 -->
    <el-dialog v-model="addVisible" title="新增用户" width="500px">
      <el-form :model="addForm" :rules="addRules" ref="addFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="addForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password" type="password" placeholder="请输入密码，默认123456" show-password />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="addForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="addForm.school" placeholder="请输入学校" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="addForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 用户详情弹窗 -->
    <el-dialog v-model="detailVisible" title="用户详情" width="600px">
      <div class="user-detail" v-if="currentUser">
        <div class="detail-header">
          <el-avatar :size="80" :src="currentUser.avatar" />
          <div class="detail-info">
            <h3>{{ currentUser.nickname || currentUser.username }}</h3>
            <p class="username">@{{ currentUser.username }}</p>
            <el-tag :type="currentUser.isVerified ? 'success' : 'info'" size="small">
              {{ currentUser.isVerified ? '已认证' : '未认证' }}
            </el-tag>
            <el-tag :type="currentUser.status === 1 ? 'success' : 'danger'" size="small" style="margin-left: 8px">
              {{ currentUser.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </div>
        </div>
        <el-divider />
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户ID">{{ currentUser.id }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
          <el-descriptions-item label="昵称">{{ currentUser.nickname || '-' }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ currentUser.phone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ currentUser.email || '-' }}</el-descriptions-item>
          <el-descriptions-item label="学校">{{ currentUser.school || '-' }}</el-descriptions-item>
          <el-descriptions-item label="学院">{{ currentUser.college || '-' }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ currentUser.createTime }}</el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <div class="detail-stats">
          <div class="stat-item">
            <div class="stat-value">{{ currentUser.textbookCount || 0 }}</div>
            <div class="stat-label">发布教材</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ currentUser.orderCount || 0 }}</div>
            <div class="stat-label">交易订单</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ currentUser.favoriteCount || 0 }}</div>
            <div class="stat-label">收藏数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ currentUser.followCount || 0 }}</div>
            <div class="stat-label">关注数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ formatGoodRate(currentUser.goodRate) }}</div>
            <div class="stat-label">好评率</div>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, getUserDetail, updateUserStatus, addUser, resetUserPassword } from '@/api/admin'

const loading = ref(false)
const total = ref(0)
const userList = ref([])
const detailVisible = ref(false)
const addVisible = ref(false)
const currentUser = ref(null)
const addFormRef = ref(null)

const addForm = reactive({
  username: '',
  nickname: '',
  password: '',
  phone: '',
  school: '',
  status: 1
})

const addRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' }
  ]
}

const searchQuery = reactive({
  pageNum: 1,
  pageSize: 10,
  username: '',
  school: '',
  status: undefined
})

const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await getUserList(searchQuery)
    userList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  searchQuery.pageNum = 1
  fetchUsers()
}

const resetSearch = () => {
  searchQuery.username = ''
  searchQuery.school = ''
  searchQuery.status = undefined
  handleSearch()
}

const handleStatusChange = async (row) => {
  try {
    await updateUserStatus(row.id, row.status)
    const text = row.status === 1 ? '启用' : '禁用'
    ElMessage.success(`${row.username} 已${text}`)
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
  }
}

const formatGoodRate = (value) => {
  if (value === null || value === undefined || Number.isNaN(Number(value))) {
    return '0%'
  }
  return `${Number(value).toFixed(1)}%`
}

const handleDetail = async (row) => {
  try {
    const res = await getUserDetail(row.id)
    currentUser.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error('Failed to fetch user detail:', error)
  }
}

const handleAdd = () => {
  Object.assign(addForm, { username: '', nickname: '', password: '', phone: '', school: '', status: 1 })
  addVisible.value = true
}

const submitAdd = async () => {
  const valid = await addFormRef.value.validate().catch(() => false)
  if (!valid) return
  
  try {
    await addUser(addForm)
    ElMessage.success('添加成功')
    addVisible.value = false
    fetchUsers()
  } catch (error) {
    console.error('Add user failed:', error)
  }
}

const handleResetPwd = (row) => {
  ElMessageBox.confirm(`确定要重置用户 ${row.username} 的密码吗？重置后密码为 123456`, '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      await resetUserPassword(row.id)
      ElMessage.success('密码已重置为 123456')
    } catch (error) {
      console.error('Reset password failed:', error)
    }
  })
}

// 信用积分样式
const getCreditClass = (score) => {
  const s = score ?? 100
  if (s >= 70) return 'credit-good'
  if (s >= 50) return 'credit-warning'
  return 'credit-danger'
}

onMounted(fetchUsers)
</script>

<style scoped lang="scss">
.search-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.user-detail {
  .detail-header {
    display: flex;
    align-items: center;
    gap: 20px;
    
    .detail-info {
      h3 {
        margin: 0 0 8px 0;
        font-size: 20px;
        color: var(--text-primary);
      }
      
      .username {
        margin: 0 0 12px 0;
        color: var(--text-secondary);
        font-size: 14px;
      }
    }
  }
  
  .detail-stats {
    display: flex;
    justify-content: space-around;
    text-align: center;
    
    .stat-item {
      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: var(--primary-color);
      }
      
      .stat-label {
        font-size: 12px;
        color: var(--text-secondary);
        margin-top: 4px;
      }
    }
  }
}

.credit-good {
  color: var(--success-color);
  font-weight: bold;
}

.credit-warning {
  color: var(--warning-color);
  font-weight: bold;
}

.credit-danger {
  color: var(--danger-color);
  font-weight: bold;
}
</style>


