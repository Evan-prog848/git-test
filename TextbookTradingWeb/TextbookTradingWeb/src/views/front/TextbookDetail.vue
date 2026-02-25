<template>
  <div class="container textbook-detail-page" v-loading="loading">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/textbooks' }">教材列表</el-breadcrumb-item>
      <el-breadcrumb-item>教材详情</el-breadcrumb-item>
    </el-breadcrumb>

    <div v-if="book" class="detail-container">
      <el-row :gutter="40">
        <!-- 左侧图片展示 -->
        <el-col :span="10">
          <div class="image-gallery">
            <el-image
              :src="activeImage || book.cover"
              class="main-image"
              fit="cover"
              :preview-src-list="book.images"
            />
            <div class="thumbnail-list">
              <div
                v-for="(img, index) in book.images"
                :key="index"
                class="thumbnail-item"
                :class="{ active: activeImage === img }"
                @click="activeImage = img"
              >
                <el-image :src="img" fit="cover" />
              </div>
            </div>
          </div>
        </el-col>

        <!-- 右侧信息 -->
        <el-col :span="14">
          <div class="book-info-header">
            <h1 class="title">{{ book.title }}</h1>
            <div class="meta">
              <span class="view-count"><el-icon><View /></el-icon> {{ book.viewCount }} 次浏览</span>
              <el-divider direction="vertical" />
              <span class="create-time">发布于 {{ book.createTime }}</span>
            </div>
          </div>

          <div class="price-section">
            <div class="price-row">
              <span class="label">售价</span>
              <span class="price">¥{{ book.price }}</span>
              <span class="original-price">原价 ¥{{ book.originalPrice }}</span>
            </div>
            <div class="condition-row">
              <span class="label">成色</span>
              <el-tag type="success">{{ book.conditionLevel }}成新</el-tag>
            </div>
          </div>

          <div class="seller-card" v-if="book.seller">
            <div class="seller-info">
              <el-avatar :size="50" :src="book.seller.avatar || defaultAvatar" />
              <div class="seller-text">
                <div class="nickname">
                  {{ book.seller.nickname }}
                  <el-tag size="small" type="warning" v-if="book.seller.isVerified">已认证</el-tag>
                </div>
                <div class="school">{{ book.seller.school }} · {{ book.seller.college }}</div>
              </div>
              <div class="actions" v-if="!isOwnTextbook">
                <el-button type="primary" plain @click="handleFollow">{{ isFollowed ? '已关注' : '关注他' }}</el-button>
                <el-button type="primary" @click="handleBuy">立即购买</el-button>
              </div>
            </div>
          </div>

          <div class="detail-list">
            <div class="detail-item">
              <span class="label">作者</span>
              <span class="value">{{ book.author }}</span>
            </div>
            <div class="detail-item">
              <span class="label">出版社</span>
              <span class="value">{{ book.publisher }}</span>
            </div>
            <div class="detail-item">
              <span class="label">ISBN</span>
              <span class="value">{{ book.isbn }}</span>
            </div>
            <div class="detail-item">
              <span class="label">适用课程</span>
              <span class="value">{{ book.course || '无' }}</span>
            </div>
          </div>

          <div class="actions-footer">
            <el-button
              :type="isFavorite ? 'warning' : 'default'"
              :icon="isFavorite ? StarFilled : Star"
              @click="handleFavorite"
            >
              {{ isFavorite ? '已收藏' : '收藏' }}
            </el-button>
            <el-button :icon="ChatDotRound" @click="handleChat">联系卖家</el-button>
          </div>
        </el-col>
      </el-row>

      <el-tabs v-model="activeTab" class="detail-tabs">
        <el-tab-pane label="教材描述" name="description">
          <div class="description-content">
            {{ book.description || '卖家暂未填写描述信息。' }}
          </div>
        </el-tab-pane>
        <el-tab-pane label="交易评价" name="reviews">
          <div class="reviews-content">
            <!-- 评价列表组件 -->
            <p>暂无评价</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 购买弹窗 -->
    <el-dialog v-model="buyDialogVisible" title="发起购买申请" width="500px">
      <el-form :model="buyForm" label-width="100px">
        <el-form-item label="教材">
          <span>{{ book?.title }}</span>
        </el-form-item>
        <el-form-item label="价格">
          <span class="price-text">¥{{ book?.price }}</span>
        </el-form-item>
        <el-form-item label="交易点">
          <el-select v-model="buyForm.tradingPointId" placeholder="请选择交易地点" style="width: 100%">
            <el-option
              v-for="point in tradingPoints"
              :key="point.id"
              :label="point.name + (point.location ? ' - ' + point.location : '')"
              :value="point.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="见面时间">
          <el-date-picker
            v-model="buyForm.meetingTime"
            type="datetime"
            placeholder="请选择见面时间"
            style="width: 100%"
            :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            type="textarea"
            v-model="buyForm.remark"
            placeholder="其他沟通信息"
            rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="buyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmBuy" :loading="submitting">
            提交申请
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { View, Star, StarFilled, ChatDotRound } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getTextbookDetail } from '@/api/textbook'
import { createOrder } from '@/api/order'
import { toggleFavorite, checkFavorite } from '@/api/favorite'
import { getTradingPointList } from '@/api/common'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const loading = ref(false)
const submitting = ref(false)
const book = ref(null)
const activeImage = ref('')
const activeTab = ref('description')
const isFavorite = ref(false)
const isFollowed = ref(false)
const buyDialogVisible = ref(false)
const tradingPoints = ref([])
const buyForm = ref({
  tradingPointId: '',
  meetingTime: '',
  remark: ''
})

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

