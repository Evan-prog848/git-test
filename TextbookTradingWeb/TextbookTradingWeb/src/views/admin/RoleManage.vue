<template>
  <div class="role-manage">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">新增角色</el-button>
        </div>
      </template>

      <el-table :data="roleList" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="roleCode" label="角色编码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="small" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="warning" link @click="handleAuth(scope.row)">分配权限</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑角色对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入角色描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog v-model="authVisible" title="分配权限" width="400px">
      <el-tree
        ref="treeRef"
        :data="menuData"
        show-checkbox
        node-key="id"
        :props="{ label: 'menuName', children: 'children' }"
      />
      <template #footer>
        <el-button @click="authVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAuth">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, getMenuTree, assignRoleMenus, getRoleMenuIds, addRole, updateRole, deleteRole } from '@/api/admin'

const loading = ref(false)
const roleList = ref([])
const authVisible = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const currentRoleId = ref(null)
const treeRef = ref(null)
const formRef = ref(null)
const menuData = ref([])

const form = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: ''
})

const rules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

const fetchRoles = async () => {
  loading.value = true
  try {
    const res = await getRoleList({ pageNum: 1, pageSize: 100 })
    roleList.value = res.data.records
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const fetchMenuTree = async () => {
  try {
    const res = await getMenuTree()
    menuData.value = res.data
  } catch (error) {}
}

const handleAdd = () => {
  dialogTitle.value = '新增角色'
  Object.assign(form, { id: null, roleName: '', roleCode: '', description: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑角色'
  Object.assign(form, row)
  dialogVisible.value = true
}

const submitForm = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  try {
    if (form.id) {
      await updateRole(form)
    } else {
      await addRole(form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchRoles()
  } catch (error) {
    console.error('Save failed:', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    fetchRoles()
  })
}

const handleAuth = async (row) => {
  currentRoleId.value = row.id
  authVisible.value = true
  if (menuData.value.length === 0) await fetchMenuTree()

  try {
    const res = await getRoleMenuIds(row.id)
    if (treeRef.value) {
      treeRef.value.setCheckedKeys(res.data)
    }
  } catch (error) {}
}

const submitAuth = async () => {
  const selectedKeys = treeRef.value.getCheckedKeys()
  try {
    await assignRoleMenus(currentRoleId.value, selectedKeys)
    ElMessage.success('权限分配成功')
    authVisible.value = false
  } catch (error) {}
}

onMounted(fetchRoles)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>


