<template>
  <div class="course-manage">
    <!-- 专业管理 -->
    <el-card style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <span>专业管理</span>
          <el-button type="primary" size="small" @click="handleAddMajor">
            <el-icon><Plus /></el-icon>
            添加专业
          </el-button>
        </div>
      </template>

      <el-row :gutter="16" style="margin-bottom: 16px;">
        <el-col :span="6">
          <el-select v-model="majorFilter.campusId" placeholder="选择校区" clearable @change="fetchMajors">
            <el-option v-for="c in campusList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="majorFilter.keyword" placeholder="搜索专业" clearable @keyup.enter="fetchMajors" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="fetchMajors">搜索</el-button>
        </el-col>
      </el-row>

      <el-table :data="majorList" v-loading="majorLoading" stripe max-height="300">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="专业名称" />
        <el-table-column prop="college" label="所属学院" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEditMajor(row)">编辑</el-button>
            <el-popconfirm title="确定删除该专业吗？" @confirm="handleDeleteMajor(row.id)">
              <template #reference>
                <el-button type="danger" link>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 课程管理 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>课程管理</span>
          <el-button type="primary" size="small" @click="handleAddCourse">
            <el-icon><Plus /></el-icon>
            添加课程
          </el-button>
        </div>
      </template>

      <el-row :gutter="16" style="margin-bottom: 16px;">
        <el-col :span="6">
          <el-select v-model="courseFilter.majorId" placeholder="选择专业" clearable @change="fetchCourses">
            <el-option v-for="m in majorList" :key="m.id" :label="m.name" :value="m.id" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="courseFilter.keyword" placeholder="搜索课程" clearable @keyup.enter="fetchCourses" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="fetchCourses">搜索</el-button>
        </el-col>
      </el-row>

      <el-table :data="courseList" v-loading="courseLoading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="code" label="课程代码" width="120" />
        <el-table-column prop="name" label="课程名称" />
        <el-table-column prop="semester" label="学期" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEditCourse(row)">编辑</el-button>
            <el-popconfirm title="确定删除该课程吗？" @confirm="handleDeleteCourse(row.id)">
              <template #reference>
                <el-button type="danger" link>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="courseFilter.pageNum"
        v-model:page-size="courseFilter.pageSize"
        :total="courseTotal"
        layout="total, prev, pager, next"
        @current-change="fetchCourses"
        style="margin-top: 16px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 专业对话框 -->
    <el-dialog v-model="majorDialogVisible" :title="isEditMajor ? '编辑专业' : '添加专业'" width="500px">
      <el-form :model="majorForm" :rules="majorRules" ref="majorFormRef" label-width="80px">
        <el-form-item label="校区" prop="campusId">
          <el-select v-model="majorForm.campusId" placeholder="请选择校区" style="width: 100%">
            <el-option v-for="c in campusList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="专业名称" prop="name">
          <el-input v-model="majorForm.name" placeholder="请输入专业名称" />
        </el-form-item>
        <el-form-item label="所属学院" prop="college">
          <el-input v-model="majorForm.college" placeholder="请输入所属学院" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="majorForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="majorDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitMajor">确定</el-button>
      </template>
    </el-dialog>

    <!-- 课程对话框 -->
    <el-dialog v-model="courseDialogVisible" :title="isEditCourse ? '编辑课程' : '添加课程'" width="500px">
      <el-form :model="courseForm" :rules="courseRules" ref="courseFormRef" label-width="80px">
        <el-form-item label="所属专业" prop="majorId">
          <el-select v-model="courseForm.majorId" placeholder="请选择专业" style="width: 100%">
            <el-option v-for="m in majorList" :key="m.id" :label="m.name" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程代码" prop="code">
          <el-input v-model="courseForm.code" placeholder="请输入课程代码" />
        </el-form-item>
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="courseForm.name" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-input v-model="courseForm.semester" placeholder="如：大一上" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="courseForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="courseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitCourse">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 校区列表
const campusList = ref([])

