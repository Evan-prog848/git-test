<template>
  <div class="container publish-page">
    <el-card class="publish-card">
      <template #header>
        <div class="card-header">
          <span class="title">{{ isEditMode ? '编辑教材' : '发布二手教材' }}</span>
        </div>
      </template>

      <!-- 录入方式选择 -->
      <div class="input-mode-selector" v-if="!isEditMode">
        <el-radio-group v-model="inputMode" size="large">
          <el-radio-button :label="1">
            <el-icon><Search /></el-icon>
            ISBN扫描录入
          </el-radio-button>
          <el-radio-button :label="0">
            <el-icon><Edit /></el-icon>
            手动录入
          </el-radio-button>
        </el-radio-group>
      </div>

      <!-- ISBN扫描区域 -->
      <div class="isbn-scan-section" v-if="inputMode === 1 && !isEditMode">
        <el-card shadow="never" class="scan-card">
          <div class="scan-content">
            <el-input
              v-model="isbnInput"
              placeholder="请输入或扫描ISBN条码"
              size="large"
              clearable
              @keyup.enter="queryIsbnInfo"
            >
              <template #prepend>ISBN</template>
              <template #append>
                <el-button type="primary" @click="queryIsbnInfo" :loading="isbnLoading">
                  查询
                </el-button>
              </template>
            </el-input>
            <div class="scan-tip">
              <el-icon><InfoFilled /></el-icon>
              输入ISBN后点击查询，系统将自动获取书籍信息
            </div>
          </div>
          <div class="book-preview" v-if="bookInfo">
            <img 
              :src="getProxyCoverUrl(bookInfo.cover)" 
              class="book-cover" 
              @error="handleImageError"
            />
            <div class="book-info">
              <h3>{{ bookInfo.title }}</h3>
              <p><span>作者：</span>{{ bookInfo.author || '未知' }}</p>
              <p><span>出版社：</span>{{ bookInfo.publisher || '未知' }}</p>
              <p v-if="bookInfo.originalPrice"><span>定价：</span>¥{{ bookInfo.originalPrice }}</p>
              <el-button type="primary" size="small" @click="applyBookInfo">
                使用此信息
              </el-button>
            </div>
          </div>
        </el-card>
      </div>

      <el-form
        :model="publishForm"
        :rules="rules"
        ref="publishFormRef"
        label-width="100px"
        class="publish-form"
      >
        <el-form-item label="教材书名" prop="title">
          <el-input v-model="publishForm.title" placeholder="请输入教材完整书名" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="作者" prop="author">
              <el-input v-model="publishForm.author" placeholder="请输入作者" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出版社" prop="publisher">
              <el-input v-model="publishForm.publisher" placeholder="请输入出版社" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="ISBN" prop="isbn">
              <el-input v-model="publishForm.isbn" placeholder="请输入ISBN号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="教材分类" prop="categoryId">
              <el-select v-model="publishForm.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 校区和课程选择 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所在校区" prop="campusId">
              <el-select v-model="publishForm.campusId" placeholder="请选择校区" style="width: 100%" @change="onCampusChange">
                <el-option v-for="c in campusList" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联课程">
              <el-select
                v-model="publishForm.courseId"
                placeholder="搜索或选择课程"
                style="width: 100%"
                filterable
                remote
                :remote-method="searchCourse"
                clearable
              >
                <el-option v-for="c in courseList" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="售价 (¥)" prop="price">
              <el-input-number
                v-model="publishForm.price"
                :precision="2"
                :step="1"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="原价 (¥)" prop="originalPrice">
              <el-input-number
                v-model="publishForm.originalPrice"
                :precision="2"
                :step="1"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="新旧程度" prop="conditionLevel">
              <el-select v-model="publishForm.conditionLevel" placeholder="请选择" style="width: 100%">
                <el-option v-for="i in 10" :key="i" :label="i + '成新'" :value="i" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 品相详情勾选 -->
        <el-form-item label="品相详情">
          <div class="condition-checkboxes">
            <el-checkbox v-model="publishForm.hasNotes" :true-label="1" :false-label="0">
              有笔记
            </el-checkbox>
            <el-checkbox v-model="publishForm.hasHighlight" :true-label="1" :false-label="0">
              有划线
            </el-checkbox>
            <el-checkbox v-model="publishForm.hasMissingPages" :true-label="1" :false-label="0">
              有缺页
            </el-checkbox>
            <el-checkbox v-model="publishForm.hasWaterStains" :true-label="1" :false-label="0">
              有水渍
            </el-checkbox>
          </div>
          <div class="condition-tip">
            <el-icon><WarningFilled /></el-icon>
            请如实勾选，虚假描述将影响您的信用评分
          </div>
        </el-form-item>

        <el-form-item label="适用课程" prop="course">
          <el-input v-model="publishForm.course" placeholder="请输入适用课程名称（可选）" />
        </el-form-item>

        <el-form-item label="教材图片" prop="images">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :file-list="publishForm.images"
            :on-change="handleImageChange"
            :on-remove="handleImageRemove"
            :on-preview="handleImagePreview"
            multiple
            :limit="5"
          >
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="el-upload__tip">
                最多上传5张图片，建议包含封面、版权页及内页图
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-dialog v-model="dialogVisible" title="图片预览" width="40%">
          <img :src="dialogImageUrl" alt="预览图片" style="width: 100%; display: block;" />
        </el-dialog>

        <el-form-item label="详细描述" prop="description">
          <el-input
            v-model="publishForm.description"
            type="textarea"
            placeholder="请详细描述教材的成色、有无笔记划线等信息"
            :rows="5"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" @click="submitForm" :loading="loading">
            立即发布
          </el-button>
          <el-button size="large" @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Plus, Search, Edit, InfoFilled, Picture, WarningFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { publishTextbook } from '@/api/textbook'
