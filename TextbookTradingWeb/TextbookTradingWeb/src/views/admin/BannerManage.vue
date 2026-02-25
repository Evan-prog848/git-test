<template>
  <div class="banner-manage">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>轮播图管理</span>
          <el-button type="primary" @click="handleAdd">新增轮播图</el-button>
        </div>
      </template>

      <el-table :data="bannerList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="图片" width="200">
          <template #default="scope">
            <el-image :src="scope.row.imageUrl" style="width: 160px; height: 60px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="link" label="链接" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <div class="upload-container">
            <el-upload
              class="banner-uploader"
              :show-file-list="false"
              :before-upload="beforeUpload"
              :http-request="handleUpload"
              accept="image/*"
            >
              <el-image v-if="form.imageUrl" :src="form.imageUrl" class="banner-preview" fit="cover" />
              <el-icon v-else class="upload-icon"><Plus /></el-icon>
            </el-upload>
            <div class="upload-tip">点击上传图片，建议尺寸 1200x400</div>
          </div>
        </el-form-item>
        <el-form-item label="链接" prop="link">
          <el-input v-model="form.link" placeholder="请输入点击跳转链接" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
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
import { Plus } from '@element-plus/icons-vue'
import { getBannerList, addBanner, updateBanner, deleteBanner } from '@/api/admin'
import request from '@/utils/request'

const loading = ref(false)
const bannerList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const form = reactive({
  id: null,
  title: '',
  imageUrl: '',
  link: '',
  sort: 0,
  status: 1
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传图片', trigger: 'change' }]
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

const handleUpload = async (options) => {
  const formData = new FormData()
  formData.append('file', options.file)
  formData.append('type', 'banner')
  try {
    const res = await request.post('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    form.imageUrl = res.data
    ElMessage.success('上传成功')
  } catch (error) {
    ElMessage.error('上传失败')
  }
}

const fetchBanners = async () => {
  loading.value = true
  try {
    const res = await getBannerList()
    bannerList.value = res.data
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增轮播图'
  Object.assign(form, { id: null, title: '', imageUrl: '', link: '', sort: 0, status: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑轮播图'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这张轮播图吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteBanner(row.id)
    ElMessage.success('删除成功')
    fetchBanners()
  })
}

const handleStatusChange = async (row) => {
  try {
    await updateBanner(row)
    ElMessage.success(`状态已更新`)
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
  }
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (form.id) {
        await updateBanner(form)
      } else {
        await addBanner(form)
      }
      ElMessage.success('保存成功')
      dialogVisible.value = false
      fetchBanners()
    }
  })
}

onMounted(fetchBanners)
</script>

<style scoped lang="scss">
.card-header { display: flex; justify-content: space-between; align-items: center; }

.upload-container {
  .banner-uploader {
    :deep(.el-upload) {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      width: 300px;
      height: 100px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: border-color 0.3s;
      
      &:hover {
        border-color: var(--primary-color);
      }
    }
  }
  
  .banner-preview {
    width: 300px;
    height: 100px;
  }
  
  .upload-icon {
    font-size: 28px;
    color: #8c939d;
  }
  
  .upload-tip {
    margin-top: 8px;
    font-size: 12px;
    color: var(--text-secondary);
  }
}
</style>


