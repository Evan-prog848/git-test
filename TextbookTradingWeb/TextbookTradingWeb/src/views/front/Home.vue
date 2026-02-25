<template>
  <div class="home">
    <!-- ========== Hero 大图区 ========== -->
    <section class="hero">
      <div class="hero-bg">
        <div class="hero-orb hero-orb--1"></div>
        <div class="hero-orb hero-orb--2"></div>
        <div class="hero-orb hero-orb--3"></div>
      </div>

      <div class="container hero-inner">
        <div class="hero-text">
          <h1 class="hero-title">让闲置教材<span class="highlight">流动</span>起来</h1>
          <p class="hero-desc">校园二手教材交易平台 · 省钱环保从这里开始</p>
          <div class="hero-search">
            <el-input
              v-model="heroSearch"
              placeholder="搜索教材名称、作者、课程..."
              size="large"
              clearable
              @keyup.enter="handleHeroSearch"
            >
              <template #append>
                <el-button type="primary" @click="handleHeroSearch">搜 索</el-button>
              </template>
            </el-input>
          </div>
          <div class="hero-stats">
            <div class="stat"><strong>5000+</strong><span>在售教材</span></div>
            <div class="stat-divider"></div>
            <div class="stat"><strong>3200+</strong><span>注册用户</span></div>
            <div class="stat-divider"></div>
            <div class="stat"><strong>8600+</strong><span>成功交易</span></div>
          </div>
        </div>

        <div class="hero-banner">
          <el-carousel class="banner" :interval="5000" arrow="hover" indicator-position="outside">
            <el-carousel-item v-for="item in banners" :key="item.id">
              <div class="banner-item" :style="{ background: item.color ? item.color : '#1e2432' }">
                <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.title" class="banner-img" />
                <div v-else class="banner-placeholder">{{ item.title }}</div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>

      <!-- 公告浮条 -->
      <div class="notice-float" v-if="notices.length">
        <div class="container notice-inner">
          <div class="notice-icon"><el-icon><Bell /></el-icon> 公告</div>
          <div class="notice-scroll">
            <span v-for="notice in notices" :key="notice.id" class="notice-item" @click="openNotice(notice)">
              {{ notice.title }}
            </span>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== 分类快速入口 ========== -->
    <section class="section-categories">
      <div class="cat-deco cat-deco--ring"></div>
      <div class="cat-deco cat-deco--dots"></div>
      <div class="container">
        <div class="section-head">
          <h2>教材分类</h2>
          <p>快速找到你需要的课程教材</p>
        </div>
        <div class="category-grid">
          <div
            v-for="(category, index) in categories"
            :key="category.id"
            class="cat-card"
            :class="'cat-card--' + ((index % 6) + 1)"
            @click="handleCategoryClick(category.id)"
          >
            <div class="cat-card__deco"></div>
            <div class="cat-card__icon">
              <el-icon v-if="category.icon"><component :is="category.icon" /></el-icon>
            </div>
            <div class="cat-card__body">
              <span class="cat-card__name">{{ category.name }}</span>
              <span class="cat-card__count">{{ category.count || 0 }}本教材</span>
            </div>
            <div class="cat-card__arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== 最新发布 ========== -->
    <section class="section-latest">
      <div class="latest-deco latest-deco--circle"></div>
      <div class="latest-deco latest-deco--grid"></div>
      <div class="container">
        <div class="section-head">
          <h2>最新发布</h2>
          <el-button type="primary" plain round @click="$router.push('/textbooks')">查看全部 <el-icon><ArrowRight /></el-icon></el-button>
        </div>
        <el-row :gutter="24">
          <el-col :span="6" v-for="book in latestBooks" :key="book.id">
            <div class="book-card" @click="handleBookClick(book.id)">
              <div class="book-cover-wrap">
                <img :src="book.cover" class="book-cover" />
                <div class="book-condition">{{ book.conditionLevel }}成新</div>
              </div>
              <div class="book-body">
                <h3 class="book-title">{{ book.title }}</h3>
                <div class="book-price-row">
                  <span class="book-price">¥{{ book.price }}</span>
                  <span class="book-original">¥{{ book.originalPrice }}</span>
                </div>
                <div class="book-meta">
                  <span><el-icon><View /></el-icon> {{ book.viewCount }}</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </section>

    <el-dialog v-model="noticeDialogVisible" title="公告详情" width="600px">
      <div v-if="currentNotice" class="notice-detail">
        <h3 class="notice-title">{{ currentNotice.title }}</h3>
        <div class="notice-time">{{ formatNoticeTime(currentNotice.createTime) }}</div>
        <div class="notice-content">{{ currentNotice.content }}</div>
      </div>
      <template #footer>
        <el-button @click="noticeDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowRight, View, Bell } from '@element-plus/icons-vue'
