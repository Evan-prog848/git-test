<template>
  <div class="container textbook-list-page">
    <div class="filter-section">
      <el-card shadow="never">
        <el-form :inline="true" :model="queryParams" class="demo-form-inline">
          <el-form-item label="校区">
            <el-select v-model="queryParams.campusId" placeholder="全部校区" clearable style="width: 150px" @change="onCampusChange">
              <el-option v-for="c in campusList" :key="c.id" :label="c.name" :value="c.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="课程">
            <el-input
              v-model="queryParams.courseName"
              placeholder="输入课程名称"
              clearable
              style="width: 180px"
            />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="queryParams.categoryId" placeholder="全部" clearable style="width: 150px">
              <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="新旧程度">
            <el-select v-model="queryParams.minCondition" placeholder="全部" clearable style="width: 150px">
              <el-option label="9成新及以上" :value="9" />
              <el-option label="8成新及以上" :value="8" />
              <el-option label="7成新及以上" :value="7" />
              <el-option label="5成新及以上" :value="5" />
            </el-select>
          </el-form-item>
          <el-form-item label="排序">
            <el-select v-model="queryParams.orderBy" placeholder="最新发布" style="width: 150px">
              <el-option label="最新发布" value="latest" />
              <el-option label="价格最低" value="price_asc" />
              <el-option label="价格最高" value="price_desc" />
              <el-option label="浏览最多" value="views" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <div class="list-section">
      <el-row :gutter="20" v-loading="loading">
        <el-col :span="6" v-for="book in textbooks" :key="book.id">
          <el-card :body-style="{ padding: '0px' }" class="book-card" @click="$router.push(`/textbook/${book.id}`)">
            <img :src="book.cover" class="book-cover" />
            <div class="book-info">
              <div class="book-title">{{ book.title }}</div>
              <div class="book-price">
                <span class="price">¥{{ book.price }}</span>
                <span class="original-price">¥{{ book.originalPrice }}</span>
              </div>
              <div class="seller-info">
                <el-tag size="small" type="info">{{ book.conditionLevel }}成新</el-tag>
                <span class="view-count"><el-icon><View /></el-icon> {{ book.viewCount }}</span>
              </div>
              <div class="school-tag" v-if="book.school">
                <el-icon><Location /></el-icon> {{ book.school }}
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[12, 24, 36, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      <el-empty v-else description="暂无相关教材" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { View, Location } from '@element-plus/icons-vue'
import { getTextbookList } from '@/api/textbook'
import { getCategoryList } from '@/api/admin'
import { getCampusList, searchCourses } from '@/api/common'

const route = useRoute()
const loading = ref(false)
const total = ref(0)
const textbooks = ref([])
const categories = ref([])
const campusList = ref([])
const courseList = ref([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 12,
  categoryId: undefined,
  campusId: undefined,
  courseName: '',
  minCondition: undefined,
  orderBy: 'latest',
  keyword: ''
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
    const res = await getTextbookList({
      ...queryParams,
      status: 1 // 只查在售的
    })
    textbooks.value = res.data.records
    total.value = res.data.total
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  fetchTextbooks()
}

const resetQuery = () => {
  queryParams.categoryId = undefined
  queryParams.campusId = undefined
  queryParams.courseName = ''
  queryParams.minCondition = undefined
  queryParams.orderBy = 'latest'
  queryParams.keyword = ''
  handleQuery()
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

// 校区变更时清空课程选择
const onCampusChange = () => {
  queryParams.courseId = undefined
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

const handleSizeChange = (val) => {
  queryParams.pageSize = val
  fetchTextbooks()
}

const handleCurrentChange = (val) => {
  queryParams.pageNum = val
  fetchTextbooks()
}

// 监听路由参数变化（比如从首页点击分类跳转过来）
watch(() => route.query, (newQuery) => {
  if (newQuery.categoryId) {
    queryParams.categoryId = Number(newQuery.categoryId)
  }
  if (newQuery.keyword) {
    queryParams.keyword = newQuery.keyword
  }
  fetchTextbooks()
}, { immediate: true })

onMounted(() => {
  fetchCategories()
  fetchCampusList()
})
</script>

<style scoped lang="scss">
.textbook-list-page {
  padding-bottom: 60px;
  position: relative;

  &::before {
    content: '';
    position: fixed;
    top: 0; left: 0; right: 0;
    height: 320px;
    background: linear-gradient(180deg, #e8f7f0 0%, transparent 100%);
    pointer-events: none;
    z-index: 0;
  }
}

.filter-section {
  margin-bottom: 28px;
  position: relative;
  z-index: 1;

  :deep(.el-card) {
    border-radius: var(--radius-lg);
    border: 1px solid rgba(43, 154, 110, .08);
    box-shadow: 0 4px 20px rgba(0, 0, 0, .04);
    background: rgba(255, 255, 255, .85);
    backdrop-filter: blur(10px);
  }

  :deep(.el-form-item__label) {
    font-weight: 600;
    color: var(--text-primary);
    font-size: 13px;
  }
}

.list-section {
  position: relative;
  z-index: 1;

  .book-card {
    margin-bottom: 24px;
    cursor: pointer;
    transition: all 0.35s cubic-bezier(0.16, 1, 0.3, 1);
    border: 1px solid var(--border-light);
    border-radius: 14px;
    overflow: hidden;
    background: #fff;

    &:hover {
      transform: translateY(-6px);
      border-color: transparent;
      box-shadow: 0 16px 40px rgba(0, 0, 0, .1);

      .book-cover {
        transform: scale(1.06);
      }
    }

    .book-cover {
      width: 100%;
      height: 220px;
      object-fit: cover;
      transition: transform 0.5s cubic-bezier(0.16, 1, 0.3, 1);
    }

    .book-info {
      padding: 14px 16px;

      .book-title {
        font-size: 15px;
        font-weight: 600;
        color: var(--text-primary);
        margin-bottom: 8px;
        line-height: 1.5;
        height: 45px;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }

      .book-price {
        margin-bottom: 8px;
        display: flex;
        align-items: baseline;
        gap: 6px;

        .price {
          color: var(--accent-color);
          font-size: 20px;
          font-weight: 800;
        }

        .original-price {
          font-size: 12px;
          color: var(--text-placeholder);
          text-decoration: line-through;
        }
      }

      .seller-info {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 12px;
        color: var(--text-secondary);
        margin-bottom: 6px;
        padding-top: 8px;
        border-top: 1px solid var(--border-light);

        .view-count {
          display: flex;
          align-items: center;
          gap: 3px;
        }
      }

      .school-tag {
        font-size: 12px;
        color: var(--primary-color);
        display: flex;
        align-items: center;
        gap: 4px;
        margin-top: 4px;
      }
    }
  }
}

.pagination {
  margin-top: 40px;
  display: flex;
  justify-content: center;
  position: relative;
  z-index: 1;
}
</style>


