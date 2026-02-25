<template>
  <div class="container my-favorites-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span class="title">我的收藏</span>
        </div>
      </template>

      <el-row :gutter="20" v-loading="loading">
        <el-col :span="6" v-for="book in favorites" :key="book.id">
          <el-card :body-style="{ padding: '0px' }" class="book-card">
            <div class="card-image-wrapper" @click="$router.push(`/textbook/${book.id}`)">
              <el-image :src="book.cover" class="book-cover" fit="cover">
                <template #error>
                  <div class="image-error">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="status-mask" v-if="book.status !== 1">
                <span>{{ book.status === 2 ? '已售出' : '已下架' }}</span>
              </div>
            </div>
            <div class="book-info">
              <div class="book-title" @click="$router.push(`/textbook/${book.id}`)">{{ book.title }}</div>
              <div class="book-price">
                <span class="price">¥{{ book.price }}</span>
              </div>
              <div class="card-actions">
                <el-button link @click="handleRemove(book.id)">取消收藏</el-button>
                <el-button type="primary" size="small" :disabled="book.status !== 1" @click="$router.push(`/textbook/${book.id}`)">
                  立即购买
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-empty v-if="favorites.length === 0 && !loading" description="暂无收藏的教材" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture } from '@element-plus/icons-vue'
import { getMyFavorites, removeFavorite } from '@/api/favorite'

const loading = ref(false)
const favorites = ref([])

const fetchFavorites = async () => {
  loading.value = true
  try {
    const res = await getMyFavorites({ pageNum: 1, pageSize: 100 })
    favorites.value = res.data.records
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const handleRemove = (id) => {
  ElMessageBox.confirm('确定要取消收藏该教材吗？', '提示').then(async () => {
    await removeFavorite(id)
    favorites.value = favorites.value.filter(item => item.id !== id)
    ElMessage.success('已取消收藏')
  })
}

onMounted(fetchFavorites)
</script>

<style scoped lang="scss">
.my-favorites-page {
  padding: 20px 0;

  :deep(> .el-card) {
    border-radius: var(--radius-md);
    border: 1px solid var(--border-light);
    box-shadow: var(--shadow-sm);
  }
}

.card-header {
  .title {
    font-size: 18px;
    font-weight: 700;
    color: var(--text-primary);
  }
}

.book-card {
  margin-bottom: 20px;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-light);
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);

  &:hover {
    transform: translateY(-4px);
    border-color: transparent;
    box-shadow: var(--shadow-lg);

    .book-cover {
      transform: scale(1.05);
    }
  }

  .card-image-wrapper {
    position: relative;
    cursor: pointer;
    overflow: hidden;

    .book-cover {
      width: 100%;
      height: 220px;
      transition: transform 0.5s cubic-bezier(0.16, 1, 0.3, 1);
    }
    .image-error {
      width: 100%;
      height: 220px;
      display: flex;
      justify-content: center;
      align-items: center;
      background: var(--bg-color);
      color: var(--text-placeholder);
      font-size: 40px;
    }
    .status-mask {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0,0,0,0.5);
      backdrop-filter: blur(2px);
      display: flex;
      justify-content: center;
      align-items: center;
      color: #fff;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .book-info {
    padding: 14px 16px;
    .book-title {
      font-size: 14px;
      font-weight: 600;
      margin-bottom: 8px;
      cursor: pointer;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      color: var(--text-primary);
      &:hover {
        color: var(--primary-color);
      }
    }
    .book-price {
      margin-bottom: 12px;
      .price {
        color: var(--accent-color);
        font-size: 18px;
        font-weight: 700;
      }
    }
    .card-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 10px;
      border-top: 1px solid var(--border-light);
    }
  }
}
</style>


