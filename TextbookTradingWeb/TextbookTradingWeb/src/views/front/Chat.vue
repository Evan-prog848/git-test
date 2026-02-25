<template>
  <div class="chat-container">
    <div class="chat-sidebar">
      <div class="sidebar-header">
        <h3>消息</h3>
      </div>
      <div class="conversation-list">
        <div
          v-for="conv in conversations"
          :key="conv.conversationId"
          class="conversation-item"
          :class="{ active: currentConversation?.conversationId === conv.conversationId }"
          @click="selectConversation(conv)"
        >
          <el-avatar :size="48" :src="conv.otherUserAvatar || defaultAvatar" />
          <div class="conv-info">
            <div class="conv-header">
              <span class="conv-name">{{ conv.otherUserNickname }}</span>
              <span class="conv-time">{{ formatTime(conv.lastMessageTime) }}</span>
            </div>
            <div class="conv-preview">
              <span class="last-message">{{ conv.lastMessageContent }}</span>
              <el-badge v-if="conv.unreadCount > 0" :value="conv.unreadCount" class="unread-badge" />
            </div>
          </div>
        </div>
        <el-empty v-if="conversations.length === 0" description="暂无消息" />
      </div>
    </div>

    <div class="chat-main">
      <template v-if="currentConversation">
        <div class="chat-header">
          <el-avatar :size="40" :src="currentConversation.otherUserAvatar || defaultAvatar" />
          <div class="header-info">
            <span class="header-name">{{ currentConversation.otherUserNickname }}</span>
            <span class="header-status">{{ wsConnected ? '在线' : '离线' }}</span>
          </div>
          <div class="header-actions">
            <el-button type="primary" link @click="viewUserProfile">查看资料</el-button>
          </div>
        </div>

        <div class="chat-messages" ref="messagesContainer">
          <div v-if="loadingMore" class="loading-more">
            <el-icon class="is-loading"><Loading /></el-icon>
            加载中...
          </div>
          <div
            v-for="msg in messages"
            :key="msg.id"
            class="message-item"
            :class="{ 'message-self': msg.isSelf }"
          >
            <el-avatar
              :size="36"
              :src="msg.isSelf ? userAvatar : currentConversation.otherUserAvatar || defaultAvatar"
            />
            <div class="message-content">
              <div class="message-bubble">
                <template v-if="msg.type === 1">{{ msg.content }}</template>
                <el-image v-else :src="msg.content" fit="cover" style="max-width: 200px" />
              </div>
              <div class="message-time">{{ formatMessageTime(msg.createTime) }}</div>
            </div>
          </div>
        </div>

        <div class="chat-input">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="3"
            placeholder="输入消息..."
            @keydown.enter.exact.prevent="sendMessage"
          />
          <div class="input-actions">
            <el-upload
              :show-file-list="false"
              :before-upload="beforeUploadImage"
              accept="image/*"
            >
              <el-button type="primary" link>
                <el-icon><Picture /></el-icon>
              </el-button>
            </el-upload>
            <el-button type="primary" @click="sendMessage" :disabled="!inputMessage.trim()">
              发送
            </el-button>
          </div>
        </div>
      </template>

      <div v-else class="chat-empty">
        <el-empty description="选择一个会话开始聊天" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Loading, Picture } from '@element-plus/icons-vue'
import { getConversations, getChatHistory, markAsRead, sendMessage as sendMessageApi } from '@/api/chat'
import websocket from '@/utils/websocket'
import { useUserStore } from '@/stores/user'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const defaultAvatar = '/default-avatar.png'
const conversations = ref([])
const currentConversation = ref(null)
const messages = ref([])
const inputMessage = ref('')
const messagesContainer = ref(null)
const loadingMore = ref(false)
const wsConnected = ref(false)
const pageNum = ref(1)
const hasMore = ref(true)

const userAvatar = computed(() => userStore.userInfo?.avatar || defaultAvatar)

// 加载会话列表
const loadConversations = async () => {
  try {
    const res = await getConversations()
    if (res.code === 200) {
      conversations.value = res.data || []
      
      // 如果URL中有userId参数，自动选择对应会话
      const targetUserId = route.query.userId
      if (targetUserId) {
        const conv = conversations.value.find(c => c.otherUserId == targetUserId)
        if (conv) {
          selectConversation(conv)
        } else {
          // 创建新会话
          currentConversation.value = {
            conversationId: null,
            otherUserId: parseInt(targetUserId),
            otherUserNickname: route.query.nickname || '用户',
            otherUserAvatar: route.query.avatar || null,
            unreadCount: 0
          }
        }
      }
    }
  } catch (error) {
    console.error('加载会话列表失败:', error)
  }
}

// 选择会话
const selectConversation = async (conv) => {
  currentConversation.value = conv
  messages.value = []
  pageNum.value = 1
  hasMore.value = true
  
  await loadMessages()
  
  // 标记已读
  if (conv.unreadCount > 0) {
    await markAsRead(conv.otherUserId)
    conv.unreadCount = 0
    websocket.markAsRead(conv.otherUserId)
  }
  
  scrollToBottom()
}

// 加载消息历史
const loadMessages = async () => {
  if (!currentConversation.value || loadingMore.value || !hasMore.value) return
  
  loadingMore.value = true
  try {
    const res = await getChatHistory(currentConversation.value.otherUserId, {
      pageNum: pageNum.value,
      pageSize: 20
    })
    if (res.code === 200) {
      const newMessages = res.data.records || []
      if (newMessages.length < 20) {
        hasMore.value = false
      }
      // 旧消息放在前面
      messages.value = [...newMessages.reverse(), ...messages.value]
      pageNum.value++
    }
  } catch (error) {
    console.error('加载消息失败:', error)
  } finally {
    loadingMore.value = false
  }
}

