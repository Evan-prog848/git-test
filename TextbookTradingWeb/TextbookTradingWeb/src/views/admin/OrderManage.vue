<template>
  <div class="order-manage">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchQuery">
        <el-form-item label="订单号">
          <el-input v-model="searchQuery.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchQuery.status" placeholder="全部" clearable style="width: 150px">
            <el-option label="待确认" :value="0" />
            <el-option label="已确认" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已取消" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="orderList" v-loading="loading" style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="textbookTitle" label="教材名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="price" label="交易价格" width="100">
          <template #default="scope">
            <span class="price">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sellerNickname" label="卖家" width="120" />
        <el-table-column prop="buyerNickname" label="买家" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getOrderStatusType(scope.row.status)">
              {{ getOrderStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="180" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="scope">
            <div class="action-buttons">
              <el-button size="small" link type="primary" @click="handleDetail(scope.row)">详情</el-button>
              <el-button
                v-if="scope.row.status === 0 || scope.row.status === 1"
                size="small"
                link
                type="danger"
                @click="handleCancel(scope.row)"
              >
                取消
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="searchQuery.pageNum"
          v-model:page-size="searchQuery.pageSize"
          :total="total"
          layout="total, prev, pager, next, jumper"
          @current-change="fetchOrders"
        />
      </div>
    </el-card>

    <!-- 订单详情弹窗 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="650px">
      <div class="order-detail" v-if="currentOrder">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getOrderStatusType(currentOrder.status)">{{ getOrderStatusText(currentOrder.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="教材名称">{{ currentOrder.textbookTitle }}</el-descriptions-item>
          <el-descriptions-item label="交易价格">
            <span class="price">¥{{ currentOrder.price }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="卖家">{{ currentOrder.sellerNickname || '-' }}</el-descriptions-item>
          <el-descriptions-item label="买家">{{ currentOrder.buyerNickname || '-' }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ currentOrder.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ currentOrder.updateTime || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <div class="contact-info">
          <h4>联系信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="卖家手机">{{ currentOrder.sellerPhone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="买家手机">{{ currentOrder.buyerPhone || '-' }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="remark-section" v-if="currentOrder.remark">
          <h4>备注</h4>
          <p>{{ currentOrder.remark }}</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderManageList } from '@/api/admin'

const loading = ref(false)
const total = ref(0)
const orderList = ref([])
const detailVisible = ref(false)
const currentOrder = ref(null)

const searchQuery = reactive({
  pageNum: 1,
  pageSize: 10,
  orderNo: '',
  status: undefined
})

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
    const res = await getOrderManageList(searchQuery)
    orderList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  searchQuery.pageNum = 1
  fetchOrders()
}

const resetSearch = () => {
  searchQuery.orderNo = ''
  searchQuery.status = undefined
  handleSearch()
}

const handleDetail = (row) => {
  currentOrder.value = row
  detailVisible.value = true
}

const handleCancel = (row) => {
  ElMessageBox.confirm('确定要强制取消该订单吗？', '警示', { type: 'warning' }).then(() => {
    ElMessage.success('订单已强制取消')
    row.status = 3
  })
}

onMounted(fetchOrders)
</script>

<style scoped lang="scss">
.search-card { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
.price { color: var(--accent-color); font-weight: bold; }
.action-buttons { display: flex; align-items: center; gap: 8px; }

.order-detail {
  .contact-info, .remark-section {
    h4 {
      margin: 0 0 12px 0;
      font-size: 14px;
      color: var(--text-primary);
    }
    p {
      margin: 0;
      color: var(--text-regular);
    }
  }
  .remark-section {
    margin-top: 16px;
  }
}
</style>


