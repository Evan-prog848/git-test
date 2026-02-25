<template>
  <div class="textbook-audit">
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span>教材发布审核</span>
        </div>
      </template>

      <el-table :data="auditList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="封面" width="100">
          <template #default="scope">
            <el-image :src="scope.row.cover" style="width: 60px; height: 80px; cursor: pointer" fit="cover" @click="viewDetail(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="书名" min-width="200" />
        <el-table-column prop="sellerName" label="发布者" width="120" />
        <el-table-column prop="price" label="售价" width="100">
          <template #default="scope">
            <span class="price">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="handleAudit(scope.row, 1)">通过</el-button>
            <el-button size="small" type="danger" link @click="handleAudit(scope.row, 2)">拒绝</el-button>
            <el-button size="small" link @click="viewDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="教材详情" width="800px">
      <div v-if="currentTextbook" class="textbook-detail">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-images">
              <el-image
                :src="currentTextbook.cover"
                fit="cover"
                class="main-image"
                :preview-src-list="currentTextbook.images || [currentTextbook.cover]"
              />
            </div>
          </el-col>
          <el-col :span="16">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="书名" :span="2">
                <strong>{{ currentTextbook.title }}</strong>
              </el-descriptions-item>
              <el-descriptions-item label="作者">{{ currentTextbook.author || '-' }}</el-descriptions-item>
              <el-descriptions-item label="出版社">{{ currentTextbook.publisher || '-' }}</el-descriptions-item>
              <el-descriptions-item label="ISBN">{{ currentTextbook.isbn || '-' }}</el-descriptions-item>
              <el-descriptions-item label="分类">{{ currentTextbook.categoryName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="售价">
                <span class="price">¥{{ currentTextbook.price }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="原价">
                <span class="original-price">¥{{ currentTextbook.originalPrice }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="成色">
                <el-tag type="success">{{ currentTextbook.conditionLevel }}成新</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="适用课程">{{ currentTextbook.course || '-' }}</el-descriptions-item>
              <el-descriptions-item label="发布者">{{ currentTextbook.sellerName }}</el-descriptions-item>
              <el-descriptions-item label="联系方式">{{ currentTextbook.sellerPhone || '-' }}</el-descriptions-item>
              <el-descriptions-item label="发布时间" :span="2">{{ currentTextbook.createTime }}</el-descriptions-item>
              <el-descriptions-item label="教材描述" :span="2">
                <div class="description">{{ currentTextbook.description || '暂无描述' }}</div>
              </el-descriptions-item>
            </el-descriptions>
          </el-col>
        </el-row>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="danger" @click="handleAuditFromDetail(2)">拒绝</el-button>
        <el-button type="primary" @click="handleAuditFromDetail(1)">通过</el-button>
      </template>
    </el-dialog>

    <!-- 拒绝审核对话框 -->
    <el-dialog v-model="rejectVisible" title="拒绝审核" width="400px">
      <el-form :model="rejectForm">
        <el-form-item label="拒绝原因">
          <el-input type="textarea" v-model="rejectForm.reason" rows="4" placeholder="请输入拒绝原因，将通知发布者" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReject">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getTextbookManageList, auditTextbook } from '@/api/admin'

const loading = ref(false)
const auditList = ref([])
const rejectVisible = ref(false)
const detailVisible = ref(false)
const currentTextbook = ref(null)
const rejectForm = ref({ id: null, reason: '' })

const fetchAuditList = async () => {
  loading.value = true
  try {
    const res = await getTextbookManageList({
      pageNum: 1,
      pageSize: 100,
      auditStatus: 0 // 只查待审核
    })
    auditList.value = res.data.records
  } catch (error) {
  } finally {
    loading.value = false
  }
}

const handleAudit = async (row, status) => {
  if (status === 1) {
    ElMessageBox.confirm('确定审核通过该教材吗？', '提示').then(async () => {
      await auditTextbook({ id: row.id, auditStatus: 1 })
      ElMessage.success('已通过审核')
      fetchAuditList()
    })
  } else {
    rejectForm.value.id = row.id
    rejectForm.value.reason = ''
    rejectVisible.value = true
  }
}

const confirmReject = async () => {
  if (!rejectForm.value.reason) {
    ElMessage.warning('请输入拒绝原因')
    return
  }
  try {
    await auditTextbook({
      id: rejectForm.value.id,
      auditStatus: 2,
      auditRemark: rejectForm.value.reason
    })
    ElMessage.success('已拒绝审核')
    rejectVisible.value = false
    fetchAuditList()
  } catch (error) {}
}

const viewDetail = (row) => {
  currentTextbook.value = row
  detailVisible.value = true
}

const handleAuditFromDetail = (status) => {
  detailVisible.value = false
  if (status === 1) {
    handleAudit(currentTextbook.value, 1)
  } else {
    handleAudit(currentTextbook.value, 2)
  }
}

onMounted(fetchAuditList)
</script>

<style scoped lang="scss">
.price {
  color: var(--accent-color);
  font-weight: bold;
}

.textbook-detail {
  .detail-images {
    .main-image {
      width: 100%;
      height: 300px;
      border-radius: 8px;
      border: 1px solid var(--border-light);
    }
  }

  .el-descriptions {
    margin-top: 20px;

    .price {
      color: var(--accent-color);
      font-size: 20px;
      font-weight: bold;
    }

    .original-price {
      color: var(--text-secondary);
      text-decoration: line-through;
      font-size: 14px;
    }

    .description {
      line-height: 1.8;
      color: var(--text-regular);
      white-space: pre-wrap;
      max-height: 200px;
      overflow-y: auto;
    }
  }
}
</style>