import { queryIsbn, getCampusList, searchCourses } from '@/api/common'

const router = useRouter()
const route = useRoute()
const publishFormRef = ref(null)
const loading = ref(false)
const isEditMode = ref(false)
const textbookId = ref(null)
const dialogVisible = ref(false)
const dialogImageUrl = ref('')

// ISBN扫描相关
const inputMode = ref(1) // 1=ISBN扫描 0=手动
const isbnInput = ref('')
const isbnLoading = ref(false)
const bookInfo = ref(null)

// 校区课程相关
const campusList = ref([])
const courseList = ref([])

const categories = ref([])

const fetchCategories = async () => {
  try {
    const res = await request.get('/category/list')
    if (res.data && res.data.length > 0) {
      categories.value = res.data.filter(c => !c.parentId || c.parentId === 0)
    } else {
      useMockCategories()
    }
  } catch (error) {
    console.error('Failed to fetch categories:', error)
    useMockCategories()
  }
}

const useMockCategories = () => {
  categories.value = [
    { id: 1, name: '计算机' },
    { id: 2, name: '数学' },
    { id: 3, name: '英语' },
    { id: 4, name: '经济管理' },
    { id: 5, name: '机械电子' },
    { id: 6, name: '文学艺术' }
  ]
}

const publishForm = reactive({
  title: '',
  author: '',
  publisher: '',
  isbn: '',
  categoryId: '',
  campusId: '',
  courseId: '',
  price: 0,
  originalPrice: 0,
  conditionLevel: 9,
  course: '',
  description: '',
  images: [],
  hasNotes: 0,
  hasHighlight: 0,
  hasMissingPages: 0,
  hasWaterStains: 0,
  inputMode: 1
})

const rules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入售价', trigger: 'blur' }],
  conditionLevel: [{ required: true, message: '请选择新旧程度', trigger: 'change' }],
}

const handleImageChange = (file, fileList) => {
  // 仅在本地维护列表供预览
  publishForm.images = fileList
}

const handleImageRemove = (file, fileList) => {
  publishForm.images = fileList
}

