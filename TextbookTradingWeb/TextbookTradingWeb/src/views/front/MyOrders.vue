<template>
  <div class="container my-orders-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span class="title">我的订单</span>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane name="buy">
          <template #label>
            <span>我买到的</span>
          </template>
          <el-table :data="buyOrders" v-loading="loading">
            <el-table-column label="订单信息" width="400">
              <template #default="scope">
                <div class="order-book-info">
                  <el-image :src="scope.row.textbookCover" class="book-cover" />
                  <div class="book-detail">
                    <div class="book-name">{{ scope.row.textbookTitle }}</div>
                    <div class="order-no">订单号：{{ scope.row.orderNo }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="price" label="实付款" width="120">
              <template #default="scope">
                <span class="price">¥{{ scope.row.price }}</span>
              </template>
            </el-table-column>
            <el-table-column label="卖家" width="150">
              <template #default="scope">
                <div class="seller-info">
                  <el-avatar :size="24" :src="scope.row.sellerAvatar" />
                  <span>{{ scope.row.sellerName }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120">
              <template #default="scope">
                <el-tag :type="getOrderStatusType(scope.row.status)">
                  {{ getOrderStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button
                  v-if="scope.row.status === 1"
                  type="primary"
                  size="small"
                  @click="handleConfirmReceipt(scope.row)"
                >
                  确认收货
                </el-button>
                <el-button
                  v-if="scope.row.status === 0"
                  type="danger"
                  size="small"
                  @click="handleCancelOrder(scope.row)"
                >
                  取消订单
                </el-button>
                <el-button
                  v-if="scope.row.status === 2"
                  type="success"
                  size="small"
                  @click="handleReview(scope.row)"
                >
                  评价
                </el-button>
                <el-button size="small" @click="viewDetail(scope.row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane name="sell">
          <template #label>
            <el-badge :value="pendingCount" :hidden="pendingCount === 0" type="danger">
              <span>我卖出的</span>
            </el-badge>
          </template>
          <el-table :data="sellOrders" v-loading="loading">
            <!-- 结构类似，操作不同 -->
            <el-table-column label="订单信息" width="400">
              <template #default="scope">
                <div class="order-book-info">
                  <el-image :src="scope.row.textbookCover" class="book-cover" />
                  <div class="book-detail">
                    <div class="book-name">{{ scope.row.textbookTitle }}</div>
                    <div class="order-no">订单号：{{ scope.row.orderNo }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="price" label="应收款" width="120">
              <template #default="scope">
                <span class="price">¥{{ scope.row.price }}</span>
              </template>
            </el-table-column>
            <el-table-column label="买家" width="150">
              <template #default="scope">
                <div class="buyer-info">
                  <el-avatar :size="24" :src="scope.row.buyerAvatar" />
                  <span>{{ scope.row.buyerName }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120">
              <template #default="scope">
                <el-tag :type="getOrderStatusType(scope.row.status)">
                  {{ getOrderStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button
                  v-if="scope.row.status === 0"
                  type="primary"
                  size="small"
                  @click="handleConfirmOrder(scope.row)"
                >
                  确认订单
                </el-button>
                <el-button
                  v-if="scope.row.status === 0"
                  type="danger"
                  size="small"
                  @click="handleRejectOrder(scope.row)"
                >
                  拒绝
                </el-button>
                <el-button size="small" @click="viewDetail(scope.row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="600px">
      <div v-loading="detailLoading" class="order-detail">
        <div v-if="orderDetail" class="detail-content">
          <!-- 订单状态 -->
          <div class="detail-header">
            <el-tag :type="getOrderStatusType(orderDetail.status)" size="large">
              {{ orderDetail.statusText }}
            </el-tag>
            <span class="order-no">订单号：{{ orderDetail.orderNo }}</span>
          </div>

          <!-- 教材信息 -->
          <el-divider content-position="left">教材信息</el-divider>
          <div class="book-info">
            <el-image :src="orderDetail.textbookCover" class="detail-cover" fit="cover" />
            <div class="book-meta">
              <div class="book-title">{{ orderDetail.textbookTitle }}</div>
              <div class="book-price">¥{{ orderDetail.price }}</div>
            </div>
          </div>

          <!-- 交易双方 -->
          <el-divider content-position="left">交易双方</el-divider>
          <div class="trade-parties">
            <div class="party">
              <span class="label">卖家：</span>
              <el-avatar :size="24" :src="orderDetail.sellerAvatar" />
              <span class="name">{{ orderDetail.sellerName }}</span>
            </div>
            <div class="party">
              <span class="label">买家：</span>
              <el-avatar :size="24" :src="orderDetail.buyerAvatar" />
              <span class="name">{{ orderDetail.buyerName }}</span>
            </div>
          </div>

          <!-- 交易信息 -->
          <el-divider content-position="left">交易信息</el-divider>
          <div class="trade-info">
            <div class="info-item">
              <span class="label">交易地点：</span>
              <span class="value">{{ orderDetail.tradingPointName || '未指定' }}</span>
            </div>
            <div class="info-item">
              <span class="label">见面时间：</span>
              <span class="value">{{ orderDetail.meetingTime ? formatTime(orderDetail.meetingTime) : '未指定' }}</span>
            </div>
          </div>

          <!-- 订单备注 -->
          <el-divider content-position="left">订单备注</el-divider>
          <div class="remark">{{ orderDetail.remark || '无' }}</div>

          <!-- 时间信息 -->
          <el-divider content-position="left">时间信息</el-divider>
          <div class="time-info">
            <div>创建时间：{{ formatTime(orderDetail.createTime) }}</div>
            <div>更新时间：{{ formatTime(orderDetail.updateTime) }}</div>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="danger" @click="openReportDialog" v-if="orderDetail && orderDetail.status >= 1">
          举报
        </el-button>
      </template>
    </el-dialog>

    <!-- 举报对话框 -->
    <el-dialog v-model="reportVisible" title="举报用户" width="550px">
      <el-form :model="reportForm" label-width="100px">
        <el-form-item label="举报类型">
          <el-select v-model="reportForm.type" placeholder="请选择举报类型" style="width: 100%">
            <el-option label="实物与描述不符" :value="1" />
            <el-option label="爽约（未按时见面）" :value="2" />
            <el-option label="态度恶劣" :value="3" />
            <el-option label="其他违规行为" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="举报描述">
          <el-input
            type="textarea"
            v-model="reportForm.description"
            placeholder="请详细描述问题"
            rows="3"
          />
        </el-form-item>
        <el-form-item label="证据图片">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :file-list="reportForm.evidenceList"
            :on-change="handleEvidenceChange"
            :on-remove="handleEvidenceRemove"
            :limit="3"
            accept="image/*"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">最多上传3张证据图片</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reportVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReport" :loading="reportLoading">提交举报</el-button>
      </template>
    </el-dialog>

    <!-- 评价对话框 -->
    <el-dialog v-model="reviewVisible" title="交易评价" width="400px">
      <el-form :model="reviewForm" label-width="80px">
        <el-form-item label="评分">
          <el-rate v-model="reviewForm.score" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input type="textarea" v-model="reviewForm.content" rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getBuyerOrders, getSellerOrders, confirmOrder, rejectOrder, completeOrder, cancelOrder, getOrderDetail } from '@/api/order'
import { reportUser } from '@/api/common'
import request from '@/utils/request'

const loading = ref(false)
const activeTab = ref('buy')
const buyOrders = ref([])
const sellOrders = ref([])
const pendingCount = ref(0)

const reviewVisible = ref(false)
const reviewForm = ref({
  orderId: null,
  score: 5,
  content: ''
})

// 订单详情相关
const detailVisible = ref(false)
const detailLoading = ref(false)
const orderDetail = ref(null)

// 举报相关
const reportVisible = ref(false)
const reportLoading = ref(false)
const reportForm = ref({
  type: null,
  description: '',
  evidenceList: [],
  evidenceImages: []
})

// 证据图片变化
const handleEvidenceChange = (file, fileList) => {
  reportForm.value.evidenceList = fileList
}

// 证据图片移除
const handleEvidenceRemove = (file, fileList) => {
  reportForm.value.evidenceList = fileList
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

const getOrderStatusText = (status) => {
  const texts = ['待确认', '已确认', '已完成', '已取消']
  return texts[status]
}

const getOrderStatusType = (status) => {
  const types = ['', 'warning', 'success', 'info']
  return types[status]
}

const fetchOrders = async () => {
  loading.value = true
  try {
    if (activeTab.value === 'buy') {
      const res = await getBuyerOrders({ pageNum: 1, pageSize: 100 })
      buyOrders.value = res.data.records
    } else {
      const res = await getSellerOrders({ pageNum: 1, pageSize: 100 })
      sellOrders.value = res.data.records
      // 计算待确认订单数量
      pendingCount.value = sellOrders.value.filter(order => order.status === 0).length
    }
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const handleTabChange = (tab) => {
  fetchOrders()
}

const handleConfirmReceipt = (row) => {
  ElMessageBox.confirm('确认已收到教材并完成交易吗？', '提示').then(async () => {
    await completeOrder(row.id)
    ElMessage.success('交易已完成')
    fetchOrders()
  })
}

const handleCancelOrder = (row) => {
  ElMessageBox.confirm('确定取消该订单吗？', '提示', { type: 'warning' }).then(async () => {
    await cancelOrder(row.id)
    ElMessage.success('订单已取消')
    fetchOrders()
  })
}

const handleReview = (row) => {
  reviewForm.value.orderId = row.id
  reviewVisible.value = true
}

const submitReview = () => {
  ElMessage.success('评价成功')
  reviewVisible.value = false
}

const handleConfirmOrder = (row) => {
  ElMessageBox.confirm('确认接受该购买申请吗？', '提示').then(async () => {
    await confirmOrder(row.id)
    ElMessage.success('已确认订单')
    fetchOrders()
  })
}

const handleRejectOrder = (row) => {
  ElMessageBox.confirm('确定拒绝该购买申请吗？', '提示', { type: 'warning' }).then(async () => {
    await rejectOrder(row.id)
    ElMessage.success('已拒绝申请')
    fetchOrders()
  })
}

const viewDetail = async (row) => {
  detailVisible.value = true
  detailLoading.value = true
  orderDetail.value = null
  try {
    const res = await getOrderDetail(row.id)
    orderDetail.value = res.data
  } catch (error) {
    ElMessage.error('获取订单详情失败')
  } finally {
    detailLoading.value = false
  }
}

onMounted(() => {
  fetchOrders()
  // 同时加载卖家订单以显示待处理数量
  loadSellerOrdersCount()
})

// 加载卖家订单数量（用于显示徽章）
const loadSellerOrdersCount = async () => {
  try {
    const res = await getSellerOrders({ pageNum: 1, pageSize: 100 })
    const orders = res.data.records || []
    pendingCount.value = orders.filter(order => order.status === 0).length
  } catch (error) {
    console.error('Failed to load seller orders count:', error)
  }
}

// 打开举报对话框
const openReportDialog = () => {
  reportForm.value = { type: null, description: '', evidenceList: [], evidenceImages: [] }
  reportVisible.value = true
}

// 提交举报
const submitReport = async () => {
  if (!reportForm.value.type) {
    ElMessage.warning('请选择举报类型')
    return
  }
  if (!reportForm.value.description.trim()) {
    ElMessage.warning('请填写举报描述')
    return
  }
  
  // 确定被举报人（买家举报卖家，卖家举报买家）
  const reportedUserId = activeTab.value === 'buy' 
    ? orderDetail.value.sellerId 
    : orderDetail.value.buyerId
  
  reportLoading.value = true
  try {
    // 1. 先上传证据图片
    let evidenceImages = []
    if (reportForm.value.evidenceList.length > 0) {
      const formData = new FormData()
      reportForm.value.evidenceList.forEach(item => {
        if (item.raw) formData.append('files', item.raw)
      })
      
      if (formData.has('files')) {
        const uploadRes = await request.post('/file/uploads', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        })
        evidenceImages = uploadRes.data || []
      }
    }
    
    // 2. 提交举报
    await reportUser({
      userId: reportedUserId,
      type: reportForm.value.type,
      description: reportForm.value.description,
      orderId: orderDetail.value.id,
      evidenceImages: evidenceImages
    })
    ElMessage.success('举报已提交，管理员将尽快处理')
    reportVisible.value = false
  } catch (error) {
    ElMessage.error('举报提交失败')
  } finally {
    reportLoading.value = false
  }
}
</script>

<style scoped lang="scss">
.my-orders-page {
  padding: 20px 0;

  :deep(.el-card) {
    border-radius: var(--radius-md);
    border: 1px solid var(--border-light);
    box-shadow: var(--shadow-sm);
  }

  :deep(.el-tabs__nav-wrap::after) {
    height: 1px;
    background-color: var(--border-light);
  }
}

.card-header {
  .title {
    font-size: 18px;
    font-weight: 700;
    color: var(--text-primary);
  }
}

.order-book-info {
  display: flex;
  align-items: center;
  .book-cover {
    width: 60px;
    height: 80px;
    margin-right: 15px;
    border-radius: var(--radius-sm);
    object-fit: cover;
  }
  .book-detail {
    .book-name {
      font-weight: 600;
      margin-bottom: 5px;
      color: var(--text-primary);
    }
    .order-no {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.price {
  color: var(--accent-color);
  font-weight: 700;
}

.seller-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.buyer-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.order-detail {
  min-height: 200px;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 15px;
  .order-no {
    color: var(--text-secondary);
    font-size: 14px;
  }
}

.book-info {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  .detail-cover {
    width: 80px;
    height: 100px;
    border-radius: var(--radius-sm);
    flex-shrink: 0;
  }
  .book-meta {
    .book-title {
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 10px;
      color: var(--text-primary);
    }
    .book-price {
      color: var(--accent-color);
      font-size: 18px;
      font-weight: 700;
    }
  }
}

.trade-parties {
  .party {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
    .label {
      color: var(--text-secondary);
      width: 50px;
    }
    .name {
      font-weight: 500;
      color: var(--text-primary);
    }
  }
}

.remark {
  color: var(--text-regular);
  line-height: 1.6;
}

.time-info {
  color: var(--text-secondary);
  font-size: 14px;
  div {
    margin-bottom: 5px;
  }
}

.upload-tip {
  font-size: 12px;
  color: var(--text-secondary);
  margin-top: 5px;
}
</style>


