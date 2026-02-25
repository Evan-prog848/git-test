<template>
  <div class="container messages-page">
    <el-card class="messages-card" :body-style="{ padding: '0px' }">
      <div class="messages-layout">
        <!-- 左侧消息列表 -->
        <div class="message-list">
          <div class="list-header">
            <h3>消息中心</h3>
          </div>
          <div
            v-for="item in contactList"
            :key="item.id"
            class="contact-item"
            :class="{ active: activeContact?.id === item.id }"
            @click="selectContact(item)"
          >
            <el-badge :value="item.unreadCount" :hidden="item.unreadCount === 0">
              <el-avatar :size="40" :src="item.avatar" />
            </el-badge>
            <div class="contact-info">
              <div class="name-time">
                <span class="nickname">{{ item.nickname }}</span>
                <span class="time">{{ item.lastTime }}</span>
              </div>
              <div class="last-msg">{{ item.lastMsg }}</div>
            </div>
          </div>
          <el-empty v-if="contactList.length === 0" description="暂无消息" :image-size="60" />
        </div>

        <!-- 右侧对话框 -->
        <div class="chat-box" v-if="activeContact">
          <div class="chat-header">
            <span>{{ activeContact.nickname }}</span>
          </div>
          <div class="chat-messages" ref="chatMsgRef">
            <div
              v-for="(msg, index) in messages"
              :key="index"
              class="msg-item"
              :class="{ 'msg-mine': msg.isMine }"
            >
              <el-avatar :size="32" :src="msg.isMine ? userStore.userInfo.avatar : activeContact.avatar" />
              <div class="msg-content">
                <div class="msg-text">{{ msg.text }}</div>
                <div class="msg-time">{{ msg.time }}</div>
              </div>
            </div>
          </div>
          <div class="chat-input">
            <el-input
              v-model="inputMsg"
              type="textarea"
              :rows="3"
              placeholder="请输入消息内容..."
              @keyup.enter.native="sendMsg"
            />
            <div class="input-actions">
              <el-button type="primary" @click="sendMsg">发送</el-button>
            </div>
          </div>
        </div>
        <div class="chat-placeholder" v-else>
          <el-empty description="选择一个联系人开始聊天" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { getMessageList, sendMessage as sendMsgApi, markAsRead } from '@/api/message'

const route = useRoute()
const userStore = useUserStore()
const activeContact = ref(null)
const inputMsg = ref('')
const chatMsgRef = ref(null)
const loading = ref(false)

const contactList = ref([]) // 这里后端接口返回的是聚合的消息列表
const messages = ref([])

// 定时轮询相关
let pollTimer = null
const POLL_INTERVAL = 3000 // 3秒轮询一次

const fetchContacts = async () => {
  loading.value = true
  try {
    const res = await getMessageList({ pageNum: 1, pageSize: 100 })
    // 这里简单处理：将消息按发送者聚合
    const list = res.data.records || []
    const contactMap = {}
    list.forEach(msg => {
      const otherUserId = msg.fromUserId === userStore.userInfo.id ? msg.toUserId : msg.fromUserId
      if (!contactMap[otherUserId]) {
        contactMap[otherUserId] = {
          id: otherUserId,
          nickname: msg.fromUserId === userStore.userInfo.id ? msg.toNickname : msg.fromUserName,
          avatar: msg.fromUserId === userStore.userInfo.id ? msg.toAvatar : msg.fromUserAvatar,
          lastMsg: msg.content,
          lastTime: msg.createTime,
          unreadCount: 0,
          messages: []
        }
      }
      contactMap[otherUserId].messages.push(msg)
    })
    contactList.value = Object.values(contactMap)

    // 加载完成后处理 URL 参数
    if (route.query.userId) {
      handleNewContact(route.query)
    }
  } catch (error) {
    // 即使加载失败，也处理 URL 参数
    if (route.query.userId) {
      handleNewContact(route.query)
    }
  } finally {
    loading.value = false
  }
}