// 专业相关
const majorLoading = ref(false)
const majorList = ref([])
const majorFilter = reactive({ campusId: '', keyword: '' })
const majorDialogVisible = ref(false)
const isEditMajor = ref(false)
const majorFormRef = ref(null)
const majorForm = reactive({ id: null, campusId: '', name: '', college: '', status: 1 })
const majorRules = {
  campusId: [{ required: true, message: '请选择校区', trigger: 'change' }],
  name: [{ required: true, message: '请输入专业名称', trigger: 'blur' }]
}

// 课程相关
const courseLoading = ref(false)
const courseList = ref([])
const courseTotal = ref(0)
const courseFilter = reactive({ majorId: '', keyword: '', pageNum: 1, pageSize: 10 })
const courseDialogVisible = ref(false)
const isEditCourse = ref(false)
const courseFormRef = ref(null)
const courseForm = reactive({ id: null, majorId: '', code: '', name: '', semester: '', status: 1 })
const courseRules = {
  majorId: [{ required: true, message: '请选择专业', trigger: 'change' }],
  name: [{ required: true, message: '请输入课程名称', trigger: 'blur' }]
}

// 获取校区列表
const fetchCampusList = async () => {
  try {
    const res = await request.get('/campus/list')
    campusList.value = res.data || []
  } catch (error) {
    console.error('获取校区列表失败:', error)
  }
}

// 获取专业列表
const fetchMajors = async () => {
  majorLoading.value = true
  try {
    const res = await request.get('/admin/course/major/list', {
      params: { ...majorFilter, pageNum: 1, pageSize: 100 }
    })
    majorList.value = res.data.records || []
  } catch (error) {
    console.error('获取专业列表失败:', error)
  } finally {
    majorLoading.value = false
  }
}

// 获取课程列表
const fetchCourses = async () => {
  courseLoading.value = true
  try {
    const res = await request.get('/admin/course/list', {
      params: courseFilter
    })
    courseList.value = res.data.records || []
    courseTotal.value = res.data.total || 0
  } catch (error) {
    console.error('获取课程列表失败:', error)
  } finally {
    courseLoading.value = false
  }
}

// 专业操作
const handleAddMajor = () => {
  isEditMajor.value = false
  Object.assign(majorForm, { id: null, campusId: '', name: '', college: '', status: 1 })
  majorDialogVisible.value = true
}

const handleEditMajor = (row) => {
  isEditMajor.value = true
  Object.assign(majorForm, row)
  majorDialogVisible.value = true
}

const handleSubmitMajor = async () => {
  await majorFormRef.value.validate()
  try {
    if (isEditMajor.value) {
      await request.put('/admin/course/major/update', majorForm)
    } else {
      await request.post('/admin/course/major/add', majorForm)
    }
    ElMessage.success('操作成功')
    majorDialogVisible.value = false
    fetchMajors()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const handleDeleteMajor = async (id) => {
  try {
    await request.delete(`/admin/course/major/${id}`)
    ElMessage.success('删除成功')
    fetchMajors()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 课程操作
const handleAddCourse = () => {
  isEditCourse.value = false
  Object.assign(courseForm, { id: null, majorId: '', code: '', name: '', semester: '', status: 1 })
  courseDialogVisible.value = true
}

const handleEditCourse = (row) => {
  isEditCourse.value = true
  Object.assign(courseForm, row)
  courseDialogVisible.value = true
}

const handleSubmitCourse = async () => {
  await courseFormRef.value.validate()
  try {
    if (isEditCourse.value) {
      await request.put('/admin/course/update', courseForm)
    } else {
      await request.post('/admin/course/add', courseForm)
    }
    ElMessage.success('操作成功')
    courseDialogVisible.value = false
    fetchCourses()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const handleDeleteCourse = async (id) => {
  try {
    await request.delete(`/admin/course/${id}`)
    ElMessage.success('删除成功')
    fetchCourses()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

onMounted(() => {
  fetchCampusList()
  fetchMajors()
  fetchCourses()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
