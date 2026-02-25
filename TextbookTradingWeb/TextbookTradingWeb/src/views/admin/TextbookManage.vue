<template>
  <div class="textbook-manage">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchQuery">
        <el-form-item label="书名">
          <el-input v-model="searchQuery.title" placeholder="请输入书名" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchQuery.categoryId" placeholder="全部" clearable style="width: 150px">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchQuery.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="下架" :value="0" />
            <el-option label="在售" :value="1" />
            <el-option label="已售" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="textbookList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="封面" width="100">
          <template #default="scope">
            <el-image :src="scope.row.cover" style="width: 60px; height: 80px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="书名" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="price" label="售价" width="100">
          <template #default="scope">
            <span style="color: #f56c6c; font-weight: bold">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sellerName" label="卖家" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="createTime" label="发布时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <div class="action-buttons">
              <el-button size="small" link type="primary" @click="handleDetail(scope.row)">详情</el-button>
              <el-button
                size="small"
                link
                :type="scope.row.status === 0 ? 'success' : 'warning'"
                @click="toggleStatus(scope.row)"
              >
                {{ scope.row.status === 0 ? '上架' : '下架' }}
              </el-button>
              <el-button size="small" link type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="searchQuery.pageNum"
          v-model:page-size="searchQuery.pageSize"
          :total="total"
          layout="total, prev, pager, next, jumper"
          @current-change="fetchTextbooks"
        />
      </div>
    </el-card>

    <!-- 教材详情弹窗 -->
    <el-dialog v-model="detailVisible" title="教材详情" width="700px">
      <div class="textbook-detail" v-if="currentTextbook">
        <div class="detail-header">
          <el-image :src="currentTextbook.cover" style="width: 120px; height: 160px; border-radius: 8px" fit="cover" />
          <div class="detail-info">
            <h3>{{ currentTextbook.title }}</h3>
            <p class="author">作者：{{ currentTextbook.author || '-' }}</p>
            <p class="publisher">出版社：{{ currentTextbook.publisher || '-' }}</p>
            <div class="price-row">
              <span class="price">¥{{ currentTextbook.price }}</span>
              <span class="original-price" v-if="currentTextbook.originalPrice">原价：¥{{ currentTextbook.originalPrice }}</span>
            </div>
            <div class="tags">
              <el-tag :type="getStatusType(currentTextbook.status)" size="small">{{ getStatusText(currentTextbook.status) }}</el-tag>
              <el-tag v-if="currentTextbook.categoryName" type="info" size="small" style="margin-left: 8px">{{ currentTextbook.categoryName }}</el-tag>
            </div>
          </div>
        </div>
        <el-divider />
        <el-descriptions :column="2" border>
          <el-descriptions-item label="教材ID">{{ currentTextbook.id }}</el-descriptions-item>
          <el-descriptions-item label="ISBN">{{ currentTextbook.isbn || '-' }}</el-descriptions-item>
          <el-descriptions-item label="新旧程度">{{ getConditionText(currentTextbook.condition) }}</el-descriptions-item>
          <el-descriptions-item label="浏览量">{{ currentTextbook.viewCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="卖家">{{ currentTextbook.sellerName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="发布时间">{{ currentTextbook.createTime }}</el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <div class="description-section">
          <h4>教材描述</h4>
          <p>{{ currentTextbook.description || '暂无描述' }}</p>
        </div>
        <div class="images-section" v-if="currentTextbook.images && currentTextbook.images.length > 0">
          <h4>教材图片</h4>
          <div class="image-list">
            <el-image 
              v-for="(img, index) in currentTextbook.images" 
              :key="index" 
              :src="img" 
              :preview-src-list="currentTextbook.images"
              style="width: 100px; height: 100px; margin-right: 10px; border-radius: 4px" 
              fit="cover" 
            />
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
import { getTextbookManageList, updateTextbookManageStatus, deleteTextbook, getCategoryList } from '@/api/admin'

const loading = ref(false)
const total = ref(0)
const textbookList = ref([])
const categories = ref([])
const detailVisible = ref(false)
const currentTextbook = ref(null)

const searchQuery = reactive({
  pageNum: 1,
  pageSize: 10,
  title: '',
  categoryId: undefined,
  status: undefined
})

const fetchCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {}
}

const fetchTextbooks = async () => {
  loading.value = true
  try {
    const res = await getTextbookManageList(searchQuery)
    textbookList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const getStatusText = (status) => {
  const map = { 0: '下架', 1: '在售', 2: '已售' }
  return map[status]
}

const getStatusType = (status) => {
  const map = { 0: 'info', 1: 'success', 2: 'warning' }
  return map[status]
}

const handleSearch = () => {
  searchQuery.pageNum = 1
  fetchTextbooks()
}

const resetSearch = () => {
  searchQuery.title = ''
  searchQuery.categoryId = undefined
  searchQuery.status = undefined
  handleSearch()
}

const toggleStatus = async (row) => {
  const newStatus = row.status === 0 ? 1 : 0
  try {
    await updateTextbookManageStatus(row.id, newStatus)
    const actionText = newStatus === 1 ? '上架' : '下架'
    ElMessage.success(`教材已${actionText}`)
    row.status = newStatus
  } catch (error) {}
}

const getConditionText = (condition) => {
  const map = { 1: '全新', 2: '九成新', 3: '八成新', 4: '七成新', 5: '较旧' }
  return map[condition] || '未知'
}

const handleDetail = (row) => {
  currentTextbook.value = row
  detailVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该教材吗？', '提示', { type: 'warning' })
    await deleteTextbook(row.id)
    ElMessage.success('删除成功')
    fetchTextbooks()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Delete failed:', error)
    }
  }
}

onMounted(() => {
  fetchCategories()
  fetchTextbooks()
})
</script>

<style scoped lang="scss">
.search-card { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }

.action-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
}

.textbook-detail {
  .detail-header {
    display: flex;
    gap: 24px;
    
    .detail-info {
      flex: 1;
      
      h3 {
        margin: 0 0 12px 0;
        font-size: 20px;
        color: var(--text-primary);
      }
      
      .author, .publisher {
        margin: 0 0 8px 0;
        color: var(--text-regular);
        font-size: 14px;
      }
      
      .price-row {
        margin: 12px 0;
        
        .price {
          font-size: 24px;
          font-weight: 600;
          color: var(--accent-color);
        }
        
        .original-price {
          margin-left: 12px;
          font-size: 14px;
          color: var(--text-secondary);
          text-decoration: line-through;
        }
      }
    }
  }
  
  .description-section, .images-section {
    h4 {
      margin: 0 0 12px 0;
      font-size: 14px;
      color: var(--text-primary);
    }
    
    p {
      margin: 0;
      color: var(--text-regular);
      line-height: 1.6;
    }
  }
  
  .images-section {
    margin-top: 16px;
    
    .image-list {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }
  }
}
</style>