import { getTextbookList } from '@/api/textbook'
import request from '@/utils/request' // 用于获取轮播图和分类等通用接口

const router = useRouter()
const banners = ref([])
const categories = ref([])
const latestBooks = ref([])
const notices = ref([])
const noticeDialogVisible = ref(false)
const currentNotice = ref(null)
const heroSearch = ref('')

const handleHeroSearch = () => {
  if (heroSearch.value.trim()) {
    router.push({ path: '/textbooks', query: { keyword: heroSearch.value.trim() } })
  }
}

const fetchBanners = async () => {
  try {
    const res = await request.get('/banner/list')
    if (res.data && res.data.length > 0) {
      banners.value = res.data.filter(b => b.status === 1)
    } else {
      useMockBanners()
    }
  } catch (error) {
    console.error('Failed to fetch banners:', error)
    useMockBanners()
  }
}

const useMockBanners = () => {
  banners.value = [
    {
      id: 1,
      title: '开学季特惠活动',
      imageUrl: '/images/books/banners/banner1.png',
      color: '#e8f3ff'
    },
    {
      id: 2,
      title: '让闲置教材流动起来',
      imageUrl: '/images/books/banners/banner2.png',
      color: '#f0f9eb'
    },
    {
      id: 3,
      title: '校园二手交易平台',
      imageUrl: '/images/books/banners/banner3.png',
      color: '#f4f4f5'
    }
  ]
}


const fetchCategories = async () => {
  try {
    const res = await request.get('/category/list')
    if (res.data && res.data.length > 0) {
      const list = res.data.filter(c => !c.parentId || c.parentId === 0)
      // 获取每个分类的教材数量
      try {
        const statsRes = await request.get('/category/stats')
        const statsMap = {}
        if (statsRes.data) {
          statsRes.data.forEach(s => { statsMap[s.name] = s.value })
        }
        list.forEach(c => { c.count = statsMap[c.name] || 0 })
      } catch (e) {
        list.forEach(c => { c.count = 0 })
      }
      categories.value = list
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
    { id: 1, name: '计算机类', icon: 'Monitor' },
    { id: 2, name: '经济管理', icon: 'DataLine' },
    { id: 3, name: '文学艺术', icon: 'Edit' },
    { id: 4, name: '理工科', icon: 'Cpu' },
    { id: 5, name: '医药卫生', icon: 'FirstAidKit' },
    { id: 6, name: '外语考试', icon: 'School' }
  ]
}

const fetchLatestBooks = async () => {
  try {
    const res = await getTextbookList({ pageSize: 8, status: 1 })
    latestBooks.value = res.data?.records || []
  } catch (error) {
    console.error('Failed to fetch latest books:', error)
    latestBooks.value = []
  }
}

const fetchNotices = async () => {
  try {
    const res = await request.get('/notice/list', { params: { pageNum: 1, pageSize: 3 } })
    notices.value = res.data?.records || []
  } catch (error) {
    console.error('Failed to fetch notices:', error)
    notices.value = []
  }
}

const formatNoticeTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleDateString('zh-CN')
}

const openNotice = (notice) => {
  currentNotice.value = notice
  noticeDialogVisible.value = true
}

const handleCategoryClick = (categoryId) => {
  router.push({ path: '/textbooks', query: { categoryId } })
}

const handleBookClick = (bookId) => {
  router.push(`/textbook/${bookId}`)
}

onMounted(() => {
  fetchBanners()
  fetchCategories()
  fetchLatestBooks()
  fetchNotices()
})
</script>

