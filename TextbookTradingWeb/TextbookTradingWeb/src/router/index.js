import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('../views/front/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('../views/front/Home.vue')
      },
      {
        path: 'textbooks',
        name: 'TextbookList',
        component: () => import('../views/front/TextbookList.vue')
      },
      {
        path: 'textbook/:id',
        name: 'TextbookDetail',
        component: () => import('../views/front/TextbookDetail.vue')
      },
      {
        path: 'publish',
        name: 'Publish',
        component: () => import('../views/front/Publish.vue'),
        meta: { requireAuth: true }
      },
      {
        path: 'profile',
        name: 'UserCenter',
        component: () => import('../views/front/UserCenter.vue'),
        meta: { requireAuth: true }
      },
      {
        path: 'orders',
        name: 'MyOrders',
        component: () => import('../views/front/MyOrders.vue'),
        meta: { requireAuth: true }
      },
      {
        path: 'favorites',
        name: 'MyFavorites',
        component: () => import('../views/front/MyFavorites.vue'),
        meta: { requireAuth: true }
      },
      {
        path: 'messages',
        name: 'Messages',
        component: () => import('../views/front/Messages.vue'),
        meta: { requireAuth: true }
      },
      {
        path: 'chat',
        name: 'Chat',
        component: () => import('../views/front/Chat.vue'),
        meta: { requireAuth: true }
      },
      {
        path: 'user/:id',
        name: 'UserProfile',
        component: () => import('../views/front/UserProfile.vue')
      },
      {
        path: 'wanted',
        name: 'WantedList',
        component: () => import('../views/front/WantedList.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/front/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/front/Register.vue')
  },
  {
    path: '/admin',
    component: () => import('../views/admin/Layout.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/admin/Dashboard.vue')
      },
      {
        path: 'users',
        name: 'UserManage',
        component: () => import('../views/admin/UserManage.vue')
      },
      {
        path: 'textbooks',
        name: 'TextbookManage',
        component: () => import('../views/admin/TextbookManage.vue')
      },
      {
        path: 'audit',
        name: 'TextbookAudit',
        component: () => import('../views/admin/TextbookAudit.vue')
      },
      {
        path: 'orders',
        name: 'OrderManage',
        component: () => import('../views/admin/OrderManage.vue')
      },
      {
        path: 'categories',
        name: 'CategoryManage',
        component: () => import('../views/admin/CategoryManage.vue')
      },
      {
        path: 'notices',
        name: 'NoticeManage',
        component: () => import('../views/admin/NoticeManage.vue')
      },
      {
        path: 'banners',
        name: 'BannerManage',
        component: () => import('../views/admin/BannerManage.vue')
      },
      {
        path: 'roles',
        name: 'RoleManage',
        component: () => import('../views/admin/RoleManage.vue')
      },
      {
        path: 'menus',
        name: 'MenuManage',
        component: () => import('../views/admin/MenuManage.vue')
      },
      {
        path: 'admins',
        name: 'AdminManage',
        component: () => import('../views/admin/AdminManage.vue')
      },
      {
        path: 'campus',
        name: 'CampusManage',
        component: () => import('../views/admin/CampusManage.vue')
      },
      {
        path: 'courses',
        name: 'CourseManage',
        component: () => import('../views/admin/CourseManage.vue')
      },
      {
        path: 'trading-points',
        name: 'TradingPointManage',
        component: () => import('../views/admin/TradingPointManage.vue')
      },
      {
        path: 'credit',
        name: 'CreditManage',
        component: () => import('../views/admin/CreditManage.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const adminToken = localStorage.getItem('adminToken')

  // 处理标题
  if (to.name) {
    document.title = `${to.name} - 二手教材交易系统`
  }

  // 管理员后台路由守卫
  if (to.path.startsWith('/admin')) {
    if (adminToken) {
      next()
    } else {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    }
    return
  }

  // 前台需要登录权限
  if (to.meta.requireAuth) {
    if (token) {
      next()
    } else {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    }
  } else {
    next()
  }
})

export default router


