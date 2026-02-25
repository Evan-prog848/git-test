<template>
  <div class="admin-layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'" class="aside">
        <div class="logo">
          <el-icon :size="24"><Management /></el-icon>
          <span v-show="!isCollapse">管理后台</span>
        </div>
        <el-menu
          :default-active="$route.path"
          class="el-menu-vertical"
          :collapse="isCollapse"
          :router="true"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <template #title>数据统计</template>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <template #title>用户管理</template>
          </el-menu-item>
          <el-sub-menu index="textbook">
            <template #title>
              <el-icon><Reading /></el-icon>
              <span>教材管理</span>
            </template>
            <el-menu-item index="/admin/textbooks">教材列表</el-menu-item>
            <el-menu-item index="/admin/audit">教材审核</el-menu-item>
            <el-menu-item index="/admin/categories">分类管理</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/admin/orders">
            <el-icon><List /></el-icon>
            <template #title>订单管理</template>
          </el-menu-item>
          <el-menu-item index="/admin/notices">
            <el-icon><Bell /></el-icon>
            <template #title>公告管理</template>
          </el-menu-item>
          <el-menu-item index="/admin/banners">
            <el-icon><Picture /></el-icon>
            <template #title>轮播图管理</template>
          </el-menu-item>
          <el-sub-menu index="campus">
            <template #title>
              <el-icon><School /></el-icon>
              <span>校区管理</span>
            </template>
            <el-menu-item index="/admin/campus">校区列表</el-menu-item>
            <el-menu-item index="/admin/courses">专业课程</el-menu-item>
            <el-menu-item index="/admin/trading-points">交易点</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/admin/credit">
            <el-icon><Medal /></el-icon>
            <template #title>信用管理</template>
          </el-menu-item>
          <el-sub-menu index="system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/admin/roles">角色管理</el-menu-item>
            <el-menu-item index="/admin/menus">菜单管理</el-menu-item>
            <el-menu-item index="/admin/admins">管理员管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-container class="right-container" :style="{ marginLeft: isCollapse ? '64px' : '200px' }">
        <!-- 顶栏 -->
        <el-header class="header">
          <div class="header-left">
            <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
              <Expand v-if="isCollapse" />
              <Fold v-else />
            </el-icon>
            <el-breadcrumb separator="/">
              <el-breadcrumb-item>首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ $route.name }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <el-avatar :size="32" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                <span class="username">管理员</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 内容区 -->
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  Management, DataAnalysis, User, Reading,
  List, Bell, Picture, Setting, Expand, Fold,
  School, Medal
} from '@element-plus/icons-vue'

const router = useRouter()
const isCollapse = ref(false)

const handleCommand = (command) => {
  if (command === 'logout') {
    router.push('/login')
  }
}
</script>

<style scoped lang="scss">
.admin-layout {
  height: 100vh;
  overflow: hidden;
}

.aside {
  background-color: #1e2432;
  color: #fff;
  transition: width 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  overflow-x: hidden;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;

  .logo {
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    font-weight: 700;
    gap: 10px;
    background: linear-gradient(135deg, var(--primary-dark), var(--primary-color));
    color: #fff;
    letter-spacing: 0.5px;
  }

  .el-menu-vertical {
    border-right: none;
    background-color: #1e2432;
    height: calc(100vh - 64px);
    overflow-y: auto;
    padding: 8px;

    &:not(.el-menu--collapse) {
      width: 200px;
    }

    :deep(.el-menu-item), :deep(.el-sub-menu__title) {
      color: #8b95a5;
      border-radius: 8px;
      margin-bottom: 2px;
      height: 44px;
      line-height: 44px;
      transition: all 0.2s ease;

      &:hover {
        background-color: rgba(255, 255, 255, 0.06);
        color: #e0e4ea;
      }
      &.is-active {
        background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
        color: #fff;
        font-weight: 500;
      }
    }

    :deep(.el-sub-menu) {
      .el-menu {
        background-color: transparent;
        padding: 0 0 0 8px;
      }
      .el-menu-item {
        background-color: transparent;
        color: #8b95a5;
        height: 40px;
        line-height: 40px;
        font-size: 13px;

        &:hover {
          background-color: rgba(255, 255, 255, 0.06);
          color: #e0e4ea;
        }
        &.is-active {
          background: rgba(43, 154, 110, 0.15);
          color: var(--primary-light);
          font-weight: 500;
        }
      }
    }
  }
}

.right-container {
  margin-left: 200px;
  transition: margin-left 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 60px;
  position: sticky;
  top: 0;
  z-index: 999;

  .header-left {
    display: flex;
    align-items: center;
    gap: 20px;

    .collapse-btn {
      font-size: 20px;
      cursor: pointer;
      padding: 6px;
      border-radius: 6px;
      transition: all 0.2s;
      color: var(--text-regular);

      &:hover {
        background: var(--bg-color);
        color: var(--primary-color);
      }
    }

    :deep(.el-breadcrumb) {
      font-size: 14px;

      .el-breadcrumb__inner {
        color: var(--text-secondary);
      }

      .el-breadcrumb__item:last-child .el-breadcrumb__inner {
        color: var(--text-primary);
        font-weight: 500;
      }
    }
  }

  .header-right {
    .el-dropdown-link {
      display: flex;
      align-items: center;
      gap: 10px;
      cursor: pointer;
      padding: 4px 8px 4px 4px;
      border-radius: var(--radius-xl);
      transition: background 0.2s;

      &:hover {
        background: var(--bg-color);
      }

      .el-avatar {
        border: 2px solid var(--primary-lighter);
      }

      .username {
        font-size: 14px;
        font-weight: 500;
        color: var(--text-primary);
      }
    }
  }
}

.main {
  background-color: var(--bg-page);
  padding: 24px;
  overflow-y: auto;
  height: calc(100vh - 60px);
}
</style>


