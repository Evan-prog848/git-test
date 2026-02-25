<template>
  <div class="credit-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>信用举报管理</span>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="待处理举报" name="pending">
          <el-table :data="pendingList" v-loading="loading" stripe>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="reporterName" label="举报人" width="120" />
            <el-table-column prop="reportedName" label="被举报人" width="120" />
            <el-table-column prop="type" label="举报类型" width="120">
              <template #default="{ row }">
                <el-tag :type="getTypeTagType(row.type)">{{ getTypeName(row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="举报描述" show-overflow-tooltip />
            <el-table-column prop="createTime" label="举报时间" width="180" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleView(row)">查看详情</el-button>
                <el-button type="success" link @click="handleApprove(row)">通过</el-button>
                <el-button type="danger" link @click="handleReject(row)">驳回</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :total="total"
            layout="total, prev, pager, next"
            @current-change="fetchPendingList"
            style="margin-top: 16px; justify-content: flex-end;"
          />
        </el-tab-pane>

        <el-tab-pane label="已处理举报" name="processed">
          <el-table :data="processedList" v-loading="processedLoading" stripe>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="reporterName" label="举报人" width="120" />
            <el-table-column prop="reportedName" label="被举报人" width="120" />
            <el-table-column prop="type" label="举报类型" width="120">
              <template #default="{ row }">
                <el-tag :type="getTypeTagType(row.type)">{{ getTypeName(row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="举报描述" show-overflow-tooltip />
            <el-table-column prop="status" label="处理结果" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'">
                  {{ row.status === 1 ? '已通过' : '已驳回' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="举报时间" width="160" />
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleView(row)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            v-model:current-page="processedPageNum"
            v-model:page-size="processedPageSize"
            :total="processedTotal"
            layout="total, prev, pager, next"
            @current-change="fetchProcessedList"
            style="margin-top: 16px; justify-content: flex-end;"
          />
        </el-tab-pane>

        <el-tab-pane label="查询用户信用" name="query">
          <el-row :gutter="16" style="margin-bottom: 16px;">
            <el-col :span="8">
              <el-input v-model="queryUserId" placeholder="输入用户ID" clearable>
                <template #append>
                  <el-button @click="queryUserCredit">查询</el-button>
                </template>
              </el-input>
            </el-col>
          </el-row>

          <div v-if="userCreditInfo" class="credit-info">
            <el-descriptions :column="3" border>
              <el-descriptions-item label="用户ID">{{ userCreditInfo.userId }}</el-descriptions-item>
              <el-descriptions-item label="当前信用分">
                <span :class="getCreditClass(userCreditInfo.score)">{{ userCreditInfo.score }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="信用等级">
                <el-tag :type="getCreditLevelType(userCreditInfo.score)">
                  {{ getCreditLevel(userCreditInfo.score) }}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>

            <h4 style="margin: 20px 0 10px;">信用记录</h4>
            <el-table :data="userCreditRecords" stripe max-height="400">
              <el-table-column prop="type" label="类型" width="120">
                <template #default="{ row }">
                  <el-tag :type="getTypeTagType(row.type)">{{ getTypeName(row.type) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="scoreChange" label="分数变化" width="100">
                <template #default="{ row }">
                  <span :class="row.scoreChange > 0 ? 'text-success' : 'text-danger'">
                    {{ row.scoreChange > 0 ? '+' : '' }}{{ row.scoreChange }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="描述" show-overflow-tooltip />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getStatusType(row.status)">{{ getStatusName(row.status) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="时间" width="180" />
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="举报详情" width="600px">
      <el-descriptions :column="2" border v-if="currentReport">
        <el-descriptions-item label="举报人">{{ currentReport.reporterName }}</el-descriptions-item>
        <el-descriptions-item label="被举报人">{{ currentReport.reportedName }}</el-descriptions-item>
        <el-descriptions-item label="举报类型">{{ getTypeName(currentReport.type) }}</el-descriptions-item>
        <el-descriptions-item label="关联订单">{{ currentReport.orderId || '无' }}</el-descriptions-item>
        <el-descriptions-item label="举报描述" :span="2">{{ currentReport.description }}</el-descriptions-item>
        <el-descriptions-item label="举报时间" :span="2">{{ currentReport.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div v-if="currentReport?.evidenceImages?.length" style="margin-top: 16px;">
        <h4>证据图片</h4>
        <el-image
          v-for="(img, index) in currentReport.evidenceImages"
          :key="index"
          :src="img"
          :preview-src-list="currentReport.evidenceImages"
          fit="cover"
          style="width: 100px; height: 100px; margin-right: 10px;"
        />
      </div>
    </el-dialog>

    <!-- 处理对话框 -->
    <el-dialog v-model="handleVisible" :title="handleApproved ? '通过举报' : '驳回举报'" width="400px">
      <el-form :model="handleForm" label-width="80px">
        <el-form-item label="处理备注">
          <el-input v-model="handleForm.remark" type="textarea" :rows="3" placeholder="请输入处理备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button :type="handleApproved ? 'success' : 'danger'" @click="submitHandle" :loading="submitLoading">
          确定{{ handleApproved ? '通过' : '驳回' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const activeTab = ref('pending')
const loading = ref(false)
const submitLoading = ref(false)
const pendingList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const queryUserId = ref('')
const userCreditInfo = ref(null)
const userCreditRecords = ref([])

// 已处理举报相关
const processedList = ref([])
const processedLoading = ref(false)
const processedPageNum = ref(1)
const processedPageSize = ref(10)
const processedTotal = ref(0)

const detailVisible = ref(false)
const currentReport = ref(null)

const handleVisible = ref(false)
const handleApproved = ref(true)
const handleForm = reactive({ remark: '' })

const typeMap = {
  1: '实物与描述不符',
  2: '爽约',
  3: '态度恶劣',
  4: '其他违规'
}

const getTypeName = (type) => typeMap[type] || '未知'

const getTypeTagType = (type) => {
  // 举报类型都用danger
  return 'danger'
}

const getStatusName = (status) => {
  const map = { 0: '待处理', 1: '已通过', 2: '已驳回' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'info' }
  return map[status] || 'info'
}

const getCreditLevel = (score) => {
  if (score >= 90) return '优秀'
  if (score >= 70) return '良好'
  if (score >= 50) return '一般'
  return '较差'
}

const getCreditLevelType = (score) => {
  if (score >= 90) return 'success'
  if (score >= 70) return ''
  if (score >= 50) return 'warning'
  return 'danger'
}

const getCreditClass = (score) => {
  if (score >= 70) return 'text-success'
  if (score >= 50) return 'text-warning'
  return 'text-danger'
}

const fetchPendingList = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/credit/pending', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value }
    })
    pendingList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取待处理举报失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取已处理举报列表
const fetchProcessedList = async () => {
  processedLoading.value = true
  try {
    const res = await request.get('/admin/credit/processed', {
      params: { pageNum: processedPageNum.value, pageSize: processedPageSize.value }
    })
    processedList.value = res.data.records || []
    processedTotal.value = res.data.total || 0
  } catch (error) {
    console.error('获取已处理举报失败:', error)
  } finally {
    processedLoading.value = false
  }
}

const queryUserCredit = async () => {
  if (!queryUserId.value) {
    ElMessage.warning('请输入用户ID')
    return
  }
  try {
    const scoreRes = await request.get(`/admin/credit/score/${queryUserId.value}`)
    const recordsRes = await request.get(`/admin/credit/user/${queryUserId.value}`)
    
    userCreditInfo.value = {
      userId: queryUserId.value,
      score: scoreRes.data || 100
    }
    userCreditRecords.value = recordsRes.data.records || []
  } catch (error) {
    ElMessage.error('查询失败')
  }
}

const handleView = (row) => {
  currentReport.value = row
  detailVisible.value = true
}

const handleApprove = (row) => {
  currentReport.value = row
  handleApproved.value = true
  handleForm.remark = ''
  handleVisible.value = true
}

const handleReject = (row) => {
  currentReport.value = row
  handleApproved.value = false
  handleForm.remark = ''
  handleVisible.value = true
}

const submitHandle = async () => {
  submitLoading.value = true
  try {
    await request.post(`/admin/credit/handle/${currentReport.value.id}`, null, {
      params: { approved: handleApproved.value, remark: handleForm.remark }
    })
    ElMessage.success('处理成功')
    handleVisible.value = false
    fetchPendingList()
  } catch (error) {
    ElMessage.error('处理失败')
  } finally {
    submitLoading.value = false
  }
}

const handleTabChange = (tab) => {
  if (tab === 'pending') {
    fetchPendingList()
  } else if (tab === 'processed') {
    fetchProcessedList()
  }
}

onMounted(() => {
  fetchPendingList()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.credit-info {
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
}

.text-success {
  color: var(--success-color);
  font-weight: bold;
}

.text-warning {
  color: var(--warning-color);
  font-weight: bold;
}

.text-danger {
  color: var(--danger-color);
  font-weight: bold;
}
</style>
