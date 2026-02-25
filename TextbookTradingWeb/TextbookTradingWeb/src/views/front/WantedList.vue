<template>
  <div class="container wanted-list-page">
    <div class="page-header">
      <div class="title-section">
        <h2>求购专区</h2>
        <p>发布你需要的教材，让卖家找上门</p>
      </div>
      <el-button type="primary" size="large" @click="handlePublish">发布求购</el-button>
    </div>

    <el-card shadow="never" class="filter-card">
      <el-input
        v-model="keyword"
        placeholder="搜索求购教材..."
        class="search-input"
        :prefix-icon="Search"
        clearable
        @keyup.enter="fetchWanted"
      />
    </el-card>

    <div class="wanted-list" v-loading="loading">
      <el-row :gutter="20">
        <el-col :span="12" v-for="item in wantedList" :key="item.id">
          <el-card shadow="hover" class="wanted-item">
            <div class="item-header">
              <span class="book-name">{{ item.title }}</span>
              <span class="price">预算: ¥{{ item.maxPrice }}</span>
            </div>
            <div class="item-body">
              <p class="desc">{{ item.description }}</p>
              <div class="tags">
                <el-tag size="small" v-if="item.author">{{ item.author }}</el-tag>
              </div>
            </div>
            <div class="item-footer">
              <div class="user">
                <el-avatar :size="24" :src="item.avatar" />
                <span>{{ item.nickname }}</span>
              </div>
              <div class="time">{{ item.createTime }}</div>
              <el-button type="primary" plain size="small" @click="handleContact(item)">我有书</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-if="!loading && wantedList.length === 0" description="暂无求购信息" />
      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="fetchWanted"
        />
      </div>
    </div>

    <!-- 发布对话框 -->
    <el-dialog v-model="dialogVisible" title="发布求购需求" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="教材名称" prop="title">
          <el-input v-model="form.title" placeholder="如：计算机网络 第八版" />
        </el-form-item>
        <el-form-item label="预算金额" prop="maxPrice">
          <el-input-number v-model="form.maxPrice" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="form.author" placeholder="如：谢希仁" />
        </el-form-item>
        <el-form-item label="需求描述" prop="description">
          <el-input type="textarea" v-model="form.description" rows="3" placeholder="描述书名、成色要求等" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { getWantedList, publishWanted } from '@/api/wanted'

const userStore = useUserStore()
const router = useRouter()
const loading = ref(false)
const keyword = ref('')
const wantedList = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const form = reactive({
  title: '',
  maxPrice: 0,
  author: '',
  description: ''
})

const rules = {
  title: [{ required: true, message: '请输入教材名称', trigger: 'blur' }],
  description: [{ required: true, message: '请输入需求描述', trigger: 'blur' }],
  maxPrice: [{ required: true, message: '请输入预算金额', trigger: 'blur' }]
}

const fetchWanted = async () => {
  loading.value = true
  try {
    const res = await getWantedList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: keyword.value || undefined
    })
    wantedList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取求购列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handlePublish = () => {
  if (!userStore.isLogin) {
    router.push('/login')
    return
  }
  dialogVisible.value = true
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await publishWanted({
          title: form.title,
          maxPrice: form.maxPrice,
          author: form.author,
          description: form.description
        })
        ElMessage.success('求购信息发布成功')
        dialogVisible.value = false
        form.title = ''
        form.maxPrice = 0
        form.author = ''
        form.description = ''
        fetchWanted()
      } catch (error) {
        console.error('发布求购失败:', error)
      }
    }
  })
}

const handleContact = (item) => {
  if (!userStore.isLogin) {
    router.push('/login')
    return
  }
  router.push({ path: '/chat', query: { userId: item.userId } })
}

onMounted(fetchWanted)
</script>

<style scoped lang="scss">
.wanted-list-page {
  padding: 20px 0 60px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  .title-section {
    h2 {
      margin: 0 0 6px 0;
      font-size: 24px;
      font-weight: 700;
      color: var(--text-primary);
    }
    p { margin: 0; color: var(--text-secondary); font-size: 14px; }
  }
}

.filter-card {
  margin-bottom: 24px;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-light);

  .search-input {
    width: 320px;

    :deep(.el-input__wrapper) {
      border-radius: var(--radius-xl);
      background: var(--bg-color);
      box-shadow: none !important;
      border: 1px solid transparent;
      padding: 4px 16px;
      transition: all var(--transition-fast);

      &:hover, &.is-focus {
        background: #fff;
        border-color: var(--primary-color);
      }
    }
  }
}

.wanted-item {
  margin-bottom: 20px;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-light);
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);

  &:hover {
    transform: translateY(-3px);
    box-shadow: var(--shadow-lg);
    border-color: transparent;
  }

  .item-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 14px;
    .book-name { font-size: 17px; font-weight: 700; color: var(--text-primary); }
    .price { color: var(--accent-color); font-weight: 700; font-size: 15px; }
  }
  .item-body {
    margin-bottom: 14px;
    .desc { font-size: 14px; color: var(--text-regular); line-height: 1.6; height: 45px; overflow: hidden; }
  }
  .item-footer {
    display: flex;
    align-items: center;
    border-top: 1px solid var(--border-light);
    padding-top: 14px;
    .user {
      display: flex;
      align-items: center;
      gap: 8px;
      flex: 1;
      font-size: 13px;
      color: var(--text-regular);
    }
    .time { font-size: 12px; color: var(--text-secondary); margin-right: 15px; }
  }
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>