const handleImagePreview = (file) => {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

const loadTextbookData = async () => {
  try {
    const res = await request.get(`/textbook/detail/${textbookId.value}`)
    const data = res.data
    
    // 填充表单数据
    Object.assign(publishForm, {
      title: data.title,
      author: data.author || '',
      publisher: data.publisher || '',
      isbn: data.isbn || '',
      categoryId: data.categoryId,
      price: data.price,
      originalPrice: data.originalPrice || 0,
      conditionLevel: data.conditionLevel,
      course: data.course || '',
      description: data.description || '',
      images: [] // 图片需要特殊处理
    })
    
    // 如果有封面图，转换为文件列表格式供预览
    if (data.cover) {
      publishForm.images = [{
        name: 'cover.jpg',
        url: data.cover,
        uid: Date.now()
      }]
    }
  } catch (error) {
    console.error('加载教材数据失败:', error)
    ElMessage.error('加载教材数据失败')
    router.back()
  }
}

const submitForm = async () => {
  publishFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 1. 先上传图片（只上传新选择的图片）
        const formData = new FormData()
        publishForm.images.forEach(item => {
          if (item.raw) formData.append('files', item.raw)
        })

        let imageUrls = []
        if (formData.has('files')) {
          const uploadRes = await request.post('/file/uploads', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          })
          imageUrls = uploadRes.data
        } else if (publishForm.images.length > 0 && publishForm.images[0].url) {
          // 编辑模式下，如果没有新上传图片，使用原有图片
          imageUrls = [publishForm.images[0].url]
        }

        // 2. 提交表单
        const { images: _, ...submitData } = publishForm // 移除images数组
        const data = {
          ...submitData,
          cover: imageUrls[0] || '',
          images: imageUrls // 发送数组而不是字符串
        }
        
        if (isEditMode.value) {
          // 编辑模式：更新教材
          data.id = parseInt(textbookId.value)
          await request.put('/textbook/update', data)
          ElMessage.success('更新成功！')
        } else {
          // 新增模式：发布教材
          await publishTextbook(data)
          ElMessage.success('发布成功！您的教材正在等待管理员审核，审核通过后将自动上架展示。')
        }
        
        router.push('/profile')
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error(error.message || '操作失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}

// ISBN查询
const queryIsbnInfo = async () => {
  if (!isbnInput.value.trim()) {
    ElMessage.warning('请输入ISBN号')
    return
  }
  
  isbnLoading.value = true
  bookInfo.value = null
  
  try {
    const res = await queryIsbn(isbnInput.value.trim())
    if (res.code === 200 && res.data) {
      if (res.data.success) {
        bookInfo.value = res.data
        ElMessage.success('查询成功')
      } else {
        ElMessage.warning(res.data.errorMessage || '未找到该书籍信息，请手动输入')
      }
    }
  } catch (error) {
    console.error('ISBN查询失败:', error)
    ElMessage.error('查询失败，请稍后重试或手动输入')
  } finally {
    isbnLoading.value = false
  }
}

// 应用ISBN查询结果
const applyBookInfo = () => {
  if (bookInfo.value) {
    publishForm.title = bookInfo.value.title || ''
    publishForm.author = bookInfo.value.author || ''
    publishForm.publisher = bookInfo.value.publisher || ''
    publishForm.isbn = bookInfo.value.isbn || isbnInput.value
    publishForm.originalPrice = bookInfo.value.originalPrice || 0
    publishForm.inputMode = 1
    ElMessage.success('已填充书籍信息，请补充其他信息')
  }
}

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.style.display = 'none'
}

// 获取代理封面图片URL
const getProxyCoverUrl = (coverUrl) => {
  if (!coverUrl) return ''
  // 通过后端代理获取图片，绕过防盗链
  return `/api/isbn/proxy-image?url=${encodeURIComponent(coverUrl)}`
}

// 加载校区列表
const fetchCampusList = async () => {
  try {
    const res = await getCampusList()
    if (res.code === 200) {
      campusList.value = res.data || []
    }
  } catch (error) {
    console.error('加载校区列表失败:', error)
  }
}

// 校区变更
const onCampusChange = () => {
  publishForm.courseId = ''
  courseList.value = []
}

// 搜索课程
const searchCourse = async (keyword) => {
  if (!keyword) {
    courseList.value = []
    return
  }
  
  try {
    const res = await searchCourses(keyword)
    if (res.code === 200) {
      courseList.value = res.data || []
    }
  } catch (error) {
    console.error('搜索课程失败:', error)
  }
}

onMounted(() => {
  fetchCategories()
  fetchCampusList()

  // 检查是否是编辑模式
  if (route.query.id) {
    isEditMode.value = true
    textbookId.value = route.query.id
    loadTextbookData()
  }
})
</script>

<style scoped lang="scss">
.publish-page {
  padding: 20px 0 60px;

  :deep(.el-card) {
    border-radius: var(--radius-md);
    border: 1px solid var(--border-light);
    box-shadow: var(--shadow-sm);
  }
}

.publish-card {
  max-width: 900px;
  margin: 0 auto;

  .card-header {
    .title {
      font-size: 20px;
      font-weight: 700;
      color: var(--text-primary);
    }
  }
}

.input-mode-selector {
  text-align: center;
  margin-bottom: 24px;
  
  .el-radio-button {
    .el-icon {
      margin-right: 4px;
    }
  }
}

.isbn-scan-section {
  margin-bottom: 24px;
  
  .scan-card {
    background: var(--bg-color);
    border: 1px solid var(--border-light);
  }
  
  .scan-content {
    margin-bottom: 16px;
  }
  
  .scan-tip {
    margin-top: 8px;
    font-size: 13px;
    color: var(--text-secondary);
    display: flex;
    align-items: center;
    gap: 4px;
  }
  
  .book-preview {
    display: flex;
    gap: 16px;
    padding: 16px;
    background: var(--bg-card);
    border-radius: var(--radius-md);
    margin-top: 16px;
    border: 1px solid var(--border-light);
    
    .book-cover {
      width: 100px;
      height: 140px;
      border-radius: var(--radius-sm);
      flex-shrink: 0;
    }
    
    .image-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--bg-color);
      color: var(--text-placeholder);
      font-size: 32px;
    }
    
    .book-info {
      flex: 1;
      
      h3 {
        margin: 0 0 8px;
        font-size: 16px;
        color: var(--text-primary);
        font-weight: 600;
      }
      
      p {
        margin: 4px 0;
        font-size: 14px;
        color: var(--text-regular);
        
        span {
          color: var(--text-secondary);
        }
      }
      
      .el-button {
        margin-top: 12px;
      }
    }
  }
}

.condition-checkboxes {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.condition-tip {
  margin-top: 8px;
  font-size: 13px;
  color: var(--warning-color);
  display: flex;
  align-items: center;
  gap: 4px;
}

.publish-form {
  margin-top: 20px;

  :deep(.el-upload-list__item-actions-label) {
    display: none !important;
  }
}
</style>


