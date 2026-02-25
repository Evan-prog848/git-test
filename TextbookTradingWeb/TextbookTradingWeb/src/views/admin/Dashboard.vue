<template>
  <div class="dashboard">
    <!-- 核心数据统计 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6" v-for="item in stats" :key="item.title">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-content">
            <div class="stat-left">
              <div class="stat-icon" :style="{ backgroundColor: item.color }">
                <el-icon :size="28"><component :is="item.icon" /></el-icon>
              </div>
            </div>
            <div class="stat-right">
              <div class="stat-title">{{ item.title }}</div>
              <div class="stat-number">{{ item.number }}</div>
              <div class="stat-trend" :class="item.trend > 0 ? 'up' : 'down'" v-if="item.trend !== undefined">
                <el-icon><component :is="item.trend > 0 ? 'CaretTop' : 'CaretBottom'" /></el-icon>
                <span>{{ Math.abs(item.trend) }}%</span>
                <span class="trend-text">较昨日</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>交易趋势</span>
              <el-radio-group v-model="trendPeriod" size="small">
                <el-radio-button label="week">近7天</el-radio-button>
                <el-radio-button label="month">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-container">
            <div class="chart-placeholder">
              <div v-for="(value, index) in chartData" :key="index" class="bar-wrapper">
                <div class="bar-area">
                  <el-tooltip :content="'¥' + (chartRawValues[index] || 0)" placement="top">
                    <div class="bar" :style="{ height: value + '%' }"></div>
                  </el-tooltip>
                </div>
                <div class="bar-label">{{ chartLabels[index] || (index + 1) }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card shadow="never">
          <template #header>
            <span>教材分类占比</span>
          </template>
          <div class="pie-container">
            <div ref="pieChartRef" class="pie-chart-echarts"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 待处理事项和最近活动 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>待处理事项</span>
              <el-badge :value="pendingCount" class="item" type="danger" />
            </div>
          </template>
          <div class="todo-list">
            <div class="todo-item" v-for="item in todoList" :key="item.id">
              <span class="dot" :class="item.type"></span>
              <span class="text">{{ item.text }}</span>
              <el-button type="primary" link @click="handleTodo(item)">{{ item.action }}</el-button>
            </div>
            <el-empty v-if="todoList.length === 0" description="暂无待处理事项" :image-size="80" />
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <span>最近活动</span>
          </template>
          <el-timeline class="activity-timeline">
            <el-timeline-item
              v-for="activity in recentActivities"
              :key="activity.id"
              :timestamp="activity.time"
              :color="activity.color"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <el-row :gutter="20" class="quick-actions">
      <el-col :span="24">
        <el-card shadow="never">
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="action-buttons">
            <el-button type="primary" :icon="Plus" @click="$router.push('/admin/textbooks')">发布教材</el-button>
            <el-button type="success" :icon="Check" @click="$router.push('/admin/audit')">审核教材</el-button>
            <el-button type="warning" :icon="User" @click="$router.push('/admin/users')">用户管理</el-button>
            <el-button type="info" :icon="Setting" @click="$router.push('/admin/banners')">轮播图管理</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch, shallowRef } from 'vue'
import * as echarts from 'echarts'
import { useRouter } from 'vue-router'
import { User, Reading, List, Money, CaretTop, CaretBottom, Plus, Check, Setting } from '@element-plus/icons-vue'
import { getStatistics } from '@/api/admin'

const router = useRouter()
const trendPeriod = ref('week')

const stats = ref([
  { title: '总用户数', number: '0', icon: User, color: '#2b9a6e', key: 'userCount', trend: 0 },
  { title: '在售教材', number: '0', icon: Reading, color: '#45b88f', key: 'textbookCount', trend: 0 },
  { title: '累计订单', number: '0', icon: List, color: '#f0883e', key: 'orderCount', trend: 0 },
  { title: '今日交易额', number: '¥0', icon: Money, color: '#e85d5d', key: 'todayAmount', trend: 0 }
])

const chartData = ref([])
const chartLabels = ref([])
const chartRawValues = ref([])

const categoryData = ref([])
const pieChartRef = ref(null)
const pieChartInstance = shallowRef(null)

const todoList = ref([])

const recentActivities = ref([])

const pendingCount = computed(() => todoList.value.length)

const pieGradient = computed(() => {
  if (!categoryData.value || categoryData.value.length === 0) {
    return 'conic-gradient(#e0e0e0 0% 100%)'
  }
  let gradientParts = []
  let currentPercent = 0
  categoryData.value.forEach(item => {
    const startPercent = currentPercent
    const endPercent = currentPercent + item.value
    gradientParts.push(`${item.color} ${startPercent}% ${endPercent}%`)
    currentPercent = endPercent
  })
  return `conic-gradient(${gradientParts.join(', ')})`
})

