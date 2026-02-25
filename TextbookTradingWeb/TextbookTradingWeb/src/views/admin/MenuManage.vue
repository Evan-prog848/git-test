<template>
  <div class="menu-manage">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" @click="handleAdd">新增菜单</el-button>
        </div>
      </template>

      <el-table
        :data="menuList"
        row-key="id"
        v-loading="loading"
        :tree-props="{ children: 'children' }"
      >
        <el-table-column prop="menuName" label="菜单名称" width="180" />
        <el-table-column prop="icon" label="图标" width="80">
          <template #default="scope">
            <span>{{ scope.row.icon || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" />
        <el-table-column prop="component" label="组件路径" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 新增/编辑菜单对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="550px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="parentMenuOptions"
            :props="{ label: 'menuName', value: 'id', children: 'children' }"
            placeholder="选择上级菜单"
            check-strictly
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="form.path" placeholder="请输入路由路径，如 /user" />
        </el-form-item>
        <el-form-item label="组件路径">
          <el-input v-model="form.component" placeholder="请输入组件路径，如 UserManage" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '@/api/admin'

const loading = ref(false)
const menuList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const form = reactive({
  id: null,
  parentId: null,
  menuName: '',
  path: '',
  component: '',
  icon: '',
  sort: 0,
  status: 1
})

const rules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  path: [{ required: true, message: '请输入路由路径', trigger: 'blur' }]
}

const parentMenuOptions = computed(() => {
  return [{ id: 0, menuName: '顶级菜单', children: menuList.value }]
})

const fetchMenus = async () => {
  loading.value = true
  try {
    const res = await getMenuTree()
    menuList.value = res.data
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增菜单'
  Object.assign(form, {
    id: null,
    parentId: 0,
    menuName: '',
    path: '',
    component: '',
    icon: '',
    sort: 0,
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑菜单'
  Object.assign(form, {
    id: row.id,
    parentId: row.parentId || 0,
    menuName: row.menuName,
    path: row.path,
    component: row.component,
    icon: row.icon,
    sort: row.sort,
    status: row.status
  })
  dialogVisible.value = true
}

const submitForm = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  try {
    const data = { ...form }
    if (data.parentId === 0) data.parentId = null
    
    if (form.id) {
      await updateMenu(data)
    } else {
      await addMenu(data)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchMenus()
  } catch (error) {
    console.error('Save failed:', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该菜单及其子菜单吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteMenu(row.id)
    ElMessage.success('删除成功')
    fetchMenus()
  })
}

onMounted(fetchMenus)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>