<style scoped lang="scss">
/* ===========================
   Hero
   =========================== */
.hero {
  position: relative;
  background: linear-gradient(160deg, #0f1923 0%, #162b3a 40%, #1a3a3a 70%, #1f3d2e 100%);
  padding: 56px 0 0;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.hero-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: .45;

  &--1 { width: 500px; height: 500px; top: -120px; right: -80px; background: #2b9a6e; }
  &--2 { width: 350px; height: 350px; bottom: -60px; left: -40px; background: #f0883e; opacity: .2; }
  &--3 { width: 200px; height: 200px; top: 40%; left: 45%; background: #3ec08d; opacity: .15; }
}

.hero-inner {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  gap: 48px;
  padding-bottom: 56px;
  max-width: 1400px;
}

.hero-text {
  flex: 1;
  min-width: 0;
}

.hero-title {
  margin: 0 0 16px;
  font-size: 42px;
  font-weight: 800;
  color: #fff;
  line-height: 1.25;
  letter-spacing: -1px;

  .highlight {
    color: #3ec08d;
  }
}

.hero-desc {
  margin: 0 0 32px;
  font-size: 16px;
  color: rgba(255,255,255,.6);
  font-weight: 400;
}

.hero-search {
  max-width: 480px;
  margin-bottom: 36px;

  :deep(.el-input-group__append) {
    padding: 0;

    .el-button {
      padding: 0 28px;
      height: 100%;
      border-radius: 0 8px 8px 0;
      font-weight: 600;
      font-size: 15px;
    }
  }

  :deep(.el-input__wrapper) {
    background: rgba(255,255,255,.08);
    border: 1px solid rgba(255,255,255,.12);
    box-shadow: none !important;
    color: #fff;

    input { color: #fff; }
    input::placeholder { color: rgba(255,255,255,.35); }
  }
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 24px;

  .stat {
    strong {
      display: block;
      font-size: 26px;
      font-weight: 800;
      color: #fff;
      letter-spacing: -0.5px;
    }
    span {
      font-size: 13px;
      color: rgba(255,255,255,.45);
    }
  }

  .stat-divider {
    width: 1px;
    height: 32px;
    background: rgba(255,255,255,.12);
  }
}

.hero-banner {
  width: 700px;
  flex-shrink: 0;

  .banner {
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 20px 60px rgba(0,0,0,.35);
    aspect-ratio: 16 / 9;
  }

  :deep(.el-carousel__container) { height: 100%; }
  :deep(.el-carousel__item) { height: 100%; }

  :deep(.el-carousel__indicators--outside) {
    margin-top: 12px;

    .el-carousel__button {
      width: 20px; height: 4px; border-radius: 2px; opacity: .35;
    }
    .is-active .el-carousel__button {
      width: 28px; opacity: 1; background: #3ec08d;
    }
  }
}

.banner-item {
  width: 100%; height: 100%;
  display: flex; justify-content: center; align-items: center;
  overflow: hidden;
}

.banner-img { width: 100%; height: 100%; object-fit: fill; }

.banner-placeholder {
  width: 100%; height: 100%;
  display: flex; justify-content: center; align-items: center;
  color: rgba(255,255,255,.7); font-size: 24px; font-weight: 700;
}

/* 公告浮条 */
.notice-float {
  background: rgba(0,0,0,.25);
  backdrop-filter: blur(12px);
  border-top: 1px solid rgba(255,255,255,.06);
}

.notice-inner {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 10px 0;
}

.notice-icon {
  display: flex; align-items: center; gap: 6px;
  color: #f0883e; font-weight: 600; font-size: 13px; white-space: nowrap;
  .el-icon { font-size: 16px; }
}

.notice-scroll {
  display: flex; gap: 24px; overflow: hidden;
}

.notice-item {
  white-space: nowrap; font-size: 13px;
  color: rgba(255,255,255,.55); cursor: pointer;
  transition: color .2s;
  &:hover { color: #3ec08d; }
}

/* 公告弹窗 */
.notice-detail { display: flex; flex-direction: column; gap: 12px; }
.notice-title { margin: 0; font-size: 18px; font-weight: 700; color: var(--text-primary); }
.notice-content { color: var(--text-regular); line-height: 1.8; white-space: pre-wrap; }

/* ===========================
   Section Head 通用
   =========================== */
.section-head {
  text-align: center;
  margin-bottom: 40px;

  h2 {
    margin: 0 0 8px;
    font-size: 28px;
    font-weight: 800;
    color: var(--text-primary);
    letter-spacing: -0.5px;
  }

  p {
    margin: 0;
    font-size: 15px;
    color: var(--text-secondary);
  }
}

/* ===========================
   分类
   =========================== */
.section-categories {
  padding: 64px 0 56px;
  background: linear-gradient(180deg, #d8f0e5 0%, #e8f5ee 40%, #eef5f0 100%);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: -40px;
    left: -30px;
    width: 400px;
    height: 400px;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(43,154,110,.12) 0%, rgba(43,154,110,.03) 50%, transparent 70%);
    pointer-events: none;
  }

  &::after {
    content: '';
    position: absolute;
    bottom: -50px;
    right: -20px;
    width: 380px;
    height: 380px;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(240,136,62,.10) 0%, rgba(240,136,62,.02) 50%, transparent 70%);
    pointer-events: none;
  }
}

.cat-deco {
  position: absolute;
  pointer-events: none;
  z-index: 0;

  &--ring {
    top: 30px;
    right: 8%;
    width: 120px;
    height: 120px;
    border-radius: 50%;
    border: 3px solid rgba(43, 154, 110, .10);
    box-shadow: 0 0 0 20px rgba(43, 154, 110, .03);
  }

  &--dots {
    bottom: 40px;
    left: 5%;
    width: 80px;
    height: 80px;
    background-image:
      radial-gradient(circle, rgba(240, 136, 62, .18) 2px, transparent 2px);
    background-size: 16px 16px;
  }
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  position: relative;
  z-index: 1;
}

.cat-card {
  position: relative;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px 24px;
  border-radius: 16px;
  cursor: pointer;
  overflow: hidden;
  transition: all .35s cubic-bezier(.16,1,.3,1);

  &:hover {
    transform: translateY(-4px) scale(1.01);
    .cat-card__icon { transform: scale(1.12) rotate(-6deg); }
    .cat-card__arrow { opacity: 1; transform: translateX(0); }
    .cat-card__deco { transform: scale(1.3); }
  }

  &__deco {
    position: absolute;
    top: -30px; right: -30px;
    width: 100px; height: 100px;
    border-radius: 50%;
    opacity: .15;
    transition: transform .5s cubic-bezier(.16,1,.3,1);
    pointer-events: none;
  }

  &__icon {
    width: 52px; height: 52px;
    border-radius: 14px;
    display: flex; align-items: center; justify-content: center;
    font-size: 26px;
    flex-shrink: 0;
    transition: transform .35s cubic-bezier(.16,1,.3,1);
  }

  &__body {
    flex: 1; min-width: 0;
  }

  &__name {
    display: block;
    font-size: 16px;
    font-weight: 700;
    margin-bottom: 4px;
  }

  &__count {
    font-size: 13px;
    opacity: .6;
  }

  &__arrow {
    font-size: 16px;
    opacity: 0;
    transform: translateX(-6px);
    transition: all .3s cubic-bezier(.16,1,.3,1);
  }

  // ---- 1: 翠绿 ----
  &--1 {
    background: linear-gradient(135deg, #ffffff 0%, #e8f7f0 100%);
    color: #1a7a52;
    .cat-card__icon { background: rgba(43,154,110,.15); color: #2b9a6e; }
    .cat-card__deco { background: #2b9a6e; }
    &:hover { box-shadow: 0 12px 32px rgba(43,154,110,.18); }
  }

  // ---- 2: 暖橙 ----
  &--2 {
    background: linear-gradient(135deg, #ffffff 0%, #fdf0e4 100%);
    color: #a55a1e;
    .cat-card__icon { background: rgba(240,136,62,.15); color: #f0883e; }
    .cat-card__deco { background: #f0883e; }
    &:hover { box-shadow: 0 12px 32px rgba(240,136,62,.18); }
  }

  // ---- 3: 靛紫 ----
  &--3 {
    background: linear-gradient(135deg, #ffffff 0%, #f0ebff 100%);
    color: #5b3dbd;
    .cat-card__icon { background: rgba(139,92,246,.15); color: #8b5cf6; }
    .cat-card__deco { background: #8b5cf6; }
    &:hover { box-shadow: 0 12px 32px rgba(139,92,246,.18); }
  }

  // ---- 4: 天蓝 ----
  &--4 {
    background: linear-gradient(135deg, #ffffff 0%, #e8f0fe 100%);
    color: #1e52a5;
    .cat-card__icon { background: rgba(59,130,246,.15); color: #3b82f6; }
    .cat-card__deco { background: #3b82f6; }
    &:hover { box-shadow: 0 12px 32px rgba(59,130,246,.18); }
  }

  // ---- 5: 珊瑚红 ----
  &--5 {
    background: linear-gradient(135deg, #ffffff 0%, #fde8e8 100%);
    color: #b32828;
    .cat-card__icon { background: rgba(239,68,68,.15); color: #ef4444; }
    .cat-card__deco { background: #ef4444; }
    &:hover { box-shadow: 0 12px 32px rgba(239,68,68,.18); }
  }

  // ---- 6: 青蓝 ----
  &--6 {
    background: linear-gradient(135deg, #ffffff 0%, #e0f7fa 100%);
    color: #0c7e8c;
    .cat-card__icon { background: rgba(6,182,212,.15); color: #06b6d4; }
    .cat-card__deco { background: #06b6d4; }
    &:hover { box-shadow: 0 12px 32px rgba(6,182,212,.18); }
  }
}

/* ===========================
   最新发布
   =========================== */
.section-latest {
  padding: 64px 0 80px;
  background: linear-gradient(180deg, #eaf0ec 0%, #eceef2 30%, #eceef2 100%);
  position: relative;
  overflow: hidden;

  .container { position: relative; z-index: 1; }

  .section-head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-align: left;

    h2 { margin: 0; }
  }
}

.latest-deco {
  position: absolute;
  pointer-events: none;
  z-index: 0;

  &--circle {
    top: -60px;
    right: 8%;
    width: 350px;
    height: 350px;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(43,154,110,.08) 0%, transparent 60%);
  }

  &--grid {
    bottom: 40px;
    left: 3%;
    width: 120px;
    height: 120px;
    background-image:
      radial-gradient(circle, rgba(43,154,110,.15) 2px, transparent 2px);
    background-size: 16px 16px;
    opacity: .6;
  }
}

.book-card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  cursor: pointer;
  margin-bottom: 24px;
  border: 1px solid var(--border-light);
  transition: all .35s cubic-bezier(.16,1,.3,1);

  &:hover {
    transform: translateY(-8px);
    border-color: transparent;
    box-shadow: 0 20px 50px rgba(0,0,0,.1);
    .book-cover { transform: scale(1.06); }
  }
}

.book-cover-wrap {
  position: relative;
  overflow: hidden;
  height: 220px;
  background: var(--bg-color);
}

.book-cover {
  width: 100%; height: 100%;
  object-fit: cover;
  transition: transform .5s cubic-bezier(.16,1,.3,1);
}

.book-condition {
  position: absolute;
  top: 12px; left: 12px;
  background: rgba(0,0,0,.55);
  backdrop-filter: blur(6px);
  color: #fff;
  font-size: 11px; font-weight: 600;
  padding: 3px 10px;
  border-radius: 20px;
}

.book-body {
  padding: 16px;
}

.book-title {
  margin: 0 0 10px;
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  line-height: 1.45;
  height: 44px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.book-price-row {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 8px;
}

.book-price {
  font-size: 22px;
  font-weight: 800;
  color: var(--accent-color);
  letter-spacing: -0.5px;
}

.book-original {
  font-size: 13px;
  color: var(--text-placeholder);
  text-decoration: line-through;
}

.book-meta {
  display: flex; align-items: center; gap: 4px;
  color: var(--text-secondary); font-size: 12px;
  .el-icon { font-size: 14px; }
}
</style>