// 加载交易点列表
const fetchTradingPoints = async () => {
  try {
    const res = await getTradingPointList()
    if (res.code === 200) {
      tradingPoints.value = res.data || []
    }
  } catch (e) {
    console.error('加载交易点失败:', e)
  }
}

const isOwnTextbook = computed(() => {
  return book.value?.seller?.id === userStore.userInfo?.id
})

const fetchBookDetail = async () => {
  loading.value = true
  try {
    const id = route.params.id
    const res = await getTextbookDetail(id)
    book.value = res.data
    if (book.value.images && book.value.images.length > 0) {
      activeImage.value = book.value.images[0]
    } else {
      activeImage.value = book.value.cover
    }
    // 检查是否已收藏
    if (userStore.isLogin) {
      try {
        const favRes = await checkFavorite(id)
        isFavorite.value = favRes.data
      } catch (e) {
        // 忽略错误
      }
    }
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const handleFavorite = async () => {
  if (!userStore.isLogin) {
    router.push('/login')
    return
  }
  try {
    await toggleFavorite(book.value.id)
    isFavorite.value = !isFavorite.value
    ElMessage.success(isFavorite.value ? '收藏成功' : '取消收藏')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleFollow = () => {
  if (!userStore.isLogin) {
    router.push('/login')
    return
  }
  isFollowed.value = !isFollowed.value
  ElMessage.success(isFollowed.value ? '关注成功' : '取消关注')
}

const handleBuy = () => {
  if (!userStore.isLogin) {
    router.push('/login')
    return
  }
  buyDialogVisible.value = true
}

const confirmBuy = async () => {
  if (!buyForm.value.tradingPointId) {
    ElMessage.warning('请选择交易地点')
    return
  }
  if (!buyForm.value.meetingTime) {
    ElMessage.warning('请选择见面时间')
    return
  }
  submitting.value = true
  try {
    await createOrder({
      textbookId: book.value.id,
      tradingPointId: buyForm.value.tradingPointId,
      meetingTime: buyForm.value.meetingTime,
      remark: buyForm.value.remark
    })
    ElMessage.success('购买申请已发送，请等待卖家确认')
    buyDialogVisible.value = false
    router.push('/orders')
  } catch (error) {
  } finally {
    submitting.value = false
  }
}

const handleChat = () => {
  if (!userStore.isLogin) {
    router.push('/login')
    return
  }
  if (book.value?.seller?.id) {
    router.push({
      path: '/messages',
      query: {
        userId: book.value.seller.id,
        nickname: book.value.seller.nickname,
        avatar: book.value.seller.avatar || ''
      }
    })
  }
}

onMounted(() => {
  fetchBookDetail()
  fetchTradingPoints()
})
</script>

<style scoped lang="scss">
.textbook-detail-page {
  padding-bottom: 60px;
}

.breadcrumb {
  margin-bottom: 24px;
}

.detail-container {
  background-color: var(--bg-card);
  padding: 40px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-light);
}

.image-gallery {
  .main-image {
    width: 100%;
    height: 500px;
    border-radius: var(--radius-md);
    border: 1px solid var(--border-light);
    overflow: hidden;
  }

  .thumbnail-list {
    margin-top: 14px;
    display: flex;
    gap: 10px;

    .thumbnail-item {
      width: 76px;
      height: 76px;
      border: 2px solid transparent;
      cursor: pointer;
      border-radius: var(--radius-sm);
      overflow: hidden;
      transition: border-color var(--transition-fast);

      &.active {
        border-color: var(--primary-color);
      }

      &:hover:not(.active) {
        border-color: var(--border-color);
      }

      .el-image {
        width: 100%;
        height: 100%;
      }
    }
  }
}

.book-info-header {
  margin-bottom: 20px;

  .title {
    margin: 0 0 10px 0;
    font-size: 24px;
    font-weight: 700;
    color: var(--text-primary);
    line-height: 1.4;
  }

  .meta {
    font-size: 14px;
    color: var(--text-secondary);
    display: flex;
    align-items: center;
  }
}

.price-section {
  background: linear-gradient(135deg, var(--primary-lighter), #f0faf5);
  padding: 20px 24px;
  margin-bottom: 24px;
  border-radius: var(--radius-md);
  border: 1px solid rgba(43, 154, 110, 0.1);

  .price-row {
    margin-bottom: 14px;
    display: flex;
    align-items: baseline;

    .label {
      color: var(--text-regular);
      margin-right: 20px;
      width: 40px;
      font-size: 14px;
    }

    .price {
      color: var(--accent-color);
      font-size: 32px;
      font-weight: 800;
      margin-right: 14px;
      letter-spacing: -1px;
    }

    .original-price {
      color: var(--text-secondary);
      text-decoration: line-through;
      font-size: 14px;
    }
  }

  .condition-row {
    display: flex;
    align-items: center;

    .label {
      color: var(--text-regular);
      margin-right: 20px;
      width: 40px;
      font-size: 14px;
    }
  }
}

.seller-card {
  border: 1px solid var(--border-light);
  padding: 20px;
  margin-bottom: 24px;
  border-radius: var(--radius-md);
  transition: box-shadow var(--transition-fast);

  &:hover {
    box-shadow: var(--shadow-sm);
  }

  .seller-info {
    display: flex;
    align-items: center;

    .el-avatar {
      border: 2px solid var(--primary-lighter);
    }

    .seller-text {
      flex: 1;
      margin-left: 14px;

      .nickname {
        font-weight: 600;
        font-size: 16px;
        margin-bottom: 4px;
        display: flex;
        align-items: center;
        gap: 8px;
        color: var(--text-primary);
      }

      .school {
        color: var(--text-secondary);
        font-size: 13px;
      }
    }

    .actions {
      display: flex;
      gap: 10px;
    }
  }
}

.detail-list {
  margin-bottom: 28px;

  .detail-item {
    margin-bottom: 12px;
    font-size: 14px;
    display: flex;
    align-items: center;

    .label {
      color: var(--text-secondary);
      width: 80px;
      flex-shrink: 0;
    }

    .value {
      color: var(--text-primary);
      font-weight: 500;
    }
  }
}

.actions-footer {
  display: flex;
  gap: 16px;
  border-top: 1px solid var(--border-light);
  padding-top: 24px;
}

.detail-tabs {
  margin-top: 40px;

  .description-content {
    line-height: 1.8;
    color: var(--text-regular);
    padding: 20px 0;
  }
}

.price-text {
  color: var(--accent-color);
  font-size: 20px;
  font-weight: 700;
}
</style>