const selectContact = (contact) => {
  activeContact.value = contact
  const sortedMessages = [...contact.messages].sort((a, b) => {
    const timeA = new Date(a.createTime).getTime()
    const timeB = new Date(b.createTime).getTime()
    return timeA - timeB
  })
  contact.messages = sortedMessages
  messages.value = sortedMessages.map(m => ({
    id: m.id,
    text: m.content,
    time: m.createTime,
    isMine: m.fromUserId === userStore.userInfo.id
  }))
  // 标记该联系人的所有消息为已读（略）
  scrollToBottom()
}

const sendMsg = async () => {
  if (!inputMsg.value.trim() || !activeContact.value) return

  const msgContent = inputMsg.value
  try {
    await sendMsgApi({
      toUserId: activeContact.value.id,
      content: msgContent,
      type: 0 // 私信
    })

    const newMsg = {
      id: Date.now(),
      content: msgContent,
      createTime: new Date().toISOString(),
      fromUserId: userStore.userInfo.id,
      toUserId: activeContact.value.id
    }

    // 同步到联系人的 messages 数组
    activeContact.value.messages.push(newMsg)
    activeContact.value.lastMsg = msgContent
    activeContact.value.lastTime = '刚刚'

    // 同步到当前显示的消息列表并保持时间顺序
    messages.value.push({
      id: newMsg.id,
      text: msgContent,
      time: newMsg.createTime,
      isMine: true
    })
    messages.value.sort((a, b) => new Date(a.time).getTime() - new Date(b.time).getTime())

    inputMsg.value = ''
    scrollToBottom()
  } catch (error) {}
}

const scrollToBottom = () => {
  nextTick(() => {
    if (chatMsgRef.value) {
      chatMsgRef.value.scrollTop = chatMsgRef.value.scrollHeight
    }
  })
}

// 开始定时轮询
const startPolling = () => {
  pollTimer = setInterval(async () => {
    // 静默刷新消息列表
    try {
      const res = await getMessageList({ pageNum: 1, pageSize: 100 })
      const list = res.data.records || []
      
      // 检查是否有新消息
      if (activeContact.value) {
        const currentContactId = activeContact.value.id
        const newMessages = list.filter(msg => {
          const isFromContact = msg.fromUserId === currentContactId
          const isToMe = msg.toUserId === userStore.userInfo.id
          return isFromContact && isToMe
        })
        
        // 更新当前聊天的消息
        if (newMessages.length > 0) {
          const existingIds = new Set(messages.value.map(m => m.id))
          newMessages.forEach(msg => {
            if (!existingIds.has(msg.id)) {
              messages.value.push({
                id: msg.id,
                text: msg.content,
                time: msg.createTime,
                isMine: false
              })
            }
          })
          messages.value.sort((a, b) => new Date(a.time).getTime() - new Date(b.time).getTime())
          scrollToBottom()
        }
      }
      
      // 更新联系人列表
      const contactMap = {}
      list.forEach(msg => {
        const otherUserId = msg.fromUserId === userStore.userInfo.id ? msg.toUserId : msg.fromUserId
        if (!contactMap[otherUserId]) {
          contactMap[otherUserId] = {
            id: otherUserId,
            nickname: msg.fromUserId === userStore.userInfo.id ? msg.toNickname : msg.fromUserName,
            avatar: msg.fromUserId === userStore.userInfo.id ? msg.toAvatar : msg.fromUserAvatar,
            lastMsg: msg.content,
            lastTime: msg.createTime,
            unreadCount: 0,
            messages: []
          }
        }
        contactMap[otherUserId].messages.push(msg)
      })
      
      // 保留当前选中的联系人
      const currentActiveId = activeContact.value?.id
      contactList.value = Object.values(contactMap)
      if (currentActiveId) {
        activeContact.value = contactList.value.find(c => c.id === currentActiveId) || null
      }
    } catch (e) {
      // 静默失败
    }
  }, POLL_INTERVAL)
}