// 发送消息
const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || !currentConversation.value) return
  
  // 先通过WebSocket发送
  if (wsConnected.value) {
    websocket.sendMessage(currentConversation.value.otherUserId, content, 1)
  } else {
    // 降级到HTTP
    try {
      await sendMessageApi({
        toUserId: currentConversation.value.otherUserId,
        content,
        type: 1
      })
      // 手动添加消息到列表
      messages.value.push({
        id: Date.now(),
        content,
        type: 1,
        isSelf: true,
        createTime: new Date().toISOString()
      })
      scrollToBottom()
    } catch (error) {
      ElMessage.error('发送失败')
      return
    }
  }
  
  inputMessage.value = ''
}

// 发送图片
const beforeUploadImage = (file) => {
  // TODO: 上传图片并发送
  ElMessage.info('图片发送功能开发中')
  return false
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).fromNow()
}

const formatMessageTime = (time) => {
  if (!time) return ''
  const d = dayjs(time)
  const now = dayjs()
  if (d.isSame(now, 'day')) {
    return d.format('HH:mm')
  } else if (d.isSame(now, 'year')) {
    return d.format('MM-DD HH:mm')
  }
  return d.format('YYYY-MM-DD HH:mm')
}

// 查看用户资料
const viewUserProfile = () => {
  if (currentConversation.value) {
    router.push(`/user/${currentConversation.value.otherUserId}`)
  }
}

// WebSocket消息处理
const handleNewMessage = (msg) => {
  // 如果是当前会话的消息
  if (currentConversation.value && 
      (msg.fromUserId === currentConversation.value.otherUserId || msg.isSelf)) {
    messages.value.push(msg)
    scrollToBottom()
    
    // 如果不是自己发的，标记已读
    if (!msg.isSelf) {
      markAsRead(currentConversation.value.otherUserId)
      websocket.markAsRead(currentConversation.value.otherUserId)
    }
  } else {
    // 更新会话列表中的未读数
    const conv = conversations.value.find(c => c.otherUserId === msg.fromUserId)
    if (conv) {
      conv.unreadCount = (conv.unreadCount || 0) + 1
      conv.lastMessageContent = msg.content
      conv.lastMessageTime = msg.createTime
    } else {
      // 新会话，重新加载列表
      loadConversations()
    }
  }
}

// 初始化WebSocket
const initWebSocket = async () => {
  const token = userStore.token
  if (!token) return
  
  try {
    await websocket.connect(token)
    wsConnected.value = true
    
    // 监听新消息
    websocket.onMessage(handleNewMessage)
    
    // 监听已读通知
    websocket.onRead((userId) => {
      // 可以更新消息状态为已读
    })
  } catch (error) {
    console.error('WebSocket连接失败:', error)
    wsConnected.value = false
  }
}

onMounted(() => {
  loadConversations()
  initWebSocket()
})

onUnmounted(() => {
  // 不断开WebSocket，保持全局连接
})
</script>

<style scoped>
.chat-container {
  display: flex;
  height: calc(100vh - 120px);
  background: var(--bg-card);
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-light);
}

.chat-sidebar {
  width: 300px;
  border-right: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid var(--border-light);
}

.sidebar-header h3 {
  margin: 0;
  font-size: 17px;
  font-weight: 700;
  color: var(--text-primary);
}

.conversation-list {
  flex: 1;
  overflow-y: auto;
}

.conversation-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  transition: all 0.2s;
  border-left: 3px solid transparent;
}

.conversation-item:hover {
  background-color: var(--bg-color);
}

.conversation-item.active {
  background-color: var(--primary-lighter);
  border-left-color: var(--primary-color);
}

.conv-info {
  flex: 1;
  margin-left: 12px;
  overflow: hidden;
}

.conv-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.conv-name {
  font-weight: 600;
  font-size: 14px;
  color: var(--text-primary);
}

.conv-time {
  font-size: 12px;
  color: var(--text-secondary);
}

.conv-preview {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.last-message {
  font-size: 13px;
  color: var(--text-secondary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-card);
}

.header-info {
  margin-left: 12px;
  flex: 1;
}

.header-name {
  font-weight: 600;
  font-size: 16px;
  display: block;
  color: var(--text-primary);
}

.header-status {
  font-size: 12px;
  color: var(--success-color);
}

.chat-messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: var(--bg-color);
}

.loading-more {
  text-align: center;
  padding: 10px;
  color: var(--text-secondary);
}

.message-item {
  display: flex;
  margin-bottom: 16px;
}

.message-item.message-self {
  flex-direction: row-reverse;
}

.message-content {
  margin: 0 12px;
  max-width: 60%;
}

.message-bubble {
  padding: 10px 14px;
  background: var(--bg-card);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  word-break: break-word;
  line-height: 1.5;
}

.message-self .message-bubble {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
  color: #fff;
}

.message-time {
  font-size: 11px;
  color: var(--text-secondary);
  margin-top: 4px;
}

.message-self .message-time {
  text-align: right;
}

.chat-input {
  padding: 16px;
  border-top: 1px solid var(--border-light);
  background: var(--bg-card);
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.chat-empty {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-placeholder);
}
</style>
