<template>
  <div class="admin-manage">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>管理员管理</span>
          <el-button type="primary" @click="handleAdd">新增管理员</el-button>
        </div>
      </template>

      <el-table :data="adminList" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="roleName" label="所属角色" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
              :disabled="scope.row.username === 'admin'"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)" :disabled="scope.row.username === 'admin'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑管理员对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="form.roleId" placeholder="请选择角色" style="width: 100%">
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.roleName"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminList, addAdmin, updateAdmin, deleteAdmin, updateAdminStatus, getAllRoles } from '@/api/admin'

const loading = ref(false)
const adminList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const roleList = ref([])

const form = reactive({
  id: null,
  username: '',
  nickname: '',
  password: '',
  roleId: null,
  status: 1
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ],
  roleId: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const fetchAdmins = async () => {
  loading.value = true
  try {
    const res = await getAdminList({ pageNum: 1, pageSize: 100 })
    adminList.value = res.data.records
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const fetchRoles = async () => {
  try {
    const res = await getAllRoles()
    roleList.value = res.data
  } catch (error) {}
}

const handleAdd = () => {
  dialogTitle.value = '新增管理员'
  Object.assign(form, { id: null, username: '', nickname: '', password: '', roleId: null, status: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑管理员'
  Object.assign(form, {
    id: row.id,
    username: row.username,
    nickname: row.nickname,
    password: '',
    roleId: row.roleId,
    status: row.status
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该管理员吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteAdmin(row.id)
    ElMessage.success('删除成功')
    fetchAdmins()
  })
}

const handleStatusChange = async (row) => {
  try {
    await updateAdminStatus(row.id, row.status)
    ElMessage.success('状态已更新')
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
  }
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          await updateAdmin(form)
          ElMessage.success('更新成功')
        } else {
          await addAdmin(form)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        fetchAdmins()
      } catch (error) {}
    }
  })
}

onMounted(() => {
  fetchAdmins()
  fetchRoles()
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style