// 停止轮询
const stopPolling = () => {
  if (pollTimer) {
    clearInterval(pollTimer)
    pollTimer = null
  }
}

onMounted(() => {
  fetchContacts()
  startPolling()
})

onUnmounted(() => {
  stopPolling()
})

const handleNewContact = (query) => {
  const userId = parseInt(query.userId)
  const nickname = query.nickname || '用户'
  const avatar = query.avatar || ''

  // 检查是否已经在联系人列表中
  let contact = contactList.value.find(c => c.id === userId)

  if (!contact) {
    // 创建新的联系人
    contact = {
      id: userId,
      nickname: nickname,
      avatar: avatar,
      lastMsg: '',
      lastTime: '',
      unreadCount: 0,
      messages: []
    }
    contactList.value.unshift(contact)
  }

  // 选中该联系人
  selectContact(contact)
}

</script>

<style scoped lang="scss">
.messages-page {
  padding: 20px 0;
  height: calc(100vh - 120px);

  :deep(.el-card) {
    border-radius: var(--radius-md);
    border: 1px solid var(--border-light);
    box-shadow: var(--shadow-sm);
    overflow: hidden;
  }
}

.messages-card {
  height: 100%;
  .messages-layout {
    display: flex;
    height: 100%;
  }
}

.message-list {
  width: 300px;
  border-right: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;

  .list-header {
    padding: 16px 20px;
    border-bottom: 1px solid var(--border-light);
    h3 { margin: 0; font-size: 16px; font-weight: 700; color: var(--text-primary); }
  }

  .contact-item {
    display: flex;
    padding: 14px 20px;
    cursor: pointer;
    transition: all 0.2s;
    border-left: 3px solid transparent;

    &:hover { background-color: var(--bg-color); }
    &.active {
      background-color: var(--primary-lighter);
      border-left-color: var(--primary-color);
    }

    .contact-info {
      flex: 1;
      margin-left: 12px;
      overflow: hidden;
      .name-time {
        display: flex;
        justify-content: space-between;
        margin-bottom: 4px;
        .nickname { font-weight: 600; font-size: 14px; color: var(--text-primary); }
        .time { font-size: 12px; color: var(--text-secondary); }
      }
      .last-msg {
        font-size: 12px;
        color: var(--text-secondary);
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }
}

.chat-box {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-color);

  .chat-header {
    padding: 14px 20px;
    background-color: var(--bg-card);
    border-bottom: 1px solid var(--border-light);
    font-weight: 600;
    color: var(--text-primary);
  }

  .chat-messages {
    flex: 1;
    padding: 20px;
    overflow-y: auto;

    .msg-item {
      display: flex;
      margin-bottom: 18px;
      .msg-content {
        margin-left: 10px;
        max-width: 70%;
        .msg-text {
          background-color: var(--bg-card);
          padding: 10px 14px;
          border-radius: var(--radius-md);
          font-size: 14px;
          box-shadow: var(--shadow-sm);
          display: inline-block;
          max-width: 100%;
          white-space: pre-wrap;
          word-break: break-word;
          line-height: 1.5;
        }
        .msg-time {
          font-size: 11px;
          color: var(--text-secondary);
          margin-top: 4px;
        }
      }

      &.msg-mine {
        flex-direction: row-reverse;
        .msg-content {
          margin-left: 0;
          margin-right: 10px;
          text-align: right;
          .msg-text {
            background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
            color: #fff;
            text-align: left;
          }
        }
      }
    }
  }

  .chat-input {
    padding: 16px 20px;
    background-color: var(--bg-card);
    border-top: 1px solid var(--border-light);
    .input-actions {
      display: flex;
      justify-content: flex-end;
      margin-top: 10px;
    }
  }
}

.chat-placeholder {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--bg-card);
  color: var(--text-placeholder);
}
</style>