const fetchStats = async (period = 'week') => {
  try {
    const days = period === 'month' ? 30 : 7
    const res = await getStatistics({ days })
    const data = res.data
    
    // 更新核心统计
    stats.value.forEach(item => {
      if (item.key === 'todayAmount') {
        item.number = `¥${data[item.key] || 0}`
      } else {
        item.number = data[item.key] || 0
      }
      
      // 模拟趋势数据
      if (item.key === 'userCount') item.trend = data.todayUserCount > 0 ? 5 : 0
      if (item.key === 'textbookCount') item.trend = data.todayTextbookCount > 0 ? 8 : 0
      if (item.key === 'orderCount') item.trend = data.todayOrderCount > 0 ? 12 : 0
      if (item.key === 'todayAmount') item.trend = 1
    })

    // 更新趋势图表
    if (data.trendData && data.trendData.length > 0) {
      const values = data.trendData.map(t => parseFloat(t.value) || 0)
      const maxValue = Math.max(...values, 1)
      chartRawValues.value = values
      chartData.value = values.map(v => Math.max((v / maxValue) * 100, v > 0 ? 10 : 2))
      chartLabels.value = data.trendData.map(t => {
        const date = new Date(t.date)
        return `${date.getMonth() + 1}/${date.getDate()}`
      })
    }

    // 更新分类占比
    if (data.categoryStats) {
      const colors = ['#2b9a6e', '#45b88f', '#f0883e', '#e85d5d', '#8b95a5']
      categoryData.value = data.categoryStats.map((item, index) => ({
        ...item,
        color: colors[index % colors.length]
      }))
    }

    // 更新待处理事项
    todoList.value = []
    if (data.pendingAuditCount > 0) {
      todoList.value.push({
        id: 'audit',
        text: `有 ${data.pendingAuditCount} 个新教材待审核`,
        type: 'warning',
        action: '立即审核',
        route: '/admin/audit'
      })
    }

    // 更新最近活动
    if (data.recentActivities) {
      recentActivities.value = data.recentActivities.map((a, index) => ({
        id: index,
        ...a
      }))
    }
  } catch (error) {
    console.error('Failed to fetch statistics:', error)
  }
}

const handleTodo = (item) => {
  if (item.route) {
    router.push(item.route)
  }
}

const initPieChart = () => {
  if (!pieChartRef.value) return
  if (pieChartInstance.value) {
    pieChartInstance.value.dispose()
  }
  pieChartInstance.value = echarts.init(pieChartRef.value)
  updatePieChart()
}

const updatePieChart = () => {
  if (!pieChartInstance.value || categoryData.value.length === 0) return
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      itemWidth: 12,
      itemHeight: 12,
      textStyle: { fontSize: 12 }
    },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['35%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 4,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      emphasis: {
        label: { show: true, fontSize: 14, fontWeight: 'bold' }
      },
      labelLine: { show: false },
      data: categoryData.value.map(item => ({
        name: item.name,
        value: item.value,
        itemStyle: { color: item.color }
      }))
    }]
  }
  pieChartInstance.value.setOption(option)
}

watch(trendPeriod, (newVal) => {
  fetchStats(newVal)
})

watch(categoryData, () => {
  updatePieChart()
}, { deep: true })

onMounted(() => {
  fetchStats(trendPeriod.value)
  setTimeout(initPieChart, 100)
})
</script>

<style scoped lang="scss">
.dashboard {
  .stats-row {
    margin-bottom: 24px;
  }

  :deep(.el-card) {
    border-radius: var(--radius-md);
    border: 1px solid var(--border-light);
    box-shadow: var(--shadow-sm);

    .el-card__header {
      font-weight: 600;
      color: var(--text-primary);
      border-bottom: 1px solid var(--border-light);
    }
  }
}

.stat-card {
  border-radius: var(--radius-md) !important;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: var(--shadow-lg);
  }
  
  .stat-content {
    display: flex;
    align-items: center;
    gap: 16px;
    
    .stat-left {
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 14px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
      }
    }
    
    .stat-right {
      flex: 1;
      
      .stat-title {
        font-size: 13px;
        color: var(--text-secondary);
        margin-bottom: 6px;
        font-weight: 500;
      }
      
      .stat-number {
        font-size: 26px;
        font-weight: 800;
        color: var(--text-primary);
        margin-bottom: 4px;
        letter-spacing: -0.5px;
      }
      
      .stat-trend {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 12px;
        
        &.up {
          color: var(--success-color);
        }
        
        &.down {
          color: var(--danger-color);
        }
        
        .trend-text {
          color: var(--text-secondary);
          margin-left: 4px;
        }
      }
    }
  }
}

.charts-row {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
}

.chart-placeholder {
  height: 100%;
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  padding: 20px;
  box-sizing: border-box;
  
  .bar-wrapper {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    max-width: 60px;
    height: 100%;
    
    .bar-area {
      flex: 1;
      width: 100%;
      display: flex;
      align-items: flex-end;
      justify-content: center;
      
      .bar {
        width: 70%;
        background: linear-gradient(to top, var(--primary-color), var(--primary-light));
        border-radius: 4px 4px 0 0;
        transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
        min-height: 8px;
        cursor: pointer;
        
        &:hover {
          background: linear-gradient(to top, var(--primary-dark), var(--primary-color));
          transform: scaleX(1.15);
        }
      }
    }
    
    .bar-label {
      margin-top: 8px;
      font-size: 12px;
      color: var(--text-secondary);
      flex-shrink: 0;
    }
  }
}

.pie-container {
  padding: 10px 0;
  
  .pie-chart-echarts {
    width: 100%;
    height: 280px;
  }
}

.todo-list {
  min-height: 200px;
  
  .todo-item {
    display: flex;
    align-items: center;
    padding: 14px 0;
    border-bottom: 1px solid var(--border-light);
    transition: all 0.2s;
    
    &:hover {
      background-color: var(--bg-color);
      margin: 0 -20px;
      padding: 14px 20px;
      border-radius: var(--radius-sm);
    }
    
    &:last-child {
      border-bottom: none;
    }
    
    .dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      margin-right: 14px;
      flex-shrink: 0;
      
      &.warning { background-color: var(--warning-color); }
      &.danger { background-color: var(--danger-color); }
      &.info { background-color: var(--primary-color); }
    }
    
    .text {
      flex: 1;
      font-size: 14px;
      color: var(--text-regular);
    }
  }
}

.activity-timeline {
  padding: 10px 0;
  max-height: 300px;
  overflow-y: auto;
}

.quick-actions {
  margin-top: 24px;
  
  .action-buttons {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
  }
}
</style>


