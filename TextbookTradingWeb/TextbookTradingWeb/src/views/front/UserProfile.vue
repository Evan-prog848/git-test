<template>
  <div class="container user-profile-page" v-loading="loading">
    <div class="profile-card" v-if="user">
      <div class="profile-header">
        <el-avatar :size="80" :src="user.avatar || defaultAvatar" />
        <div class="profile-info">
          <h2 class="profile-name">
            {{ user.nickname }}
            <el-tag v-if="user.isVerified" type="success" size="small">已认证</el-tag>
          </h2>
          <p class="profile-school" v-if="user.school">{{ user.school }} · {{ user.college }} · {{ user.major }}</p>
          <p class="profile-time">注册于 {{ formatTime(user.createTime) }}</p>
        </div>
        <div class="profile-actions" v-if="!isSelf">
          <el-button type="primary" @click="goChat">发消息</el-button>
        </div>
      </div>

      <div class="profile-stats">
        <div class="stat-item">
          <strong>{{ user.textbookCount || 0 }}</strong>
          <span>发布教材</span>
        </div>
        <div class="stat-item">
          <strong>{{ user.orderCount || 0 }}</strong>
          <span>成交订单</span>
        </div>
        <div class="stat-item">
          <strong>{{ user.goodRate ? (user.goodRate * 100).toFixed(0) + '%' : '暂无' }}</strong>
          <span>好评率</span>
        </div>
        <div class="stat-item">
          <strong>{{ user.favoriteCount || 0 }}</strong>
          <span>收藏</span>
        </div>
      </div>
    </div>

    <div class="textbook-section" v-if="user">
      <h3>TA发布的教材</h3>
      <el-row :gutter="20" v-loading="textbookLoading">
        <el-col :span="6" v-for="book in textbooks" :key="book.id">
          <el-card shadow="hover" class="book-card" @click="$router.push(`/textbook/${book.id}`)">
            <img :src="book.coverImage || '/images/default-book.png'" class="book-cover" />
            <div class="book-info">
              <div class="book-title">{{ book.title }}</div>
              <div class="book-price">
                <span class="price">¥{{ book.price }}</span>
                <span class="original-price" v-if="book.originalPrice">¥{{ book.originalPrice }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-if="!textbookLoading && textbooks.length === 0" description="暂未发布教材" />
    </div>

    <el-empty v-if="!loading && !user" description="用户不存在" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserProfile } from '@/api/user'
import { getTextbookList } from '@/api/textbook'
import { useUserStore } from '@/stores/user'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
const loading = ref(false)
const textbookLoading = ref(false)
const user = ref(null)
const textbooks = ref([])

const isSelf = computed(() => user.value && userStore.userInfo?.id === user.value.id)

const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).format('YYYY-MM-DD')
}

const fetchUser = async () => {
  loading.value = true
  try {
    const res = await getUserProfile(route.params.id)
    user.value = res.data
  } catch (error) {
    console.error('获取用户信息失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchTextbooks = async () => {
  textbookLoading.value = true
  try {
    const res = await getTextbookList({
      userId: route.params.id,
      pageNum: 1,
      pageSize: 12,
      status: 1
    })
    textbooks.value = res.data.records || []
  } catch (error) {
    console.error('获取教材列表失败:', error)
  } finally {
    textbookLoading.value = false
  }
}

const goChat = () => {
  if (!userStore.isLogin) {
    router.push('/login')
    return
  }
  router.push({ path: '/chat', query: { userId: route.params.id } })
}

onMounted(() => {
  fetchUser()
  fetchTextbooks()
})
</script>

<style scoped lang="scss">
.user-profile-page {
  padding: 30px 0 60px;
}

.profile-card {
  background: #fff;
  border-radius: var(--radius-lg);
  padding: 32px;
  margin-bottom: 30px;
  border: 1px solid var(--border-light);
  box-shadow: 0 4px 20px rgba(0, 0, 0, .04);
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 28px;

  .profile-info {
    flex: 1;
  }

  .profile-name {
    margin: 0 0 6px;
    font-size: 22px;
    font-weight: 700;
    color: var(--text-primary);
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .profile-school {
    margin: 0 0 4px;
    font-size: 14px;
    color: var(--text-regular);
  }

  .profile-time {
    margin: 0;
    font-size: 13px;
    color: var(--text-secondary);
  }
}

.profile-stats {
  display: flex;
  gap: 0;
  border-top: 1px solid var(--border-light);
  padding-top: 24px;

  .stat-item {
    flex: 1;
    text-align: center;

    strong {
      display: block;
      font-size: 22px;
      font-weight: 800;
      color: var(--text-primary);
      margin-bottom: 4px;
    }

    span {
      font-size: 13px;
      color: var(--text-secondary);
    }
  }
}

.textbook-section {
  h3 {
    font-size: 18px;
    font-weight: 700;
    color: var(--text-primary);
    margin: 0 0 20px;
  }

  .book-card {
    margin-bottom: 20px;
    cursor: pointer;
    border-radius: 14px;
    border: 1px solid var(--border-light);
    overflow: hidden;
    transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 12px 32px rgba(0, 0, 0, .1);
      border-color: transparent;

      .book-cover { transform: scale(1.05); }
    }

    :deep(.el-card__body) { padding: 0; }

    .book-cover {
      width: 100%;
      height: 200px;
      object-fit: cover;
      transition: transform 0.4s;
    }

    .book-info {
      padding: 12px 14px;

      .book-title {
        font-size: 14px;
        font-weight: 600;
        color: var(--text-primary);
        margin-bottom: 6px;
        line-height: 1.4;
        height: 40px;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }

      .book-price {
        display: flex;
        align-items: baseline;
        gap: 6px;

        .price {
          color: var(--accent-color);
          font-size: 18px;
          font-weight: 800;
        }

        .original-price {
          font-size: 12px;
          color: var(--text-placeholder);
          text-decoration: line-through;
        }
      }
    }
  }
}
</style>
