<template>
  <div class="layout">
    <header class="header" :class="{ 'header--scrolled': isScrolled }">
      <div class="container header-content">
        <div class="logo" @click="$router.push('/')">
          <div class="logo-icon">
            <el-icon><Reading /></el-icon>
          </div>
          <span class="logo-text">书循</span>
        </div>
        <nav class="nav">
          <router-link to="/" class="nav-link" :class="{ active: $route.path === '/' }">首页</router-link>
          <router-link to="/textbooks" class="nav-link" :class="{ active: $route.path === '/textbooks' }">所有教材</router-link>
          <!-- <router-link to="/wanted" class="nav-link" :class="{ active: $route.path === '/wanted' }">求购信息</router-link> -->
          <router-link v-if="userStore.isLogin" to="/publish" class="nav-link" :class="{ active: $route.path === '/publish' }">发布教材</router-link>
        </nav>
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索书名、作者、课程..."
            :prefix-icon="Search"
            @keyup.enter="handleSearch"
            clearable
          />
        </div>
        <div class="user-info">
          <template v-if="userStore.isLogin">
            <router-link to="/messages" class="icon-btn" title="消息">
              <el-icon :size="20"><ChatDotRound /></el-icon>
            </router-link>
            <el-dropdown @command="handleCommand" trigger="click">
              <span class="avatar-dropdown">
                <el-avatar :size="34" :src="userStore.userInfo.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
                <span class="nickname">{{ userStore.userInfo.nickname }}</span>
                <el-icon class="arrow"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><UserFilled /></el-icon> 个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="orders">
                    <el-icon><List /></el-icon> 我的订单
                  </el-dropdown-item>
                  <el-dropdown-item command="favorites">
                    <el-icon><Star /></el-icon> 我的收藏
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon> 退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button @click="$router.push('/login')" round>登录</el-button>
            <el-button type="primary" @click="$router.push('/register')" round>注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <main class="main">
      <router-view v-slot="{ Component }">
        <transition name="fade-slide" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <footer class="footer">
      <div class="container footer-inner">
        <div class="footer-grid">
          <div class="footer-brand">
            <div class="footer-logo">
              <el-icon><Reading /></el-icon>
              <span>书循</span>
            </div>
            <p class="footer-desc">让知识传递，让书籍循环。校园二手教材交易平台，助力绿色校园。</p>
          </div>
          <div class="footer-links">
            <h4>快速导航</h4>
            <router-link to="/textbooks">浏览教材</router-link>
            <router-link to="/wanted">求购信息</router-link>
            <router-link to="/publish">发布教材</router-link>
          </div>
          <div class="footer-links">
            <h4>帮助支持</h4>
            <a href="javascript:;">使用指南</a>
            <a href="javascript:;">常见问题</a>
            <a href="javascript:;">联系我们</a>
          </div>
        </div>
        <div class="footer-bottom">
          <p>© 2025 书循 - 校园二手教材交易系统</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Reading, ChatDotRound, ArrowDown, UserFilled, List, Star, SwitchButton } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const searchKeyword = ref('')
const isScrolled = ref(false)

const handleScroll = () => {
  isScrolled.value = window.scrollY > 10
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/textbooks',
      query: { keyword: searchKeyword.value.trim() }
    })
  }
}

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  } else {
    router.push(`/${command}`)
  }
}
</script>

<style scoped lang="scss">
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-page);
}

/* ===== Header ===== */
.header {
  position: sticky;
  top: 0;
  z-index: 1000;
  height: 68px;
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  border-bottom: 1px solid transparent;
  transition: all var(--transition-base);

  &--scrolled {
    background: rgba(255, 255, 255, 0.95);
    border-bottom-color: var(--border-light);
    box-shadow: 0 1px 8px rgba(0, 0, 0, 0.06);
  }

  .header-content {
    display: flex;
    align-items: center;
    height: 100%;
    max-width: 1240px;
    margin: 0 auto;
    padding: 0 24px;
    gap: 8px;
  }
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  margin-right: 12px;
  flex-shrink: 0;

  .logo-icon {
    width: 36px;
    height: 36px;
    border-radius: 10px;
    background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 20px;
    transition: transform var(--transition-base);
  }

  &:hover .logo-icon {
    transform: rotate(-8deg) scale(1.05);
  }

  .logo-text {
    font-size: 22px;
    font-weight: 800;
    color: var(--text-primary);
    letter-spacing: -0.5px;
  }
}

.nav {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
  margin-left: 12px;

  .nav-link {
    padding: 6px 16px;
    font-size: 15px;
    font-weight: 500;
    color: var(--text-regular);
    border-radius: var(--radius-sm);
    transition: all var(--transition-fast);
    text-decoration: none;
    white-space: nowrap;

    &:hover {
      color: var(--primary-color);
      background: var(--primary-lighter);
    }

    &.active {
      color: var(--primary-color);
      background: var(--primary-lighter);
      font-weight: 600;
    }
  }
}

.search-bar {
  width: 260px;
  flex-shrink: 0;

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

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
  margin-left: 8px;

  .icon-btn {
    width: 38px;
    height: 38px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    color: var(--text-regular);
    transition: all var(--transition-fast);
    text-decoration: none;

    &:hover {
      background: var(--primary-lighter);
      color: var(--primary-color);
    }
  }

  .avatar-dropdown {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    padding: 4px 8px 4px 4px;
    border-radius: var(--radius-xl);
    transition: background var(--transition-fast);

    &:hover {
      background: var(--bg-color);
    }

    .el-avatar {
      border: 2px solid var(--primary-lighter);
    }

    .nickname {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-primary);
      max-width: 80px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .arrow {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }

  .el-button {
    font-weight: 500;
  }

  .el-button--primary {
    background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
    border: none;

    &:hover {
      opacity: 0.9;
    }
  }
}

/* ===== Main ===== */
.main {
  flex: 1;
  padding: 28px 0;
  background-color: var(--bg-page);
}

/* ===== Footer ===== */
.footer {
  background: #1a1d23;
  color: #9ca3af;
  padding: 0;

  .footer-inner {
    max-width: 1240px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .footer-grid {
    display: grid;
    grid-template-columns: 2fr 1fr 1fr;
    gap: 48px;
    padding: 48px 0 36px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  }

  .footer-brand {
    .footer-logo {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #fff;
      font-size: 20px;
      font-weight: 700;
      margin-bottom: 12px;

      .el-icon {
        font-size: 24px;
        color: var(--primary-light);
      }
    }

    .footer-desc {
      font-size: 14px;
      line-height: 1.7;
      color: #6b7280;
      margin: 0;
      max-width: 320px;
    }
  }

  .footer-links {
    display: flex;
    flex-direction: column;
    gap: 10px;

    h4 {
      color: #e5e7eb;
      font-size: 15px;
      font-weight: 600;
      margin: 0 0 6px;
    }

    a {
      font-size: 14px;
      color: #6b7280;
      text-decoration: none;
      transition: color var(--transition-fast);

      &:hover {
        color: var(--primary-light);
      }
    }
  }

  .footer-bottom {
    padding: 20px 0;
    text-align: center;

    p {
      margin: 0;
      font-size: 13px;
      color: #4b5563;
    }
  }
}
</style>


