<template>
  <div class="trading-point-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>交易点管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加交易点
          </el-button>
        </div>
      </template>

      <el-row :gutter="16" style="margin-bottom: 16px;">
        <el-col :span="6">
          <el-select v-model="filter.campusId" placeholder="选择校区" clearable @change="fetchData">
            <el-option v-for="c in campusList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="filter.keyword" placeholder="搜索交易点" clearable @keyup.enter="fetchData" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="fetchData">搜索</el-button>
        </el-col>
      </el-row>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="交易点名称" />
        <el-table-column prop="location" label="具体位置" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确定删除该交易点吗？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button type="danger" link>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="fetchData"
        @current-change="fetchData"
        style="margin-top: 16px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑交易点' : '添加交易点'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="所属校区" prop="campusId">
          <el-select v-model="form.campusId" placeholder="请选择校区" style="width: 100%">
            <el-option v-for="c in campusList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易点名称" prop="name">
          <el-input v-model="form.name" placeholder="如：图书馆大厅" />
        </el-form-item>
        <el-form-item label="具体位置" prop="location">
          <el-input v-model="form.location" placeholder="请输入具体位置描述" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const campusList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filter = reactive({ campusId: '', keyword: '' })

const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = reactive({
  id: null,
  campusId: '',
  name: '',
  location: '',
  sort: 0,
  status: 1
})

const rules = {
  campusId: [{ required: true, message: '请选择校区', trigger: 'change' }],
  name: [{ required: true, message: '请输入交易点名称', trigger: 'blur' }]
}

const fetchCampusList = async () => {
  try {
    const res = await request.get('/campus/list')
    campusList.value = res.data || []
  } catch (error) {
    console.error('获取校区列表失败:', error)
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/trading-point/list', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value, ...filter }
    })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取交易点列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, campusId: '', name: '', location: '', sort: 0, status: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await request.put('/admin/trading-point/update', form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/admin/trading-point/add', form)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (id) => {
  try {
    await request.delete(`/admin/trading-point/${id}`)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const handleStatusChange = async (row) => {
  try {
    await request.put(`/admin/trading-point/status/${row.id}`, null, {
      params: { status: row.status }
    })
    ElMessage.success('状态更新成功')
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    ElMessage.error('状态更新失败')
  }
}

onMounted(() => {
  fetchCampusList()
  fetchData()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
